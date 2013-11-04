##Other Sequences

We have seen that lists are *linear* - access to the first element is much faster than access to the middel or end of a list.

Scala also has an alternative sequence implementation, `Vector`, which has more evenly balanced access patterns than `List`. Vectors are essentially very very shallow trees.

A vector of up to 32 elements is just an array, where the elements are stored in sequence. If a vector becomes larger than 32 elements, its representation changes - we would then have a vector of 32 pointers to arrays of 32 elements. Once that is exhausted (ie, we have 32 * 32 elements), the representation changes again, and becomes a vector of pointers to pointers to arrays of 32 elements, everything becomes one level deeper. You get the idea.

![img](http://i.imgur.com/cwjsc4w.png)

How much time would it take to retreive an element at some index in a vector? For lists, it very much depends on what the index is; fast for 0, linearly slow for indices towards the end of the list.

Vectors are much better behaved; to get an index of a vector of length 32 is a single index access. If the vector has size up to about 1000, it's about 2 accesses. Generally the number of accesses are equal to the depth of the vector. That depth grows very slowly - a depth of 6 gives you a billion elements.

Another advantage of vectors is that they're fairly good on bulk operations that traverse a sequence; like a `map` that applies a function to every element of a sequence or a `fold` that reduces adjacent elements with an operator. For a vector we can do those types of things in chuncks of 32, which happens to coincide quite closely to the cache line in modern processors. Meaning that all the 32 adjacent elements will be in a single cache line and accesses will be fairly fast.

For lists on the other hand, you have that recursive structure where each element is in a cons cell with a pointer to the next, and you have no guarantee that those cons cells are anywhere near each other - they might be in different cache lines, different pages, so the locality for list accesses could be much worse than the locality for vector accesses.

If vectors are so much better, why keep lists at all? Well, it turns out that if your operations fit into the model that you're usually just after taking the head of a sequence (which for lists is a constant time operation and for vectors might mean going down several layers) and then taking the tail to process the rest (which again is constant time for lists and more complicated for vectors), then lists are much better. Basically, if your access patterns have this recursive structure, lists are what you want.

If however your access patterns are typically bulk operations, `map`, `fold`, `filter`, then a vector would be preferable.

###Operations on Vectors

Vectors are created analogously to lists

```scala
val nums = Vector(1, 2, 3, -88)
val people = Vector("Bob", "James", "Peter")
```

They support the same operations as lists, with the exception of ::. Because :: in a list is the primitive thing that builds the list and lets us pattern match against it.

Instead of :: Vectors have

* `x +: xs` : create a new vector with leading element `x` followed by all elements of `xs`
* `xs :+ x` : create a new vector with trailing element `x`, preceded by all elements of `xs`

Note that `:` always points to where the sequence is.

What would it take to append an element to a Vector - recall that all scala's collections are immutable, so we have to create a new Vector, we can't touch the existing one. Here's a picture that illustrates it perfectly:

![img](http://i.imgur.com/6YOtuzz.png)

Nailed it.

Basically, we'll take the last array of our vector, and create a new one that contains the element we're appending. That gives us a new array of 32 elements, which we then have to combine somehow with the original vector. We can't change the pointer from the original to the new array, because that of course would change the old vector.

So, we create another copy of the root array, that points to our new element, and also points to the other elements our previous copy pointed to. Finally, we have to create another root, which points again to our new copy and the old immediate descendants. And we're done! The new vector is in red, whereas the blue one wasn't touched at all.

Analyzing the complexity of this, we see we have to create a new 32 element array for every level we did the change - in our case, three of these arrays would've been created.

###Collection Hierarchy

A common base class of `List` and `Vector` is `Seq`, the class of all *sequences*. `Seq` itself is a subclass of `Iterable`.

###Arrays and Strings

Arrays and Strings both support the same operations as `Seq` and can be implicitly converted to sequences where needed. They're not really subclasses of `Seq` since they come from the Java universe.

```scala
val xs = Array(1, 2, 3, 4)
xs map (x => x * 2)
```

###Ranges

a range simply represents a sequence of evenly spaced integers. There are three common operators:

* `to` (inclusive)
* `until` (exclusive)
* `by` (to determine step value)

```scala
val r: Range 1 until 5 // 1, 2, 3, 4
val s: Range 1 to 5    // 1, 2, 3, 4, 5

1 to 10 by 3   // 1, 4, 7, 10
6 to 1 by -2   // 6, 4, 2
```

Ranges are represented as single objects with three fields: the lower bounds, the upper bounds, and the step value.

###More Sequence Operations

SO MANY!

![img](http://i.imgur.com/9EdwTWq.png)

###Combinations

To list all combinations of numbers `x` and `y` where `x` is drawn from `1..M` and `y` is drawn from `1..N`

```scala
(1 to M) flatMap (x => (1..N) map (y => (x, y)))
```

###Scalar Product

The scalar product of two vectors is the sum of the product of corresponding elements Xi and Yi of the two vectors. We can take the mathematical definition and map it directly to code

```scala
def scalarProduct(xs: Vector[Double], ys: Vector[Double]): Double =
	(xs zip ys).map(xy => xy._1 * xy._2).sum
```

An alternative way to write this is with a *pattern matching function value*

```scala
def scalarProduct(xs: Vector[Double], ys: Vector[Double]): Double =
	(xs zip ys).map { case (x,y) => x * y }.sum
```

Generally the function value `{case p1 => e1 ... case pn => en }` is just equivalent short hand to `x => x match { case p1 => e1 ... case pn => en }`