##Pairs and Tuples

Let's make a function to sort our lists that's more efficient than insertion sort, which is for dummies.

A good algorithm for this is *merge sort*:

* if the list consists of zero or one elements, it is already sorted.
* otherwise, separate the list into two sub-lists, each containing around half the elements of the original list
* sort the two sub-lists
* merge the two sorted sub-lists into a single sorted list

```scala
def msort(xs List[Int]): List[Int] = {
	val n = xs.length / 2
	if (n == 0) xs
	else {
		def merge(xs: List[Int], ys: List[Int]) = ???
		val (fst, snd) = xs splitAt n
		merge(msort(fst), msort(snd))
	}
}
```

let's look at an implementation of `merge`:

```scala
def merge(xs: List[Int], ys: List[Int]) =
	xs match {
		case Nil =>
			ys
		case x :: xs1 =>
			ys match {
				case Nil =>
					xs
				case y :: ys 1 =>
					if (x < y) x :: merge(xs1, ys)
					else y :: merge(xs, ys1)
			}
	}
```

wat.

We'll improve this later, but here's how it works - we'll pattern match first on the left list. If the left list is `Nil`, then the merge must consist of all elements of the right list, so we return `ys`.

If the left list is not `Nil`, and it consists of a head element `x` followed by a tail `xs1`, then we do a pattern match on the right hand list `ys`. If that is `Nil`, then we can simply return `xs` - otherwise, then we have two head elements, `x` and `y`, and two tail lists, `xs1` and `ys1`.

We compare the head elements with each other - if `x` is smaller than `y`, then obviously `x` must be the first element of our sorted list. So we take `x` followed by a merge of all the remaining elements from the `xs` list (that's `xs1`) and all the elements of the `ys` list. If `y` is greater, then we can take `y` as the first element of the sorted list, followed by a merge of all the `xs` elements and the elements of `ys` that follow `y` (`ys1`)

###splitAt

The `splitAt` function on lists returns two sublists - the elements up to the given index, and the elements from that index. The lists are returned in a *pair*.

###Detour: Pair and Tuples

Let's take a second to look at pairs and their generalization, tuples. A pair in scala is written `(x,y)`, where `x` and `y` are the elements of the pair.

```scala
val pair = ("answer", 42)
```

The type of the above pair is `(String, Int)`

We can also decompose pairs using pattern matching, like this:

```scala
val (label, value) = pair // label: String = answer; value: Int = 42
```
This works the same with tuples of more than two elements; you can have triples, quadruples, etc.

###Translation of Tuples

So far, all the types we've encountered are actually abbreviations for some instance of a class type. Tuples are no exception. A tuple type `(T1, ..., Tn)` is an abbreviation of the parameterized type `scala.Tuple`*n*`[T1, ..., Tn]`

A tuple expression of `(e1, ..., en)` is equivalent to the function application `scala.Tuple`*n*`(e1, ..., en)`

Finally, a tuple pattern of `(p1, ..., pn)` is equivalent to the constructor pattern `scala.Tuple`*n*`(p1, ..., pn)`

###The Tuple Class

Here, all the `Tuple`*n* classes are modeled after the following pattern:

```scala
case class Tuple2[T1, T2](_1: +T1, _2: +T2) {
	override def toString = "(" + _1 _ "," + _2 + ")"
}
```
Meaning we can of course get to the fields of a typle with _1 and _2, etc. The pattern matching form is cooler.

###Excercise

The `merge` function as given uses a nested pattern match - that's not so great. It doesn't reflect the symmetry of the merge algorithm - it makes us do a merge on the left and side and then a nested match on the right hand side. For merge it doesn't really matter what side is left or right.

So let's rewrite `merge` using a pattern match over pairs!

```scala
def msort(xs List[Int]): List[Int] = {
	val n = xs.length / 2
	if (n == 0) xs
	else {
		def merge(xs: List[Int], ys: List[Int]): List[Int] = (xs, ys) match {
			case (Nil, ys) => ys
			case (xs, Nil) => xs
			case (x :: xs1, y :: ys1) =>
				if (x < y) x :: merge(xs1, ys)
				else y :: merge(xs, ys1)
		}
		val (fst, snd) = xs splitAt n
		merge(msort(fst), msort(snd))
	}
}
```

_NEATO._