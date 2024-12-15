package com.example

import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors

//Актор для вычисления интеграла
object IntegralActor {
    case class Calculate(start: Double, end:Double, numSteps: Int, replyTo: ActorRef[Result])
    case class Result(value: Double)

    //Поведение актора, который вычисляет интеграл
    def apply(): Behavior[Calculate] = Behaviors.receive { (context, message) =>
        val step = (message.end - message.start) / message.numSteps
        // Вычисляем сумму значений функции в промежуточных точках
        val intermediateSum = (1 until message.numSteps).map { i =>
            val x = message.start + i * step
            f(x)
        }.sum
        // Прибавляем значения на концах интервала (метод трапеций)
        val result = (f(message.start) + f(message.end)) / 2 + intermediateSum
        // Умножаем на шаг
        val integral = result * step

        context.log.info(s"Computed integral: $integral")
        message.replyTo ! Result(integral)
        Behaviors.same
    }

    // Функция для интегрирования (x^2)
    def f(x: Double): Double = x * x 
}

// Актор для суммирования частичных результатов
object SumActor{
    //добавим remainingResults для количества оставшихся вычислений
    case class AddResult(sum: Double, remainingResults: Int)
    case class FinalResult(result: Double)

    def apply(): Behavior[AddResult] = Behaviors.setup { (context) =>
        var totalSum = 0.0
        var resultsLeft = 0

        Behaviors.receiveMessage {
            case AddResult(sum, remainingResults) => 
                totalSum += sum
                resultsLeft = remainingResults
                //Логируем промежуточный результат
                context.log.info(s"Partial sum: $sum, current total: $totalSum, remaining: $resultsLeft")
                // Если все результаты получены, выводим финальный результат
                if (resultsLeft == 0) {
                    context.log.info(s"Final result: $totalSum")
                }

                Behaviors.same
        }
    }
}

//Актор для управления процессом
object MainActor {
  case class StartCalculation(l: Double, r: Double, steps: Int)

  def apply(): Behavior[StartCalculation] = Behaviors.setup { (context) =>
    // Создание акторов для вычисления интеграла и суммирования
    val integralActor = context.spawn(IntegralActor(), "integralActor")
    val sumActor = context.spawn(SumActor(), "sumActor")

    def sendCalculation(
      start: Double, 
      stepSize: Double, 
      numSteps: Int, 
      replyTo: ActorRef[IntegralActor.Result]
    ): Unit = {
      integralActor ! IntegralActor.Calculate(start, start + numSteps * stepSize, numSteps, replyTo)
    }

    Behaviors.receiveMessage {
      case StartCalculation(l, r, steps) =>
        // Вычисление шага для каждого подактора
        val stepSize = (r - l) / steps
        // Количество акторов
        val numActors = 4
        var remainingResults = numActors

        (0 until numActors).foreach { i =>
          val start = l + i * ((r-l) / numActors) 
          val numSteps = steps / numActors
          // Актор для обработки результата других акторов
          val replyTo = context.spawn(Behaviors.receiveMessage[IntegralActor.Result] {
            case IntegralActor.Result(partialSum) =>
              remainingResults -= 1
              sumActor ! SumActor.AddResult(partialSum, remainingResults)
              Behaviors.same
          }, s"responseActor-${i}")
          // Отправляем вычисления на интеграцию
          sendCalculation(start, stepSize, numSteps, replyTo)
        }
        Behaviors.same
    }
  }
}

@main def Main(): Unit = {
  val system = ActorSystem(MainActor(), "main")
  system ! MainActor.StartCalculation(0, 5, 1000) //Результат интеграла x^2 от 0 до 5 41.6
}