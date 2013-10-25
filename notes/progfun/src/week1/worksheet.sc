object worksheet {
  def abs(x: Double) = if (x < 0) -x else x       //> abs: (x: Double)Double
  
  def sqrt(x: Double) = {
	  def sqrtIter(guess: Double): Double =
  		if (isGoodEnough(guess)) guess
  		else sqrtIter(improve(guess))
  
  	def isGoodEnough(guess: Double) =
  		abs(guess * guess - x) / x < .001
  
  	def improve(guess: Double) =
  		(guess + x / guess) / 2
  
  	sqrtIter(1.0)
	}                                         //> sqrt: (x: Double)Double
	  
}