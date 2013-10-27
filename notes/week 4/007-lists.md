##Lists

A list having `x1, ..., xn` as elements is written `List(x1, ..., xn)`

```scala
val fruit = List('apples', 'banannas', 'pears')
val nums = List(1, 2, 3, 4)
val diag3 = (List(List(1, 0, 0)))
```

Lists are sequences, just like arrays - but there are two important differences. Lists are immutable, so you can't change an element of a list. Second, lists are recursive, while arrays are flat. They're very much like the Cons list we constructed from scratch before.

A list like `val fruit = List("apples", "oranges", "pears")` would look like this:

![img](http://i.imgur.com/0fII5qp.png)

Something more complicated like val List(List(1, 0, 0), List(0, 1, 0), List(0, 0, 1)) would look like

![img](http://i.imgur.com/Bm58iGI.png)

###The List Type

Like arrays, lists are homogeneous; the elements of a list must all have the same type. The tye of a list with elements of type `T` is written `scala.List[T]` or just `List[T]`

###Constructors of Lists

We've seen lists constructed homogeneously; we've written `List('some', 'elements')`; that's actually syntactic sugar for something more fundamental. All lists in scala are constructed from the empty list `Nil`, and the construction operation :: (pronounced *cons*): `x :: xs` gives a new list with the first element `x`, followed by the elements of `xs`

```scala
fruit = "apples" :: ('oranges' :: ('pears' :: Nil))
```

###Right Associativity

Convention: Operators ending in ":" associate to the right.

That means that if we have two double colons, like `A :: B :: C`, it's interperted as `A :: (B :: C)`

So we can omit the parentheses. We can create new lists like this: `val nums = 1 :: 2 :: 3 :: 4 :: Nil`

###Operations on Lists

All operations on lists can be expressed in terms of the following three operations:

`head` which is the first element in the list
`tail` the list composed of all elements except the first
`isEmpty` '`true`' if the list is empty, `'false'` otherwise

These operations are defined as methods on objects of type list. For example:

```scala
fruit.head == 'apples'
fruit.tail.head == 'oranges'
```

###List Patterns

It's also possible and useful to decompose lists with pattern matching.

`Nil` 					the `Nil` constant
`p :: ps`  				A pattern that matches a list with a `head` matching `p` and a `tail` matching `ps`
`List(p1, ..., pn)`		same as `p1 :: ... :: pn :: Nil`


`1 :: 2 :: xs`		Lists that start with `1` and then `2`; the rest of the list is arbitrary and bound to the variable `xs`

###Sorting Lists

Suppose we want to sort a list of numbers in ascending order

We could sort the list `List(7, 3, 9 , 2)` by sorting the tail `List(3, 9, 2)`, to obtain `List(2, 3, 9)`. The next step is to insert the head 7 in the right place to optain the result `List(2, 3, 7, 9)`

This idea is *insertion sort*:

```scala
def isort(xs: List[Int]): List[Int] = xs match {
	case List() => List()
	case y :: ys => insert(y, isort(ys))
}
```

This is the most standard way to decompose a list; first we ask if a list is empty, and if not, we ask, well what is its head and tail.

We still have to define the function `insert`.

```scala
def insert(x: Int, xs: List[Int]): List[Int] = xs match {
	case List() => List(x)
	case y :: ys => if (x <= y) x :: xs else y :: insert(x, ys)
}
```