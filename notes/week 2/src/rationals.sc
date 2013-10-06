object rationals {
  val x = new Rational(3, 6)                      //> x  : Rational = 1/2
  x.numer                                         //> res0: Int = 3
  x.denom                                         //> res1: Int = 6
  
  val y = new Rational(5, 7)                      //> y  : Rational = 5/7
  x + y                                           //> res2: Rational = 17/14
  
  val z = new Rational(3, 2)                      //> z  : Rational = 3/2
  
  x - y - z                                       //> res3: Rational = 12/-7
  
  x < z                                           //> res4: Boolean = true
  x.max(z)                                        //> res5: Rational = 3/2
}

class Rational(x: Int, y: Int) {
	require(y != 0, "denominator must be nonzero")
	private def gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)
	private val g = gcd(x, y)
	def numer = x
	def denom = y
	
	def + (that: Rational) =
		new Rational(
			numer * that.denom + that.numer * denom,
			denom * that.denom)
			
	def < (that: Rational) = numer * that.denom < that.numer * denom
	
	def max(that: Rational) = if (this < that) that else this
			
	def - (that: Rational) = this + -that
			
	def unary_- : Rational = new Rational(-numer, denom)
			
	override def toString = numer / g + "/" + denom / g
}