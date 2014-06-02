##Reduction of Lists
Another common operation is to combine the elements of a list using a given operator - ie `sum(List(x1, ..., xn))`, or `product(List(x1, ..., xn))`

We can implement this with the usual recursive schema:

```scala
def sum(xs: List[Int]): Int = xs match {
	case Nil => 0
	case y :: ys => y + sum(ys)
}
```

###ReduceLeft

This pattern can be abstracted out using the generic method `reduceLeft`; this inserts a given binary operator between each successive element of a list.

`List(x1, ..., xn)  reduceLeft op = (...(x1 op x2) op ...) op xn`

Here's a pretty drawing martin did for us:


Here's a change from jekyl, dawg.


![img](http://i.imgur.com/sh4mMtU.png)

Using `reduceLeft`, we can simplify:

```scala
def sum(xs: List[Int]) = (0 :: xs) reduceLeft ((x,y) => x + y)
def product(xs: List[Int]) = (1 :: xs) reduceLeft ((x,y) => x * y)
```

###A shorter way to write functions

By the way - instead of writing `((x,y) => x * y)`, we can also just write `(_ * _)`

Every _ represents a new parameter, going from left to right. The parameters are implicitly defined at the next outer pair of parentheses (or the whole expression if there are no enclosing parentheses)

So, `sum` and `product` can just be written like this:

```scala
def sum(xs: List[Int]) = (0 :: xs) reduceLeft (_ + _)
def product(xs: List[Int]) = (1 :: xs) reduceLeft (_ * _)
```

###FoldLeft
The function `reduceLeft` is defined in terms of a more general function, `foldLeft`. It's like `reduceLeft`, but it takes an *accumulator*, or zero-element, `z`, which is returned when `foldLeft` is called on an empty list.

`(List(x1, ..., xn) foldLeft z)(op) = (...(z op x1) op ...) op xn`

Here's `sum` and `product` again!

```scala
def	sum(xs: List[Int]) = (xs foldLeft 0) (_ + _)
def product(xs: List[Int]) = (xs foldLeft 1) (_ * _)
```

###Implementations of ReduceLeft and FoldLeft

```scala
abstract class List[T] { ...
	def reduceLeft(op: (T, T) => T): T = this match {
		case Nil => throw new Error("Nil.reduceLeft")
		case x :: xs => (xs foldLeft x)(op)
	}
	def foldLeft[U](z: U)(op: (U,T) => U): U = this match {
		case Nil => z
		case x :: xs => (xs foldLeft op(z, x))(op)
	}
}
```

###FoldRight and ReduceRight

Applications of `foldLeft` and `reduceLeft` unfold on trees that lean to the left, as evidenced by another lovely drawring:

![img](http://i.imgur.com/ANbCvsv.png)

It would make sense to have a dual pair of operations that unfold trees which lean to the right, ie

![img](http://i.imgur.com/6ZQ8UNo.png)
![img](http://i.imgur.com/jpOn1Jz.png)

Here's the implementation:
```scala
abstract class List[T] { ...
	def reduceRight(op: (T, T) => T): T = this match {
		case Nil => throw new Error("Nil.reduceRight")
		case x :: Nil => x
		case x :: xs => op(x, xs.reduceRight(op))
	}
	def foldLeft[U](z: U)(op: (U,T) => U): U = this match {
		case Nil => z
		case x :: xs => op(x, (xs foldRight z)(op))
	}
}
```

###Difference between FoldLeft and FoldRight

For operators that are associative and commutative, `foldLeft` and `foldRight` are equivalent - but sometimes only one of the two operators is appropriate.

As an example, here's another formulation of `concat`:

```scala
def concat[T](xs: List[T], ys: List[T]): List[T] = (xs foldRight ys)(_ :: _)
```

Here, it isn't possible to replace `foldRight` by `foldLeft` - why? Well, the types don't match up. We end up trying to do a `foldLeft` over a list `xs`, meaning we apply an operation over each element of that list - the operation cons is not applicable to arbitrary elements, only lists.