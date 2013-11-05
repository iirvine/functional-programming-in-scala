##Handling Nested Sequences

Higher order functions on sequences often replace loops in imperative languages; programs that use many nested loops can often be expressed then using *combinations* of these higher order functions.

Here's an example: we want to find all pairs of positive integers `i` and `j`, such that `1 <= j < i < n`, and `i + j` is prime.

For example, if `n = 7`, the pairs we want to find are:

![img](http://i.imgur.com/KrSFNE3.png)

In an imperative programming language, we'd probably use two nested loops, one for `i`, one for `j`, together with a test to see if `i + j` is a prime number, and some kind of buffer to collect the results.

What's a purely functional way to do this?

A natural way is to generate datastructures bit by bit until we have generated the structure we need for the final result.

The first data structure we want to generate is the sequence of all pairs of integers `(i, j)` such that `1 <= j < i < n`. Then, we want to filter the pairs for which `i + j` is prime.

How do we generate this sequence of pairs of integers? The natural way is probably to generate all the integers `i` between 1 and `n` (excluded), then, for each integer `i`, generate the list of pairs `(i, 1), ..., (i, i-1)`

This can be achieved by combining `until` and `map`:

```scala
(1 until n) map (i =>
	(1 until i) map (j => (i, j)))
```

If we do this in the worksheet, we get back a Vector[Vector] - why? Well, recall our class hieararchy from last session; `Range` is a subtype of `Seq`.

The `Range` that we started with, `(1 until n)`, got transformed by a `map`, which produced a sequence of `Pair`s... sequences of pairs are not elements of `Range`s so we needed some other representation. What we got was something that sits between `Seq` and `Range`, an `IndexedSequence`, essentially a sequence that uses random access.

And basically, the prototypical default implementation of an `IndexedSequence` is just a `Vector`. The type inferencer decided that it couldn't do what we're looking for with `Ranges`, they can't contain `Pair`s, so it went up the hiearchy and took the next best type.

That's still not the right thing to do - we want to generate a single collection of pairs, not a collection of vectors. We need to concatenate all the element vectors into one sequence of pairs.

We can combine all the sub-sequences using `foldRight` with `++`:

```scala
(xss foldRight Seq[Int]())(_ ++ _)
```

Or, we could just use the built-in method `flatten`.

(1 until n) map (i =>
	(1 until i) map (j => (i, j))).flatten

But wait - _there's more!!_

We can apply a useful law - remember the `flatMap` function from before? Check it:

`xs flatMap f = (xs map f).flatten`

Essentially, `flatMap` is exactly the same thing as `map`ping `f`, giving us a collection of collections, and then applying `flatten`. So we can contract the two to just use a `flatMap`:

```scala
(1 until n) flatMap (i =>
	(1 until i) map (j => (i, j)))
```

Now we need to filter our sequence according to the criterion, that the sum of the pair is prime.

```scala
def isPrime(n: Int) = (2 until n) forall (n % _ != 0)
(1 until n) flatMap  (i =>
		(1 until i) map (j => (i, j))) filter (pair =>
			isPrime(pair._1 + pair._2))
```

This works but.... *ugh*.

Is there a simpler way to organize this expression that makes it more understandable? One thing we could try to do is name the intermediate results, so split our large expression into several smaller ones. But it turns out there's a more fundmental way to express problems like this in a higher level notation that's easier to understand!

##For-Expressions

Higher order functions such as `map`, `flatMap`, and `filter` provide us powerful contructs for manipulating lists - but sometimes the level of abstraction required by these functions make our programs difficult to understand. This is where scala's `for` expression comes to the rescue.

Here's an example: let `persons` be a list of elements of class `Person`:

```scala
case class Person(name: String, age: Int)
```

To obtain the names of persons over 20 years old, you can write:

```scala
for (p <- persons if p.age > 20) yield p.name
```

This is equivalent to

```scala
persons filter (p => p.age > 20) map (p => p.name)
```

For-expressions are similar to for loops in immperative languages, but there's an important difference. A for loop operates with a side-effect, it changes something - a for expression doesn't. A for expression produces a new result - each element of the result is produced by a `yield` expression.

###Syntax

A for-expression is of the form ```for (s) yield e ```, where `s` is a sequence of *generators* and *filters*, and `e` is an expression whose value is returned by an iteration.

* a generator is of the form `p <- e`, where `p` is a pattern and `e` an expression whose value is a collection. The idea is that we would let `p` range over all elements of the colletion `e`
* a filter is of the form `if f` where `f` is a boolean expression; the idea here is that the filter will remove from consideration all the elements of the collection where `f == false`
* the sequence must always start with a generator
* if there are several generators in the sequence, the last generators vary faster than the first. The first one steps through more slowly, and for each element of the first, the second generator will be traversed, and so on.

We can also write braces instead of parens, so that our sequences of generators and filters can be written on multiple lines without requiring semicolons.

Here's an example of the original problem:

```scala
for {
	i <- 1 until n
	j <- 1 until i
	if isPrime(i + j)
} yield (i, j)
```

And here's a version of `scalarProduct` from last time:

```scala
(for((x,y) <- xs zip ys) yield x * y).sum
```