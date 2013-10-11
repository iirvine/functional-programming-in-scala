###Packages

Generally in Scala, as in Java, classes are organized into packages; using a package clause at the top of your source file.

###Traits

In Java, as well as in Scala, a class can only have one superclass - it's a single inheritance language. In practice this can be quite constraining; often times a type would naturally have several super types, or you want to inherit behavior from several super entities that all contribute to the final code of the class.

To do that we can use a concept called *traits*; they're essentially declared like an abstract class, but with `trait` instead of `abstract`

```scala
	def height: Int
	def width: Int
	def surface = height * width
```

Classes, objects, and traits can inherit from at most one class but arbitrarily many traits:

```scala
class Square Shape with Planar with Movable
```

Traits resemble interfaces in Java but are more powerful - they can contain fields and concrete methods. These implementations can be overridden in subclasses. On the other hand, traits cannot have (value) parameters - only classes can (like the numerator and denominator of class `Rational`).

###Top Types
At the top of the scala type hierarchy we find:

* `Any`  the base type of all types. It defines methods like '==', '!=', 'hashCode', and 'toString'
* `AnyRef`	the base type of all reference types; an alias of `java.lang.Object`
* `AnyVal`	the base type of all primitive types; for now these are just the primitives that scala inherits from java

###The Nothing Type

`Nothing` is at the bottom of scala's type hierarchy - it is a subtype of every other type.

There is no value of type `Nothing` - why is that useful?

* to signal abnormal termination; sometimes a function would not return, but instead throw an exception or terminate the program. What would the return type of that function be? The best possible type is nothing - it doesn't return anything.
* as an element type of empty collections (like Set[Nothing])

###Exceptions
Scala's exception handling is similar to Java:

```scala
throw Exc
```

aborts evaluation with the exception Exc - the type of this expression is `Nothing`.

###Null
Every reference class type also has `null` as a value - when somebody expects a string, you could pass it null. The type of `null` is `Null`.

`Null` is a subtype of every class that inherits from `Object` - it is incompatible with subtypes of `AnyVal`

```scala
val x = null
val y: String = x
val z: Int = null //Throws a type mismatch error
```