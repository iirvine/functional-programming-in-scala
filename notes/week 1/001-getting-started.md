# Week One: Getting Started

##Programming Paradigms
Three main programming paradigms:

* imperative programming
* functional programming
* a lesser known one called logic programming

Let's review what imperative programming is as a paradigm:

* modifying mutable variables
* using assignments
* and control structures such as if-then-else, loops, break, continue, and return

The most common informal way to understand imperative programs is as instruction sequences for a [Von Neumann computer](http://en.wikipedia.org/wiki/Von_Neumann_computer) 

![http://upload.wikimedia.org/wikipedia/commons/e/e5/Von_Neumann_Architecture.svg](http://upload.wikimedia.org/wikipedia/commons/e/e5/Von_Neumann_Architecture.svg)

Consists of essentially a processor and memory, and a bus that reads both instructions and data from the memory into the processor.

What's important about this is that the width of that bus is about 1 [machine word](http://en.wikipedia.org/wiki/Word_(computer_architecture)), 32/64 bits.

It turns out that this model of a computer has shaped programming to no small degree. A strong correspondence between:

* mutable variables -> memory cells
* variable dereferences -> load instructions
* variable assignments -> store instructions 
* control instructions -> jumps

That's all very well - but the problem is scaling up. We want to avoid thinking about programs just word by word. We want to reason in larger structures...

##Scaling Up
In the end, the pure imperative programming paradigm is limited by the "Von Neumann" bottleneck: 
>*One tends to conceptualize data structures word-by word.*

If want to scale up, we have to define higher level abstractions; collections, polynomials, geometric shapes, strings, documents...

Ideally, to be thorough, we need to develop *theories* of these higher level abstractions so that we are able to reason about them.

##What is a theory precious
In mathematics, a theory consists of:

* one or more data types
* operations on these types
* laws that describe the relationships between values and operations

Here's what's important: *a theory in mathematics does not describe mutations.* IE, changing something while keeping the identity the same.

##Theories without Mutation
For instance, the theory of polynomials defines the sum of two polynomials by laws such as
>_(a*x + b) + (c*x + d) = (a+c)*x + (b+d)_

IE,. to sum two polynomials of degree 1 we take their two coefficients of the same degree and we sum those coefficients.

There would be laws of all the other useful operators for polynomials. But what the theory does *not* do is define an operator to change a coefficient while keeping the polynomial the same. Whereas if we look at imperative programming, one can do precisely that...

    class Polynomial { double[] coefficient; } 
    Polynomial p = ...;
    p.coefficient[0] = 42
    
The polynomial p is still the same, but we've changed it's coefficient. This isn't available in mathematics - it would detract from the theory and in fact could damage it by breaking laws

Another example - strings. Most programming languages have strings, and would define a concatenation operator. One of the laws of concatenation is that it is associative, such that 
>_(a ++ b) ++ c = a ++ (b ++ c)_

But it does not define an operator to change a sequence element while keeping the sequence the same

Some languages do get this right; ie, Java's strings are immutable; Java does not give you an operator to change a character in a string while keeping the string the same

##Consequences for Programming
If we want to implement high-level concepts following their mathematical theories, there's no place for mutation

* the theories do not admit it
* mutation can destroy useful laws in the theories

Therefore, let's:

* concentrate on defining theories for operators expressed as functions
* avoid mutations
* have powerful ways to abstract and compose functions

##Functional Programming
In a *restricted* sense, functional programming means programming without mutable variables, assignments, loops, or other imperative control structures... It takes a lot of things away.

In a *wider* sense, FP means focusing on the functions. In particular, function can be values that are produced, consumed, and composed

Functional Programming languages can be viewed the same way - in a restricted sense, a functional programming language is one which does not have mutable variables, assignments, or imperative control structures. 

In a wider sense, a FPL enables the construction of elegant programs that focus on functions. In particular, functions are first-class citizens, meaning essentially that we can do with a function what we could do with any other piece of data:

* they can be defined anywhere, including inside other functions; you can define a string anywhere, you should be able to define a function anywhere
* like any other value, they can be passed as parameters to functions and returned as results
* as for other values, there exists a set of operators to compose functions into richer functions

