file://<WORKSPACE>/src/main/scala/sample.scala
### scala.MatchError: TypeDef(B,TypeBoundsTree(EmptyTree,EmptyTree,EmptyTree)) (of class dotty.tools.dotc.ast.Trees$TypeDef)

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 3064
uri: file://<WORKSPACE>/src/main/scala/sample.scala
text:
```scala
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
      //коллекция пар (проверка, сообщение).
      Seq(
        (password.length >= 8) -> "The password must contain at least 8 characters",
        password.exists(UPPERCASE_LETTERS.contains) -> "The password must contain at least 1 uppercase letter",
        password.exists(LOWERCASE_LETTERS.contains) -> "The password must contain at least 1 lowercase letter",
        password.exists(NUMBERS_LETTERS.contains) -> "The password must contain at least 1 digit",
        password.exists(SPECIAL_LETTERS.contains) -> "The password must contain at least 1 special symbol"
      //функция перебирает пары и возвращает первое сообщение об ошибке, если условие не выполняется
      ).collectFirst {
        case (false, errorMessage) => Right(errorMessage)
    }.getOrElse(Left(true))
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
    }.map{password =>
    goodEnoughPasswordTry(password) match {
      case Left(_) => password
      case Right(error) =>
        println(s"Error: $error")
        throw new Exception("Invalid password")
    }
  }.recoverWith { case _ => readPassword()}
}
//****************************************
//2.3
trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}а@@

@main def main():Unit = {
    //2.1
    println("Task 2.1")
    val result = integral(x=>x*x,0,1,1000) //интеграл х^2 на отрезке [0,1]
    println(result)
    println("****************************************")
    //2.2 (а)
    println("Task 2.2 (a)")
    println(goodEnoughPasswordOption("Test123!"))   
    println(goodEnoughPasswordOption("abcde1!"))   
    println(goodEnoughPasswordOption("HelloWorld")) 
    println(goodEnoughPasswordOption("A123@mail")) 
    println("****************************************")
    //2.2 (b)
    println("Task 2.2 (b)")
    println(goodEnoughPasswordTry("Test123!"))   
    println(goodEnoughPasswordTry("abcde1!"))   
    println(goodEnoughPasswordTry("HelloWorld")) 
    println(goodEnoughPasswordTry("A123@mail")) 
    println("****************************************")
    //2.2 (c)
    println("Task 2.2 (c)")
    val passwordFuture = readPassword()
    val finalPassword = Await.result(passwordFuture, Duration.Inf)
    println(s"Password success: $finalPassword")
    println("****************************************")
}
```



#### Error stacktrace:

```
dotty.tools.pc.completions.KeywordsCompletions$.checkTemplateForNewParents$$anonfun$2(KeywordsCompletions.scala:218)
	scala.Option.map(Option.scala:242)
	dotty.tools.pc.completions.KeywordsCompletions$.checkTemplateForNewParents(KeywordsCompletions.scala:215)
	dotty.tools.pc.completions.KeywordsCompletions$.contribute(KeywordsCompletions.scala:44)
	dotty.tools.pc.completions.Completions.completions(Completions.scala:122)
	dotty.tools.pc.completions.CompletionProvider.completions(CompletionProvider.scala:90)
	dotty.tools.pc.ScalaPresentationCompiler.complete$$anonfun$1(ScalaPresentationCompiler.scala:146)
```
#### Short summary: 

scala.MatchError: TypeDef(B,TypeBoundsTree(EmptyTree,EmptyTree,EmptyTree)) (of class dotty.tools.dotc.ast.Trees$TypeDef)