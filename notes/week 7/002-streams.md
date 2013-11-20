#Streams

We've seen a number of immutable collections that provide powerful operations for combinatorial serach; for instance, if we wanted to find the second prime number between 1000 and 10000: `((1000 to 10000) filter isPrime)(1)`

This is much shorter than the recursive alternative

![img](http://i.imgur.com/FWtrunV.png)

However, the shorter version has a serious problem - its evaluation is very, very inefficient. 

It constructs *all* prime numbers between 1000 and 10000 in a list, but only ever looks at the first two elements of that list. 

Reducing the upper bound would speed things up - maybe our upper bound 10000 is too high and we should reduce it. But not knowing *a priori* where the prime numbers are, we'd always risk that we miss the second prime number all together. We're in the uncomfortable position of either having really bad performance because the upper bound is too high, or not finding the prime number at all because the bound is too low.

###Delayed Evaluation
However, we can make the short code efficient with a trick - we'll *avoid computing the tail of a sequence unil it is needed for the evaluation result (which might be never)*. 

That idea is implemented in a new class, the `Stream`. They're similar to lists, but their tail is evaluated only on demand.

###Defining Streams
Streams are defined from a constant `Stream.empty` and a constructor `Stream.cons`.

For instance: 

```scala
val xs = Stream.cons(1, Stream.cons(2, Stream.empty))
```

They can also be defined by using the `Stream` object as a factory (`Stream(1, 2, 3)`)

the `toStream` method on a collection will turn the collection into a stream: `(1 to 1000).toStream`

The result of that call is a `Stream[Int] = Stream(1, ?)` what's that mean? Well, a stream is essentially a recursive structure like a list, so we have a one in the first node, but the tail is not yet evaluated.

![img](http://i.imgur.com/59zFeJS.png)

The tail will be evaluated when somebody asks for it explicitly.

###Stream Ranges
Lets try to write a function that returns `(lo until hi).toStream` directly:

```scala
def streamRange(lo: Int, hi: Int): Stream[Int] =
    if (lo >= hi) Stream.empty
    else Stream.cons(lo, streamRange(lo + 1, hi))
```

Let's compare that to a function that does the same thing for lists:

```scala
def listRange(lo: Int, hi: Int): List[Int] = 
    if (lo >= hi) Nil
    else lo :: listRange(lo + 1, hi)
```

It turns out these two functions are completely isomorphic - they have exactly the same structure, only one returns a stream and one returns a list. But their operational behavior is completely different!

`listRange(1, 10)` would generate the complete list in one go:

![img](http://i.imgur.com/TfJk6ec.png)

`streamRange(1,10)` would generate one cons square and then stop - the rest would be a question mark. Instead there's an object that knows how to reconstitue the stream if somebody demands it.

![img](http://i.imgur.com/lvHz3oL.png)

###Methods on Streams
`Stream` supports almost all methods on `List` - for instance to find the second prime number between 1000 and 10000: `((1000 to 10000).toStream filter isPrime)(1)`

###Stream Cons Operator
The one major exception is ::.

`x :: xs` always produces a list, never a stream. 

There is an alternative operator, `#::` which produces a stream. 

###Implementation of Streams

```scala
trait Stream[+A] extends Seq[A] {
    def isEmpty: Boolean
    def head: A
    def tail: Stream[A]
}
```

Here's a concrete implementation:

```scala
object Stream {
    def cons[T](hd: T, tl: => Stream[T]) = new Stream[T] {
        def isEmpty = false
        def head = hd
        def tail = tl
    }
    val empty = new Stream[Nothing] {
        def isEmpty = true
        def head = throw new NoSuchElementException("empty.head")
        def tail = throw new NoSuchElementException("empty.tail")
    }
}
```

Notice that the `tl` paramater for the `cons` method here is a by-name parameter; this differs from the `List` `cons` class, where it is a normal parameter.

Because `tl` is a call by name parameter, when we first construct the `cons` cell for a stream the tail is not evaluated. It'll be evaluated the first time somebody dereferences the `tl` parameter - in this case, that'll happen when somebody calls the `tail` method.

###Other Stream Methods
Other stream methods are implemented analogously to their list counterparts. Here's `filter`:

```scala
class Stream[+T] {
    ...
    def filter(p: T => Boolean): Stream[T] = 
        if (isEmpty) this
        else if (p(head)) cons(head, tail.filter(p))
        else tail.filter(p)
}
```

Notice that in the case of a `head` element that passes the predicate function `p`, we do a computation of `tail.filter(p)`, but that computation is the second, `tl` parameter of a `cons` construction. That means the evaluation of `filter` down the spine of the stream will be delayed again until somebody wants to find out what the result of taking the tail of the result stream is.