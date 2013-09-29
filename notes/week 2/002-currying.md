##Currying

Let's look again at our summation functions - sumInts, sumCubes, sumFactorials:

![img](http://i.imgur.com/oehbY74.png)

Last time we figured out how we could factor out the body of these functions and now simply pass the function to apply to each element in the interval, and the two bounds of the interval.

But there's still repetition - each of the three functions takes two parameters, a and b, and then passes them on to the sum functions. Could we get rid of these two functions and thereby be even shorter?

Let's rewrite the sum function like so:

```scala
def sum(f: Int => Int): (Int, Int) => Int = {
	def sumF(a: Int, b: Int): Int =
		if (a > b) 0
		else f(a) + sumF(a + 1, b)
	sumF
}
```

sum is now a function that returns another function. It takes a single parameter f, of type Int to Int, and it returns a function as its result (indicated by the (Int, Int => Int) function return type). So sum is now a function that returns another function, in particular, a locally defined function.


Now we can define our sum functions like this:

```scala
def sumInts = sum(x => x)
def sumCubes = sum(x => x * x * x)
def sumFactorials = sum(fact)
```

These functions can in turn be applied like any other function:

```scala sumCubes(1, 10) + sumFactorials(10, 20)```

Can we avoid the middlemen, sumInts, sumCubes, etc? Yup:

```scala
sum(cube)(1, 10)
```

sum(cube) applies sum to cube and returns the *sum of cubes* function - it is therefor equivalent to sumCubes. This function is then applied to the arguments (1, 10).


###Multiple Parameter Lists

Another piece of syntactic sugar - the definition of functions that return functions is so useful in functional programming that there is a special syntax for it in Scala; for example, the following definition of sum is equivalent to the one with the nested sumF function, but shorter

```scala
def sum(f: Int => Int)(a: Int, b: Int): Int =
	if (a > b) 0 else f(a) + sum(f)(a + 1, b)
```

We can just combine the two parameter lists of the outer function and the nested function and write them one after the other.

Question: given ```scala def sum(f: Int => Int)(a: Int, b: Int): Int = ...```, what is it's type?

Answer:
(Int => Int) => (Int, Int) => Int

It first is a function that takes a function as an argument, so that would be the argument type - that returns a function that takes two integers as arguments, and that finally returns an Int

Note that functional types associate to the right. That is to say that

Int => Int => Int

is equivalent to

Int => (Int => Int)

















