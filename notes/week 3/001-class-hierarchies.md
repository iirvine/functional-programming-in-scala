##Class Hierarchies

###Abstract Classes
Consider the task of writing a class for sets of integers with the following operations:

```scala
abstract class IntSet {
	def incl(x: Int): IntSet
	def contains(x: Int): Boolean
}
```

`IntSet` is an *abstract class*

Abstract classes can contain members which are missing an implementation. Consequently, no instances of an abstract class can be created with the `new` operator

###Class Extensions
Let's consider implementing sets as binary trees.

The invariant that we want to maintain is that for each node, the nodes on its right hand side all have integer values that are higher than the node, and the nodes on its left all have values that are less

There are two types of possible trees: a tree for the empty set, and a tree consisting of an integer and two sub-trees.

```scala
class Empty extends IntSet {
	def contains(x: Int): Boolean = false
	def incl(x: Int): IntSet = new NonEmpty(x, new Empty, new Empty)
}
```

So, what's a NonEmpty set?

In this implementation, it'd be represented by a class that takes an element (the integer stored in the node), and a left and a right subtree (in this case, an IntSet).

```scala
class NonEmpty(elem: Int, left: IntSet, right: IntSet) extends IntSet {
	def contains(x: Int): Boolean =
		if (x < elem) left contains x
		else if (x > elem) right contains x
		else true

	def incl(x: Int): IntSet =
		if (x < elem) new NonEmpty(elem, left incl x, right)
		else if (x > elem) new NonEmpty(elem, left, right incl x)
		else this
}
```

The implementation of contains and includes makes use of the sorted criterion of trees; for contains, we always only have to look in one of the possible subtrees. If the given number is less than the current element value, then we know we'll have to look in the left subtree. If it's greater, then we look in the right. If it's neither less or greater, it must be equal, in which case we've found the element.

`incl` follows a similar algorithm - if the element we're adding is less than the element in the tree, we put it in the left subtree. If it's greater, we put it in the right. Otherwise, the element is already in the tree and we can return the tree as-is, there's nothing to be added.

One important thing to note is that we're still purely functional - there's no mutation here. When we say we're "including" an element in a subtree, we mean we're creating a new tree that contains the previous element of the tree and a larger left subtree where x is included in the previous left subtree, and the current subtree on the right.

Let's say we've got this tree here, and we want to include the number 3 in it:
![img](http://i.imgur.com/fEiJdZj.png)

Here's how it works. We'd create the new node 3, with two empty subtrees, which would be the left subtree of a new node 5, with an empty right hand subtree, and finally, the tree would be a new tree, with the node 7, and the same right hand side tree as before.
![img](http://i.imgur.com/ZMpps3u.png)

So really, we end up with *two trees* - the old one, and the new one. The two trees share the subtree on the right hand side, but they differ on the left tree.

These are called *persistent* data structures; even when we do "changes" to them, the old data structure is maintained, it doesn't go away.

###Terminology
`Empty` and `NonEmpty` both extend the base class `IntSet` - this implies that the types `Empty` and `NonEmpty` conform to the type `IntSet`. In other words, an object of type `Empty` or `NonEmpty` can be used where an object of type `IntSet` is required.


###Object Definitions
In the `IntSet` example, one could argue that there is really only a single empty `IntSet` - it seems overkill to have the user create many instances of it. We can epxress this case better with an *object definition*:

```scala
object Empty extends IntSet {
	def contains(x: Int): Boolean = false
	def incl(x: Int): IntSet = new NonEmpty(x, new Empty, new Empty)
}
```

This defines a *singleton object* named `Empty`. No other `Empty` instance can be (or need to be) created. Singleton objects are values, so `Empty` evaluates to itself.

###Programs
So far we've just executed code from the REPL or the worksheet, but it's also possible to create standalone applications in Scala - each such application contains an object with a `main` method.

For instance, here is the "Hello World!" program in Scala:

```scala
object Hello {
	def main(args: Array[String]) = println("hello world!")
}
```

###Dynamic Binding
Object oriented languages (including scala) implement *dynamic method dispatch* - this means that the code invoked by a method call depends on the runtime type of the object that contains the method.

```
Empty contains 1
-> [1/x][Empty/this] false
= false
```

What do you do? Well, you look up the `contains` method in empty, and performing the necessary substitutions we get false.