##Blocks and Lexical Scoping

It's good functional programming style to split up a task into many small functions; but the names of functions like sqrtIter, improve, and isGoodEnough matter only for the *implementation* of sqrt, not for its usage.

Normally we would not like users to access these functions directly - we wan't to avoid namespace pollution by putting the auxciliary functions inside of sqrt

The way we do that is using a block:

```scala
{
	val x = f(3)
	x * x
}
```

A block is delimited by braces; it contains a sequence of definitions or expressions.

The last element of a block is an expression that defines its values.

Blocks are expressions in scala, so they can be used everywhere an expression can - including the right hand side of a function definition.

```scala
def sqrt(x: Double) = {  }
```

###Blocks and Visibility

The definitions inside a block are only visible from within the block

The definitions inside a block *shadow* definitions of the same names outside the block.

```scala
val x = 0
def f(y: Int) = y + 1
val result = {
	val x = f(3)
	x * x
}
```

f is visible here - it refers to the outer block. but the name x here refers to the inner x - it is shadowing the outer name.

###Lexical Scoping
Definitions of outer blocks are visible inside a block unless they are shadowed;

###Semicolons
In scala, semicolons at the end of lines are in most cases optional. The only case where you would need them is when you want to put multiple statements on one line, eg:

```scala
val y = x + 1; y * y
```

How to write expressions that span several lines? Two ways:

![img](http://i.imgur.com/vSsO9eP.png)

or,

![img](http://i.imgur.com/vWDT0lq.png)
