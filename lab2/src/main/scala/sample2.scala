//Лаб 1.2
def printHello(n:Int):Unit = {
    //0 until n - выражение, которое создаёт последовательность чисел от 0 до n - 1
    //Оператор until генерирует последовательность чисел, не включая верхнюю границу n. 
    //То есть, если n = 5, цикл выполнится для значений 0, 1, 2, 3, 4.
    for (i <- 0 until n) {
        val x = if (i % 2 == 0) i else n - i
        println(s"Hello $x")
    }
}

//------------------------------------------------
//Лаб 1.3
def myFunc(a: Seq[Int]): (Seq[Int], Seq[Int]) = { 
    //создаем пары вида (число, индекс) 
    val even = a.zipWithIndex
    //фильтруем только те пары, где индекс четный
    .filter{ case (_,index) => index % 2 == 0 }
    //извлекаем только числа (первый элемент пары)
    .map(_._1)
    //аналогично для нечетных
    val odd = a.zipWithIndex.filter{ case (_, index) => index % 2 != 0 }.map(_._1)
    //возвращаем результат
    (even,odd)
}

def findMax(a: Seq[Int]): Int = a.reduce((a,b) => if (a > b) a else b)

//------------------------------------------------
//Лаб 1.5
def identifyType(value: Any): String = value match { //Запускает сопоставление с образцом на значении value
    case i: Int => s"Это целое число: $i"
    case s: String => s"Это строка: '$s'"
    case d: Double => s"Это число с плавающей запятой: $d"
    case l: List[_] => s"Это список с длиной ${l.length}"
    case null => "Это null"
    case _ => "Неизвестный тип"
}

//------------------------------------------------
//Лаб 1.6
//A, B, C - типовые переменные (любой тип данных)
def compose[A, B, C](f: B => C, g: A => B): A => C = {
  (x: A) => f(g(x))
}

@main def main():Unit = {
    //Лаб 1.1
    println("Hello, World!")
    println("---------------------------")
    //Лаб 1.2
    printHello(5)
    println("---------------------------")
    //Лаб 1.3
    val arr = Seq(10,21,32,43,54,65,76,87)
    println(s"Исходная коллекция: $arr")
    val (evenInd, oddInd) = myFunc(arr)
    println(s"Элементы с четным индексом: $evenInd")
    println(s"Элементы с нечетным индексом: $oddInd")
    val maxEl = findMax(arr)
    println(s"Максимальный элемент: $maxEl")
    println("---------------------------")
    //Лаб 1.4
    println(myFunc)
    println("---------------------------")
    //Лаб 1.5
    println(identifyType(52))          
    println(identifyType("Hello"))     
    println(identifyType(3.14))        
    println(identifyType(List(1, 2)))  
    println(identifyType(null))        
    println(identifyType(true))  
    println("---------------------------")
    //Лаб 1.6
    val f: Int => String = x => s"Результат: ${x * 2}"
    val g: Double => Int = y => (y * 3).toInt
    val composedFunc = compose(f, g)
    println(composedFunc(4.5)) 
}