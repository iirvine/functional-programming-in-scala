package week5

object listfun {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  val data = List("a", "a", "a", "b", "c", "c", "a")
                                                  //> data  : List[String] = List(a, a, a, b, c, c, a)
  
  def pack[T](xs: List[T]): List[List[T]] = xs match {
  	case Nil => Nil
  	case x :: xs1 =>
  		val (first, rest) = xs span (y => y == x)
  		first :: pack(rest)
  }                                               //> pack: [T](xs: List[T])List[List[T]]
  
  val what = pack(data)                           //> what  : List[List[String]] = List(List(a, a, a), List(b), List(c, c), List(a
                                                  //| ))
  
  def encode[T](xs: List[T]): List[(T, Int)] =
  	pack(xs) map (ys => (ys.head, ys.length)) //> encode: [T](xs: List[T])List[(T, Int)]
  	
  encode(data)                                    //> res0: List[(String, Int)] = List((a,3), (b,1), (c,2), (a,1))
}