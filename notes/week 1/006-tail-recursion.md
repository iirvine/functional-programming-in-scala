##Tail Recursion

###Review: Evaluating a Function Application
One simple rule: one evaluates a function application f(e1,....,en)

  - by evaluating the expressions e1,...,en resulting in the values v1,...,vn, then
  - by replacing the application with the body of the function f, in which
  - the actual parameters v1,...,vn replace the formal parameters of f

This can be formalized as a *rewriting of the program itself*:
![img](http://i.imgur.com/QD0aaaF.png)

Say you have a program with a function definition f, with parameters x1 to xn, and a body B - and then, somewhere else you have a function call: f applied to argument values v1 through vn. That program can be rewritten to a program that contains the same function definition, but the function application has now been replaced by the body of f, B, and at the same time the formal parameters x1 to xn have been replaced by the argument values; the rest of the program is assumed to be unchanged.

Here, [v1/x1,...,vn/xn] means "the expression B, in which all occurrences of xi have been replaced by vi". This notation is called a *substitution*

Consider gcd, the function that computes the greatest common divisor of two numbers, with Euclid's algorithm.

```scala
def gcd(a: Int, b: Int): Int =
	if (b == 0) a else gcd(b, a % b)
```

The evaluation of this algorithm occurs like so:

![img](http://i.imgur.com/ciOGYQ7.png)

Consider factorial:

```scala
def factorial(n: Int): Int =
  if (n== 0) 1 else n * factorial(n-1)
```

![img](http://i.imgur.com/Xehw44B.png)

What's the difference between these two sequences, gcd and factorial?

If we come back to gcd, we see that the reduction sequence essentially oscillates - it goes from one call to gcd to the next through each step, until it terminates.

Factorial on the other hand, in each couple of steps we add one more element to our expression; our expression becomes bigger and bigger, until the end when we reduce it to the the final value.

###Tail Recursion
That difference in the rewriting rules translates to an actual difference in execution on a computer; in fact it turns out if you have a recursive function that calls itself as its last action, then you can reuse the stak frame of that function. This is called *tail recursion*.

By applying that trick, a tail recursive function can execute in constant stack space; it's really just another formulation of an iterative process. You could say that a tail recursive function is just the functional form of a loop - and it executes just as efficiently as a loop.

If we go back to gcd, we see that it's calling itself as its last action - that translates to a rewriting sequence that's essentially constant size, which will in the actual execution on a computer translate into a tail recursive call that can execute in constant space.

On the other hand, in factorial after the call to factorial(n-1), there's still work to be done - we have to multiply the result of that call with the number n. That call here is not a tail recursive call. You can see that in the reduction sequence - there's a build up of intermediate results that we have to keep until we can compute the final value.

Both factorial and gcd only call itself; of course, a function can also call other functions. The generalization of tail recursion is that if the last action of a function consists of calling another function, maybe the same, maybe another, the stack frame could be reused for both functions. Such calls are called *tail calls*

###Tail Recursion in Scala
Should every function be tail recursive? Not really... in scala, only directly recursive calls to the current function are optimized. The interest of tail recursion is only to avoid very deep recursion chains.... most implementations of the JVM limit the amount of recursion to a couple thousand stack frames. If the input data is such that these deep recursive chains could happen, it makes sense to reformulate your function to be tail recursive to run in constant stack space, to avoid stack overflow exceptions.

On the other hand, if your input data are not susceptible to deep recursive trains, clarity trumps efficiency - write your function the clearest way you can (which often is not tail recursive).

Factorial grows very very quickly - even after a very low number of recursive steps you'll already exceed the range of integers. It's not worth making factorial a tail recursive function.