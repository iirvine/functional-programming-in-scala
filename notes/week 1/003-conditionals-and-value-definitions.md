###Conditional Expressions

To express choosing between two alternatives, Scala has a conditional expression if-else

It looks like a if-else in Java, but is used for expressions, not statements

```scala 
def abs(x: Int) = if (x >= 0) x else -x
```

x >=0 is a *predicate*, of type Boolean

####Boolean expressions 

can be composed of 

> true false //Constants

> !b //Negation

> b && b   //Conjunction

> b || b  //Disjunction

####Rewrite rules for Booleans
How do we define meaning for boolean expressions? Well, simply by giving rewrite rules, which give some template for boolean expressions on the left, and how to rewrite it on the right

> !true --> false

> !false --> true

True and some other expression *e* will always give you the same as *e*; false and some other expression *e* will always give you false
> true && e --> e

> false && e --> false

Rules for or are analogous for rules of and; they're the duals of those
> true || e --> true

> false || e --> e

Note that && and || do not always need their right operand to be evaluated; we say these expressions use short-circuit evaluation

####Value Definitions
We have seen that function parameters can be passed by value or be passed by name - the same distinction applies to definition. 

The ```def``` form is in a sense, call by name; it's right and side is evaluated on each use. 

There is also a ```val``` form, which is "by-value"; eg:

```scala
val x = 2
```

```scala
val y = square(x)
```

The right hand side of a val definition is evaluated at the point of the definition itself. Afterwards, the name refers to the value - for instance, y above refers to 4, not to square(2).

####Value Definitions and Termination
The difference between the ```val``` and ```def``` forms becomes apparent when the right hand side doesn't terminate

```scala
def loop: Boolean = loop
```

If we say ```def x = loop```, nothing happens - we just defined another name for loop. Whereas, if we define ```val x = loop```, we're caught in an infinite loop. 
