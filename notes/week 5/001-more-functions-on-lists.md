##More Functions on Lists

###Sublists and element access:

* `xs.length` : The number of elements of `xs`.
* `xs.last`   : The lists's last element, exception if `xs` is empty
* `xs.init`   : A list consisting of all elements of `xs` except the last one, exception if `xs` is empty
* `xs take n` : A list consisting of the first `n` elements of `xs`, or `xs` itself if it is shorter than `n`
* `xs drop n` : The rest of the collection after taking `n` elements
* `xs(n)`		: The element of `xs` at index `n`

###Creating new lists:

* `xs ++ ys`        : the list cinsisting of all elements of `xs` followed by all elements of `ys`
* `xs.reverse`      : the list containing the elements of `xs` in reversed order
* `xs.updated(n,x)` : In a sense, the functional equivalent of mutable elements in an array. We can't exactly do that, since lists are immutable - what we can do is return a new list, that contains all the elements of `xs`, except at the given index, where it contains `x`.

###Finding elements

* `xs indexOf x`  : the index of the first element in `xs` equal to `x`, or -1 if `x` does not appear in `xs`
* `xs contains x` : the same as `xs indexOf x >= 0`

###Implementation Issues

We know that the complexity of `head` is a simple field selection; it's a very small, constant time. Can `last` be implemented as efficiently? `tail` again is just a simple selection of a field of a list, ie constant time; can `init` be implemented as efficiently?

###Implementation of last

```scala
def last[T](xs: List[T]): T = xs match {
	case List() => throw new Error("last of empty list")
	case List(x) => x
	case y :: ys => last(ys)
}
```

So `last` takes steps proportional to the length of the list `xs` - we need to take one recursion for each element in the list.

###Implementation of init

```scala
def init[T](xs: List[T]): List[T] = xs match {
	case List() => throw new Error("init of empty list")
	case List(x) => List()
	case y :: ys => y :: init(ys)
}
```

###Implementation of Concatenation

How can concatenation be implemented? Recall that `xs ::: ys` is really the same as the call of the method ` ::: ` with receiver `ys`, and `xs` as the argument - ie, `ys.:::(xs)`. It's the prepend of `xs` on top of `ys`. Very much like the prepend function we wrote last week, except of an entire list instead of a single element.

So far, everything we've done has been with a pattern-match on the list in question - now there are *two* lists. oh man... which list should we pattern match on?

When we've done this pattern matching we've typically constructed lists from left to right - we were asking the question "what's the first element of the result list, and what's the remainder?".

In this case, the first element of the result list here clearly depends on `xs` - so it makes sense to match on that.

```scala
def concat[T](xs: List[T], ys: List[T]) = xs match {
	case List() => ys
	cast z :: zs => z :: concat(zs, ys)
}
```

What is the complexity of `concat`? Well, it's clear that we'll need a call of `concat` for each element of the left list, so complexity will correspond to the length of the list `xs`.

###Implementation of reverse

```scala
def reverse[T](xs: List[T]): List[T] = xs match {
	case List() => xs
	case y :: ys => reverse(ys) ++ List(y)
}
```

What's the complexity of `reverse`? Well, we know that concatenation is linear, proportional to the size of the list on the left hand size of the operator. That list in this case is a list that grows from 1 to the length of `xs`.

Furthermore, we do one step for each element in the reversed list, because we go through each element of `ys` and put it at the end of the reversed list.

So, taken together, that gives us a quadratic complexity of N * N, which is a bit disappointing. We all know that with an array or a mutable linked list of pointers that we could reverse in linear time - we'll see later on how we might do better.