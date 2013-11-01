##Higher-Order List Functions

The examples have shown that functions on lists often have similar functions - often, we're doing one of several recurring patterns, like

* transforming each element in a list in a certain way
* retrieving a list of all elements satisfying a criterion
* combining the elements of a list using an operator

Functional languages allow programmers to write generic functions that implement patterns such as these using *higher order functions*

###Applying a Function to Elements of a List

A common operation is to transform each element of a list and then return the list of results.

eg, to multiply each element of a list by the same factor, we could write:

```scala
def scaleList(xs: List[Double], factor: Double): List[Double] = xs match {
	case Nil => xs
	case y :: ys => y * factor :: scaleList(ys, factor)
}
```

###Map

That scheme can be generalized to the method `map` of the `List` class.

A simple way to define `map`:

```scala
abstract class List[T] { ...
	def map[U](f: T => Y): List[U] = this match {
		case Nil => this
		case x :: xs => f(x) :: xs.map(f)
	}
}
```

(in fact, the actual definition of `map` is a bit more complicated because it is tail-recursive, and also because it works for arbitrary collections, not just lists)

Here's a much more concise `scaleList` using `map` (so concise in fact it hardly warrants writing a separate function for it):

```scala
def scaleList(xs: List[Double], factor: Double) = xs map (x => x * factor)
```

###Filtering

Another common operation is the selection of all elements satisfying a given condition. Here's `filter`:

```scala
abstract class List[T] {
	...
	def filter(p: T => Boolean): List[T] = this match {
		case Nil => this
		case x :: xs => if (p(x)) x :: xs.filter(p) else xs.filter(p)
	}
}
```

###Variations

There are a LOT of methods that extract sublists from a list based on some predicate. More than I feel like typing out.

![img](http://i.imgur.com/B6d4CcM.png)