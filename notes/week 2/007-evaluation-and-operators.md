##Classes and Substitutions

We previously defined the meaning of a function application using a computation model based on substitution - now we extend this model to classes and objects

How is an instantiation of the class `new C(e1, ..., en)` evaluated?

Answer: the expression arguments `e1,...,em`, are evaluated just like the arguments of a normal function - that's it. The resulting expression, say, `new C(v1, ..., vm)` is already a value. We just take these new instance creation expressions as values.

Now suppose we have a class definition like this:

```scala
class C(x1,...,xm) { def f(y1,...,yn) = b ... }
```

where

* the formal parameters of the class are `x1,...,xm`
* the class defines a method `f` with formal parameters `y1,...,yn`

How is the following expression evaluated?

```new C(v1,...,vm).f(w1,...,wn)```

Answer: the expression is rewritten to:
![img](http://i.imgur.com/E87F0Rp.png)

There are three substitusions at work here:

* the substitution of the formal parameters `y1,...,yn` of the function `f` by the arguments `w1,...,wn`
* the substitution of the formal parameters `x1,...,xm` of the class `C` by the class arguments `v1,...,vm`
* the substitution of the self reference *this* by the value of the object `new C(v1,...,vn)`

###Operators

In principle, the rational numbers defined by `Rational` are as natural as integers.

But for the user of these abstractions, there is a noticeable difference: we write x + y, if x and y are integers, but we write r.add(s) if r and s are rational numbers.

In Scala, we can eliminate this difference. There are two steps.

###Step 1: Infix Notation

Any method with a parameter can be used like an infix operator, so we can write

```
		/* in place of */
r add s  					r.add(s)
r less s 					r.less(s)
r max s 					r.max(s)
```

###Step 2: Relaxed Identifiers

Operators can be used as identifiers. Normally in programming languages, operators must be alphanumeric. They start with a letter, followed by a sequence of letters or numbers. In scala we have an alternative, *symbolic identifiers*, that start with an operator symbol (like +, -), followed by other uperator symbols.

In this definition, the underscore character counts as a letter. Alphanumeric identifiers can also end in an underscore, followed by some operator symbols.

###Precedence Rules

Wait a second... if all operators are user-defined, how is their precedence established? There's actually one universal rule in scala - the precedence of an operator is determined by its first character. Here's a table listing the characters in increasing order of priority precedence:
![img](http://i.imgur.com/oL1GfGg.png)

