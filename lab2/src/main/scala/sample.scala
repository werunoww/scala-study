import scala.util.{Try, Success, Failure}
import scala.concurrent.Future
import scala.io.StdIn.readLine
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.Duration
//2.1
def integral(f:Double=>Double, l:Double, r:Double, steps:Int):Double = {
    //шаг разбиения
    val stepSize = (r - l) / steps
    //точки разбиения (точки, которые делят интеграл на равные части)
    //(l, l+stepSize, l+2*stepSize ... r)
    val points = (0 to steps).map(i => l + i * stepSize)
    //значения функции f в этих точках.
    val functionValues = points.map(f)
    // Вычисление суммы трапеций
    val sum = functionValues
    //подмассивы из двух соседних элементов
    .sliding(2)
    //формула для площади трапеции
    .map { case Seq(y1, y2) => (y1 + y2) * stepSize / 2 }
    .sum
    return sum
}

//****************************************
//2.2
val LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz"
val UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
val NUMBERS_LETTERS = "1234567890"
val SPECIAL_LETTERS = "!@#$%^&*()[]{};:,./<>?|"
//2.2(a)
def goodEnoughPasswordOption(password: String): Boolean = {
    //список функций, каждая проверяет одно из условий
    val conditions: Seq[Boolean] = Seq(
        password.length >= 8, 
        password.exists(LOWERCASE_LETTERS.contains), 
        password.exists(UPPERCASE_LETTERS.contains), 
        password.exists(NUMBERS_LETTERS.contains),   
        password.exists(SPECIAL_LETTERS.contains)
    )   
    //"&&" применяется для уменьшения списка результатов
    return conditions.reduce(_ && _)
}
//2.2(b)
def goodEnoughPasswordTry(password: String): Either[Boolean, String] = {
    Try {
    if (password.length < 8) {
      Right("The password must contain at least 8 characters")
    } else if (!password.exists(UPPERCASE_LETTERS.contains)) {
      Right("The password must contain at least 1 upercase letter")
    } else if (!password.exists(LOWERCASE_LETTERS.contains)) {
      Right("The password must contain at least 1 lowercase letter")
    } else if (!password.exists(NUMBERS_LETTERS.contains)) {
      Right("The password must contain at least 1 digit")
    } else if (!password.exists(SPECIAL_LETTERS.contains)) {
      Right("The password must contain at least 1 special symbol")
    } else {
      Left(true)
    }
  } match {
    case Success(result) => result
    case Failure(_) => Right("Error...")
  }
}
//2.2(c)
def readPassword(): Future[String] = {
    //чтение пароля (асинхронно)
    Future {
        printf("Enter password: ")
        readLine()
    //проверка пароля
    }.flatMap(password =>
    goodEnoughPasswordTry(password) match {
      //пароль подходит
      case Left(_) => 
        //завершение выполнения
        Future.successful(password)
      case Right(error) =>
        println(s"Error: $error")
        //запрос пароля заново
        readPassword()
    }
  )
}
//****************************************
//2.3
trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}

@main def main():Unit = {
    //2.1
    println("Task 2.1")
    val result = integral(x=>x*x,0,1,1000) //интеграл х^2 на отрезке [0,1]
    println(result)
    println("****************************************")
    //2.2 (а)
    println("Task 2.2 (a)")
    println(goodEnoughPasswordOption("Test123!"))   // true
    println(goodEnoughPasswordOption("abcde1!"))   // false
    println(goodEnoughPasswordOption("HelloWorld")) // false
    println(goodEnoughPasswordOption("A123@mail")) // true
    println("****************************************")
    //2.2 (b)
    println("Task 2.2 (b)")
    println(goodEnoughPasswordTry("Test123!"))   // true
    println(goodEnoughPasswordTry("abcde1!"))   // false
    println(goodEnoughPasswordTry("HelloWorld")) // false
    println(goodEnoughPasswordTry("A123@mail")) // true
    println("****************************************")
    //2.2 (c)
    println("Task 2.2 (c)")
    val passwordFuture = readPassword()
    val finalPassword = Await.result(passwordFuture, Duration.Inf)
    println(s"Password success: $finalPassword")
    println("****************************************")
}