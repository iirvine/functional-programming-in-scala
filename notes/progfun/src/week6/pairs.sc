package week6

object pairs {
	def isPrime(n: Int) = (2 until n) forall (n % _ != 0)
                                                  //> isPrime: (n: Int)Boolean

  val n = 7                                       //> n  : Int = 7
  (1 until n) flatMap  (i =>
		(1 until i) map (j => (i, j))) filter (pair =>
			isPrime(pair._1 + pair._2))
                                                  //> res0: scala.collection.immutable.IndexedSeq[(Int, Int)] = Vector((2,1), (3,2
                                                  //| ), (4,1), (4,3), (5,2), (6,1), (6,5))
}