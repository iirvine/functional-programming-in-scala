##Functions and Data

In this section, we'll learn how functions create and encapsulate data structures.

Here's our example: we want to design a package for doing rational arithmetic.

Quick reminder - a rational number `x/y` is represented by two integers:

* its *numerator* x
* its *denominator* y

In a sense we can already do that with what we know from scala; we could define two functions:
![img](http://i.imgur.com/yHvSPcA.png)

They each would get all the bits of both rational numbers as arguments; the numerators and denominators of both rational numbers, and then they'd both implement the usual algorithms for rational arithmetic.

The problem with this approach is that it would be difficult to manage all these numbers; it's a much better approach to combine the numerator and denominator of a rational number into a datastructure. In scala, we do this by defining a class.

###Classes
```scala
class Rational(x: Int, y: Int) {
	def numer = x
	def denom = y
}
```

This definition introduces two entities:

* a new *type*, named `Rational`
* a *constructor* `Rational` to create elements of this type

Scala keeps the names of types and values in *different namespaces*; it always knows from the context whether you mean a type or the value. that way there's no conflict between the two definitions, the constructor and the type, of `Rational`

###Objects
A *type* in a programming language is essentially a set of values; the values that belong to a class type are called *objects*.

We create an object by prefixing an application of the constructor of the class with the operator `new`: `new Rational(1, 2)`

###Members of an Object
Objects of class `Rational` have two *members*. `numer` and `denom`; we select the members of an object with the infix operator `.`, like in Java

###Rational Arithmetic
We can now define the usual arithmetic functions that implement the standard rules; addition, subtraction, multiplication

![img](http://i.imgur.com/CTuXv8C.png)

One thing we could do is use the class `Rational` as a pure datatype; something that just gives us the data. We'd define the operations as functions outside of the class; addRational would take two `Rationals` and give you a `Rational`:

```scala
def addRational(r: Rational, s: Rational): Rational =
	new Rational(
		r.numer * s.denom + s.numer * r.denom,
		r.denom * s.denom)
```

To make things print nicely, we could also define a `makeString` function that takes a rational and produces the numerator of the rational and the denominator, seperated by a slash.

```scala
def makeString(r: Rational) =
	r.numer + "/" + r.denom
```

When all is said and done, we'd have an invocation that looked something like this:

```scala
makeString(addRational(new Rational(1,2), new Rational(2, 3))) // 7/6
```

###Methods
We can go deeper...... we can package functions operation on a data abstraction in the data abstraction itself.

Such functions are called *methods*.

For example, `Rationals` now would have, in addition to the functions `numer` and `denom`, the functions `add`, `sub`, `mul`, `div`, `equal`, `toString`

