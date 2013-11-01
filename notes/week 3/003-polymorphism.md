###Polymorphism

As a motivating example, let's look at a datastructure that's been fundamental in many functional languages from the beginnings - the Cons-List

It is an immutable linked list, constructed from two building blocks:

* the empty list, which we call `Nil`
* a cell containg an element and a pointer to the remainder of the list, called `Cons`

Here's what `List(1,2,3)` would look like:
![img](http://i.imgur.com/Cbla4yn.png)

We'd have a reference to a `Cons` cell that contains the 1, and a reference to a `Cons` cell that contains 2, which has a reference to a `Cons` cell that contains 3, which has a reference to `Nil`

A nested list like `List(List(true, false), List(3))` would look like:
![img](http://i.imgur.com/XYcKqde.png)

###Cons-Lists in scala
How would we write this as a class hierarchy in scala?

Here's an outline that would represent lists of integers in this fashion:

```scala
trait IntList ...
class Cons(val head: Int, val tail: IntList) extends IntList ...
class Nil extends IntList ...
```

Notice there's a bit of new syntax in the `Cons` declaration - `val head: Int` defines a parameter and a field definition in the class itself. It's equivalent to:

```scala
class Cons(_head: Int, _tail:IntList) extends IntList {
	val head = _head
	val tail = _tail
}
```

A list is either

* an empty list `new Nil`, or
* a list `Cons(x, xs)` consisting of a `head` element x and a `tail` list xs

###Type Parameters
There's one problem with our type hierarchy - it's way too narrow to only define lists with `Int` elements. If we did it that way, we'd need a type hierarchy for `Double`, `Boolean`, and so on...

What we need to do is generalize the definition - we can do that using the `type` parameter

```scala
trait List[T]
class Cons[T](val: head T, val tail: List[T]) extends List[T]
class Nil[T] extends List[T]
```

So, we're going to define a base trait, List, which takes a type parameter `T`. That base trait List[T] will have two subclasses, `Cons[T]` and `Nil[T]`. `Cons[T]` will now have a head element of type `T`, and a tail element of type `List[T]`

###Generic Functions
Like classes, functions can have type parameters. For instance, here's a function that creates a list consisting of a single element:

```scala
def singleton[T](elem: T) = new Cons[T](elem, new Nil[T])
```

We can then write:

```scala
singleton[Int](1)
singleton[Boolean](true)
```

###Type Inference
The scala compiler can usually deduce the correct type parameters from the value arguments of a function call - so in most cases the type parameters can be left out. The above could just be written as:

```scala
singleton(1)
singleton(true)
```

###Types and Evaluation

Type parameters do not affect evaluation in scala at all - we can assume that all type parameters and type arguments are removed before evaluation of the program. This is called *type erasure*. Types are only important for the compiler to verify that the program satisfies certain correctness properties, but they're not relevant for the actual execution.

###Polymorphism
Polymorphism means that a function type comes "in many forms" - basically, the function can be applied to arguments of many types, or the type can have instances of many types.

We have seen two principle forms of polymorphism - subtyping, and generics.

Subtyping means that instances of a subclass can be passed to a base class - ie, given our `List` hierarchy, anywhere we have a parameter that accepts type `List`, we can pass either a `Nil` or a `Cons`.

Generics means that we can create many instances of a function or class by type parameterization. By using generics, we could create a `List[Int]`, or a `List[List[Boolean]]`, whatever dawg.
