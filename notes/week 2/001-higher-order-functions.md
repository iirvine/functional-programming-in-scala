##Higher Order Functions

One thing particular about functional languages is that they treat functions as *first class values* - meaning, like any other value, a function can be passed as a parameter to another function or returned as a result. This provides a flexible way to compose programs.

Functions that take other functions as parameters or that return functions as results are called higher order functions - that's the opposite of a first order function, which is a function that operates on simple data types such as integers or lists, but not other functions.

Let's suppose we want to take the sum of all the integers between a and b:

```scala
def sumInts(a: Int, b, Int): Int =
	if (a > b) 0 else a + sumInts(a + 1, b)
```

Let's vary the problem a bit - now we want to take the sum of the cubes of all numbers between a and b.

```scala
def cube(x: Int): Int = x * x * x
def sumCubes(a: Int, b: Int): Int =
	if (a > b) 0 else cube(a) + sumCubes(a + 1, b)
```

Next let's take the sum of all factorials between a and b...

```scala
def sumFactorials(a: Int, b: Int): Int =
	if (a > b) 0 else fact(a) + sumFactorials(a + 1, b)
```

By now we can see the principle - the program is exactly the same as sumInts and sumCubes. Of course these are all special cases of the mathematical sum of values of f(n), where f is a given function and n is taken from the interval between a and b

![img](http://i.imgur.com/ber3LCH.png)

The question is that if mathematics has a special notation for that, shouldn't programming? Can we factor out the common pattern?

###Summing with Higher-Order Functions
Let's define a function sum, which takes a parameter f of type Int to Int, and the two parameter bounds of a and b:

```scala
def sum(f: Int => Int, a: Int, b: Int): Int =
	if (a > b) 0
	else f(a) + sum(f, a + 1, b)
```

The new thing here is that f is a parameter of the sum function - it's not a given function it's a parameter.

Once we have that, we can then write sumInts/sumCubes/sumFactorials like

```scala
sum(id, a, b)   //the id function simply returns its parameter unchanged
sum(cube, a, b)
sum(fact, a, b)
```

What we've done effectively is reuse the pattern that defines the sum function so that we only had to write that once.

###Function Types
The type A => B is the type of a *function* that takes an argument of type A and returns a result of type B. So, Int => Int is the type of functions that map integers to integers.

So, we've shortened the definitions of sumInts et al, but there's an annoying detail - we had to name all the auxiliary functions (cube, factorial). We're adding code to our program that we don't need.

Passing functions as parameters leads to the creation of many small functions - that's tedious. Compare to strings - we don't need to define a string using ```def``` - instead we just directly write ```println("abc")```

That's because strings exist as *literals* - since functions are important in our language, it makes sense to introduce function literals, which would let us write a function without giving it a name. These are called anonymous functions.

Example - a function that raises its argument to a cube:

```scala
(x: Int) => x * x * x
```

Here, (x: Int) is the parameter of the function, and x * x * x is it's body. The type of the parameter can be omitted if it can be inferred by the compiler from the context.

If there are several parameters, they are seperated by commas:

```scala
(x: Int, y: Int) => x + y
```

Are anonymous functions essential, or can they be defined in some other way? It turns out every anonymous function can be defined another way - using a def. One can therefore say that anonymous functions are syntactic sugar.

Using anonymous functions, we can write sums in a shorter way:

```scala
def sumInts(a:Int, b: Int) = sum(x => x, a, b)
def sumCubes(a: Int, b: Int) = sum(x => x * x * x, a, b)
```