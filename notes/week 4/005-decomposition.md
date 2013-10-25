###Decomposition

Suppose we want to write a small interpreter for arithmetic expressions - we'll keep it simple and restrict ourselves to numbers and additions.

Expressions can be represented as a class hierarchy, with a base trait `Expr` and two subclasses, `Number` and `Sum`. If we want to explore a tree of nodes consisting of numbers and sums, we'd like to know what kind of tree is it, and what components it has. To be able to do that, we could use the following implementation

```scala
trait Expr {
	def isNumber: Boolean
	def isSum: Boolean
	def numValue: Int
	def leftOp: Expression
	def rightOp: Expression
}

class Number(n : Int) extends Expr {
	def isNumber: Boolean = true
	def isSum: Boolean = false
	def numValue: Int = n
	def leftOp: Expr = throw new Error("Number.leftOp")
	def rightOp: Expr = throw new Error("Number.rightOp")
}
```

For a complete functionality of exploration, when we look at an expression we'd like to know if it's a number or a sum; thus, `isNumber` and `isSum`. If it's a number, we'd like to know the value of the number, hence, `numValue`. If it's a sum, we'd like to know its operands, so we have `leftOp` and `rightOp`.

