##Implicit Parameters

Problem: our mergesort from last session can also be used for `List[Int]` - it makes sense to apply the same function to lists of other element types, `Double`, `Boolean`, etc.

The most straight forward way to do that would be to parameterize `msort`, so instead of `List[Int]`, it'll take a `List[T]`:

```scala
def msort[T](xs: List[T]): List[T] = ...
```

But aw crap, that doesn't work! The comparison < in `merge` is not defined for arbitrary types `T`. Now that our list elements can have an arbitrary type, we can no longer be sure that there is in fact a less than function defined on those elements.

So, what can we do? Well, what  if we parameterized `merge` with the necessary comparison function?

```scala
def msort[T](xs: List[T])(lt: (T, T) => Boolean)= {
	...
	merge(msort(fst)(lt), msort(snd)(lt))
}
```

and merge would look like:

```scala
def merge(cs: List[T], ys: List[T]) = (xs, ys) match {
	...
	case (x :: xs1, y :: ys1) =>
		if (lt(x, y)) ..
		else ...
}
```

Then we would call it like:

```scala
val nums = List(2, -4, 5, 7, 1)
msort(nums)((x, y) => x < y)

val fruits = List("apple", "pineapple", "orange", "banana")
msort(fruits)((x, y) => x.compareTo(y) < 0)
```

###Parametrization with Ordered

There's already a class in the standard library that represents ordering... `scala.math.Ordering[T]` provides ways to compare elements of type `T`. So instead of parameterizing with the `lt` operation directly, we could parameterize with `Ordering` instead:

```scala
def msort[T](xs: List[T])(ord: Ordering) =
	def merge(xs: List[T], ys: List[T]) =
	...	if (ord.lt(x,y)) ...
... merge(msort(fst)(ord), msort(snd)(ord))
```

now we can use the predefined orderings in our calls:

```scala
msort(nums)(Ordering.Int)
```

There's just one problem - this sucks. Passing around these `lt` or `ord` values is rather cumbersome - it would be nice if we could just synthesize the right comparison operation directly just given the type `T`. We can make it at least appear that way by not passing `Ord` explicitly and making it an *implicit* parameter....


```scala
def msort[T](xs: List[T])(implicit ord: Ordering) =
	def merge(xs: List[T], ys: List[T]) =
	...	if (ord.lt(x,y)) ...
... merge(msort(fst), msort(snd))

msort(nums)
msort(fruits)
```

HOW DID THAT HAPPEN

When we write an implicit parameter, and we don't write an actual argument that matches that parameter, the compiler will figure out the right implicit to pass based on the demanded type.

The compiler will search for an implicit definition that

* is marked `implicit`
* has a type compaitble with `T`
* is visible at the point of the function call, or is defined in a companion object associated with `T`

If there is a single (most specific) definition, it will be taken as the actual argument for the implicit parameter. Otherwise, it's an error.