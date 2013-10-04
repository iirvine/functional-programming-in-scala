##Fixed Points

Whazzat??

A number x is called a *fixed point* of function f if `f(x) = x`

Let's see a pitcher!

Let's suppose we have a function that maps x to 1 + x/2. The graph of that function would look like this:

![img](http://i.imgur.com/KmDoSEA.png)

The fixed point would be where the diagonal hits the graph of the function, in this case 2.

Turns out that for some functions f we can locate the fixed points by starting with an initial estimate and then applying f in a repetitive way.

> x, f(x), f(f(x)), f(f(f(x))), ...

until the value does not vary anymore (or the change is sufficiently small).

This leads to the following function for finding a fixed point:

```scala
val tolerance = 0.0001
def isCloseEnough(x: Double, y: Double) =
	abs((x - y) / x) / x < tolerance
def fixedPoint(f: Double => Double)(firstGuess: Double) = {
	def iterate(guess: Double): Double = {
		val next = f(guess)
		if (isCloseEnough(guess, next)) next
		else iterate(next)
	}
	iterate(firstGuess)
}
```

###Return to Square Roots

Here is a specification for the sqrt function:

> sqrt(x) = the number y such that y * y =x

Or, dividing both sides of the equation with y:

> sqrt(x_ = the number y such that y = x / y)

Consequently, sqrt(x) is a fixed point of the function (y => x / y)

This suggests to calculate sqrt(x) by iteration towards a fixed point:

```scala
def sqrt(x: Double) =
	fixedPoint(y => x/y)(1.0)
```

Unfortunately, this does not converge. We get an infinite computation. Doh.

Maybe some println instructions to the fixedPoint function would help. For each iteration step we'll write what the current guess is.

![doh](http://i.imgur.com/npOzbsy.png)

We can see that our guess oscillates between 1 and 2 all the time. If you do the execution, this is precisely what happens.

###Average Dampening
How can we do better?

One way to control such oscillations is to prevent the estimation from varying too much, by averaging successive values of the original sequence:

```scala
def sqrt(x: Double) = fixedPoint(y => (y + x / y) / 2)(1.0)
```

###Functions as Return Values

It's pretty keen to be able to pass functions as arguments - but functions that return functions are pretty cool too.

Consider iteration towards a fixed point. We begin by observing that sqrt(x) is a fixed point of the function `y => x / y`.

Then, the iteration converges by averaging successive values. This technique of *stabilizing by averaging* is general enough to merit being abstracted into its own function:

```scala
def averageDamp(f: Double => Double)(x: Double) = (x + f(x) / 2)
```

This function takes an arbitrary function of Double => Double, and a value x of type Double; it then computes the average of x and f(x)

What would sqrt(x) look like with fixedPoint and averageDamp?

```scala
def sqrt(x: Double) =
	fixedPoint(averageDamp(y => x / y))(1)
```

So, averageDamp is a function that takes a function (in this case, a function that is at the root of the square root specification), and it returns another function, namely, the function that is essentially the same iteration but with average damping applied.

