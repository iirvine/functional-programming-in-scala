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

Here's a simple example: 

```scala
for (x <- e1) yield e2

// Translates to:

e1.map(x => e2)
```

