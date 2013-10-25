package Cons

object nth {
  def nth[T](n: Int, xs: List[T]): T =
  	if (xs.isEmpty) throw new IndexOutOfBoundsException
  	else if (n == 0) xs.head
  	else nth(n - 1, xs.tail)                  //> nth: [T](n: Int, xs: Cons.List[T])T
  	
  val list = new Cons(1, new Cons(2, new Cons(3, new Nil)))
                                                  //> list  : Cons.Cons[Int] = Cons.Cons@418c56d
  
  nth(2, list)                                    //> res0: Int = 3
  nth(-1, list)                                   //> java.lang.IndexOutOfBoundsException
                                                  //| 	at Cons.nth$$anonfun$main$1.nth$1(Cons.nth.scala:5)
                                                  //| 	at Cons.nth$$anonfun$main$1.apply$mcV$sp(Cons.nth.scala:12)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$$anonfun$$exe
                                                  //| cute$1.apply$mcV$sp(WorksheetSupport.scala:76)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$.redirected(W
                                                  //| orksheetSupport.scala:65)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$.$execute(Wor
                                                  //| ksheetSupport.scala:75)
                                                  //| 	at Cons.nth$.main(Cons.nth.scala:3)
                                                  //| 	at Cons.nth.main(Cons.nth.scala)
}