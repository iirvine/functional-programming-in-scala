package week4

object show {
  def show(e: Expr): String = e match {
  	case Number(x) => x.toString
  	case Sum(l, r) => show(l) + " + " + show(r)
  }                                               //> show: (e: week4.Expr)String
  
  show(Sum(Number(1), Number(44)))                //> res0: String = 1 + 44
}