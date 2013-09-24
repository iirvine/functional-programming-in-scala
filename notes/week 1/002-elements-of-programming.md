#Elements of Programming
Every non trivial programming language provides:

* primitive expressions representing the simplest elements
* ways to combine expressions
* ways to *abstract* expressions, which introduce a name for an expression by which it can then be referred to


###The read-eval-print Loop
Functional programming is a bit like using a calculator... most functional languages have an interactive shell, or REPL, that lets us write expressions and responds with their value. We can start the scala repl by typing scala, or by typing sbt console 

###Evaluation 
A non-primitive expression is evaluated as follows:

* take the leftmost operator
* evaluate its operands (left before right)
* apply the operator to the operands

A name is evaluated by replacing it with the right hand side of its definition.

We apply these steps one by one until an evaluation results in a value - for the moment, a value is just a number.

####Example
>_(2 * pi) * radius_

First we look up the value of pi:
>_(2 * 3.14159) * radius_

Then we perform the arithmetic operation on the left:
>_6.28318 * radius_

Then we look up the value for radius and finally we perform our final multiplication, yielding the result
>_6.28318 * 10_

> _62.8318_

###Parameters
Definitions can have parameters - for instance:

    def square(x: Double) = x * x
    def sumOfSquares(x: Double, y: Double) = square(x) + square(y)

###Parameter and Return Types
Function parameters come with their type, given after a colon. If a return type is given, it follows the parameter list

    def power(x: Double, y: Int): Double = ...

###Evaluation of Function Applications
Applications of parameterized functions are evaluated in a similar way as operators:

* evaluate all function arguments, left to right
* replace the function application by the function's right hand side, and at the same time
* replace the formal parameters of the function by the actual arguments

> _sumOfSquares(3, 2+2)_ 

evaluates to

> _sumOfSquares(3, 4)_

then, we take the definition of sum of squares and plop it in:

>_square(3) + square(4)_

then we repeat the process with the square application

>_3 * 3 + square(4)_

>_9 + square(4)_

>_9 + 4 * 4_

>_9 + 16_

>_25_

###The substitution model
This scheme of expression evaluation is called the substitution model. The idea is that all evaluation does is *reduce an expression to a value*

Simple as it is, it's been shown that it can express every algorithm, and thus is equivalent to a Turing machine.  ([Alonzo Church and the lambda calculus](http://en.wikipedia.org/wiki/Lambda_calculus))

The substitution can be applied to all expressions, as long as they have no side effects. What is a side effect?

    c++
    
Evaluating this expression means that we would: return the old value of c, and at the same time, we increment the value. Turns out there's no good way to represent this evaluation sequence by a simple rewriting of the term; we need something else, like a store where the current value of the variable is kept.

In other words, the express c++ has a side effect on the current value of the value; that side effect cannot be expressed by the substitution model.

One of the motivations for ruling out side effects in FP is that we can keep to a simple model of evaluation.

###Termination
Once we have the substitution model, another question comes up: does every expression reduce to a value (in a finite number of steps?)

Nope. Eg:

    def loop: Int = loop
    loop
    
So, what would happen here? According to our model, we have to evaluate that name by replacing it with what's on its right hand side.

    loop -> loop -> loop....

We have reduced the name to itself, so this expression will never terminate.

###Changing the evaluation strategy
The scala interpreter will reduce function arguments to values before rewriting the function application - that's not the only evaluation strategy.

One could alternatively apply the function to unreduced arguments:

>_sumOfSquares(3, 2 + 2)_
   
In this strategy, we keep the right hand side, and don't reduce 2+2 to 4. We simply pass it as an expression to the square function

>_square(3) + square(2+2)_

We keep it around until the last possible evaluation, even ending up with an evaluation of square(2+2) that yields (2 + 2) * (2 + 2)

>_3*3 + square(2+2)_

>_9 + square(2+2)_

>_9 + (2 + 2) * (2 + 2)_

>_9 + 4 * (2+2)_

>_9 + 4 * 4_

>_25_

###Call by name and Call by value
We've seen two evaluation strategies - the first is call by value, and the last is call by name. 

An important theory of lamda calculus is that both strategies reduce to the same final values, as long as:

* the reduced expression consists of pure functions and
* both evaluations terminate

Call by value has the advantage that every function argument is only evaluated once.

Call by name has the advantage that a function argument is not evaluated at all if the corresponding parameter is unused in the evaluation of the function body

Call by value is basically, reduce all parameters first, then apply functions
Call by name is basically, apply functions, then reduce parameters

###CBN, CBV, and termination
We know that the two evaluation strategies reduce an expression to the same value, as long as both evaluations terminate. What if they don't terminate?

There we have an important theorum, which says if the CBV evaluation of an expression e terminates, then CBN evaluation of e terminates too.

The other direction is *not* true.

For example:

    def first(x: Int, y: Int) = x
    
consider the expression first(1, loop):

Under CBN, we'll reduce the first expression without reducing the argument, and it would just yield 1 in the first step, since first() doesn't do anything with the second parameter.

Under CBV, we have to reduce the arguments to this expression, so we have to reduce loop - loop reduces to itself, infinitely, and we'd make no progress

###Scala's evaluation strategy
Scala normally uses call by value. Why? Well, in practice it turns out that CBV is exponentially more efficient than CBN, because it avoids this repeated recomputation of argument expressions that CBN entails.

Other argument for CBV is that it plays much nicer with imperative effects and side effects, because you tend to know much better when expressions will be evaluated

Except that, sometimes you really want to force CBN - you can do that in Scala by adding a name in front of the type:

    def constOne(x: Int, y: => Int) = 1