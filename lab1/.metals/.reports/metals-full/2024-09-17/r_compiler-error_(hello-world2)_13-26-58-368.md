file://<WORKSPACE>/src/main/scala/Main.scala
### dotty.tools.dotc.core.TypeError$$anon$1: Toplevel definition main is defined in
  <WORKSPACE>/src/main/scala/sample.scala
and also in
  <WORKSPACE>/src/main/scala/Main.scala
One of these files should be removed from the classpath.

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 102
uri: file://<WORKSPACE>/src/main/scala/Main.scala
text:
```scala
object Main extends App {
  val a = Seq(1,2,3,5)
  val b = a.appended(7)
  println(a)
  println(b)

  @@def f2(a: Seq[Int]) = for (i<-0 to a.size)println(i+3)
  @main def main():Unit=f2(a)
}
```



#### Error stacktrace:

```

```
#### Short summary: 

dotty.tools.dotc.core.TypeError$$anon$1: Toplevel definition main is defined in
  <WORKSPACE>/src/main/scala/sample.scala
and also in
  <WORKSPACE>/src/main/scala/Main.scala
One of these files should be removed from the classpath.