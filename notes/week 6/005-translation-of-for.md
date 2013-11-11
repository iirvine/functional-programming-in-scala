##For Expressions and Higher Order Functions

The syntax of `for` is closely related to the higher-order function `map,`, `flatMap`, and `filter`

First of all, these functions can all be defined in terms of `for`:

```scala
def mapFun[T, U](xs: List[T], f: T => U): List[U] =
    for (x <- xs) yield f(x)

def flatMap[T, U](xs: List[T], f: T => Iterable[U]): List[U] =
    for (x <- xs; y <- f(x)) yield y

def filter[T](xs: List[T, p: T => Boolean]): List[T] = 
    for (x <- xs if p(x)) yield x
```

In Reality, it goes the other way - the scala compiler expresses for-expressions in terms of `map`, `flatMap`, and a lazy variant of `filter`. 

Here's a simple example. A simple for expression that consists of just one generator, that consists of arbitrary expressions `e1` and `e2`, will be translated to an application of `map`. 

```scala
for (x <- e1) yield e2

// Translates to:

e1.map(x => e2)
```

A for-expression that has a generator followed by a filter, which is in turn followed by further generators or filters, here subsumed by `s`

```scala
for (x <- e1 if f; s) yield e2
```

can be rewritten to another for expression, that contains a generator, and the filter has been absorbed into the generator:

```scala
for (x <- e1.withFilter(x => f); s) yield e2
```

At first approximation, we can read `withFilter` like `filter`; the generator will be reduced to all those elements that pass the condition `f`.

`withFilter` is actually a _lazy_ variant of `filter`, meaning, it doesn't immediately produce a new datastructure of all the filtered elements. That would be _wasteful_.

Instead, it remembers that any following call to `map` or `flatMap` has to be filtered by the function `f`. 

The third and last form of for-expressions is the one where a leading generator is followed not by a filter but another generator:

```scala
for (x <- e1; y <- e2; s) yield e3
```

Again that can be followed by an arbitrary sequence of filters and generators `s`. That for expression will be translated into a call of `flatMap`.

The idea here is that we take the for expression that takes all the remaining computations (so, we generate a `y` from `e2`, and do some more stuff, and then yield `e3`), that would be a collection-valued operation, because `y <- e2` is a generator.

So what we need to do is take everything that comes out of this for-expression and `flatMap` it, concatenating it all into the result list. Which is precisely what happens:

```scala
e1.flatMap(x => for (y <- e2; s) yield e3)
```

So what happened in the first case is that we translated directly into an application of `map`; in the second and third case, we translated into another for-expression that has one less element, either one fewer filter, or one fewer generator.

Each of these translation steps can be repeated, yielding simpler and simpler for-expressions until finally we must hit the simplest case that must translate to a map.

Take the for expression that computed pairs whose sum is prime:

```scala
for {
    i <- 1 until n
    j <- 1 until i
    if isPrime(i + j)
} yield (i, j)
```

Applying the translation scheme to this expression gives us:

```scala
(1 until n).flatMap(i =>
    (1 until i).withFilter(j => isPrime(i+j))
        .map(j => (i, j)))
```

This is almost exactly the expression we came up with first! _WOW_.

Let's translate a query on our books database, like 

```scala
for (b <- books; a <- b.authors if a startsWith "Bird")
yield b.title
```

```scala
books.flatMap(b => 
    // translated to:
    // for (a <- b.authors if a startsWith "Bird") yield b.title)
    // translated to:
    // for (a <- b.authors withFilter(a => a.startsWith "Bird")) yield b.title
    //translated to:
    // b.authors withFilter(a => a.startsWith "Bird") map (y => y.title)
```

Interestingly, the translation of for is not limited to just lists or sequences, or even collections; it is based solely on the presence of the methods `map`, `flatMap`, and `withFilter`.

This lets us use the syntax for our own types as well - we must only define those three functions for these types. 

There are many types for which this is useful; arrays, iterators, databases, XML data, optional values, parsers.

###For and Databases
For example, `books` might not be a list, but a database stored on some server.

As long as the client interface to the database defines the methods `map`, `flatMap`, and `withFilter`, we can use the `for` syntax for querying the database. 

This is the basis for the scala data base connection frameworks, like ScalaQuery and Slick. Similar ideas underly Microsoft's LINQ framework.