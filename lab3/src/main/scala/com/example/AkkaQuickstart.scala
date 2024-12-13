//#full-example
package com.example


import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.example.GreeterMain.SayHello

//Актор, который отправляет сообщения
object Greeter {
  //Сообщение Greet, которое отправляет запрос на сообщение (поле whom - адресат, replyTo - ссылка на него)
  final case class Greet(whom: String, replyTo: ActorRef[Greeted])
  //Ответ на сообщение 
  final case class Greeted(whom: String, from: ActorRef[Greet])
  //Поведение актора при приеме сообщения
  def apply(): Behavior[Greet] = Behaviors.receive { (context, message) =>
    //Логирование сообщения
    context.log.info("Hello {}!", message.whom)
    //Отправка ответа актору из поля replyTo
    message.replyTo ! Greeted(message.whom, context.self)
    //актор не меняет поведение 
    Behaviors.same
  }
}

//Актор-бот, который нужен для отправки нескольких сообщений
object GreeterBot {
  //Поведение актора при приеме сообщения
  def apply(max: Int): Behavior[Greeter.Greeted] = {
    //Начальное состояние актора
    bot(0, max)
  }
  //Функция, работающая рекурсивно. Обрабатывает сообщения
  private def bot(greetingCounter: Int, max: Int): Behavior[Greeter.Greeted] =
    Behaviors.receive { (context, message) =>
      //Счетчик полученных сообщений
      val n = greetingCounter + 1
      //Логирование сообщений
      context.log.info("Greeting {} for {}", n, message.whom)
      //Проверка на максимальное число сообщений
      if (n == max) {
        //Завершение работы, если максимальное число достигнуто
        Behaviors.stopped
      } else {
        //Отправка нового сообщения 
        message.from ! Greeter.Greet(message.whom, context.self)
        //Переход к следующему состоянию
        bot(n, max)
      }
    }
}

//Актор, который управляет взаимодействием акторов
object GreeterMain {
  //Сообщение, которое используется для отправки приветствий
  final case class SayHello(name: String)
  //Поведение актора
  def apply(): Behavior[SayHello] =
    Behaviors.setup { context =>
      //Создание актора
      val greeter = context.spawn(Greeter(), "greeter")

      //Определение поведения
      Behaviors.receiveMessage { message =>
        //Создание актора-бота
        val replyTo = context.spawn(GreeterBot(max = 3), message.name)
        //Отправка сообщения Greet к Greeter
        greeter ! Greeter.Greet(message.name, replyTo)
        Behaviors.same
      }
    }
}

object AkkaQuickstart extends App {
  //Создание актор-системы
  val greeterMain: ActorSystem[GreeterMain.SayHello] = ActorSystem(GreeterMain(), "AkkaQuickStart")
  //Отправка сообщения "Charles" в GreeterMain
  greeterMain ! SayHello("Charles")
}