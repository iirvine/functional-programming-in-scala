##Scala Syntax

###Language elements seen so far:

We have seen language elements to express types, expressions, and definitions.

Here's the Extended Backus-Naur form (EBNF) for them. Wait what's Extended Backus-Naur form (EBNF)? It's a standard grammar to describe language elements.

> | denotes an alternative
> [...] an option (0 or 1)
> {...} a repetition (0 or more)

###Types

![types](http://i.imgur.com/gpOW5jm.png)

We've seen two forms of types (thus we've got a bar for Type = ...). A type in our language is either a SimpleType, or a FunctionType.

A simple type so far is just an identifier.

A function Type always contains an arrow; the left hand side of the arrow could be a SimpleType, or a set of Types in parentheses.

A *type* can be:

* a numeric type - `Int`, `Double`, (and `Byte`, `Short`, `Char`, `Long`, `Float`)
* The `Boolean` type with the values `true` and `false`
* the `String` type
* a *function type*, like `Int => Int`, `(Int, Int) => Int`

Later we'll see more forms of types.

###Expressions

![expressions](http://i.imgur.com/rzLeGbP.png)

An *expression* can be:

* An identifier, such as `x`, `isGoodEnough`
* a literal, like `0`, `1.0`, `"abc"`
* a function application, like `sqrt(x)`
* an operator application, like `-x`, `y + x`
* a selection, like math.abs
* a conditional expression like `if (x < 0) - x else x`
* a block, like `{ val x = math.abs(y ; x * 2) }`
* an anonymous function, like `x => x + 1`

###Definitions

![definitions](http://i.imgur.com/KuMIh5t.png)

A *definition* can be:

* a *function definition*, like `def square(x: Int) = x * x`
* a *value definition*, like `val y = square(2)`

A *parameter* can be

* a call-by-value parameter, like `(x: Int)`,
* a call-by-name parameter, like `(y: => Double)`

That's all the syntax we've seen so far - we'll add more as we need them over the next few weeks.