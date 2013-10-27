##Decomposition

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

Number is a subclass of expression. We  have two classification methods, isNumber and isSum, as well as Accessor methods numValue, leftOp, and rightOp. For a complete functionality of exploration, when we look at an expression we'd like to know if it's a number or a sum; thus, `isNumber` and `isSum`. If it's a number, we'd like to know the value of the number, hence, `numValue`. If it's a sum, we'd like to know its operands, so we have `leftOp` and `rightOp`.

Here's `Sum`:

```scala
class Sum(e1: Expr, e2: Expr) extends Expr {
	def isNumber: Boolean = false
	def isSum: Boolean = true
	def numValue: Int = throw new Error("Sum.numValue")
	def leftOp: Expr = e1
	def rightOp: Expr = e2
}
```

The idea here is that `new Sum(e1, e2) == e1 + e2`.

###Evaluation of Expressions

We can now write an evaluation function as follows; the idea is that the evaluation function should take one of these expression trees and return the number that it represents.

```scala
def eval(e: Expr): Int {
	if (e.isNumber) e.numValue
	else if (e.isSum) eval(e.leftOp) + eval(e.rightOp)
	else throw new Error("Unknown expression " + e)
}
```

So, `eval(Sum(Num(1), Num(2)))` should give us 3. How do we write that? Well, one way is, given an expression, we ask what it is. Is it a number? If so, then we can return the numeric value of that expression. Otherwise, if that expression is a sum, we take both its operands and we evaluate both of them using eval.

So far so good, but there's a problem - writing all these classification and accessor functions quickly becomes tedious - we've already written 15 methods just to model expressions consisting of sums and numbers.

Let's say we wanted to add to our expession tree; we want two new expressions - `Prod`, which represents the product of two expressions, and `Var` which represents variables. Variables would take a string that represented their name.

```scala
class Prod(e1: Expr, e2: Expr) extends Expr //e1 * e2
class Var(x: String) extends Expr
```

If we wanted to continue with our scheme of classification and accessor methods, we'd need to add methods for those two new classes but also to all the classes we've already defined. Ugh. If we continue with this game we find that the number of methods we need to define tends to grow quadratically, which is just dumb.

###Non-Solution: Type Tests and Type Casts

What do we doooo... a 'hacky' solution could use type tests and type casts. Most classes have some form of typechecking or type casting - Scala lets us do this using methods defined in class `Any`:

```scala
def isInstanceOf[T]: Boolean  // checks whether this object's type conforms to 'T'
def asInstanceOf[T]: T        // treats this object as an instance of type 'T'; throws 'ClassCastException' if it isn't

Scala				Java
x.isInstanceOf[T]   x instanceof T
x.asInstanceOf[T]   (T) x
```

These correspond to Java's type tests and casts. Don't use these in scala - they're for dumbsters. They're a very low level, unsafe operation. There's a better way.

Here's what eval would look like with type tests and casts:

```scala
def eval(e: Expr): Int =
	if (e.isInstanceOf[Number])
		e.asInstanceOf[Number].numValue
	else if (e.isInstanceOf[Sum])
		eval(e.asInstanceOf[Sum].leftOp) +
		eval(e.asInstanceOf[Sum].rightOp)
	else throw new Error("Unknown expression " + e)
```

Assessment of this solution: bleh. The good part is that we don't need any classification methods - these instanceOf tests fulfill that role now. And since we'll only call access methods after we've cast to an appropriate type, we only need access methods for classes where the value is defined, meaning our base trait `Expr` could be empty, and `Num` only needs `numVal` and `Sum` only needs `leftOp` and `rightOp`.

But typetesting and casting is unsafe - we don't necessarily know at runtime if the cast will succeed. We've guarded every cast with a type test, so we can assure statically that all these casts will succeed, but in general that's not assured.

###Solution 1: Object Oriented Decomposition

Suppose all we want to do is *evaluate* expressions; we could just have a direct object oriented solution for that. Instead of making eval a method which exists outside our hierarchy, we just write is as a method of `Expr` itself.

```scala
trait Expr {
	def eval: Int
}

class Number(n: Int) extends Expr {
	def eval: Int = n
}

class Sum(e1: Expr, e2: Expr) extends Expr {
	def eval: Int = e1.eval + e2.eval
}
```

But what happends if we'd like to display expressions now? We want to have another method `def show: String` in our base trait; then we'd have to have an implementation of `show` in `Number` and in `Sum` and every other class we define. We needed to touch all the classes in our hierarchy to add a new feature, and in a real system this is problematic.

###Limitations of OO Decomposition

There's a more pervasive limitation - what if we want to simplify the expressions, instead of evaluate or showing it, maybe using the usual rule of distribution. We want to replace sums of products with the same left operands with a product of sums:

> a * b + a * c -> a * (b + c)

How would we go about doing that? We can't really have a simplify method in either `Product` or `Sum`, because the simplification involves a whole subtree, not a single node. It can't be encapsulated as a method in a single object without looking at other objects.

We could put `simplify` in the `Sum` operation, but then it'd have to look at its two operands and verify that they're indeed both products, and that the left operand of each product is the same tree.

Doing that, we're back at square one - classification and access methods. So, OO decomposition is good for some things, like implementing the `eval` function, but it can't do other things, like a non-local simplification. It might not be the best solution if you have many new methods you want to introduce, because you have to touch all subclasses to do it. In the next lecture we'll look at some techniques to address these problems...