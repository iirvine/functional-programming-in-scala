##Pure Object Orientation
Odersky's about to blow your mind, dawg - argues that scala is actually also object oriented, and in a very pure form, despite the fact that everything we've seen about it so far has been functional.

A pure object oriented language is one in which every value is an object; also, every operation is essentially a method call on some object.

Is the language is based on classes, this means that the type of each value is a class.

So, is Scala a pure object oriented language?

At first glance, there seem to be some exceptions: primitive types, functions... but let's look closer.

###Standard Classes
Conceptually, types such as `Int` or `Boolean` do not receive special treatment in scala; they are like the other classes, defined in the `scala` package.

For reasons of efficiency, the scala compiler represents the values of type `scala.Int` by 32 bit integers, and values of type `scala.Boolean` by Java's Booleans etc; but this we can treat simply as an optimization and a measure to improve interoperability between Scala and Java code. Conceptually, these things can be treated just like normal classes

One *could* define Boolean as a class from first principles, without any changes in user code, without resort to primitive booleans:

```scala
package idealized.scala
abstract class Boolean {
	def ifThenElse[T](t: => T, e: => T): T

	def && (x: => Boolean): Boolean = ifThenElse(x, False)
	def || (x: => Boolean): Boolean = ifThenElse(true, x)
	def unary_!: Boolean			= ifThenElse(false, true)

	def == (x: Boolean): Boolean 	= ifThenElse(x, x.unary_!)
	def != (x: Boolean): Boolean	= ifThenElse(x.unary_!, x)
}
```

We would have one abstract method, called ifThenElse; it's a parameterized method, containing a type parameter T; it has a 'then' part, `t`, and an else part, `e`. Both the then and else part take an expression of type T; the result of ifThenElse would be T.

The idea is that instead of `if (cond) thenExpression else elseExpression`, we'd translate that to the ifThenElse method call of our condition, and we'd pass tE and eE as the arguments to the ifThenElse method `cond.ifThenElse(te, ee)`

Once we have ifThenElse, how would we define other operators on booleans, like the conjunction (&&) and disjunction (||)? Turns out that all the operations on booleans can be defined in terms of ifThenElse.

Let's look at &&: it would take another expression of type Boolean, and it would then call ifThenElse(x, false); what this means is that if the Boolean itself is true, then we would return the argument x; on the other hand, if the Boolean is false, then the result is immediately false.

We can apply these tricks to the rest of the operations on booleans.

Once we have that outline, we still have to define the boolean constants, false and true; we can't be using the primitive booleans because we pass them to ifThenElse with one of our idealized booleans. False and true must themselves be constants of type idealized.scala.Boolean

Here's how we define them:

```scala
package idealized.scala

object true extends Boolean {
	def ifThenElse[T](t: => T, e => e) = t
}

object false extends Boolean {
	def ifThenElse[T](t: => T, e => e) = e
}
```

Each of them would be an object; for the true constant, all we need to do is define what the definition of ifThenElse should be for that constant. IE, what is the definition of if (true), thenExpression else elseExpression? well, we've seen from the rewrite rules that this will just become thenExpression

So our true constant is just the implementation of that rewrite rule; we say ifThenElse of a then expression and an else expression gives us the then part. Conversely, ifThenElse of a then part and an else part gives us the else part.