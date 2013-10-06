##Classes and Objects, Dawg

Our `Rational` class does not simplify... we get things like 79/40. Why is that?

###Data Abstraction

One would expect rational numbers to be *simplified*, reduced to their smalled numerator and denominator by dividing both with a divisor.

We could implement this in each rational operation, add a simplification step to add, multiply, etc, but it would be easy to forget this division in an operation. It'd also violate the principle of Don't Repeat Yourself

A better alternative consists of simplifying the representation in the class when the objects are constructed.

Let's implement this in our `Rational` class:

```scala
class Rational(x: Int, y: Int) {
	private def gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)
	private val g = gcd(x, y)
	def numer = x / g
	def denom = y / g
	...
}
```

gcd and g are *private* members; we can only access them from inside the `Rational` class. In this example, we calculate `gcd` immediately, so its value can be re-used in calculations of `numer` and `denom`.

We could change that... we could call `gcd` in the code of `numer` and `denom`:

```scala
class Rational(x: Int, y: Int) {
	private def gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)
	def numer = x / gcd(x,y)
	def denom = y / gcd(x,y)
}
```

That we we avoid the additional field `g`; it could be advantageous if it is expected that the functions `numer` and `denom` are called infrequently, we can amortize the cost of the `gcd` operations.

We could also turn `numer` and `denom` into `val`s, so that they're computed only once:

```scala
class Rational(x: Int, y: Int) {
	private def gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)
	private val g = gcd(x, y)
	val numer = x / g
	val denom = y / g
}
```

That would be advantageous if the functions numer and denom are called often; we've already computed what they are and we don't repeat the operations.

###The Client's View
What's important here is that no matter which of the three alternatives we choose, clienst observe exactly the same behavior in each case. This ability to choose different implementations of the data without affecting clients is called *data abstraction*.

###Self Reference
On the inside of a class, the name `this` represents the object on which the current method is executed.

```scala
class Rational(x: Int, y: Int) {
	...

	def less(that: Rational) = numer * that.denom < that.numer * denom

	def max(that: Rational) = if (this.less(that)) that else this

}
```

Not that a simple name `x`, which refers to another member of the class, is an abbreviation of `this.x`. The members of a class can always be referenced with `this` as the prefix.

###Preconditions
There's no such thing as a rational number with a denominator of zero... how can we guard against users creating illegal rationals like that?

We can enforce this by calling the `require` function:

```scala
class Rational(x: Int, y: Int) {
	require(y != 0, "denominator must be nonzero")
}
```

`require` is a predefined function that takes a condition and an optional message string. If the condition is false, an `IllegalArgumentException` is thrown with the given message string.

###Assertions
Besides `require`, there's another test called `assert`. `assert` also takes a condition and an optional message string as parameters, eg

```scala
val x = sqrt(y)
assert(x >= 0)
```
Like `require`, a failing `assert` will also throw an exception, but it's a different one: `AssertionError`.

This reflects a difference in intent

* `require` is used to enforce a precondition on the caller of a function
* `asser` is used to check the code of the function itself

###Constructors
In scala, a class implicitly introduces a constructor, called the *primary contstuctor* of the class.

The primary constructor:

* takes the parameters of the class
* executes all statements in the class body

If you know Java, you're used to classes having several constructors; that's also possible in scala though the syntax is different. Let's say we want another constructor for `Rational`, that only takes one integer, the denominator

```scala
class Rational(x: Int, y: Int) {
	def this(x: Int) = this(x, 1)
}
```