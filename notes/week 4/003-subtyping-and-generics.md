##Polymorphism

There are two principle forms:

* subtyping, where we can pass instances of a subtype where a basetype was required
* generics, where we can parameterize types with other types

We're going to look at the interactions between the two concepts. There are two main areas:

* bounds, where we can subject type parameters to some type constraints
* variance, which defines how parameterized types behave under subtyping

###Type Bounds

Let's say we wanted to write a method `assertAllPos`, which takes an `IntSet` and returns the `IntSet` itself if all its elements are positive, and throws an exception otherwise.

What would be the best type we can give to `assertAllPos`? Maybe


```scala
def assertAllPost(s: IntSet): IntSet
```

In most situations this is fine, but can one be more precise?

If 	`assertAllPos` gets an empty set, it returns Empty - if it gets a non-empty argument, it will give you back a non-empty result. That knowledge is not reflected in the type above, we just say it takes an IntSet and returns an IntSet.

###Type Bounds

One way to express this might be with type bounds, like so:

```scala
def assertAllPos[S <: IntSet](r: S): S = ...
```

Here, we're saying that `assertAllPos` takes *some type* `S`, which has to be a subtype of `IntSet` (either `Empty` or `NonEmpty`), and a set of that type (r), and we return a result of the same type.

Here, the `<: IntSet` is an upper bound of the type parameter `S`; what it means is that we can instantiate `S` to any type argument as long as the type conforms to the bound

Generally:

* `S <: T` means *S is a subtype of T*
* `S >: T` means *S is a supertype of T, or T is a subtype of S*

###Lower Bounds

We can also use a lowery bound for a type variable

```[S >: NonEmpty]```

introduces a type parameter `S` that can range only over *supertypes* of `NonEmpty`; so `S` could only be one of `NonEmpty, IntSet, AnyRef,` or `Any`.

It's not immediately apparent where this can be useful - we'll circle back to this.

###Mixed Bounds

Finally, it's possible to mix a lower and upper bound:

```[S >: NonEmpty <: IntSet]```

That would restrict `S` to any type on the interval between `NonEmpty` and `IntSet`

###Covariance

There's another interaction between subtyping and type parameters to consider - given `NonEmpty <: IntSet`, is `List[NonEmpty] <: List[IntSet]`?

What if we've wrapped both types in a list? Should a list of `NonEmpty` also be a subtype of list of `IntSet`

Intuitively, this makes sense - a list of non-empty sets is a special case of a list of arbitrary sets. From a domain modeling perspective, a list of non-empty should indeed be a subtype of a list of `IntSet`s.

We call types for which this relationship holds *covariant* because their subtyping relationship varies exactly like the type parameter. So, `List` is a covariant type. Does covariance make sense for all types, not just for `List`?

###Arrays

For perspective, let's look at the concept of arrays in Java (also C#)

Just a reminder, in those languages an array of elements of type `T` is written `T[]`; in scala we use parameterized type syntax  to refer to the same type: `Array[T]`

Arrays in Java are covariant, so one would have: `NonEmpty[] <: IntSet[]`

But covariant array typing causes problems. Think about this:

```java
NonEmpty[] a = new NonEmpty[]{ new NonEmpty(1, Empty, Empty) }
IntSet[] b = a
b[0] = Empty
NonEmpty s = a[0]
```

OH GOD. It looks like we just assigend an `Empty` set to a variable of type `NonEmpty`. If types are supposed to prevent something it's clearly this.

Our third line would give us a runtime exception, an `ArrayStoreException`; this protects the assignment of `Empty` into this array. To make up for the problems of covariance in arrays, Java needs to store in every array a type tag that reflects what type the array was created with. When we assign something into the array, the runtime type of the item is checked against the type tag.

This doesn't seem like a great deal - we've traded a compile time error for a runtime error, and incurred the runtime cost of checking types everytime something is put into an array. One could argue that it was a mistake to make arrays covariant; it created a hole in the type system that had to be patched.

So when does it make sense for a type to be a subtype of another, and when does that not make sense?

###Liskov Substitution Principle

Good ole' Barbara Liskov has got your back on this one.

>If `A` <: `B`, then everything one can do with a value of type `B` should also be able to do with a value of type `A`

Essentially, we should be able to substitute and `A` for a `B` and do all the same things.