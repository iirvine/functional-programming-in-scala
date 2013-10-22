##Functions as Objects

We have seen that Scala's numeric types and the `Boolean` type can be implemented like normal classes... what about functions?

In fact, function values *are* treated as objects in Scala.

The function type `A => B` is just an abbreviation for the class `scala.Function[A,B]`, roughly defined as:

```scala
package scala
trait Function1[A, B] {
	def apply(x: A): B
}
```

So functions are objects with `apply` methods.

There are also traits `Function2, Function3`... for functions which take more parameters (currently up to 22)

###Expansion of Function Values

An anonymous function such as `(x: Int) => x * x` is expanded to

```scala
{ class AnonFun extends Function1[Int, Int] {
		def apply(x: Int) = x * x
	}
	new AnonFun
}
```

Or, shorter, using *anonymous class syntax*, like in Java:

```scala
new Function1[Int, Int] {
	def apply(x: Int) = x * x
}
```

###Expansion of Function Calls
We've seen how we represent functions, but what about applications of these functions?

A function call, such as `f(a,b)`, where `f` is a value of some class type, is expanded to `f.apply(a,b)`

So the OO-translation of

```scala
val f = (x: Int) => x * x
f(7)
```

would be

```scala
val f = new Function1[Int, Int] {
	def apply(x: Int) = x * x
}
f.apply(7)
```

###Functions and Methods
Note that anything defined with a `def`, ie, a method, like ```def f(x: Int): Boolean = ...` is not itself a function value; but if the name of a method is used in a place where a function type is expected, it's converted automatically to the function value.

