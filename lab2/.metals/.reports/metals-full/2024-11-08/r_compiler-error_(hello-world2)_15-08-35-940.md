file://<WORKSPACE>/src/main/scala/sample.scala
### dotty.tools.dotc.core.TypeError$$anon$1: Toplevel definition main is defined in
  <WORKSPACE>/src/main/scala/sample3.scala
and also in
  <WORKSPACE>/src/main/scala/sample2.scala
One of these files should be removed from the classpath.

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 955
uri: file://<WORKSPACE>/src/main/scala/sample.scala
text:
```scala
// //добавление к каждому элементу 1
// def plusToElement(a: Seq[Int]) = a.map(i=>i+1)
// //сумма всех элементов
// def sumElements(a: Seq[Int]) = a.reduce((a,b) => a + b)
// //элементы которые делятся на 3
// def treElement(a: Seq[Int]) = {
//     for (i <- a.indices if (i+1) % 3 == 0) yield a(i)
// }
// //выводит каждый третий элемент
// def filterElement(a: Seq[Int]) = { 
//     a.zipWithIndex
//     .filter(p => p._2 % 3 == 0)
//     .map(p => p._1)
// }
// //функция принимающая функцию
// def compose(a:Int=>Double, b:Double=>String):Int=>String = x=>b(a(x))
// @main def main():Unit = {
//     val seq = Seq(1,2,3,4,5,6,7,8,9)
//     val f:Int => Double = x => x*1.5
//     val f1:Double => String = x => if (x>10) "Yes" else "No"
//     val x:Int=6
//     println(plusToElement(seq))
//     println(sumElements(seq))
//     println(treElement(seq))
//     println(filterElement(seq))
//     println(compose(f1,f)(x)))
// }

@main def hello() =@@ println("Hello, World!")
```



#### Error stacktrace:

```

```
#### Short summary: 

dotty.tools.dotc.core.TypeError$$anon$1: Toplevel definition main is defined in
  <WORKSPACE>/src/main/scala/sample3.scala
and also in
  <WORKSPACE>/src/main/scala/sample2.scala
One of these files should be removed from the classpath.