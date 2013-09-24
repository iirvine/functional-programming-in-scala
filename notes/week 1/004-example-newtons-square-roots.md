###Example: Square Roots with Newton's method

###Task
We will define a function sqrt such that

```scala
def sqrt(x: Double): Double = ...
```

The classical way to achieve this is by successive approximations using Newton's method.

To compute sqrt(x):

- start with an initial estimate y (let's pick y = 1)
- repeatedly improve the estimate by taking the mean of y and x/y

A typical way to code algorithms in functional languages is to go step by step - we take a small task and formulate it as a function; then probably, that task will need further tasks that will be defined in their own function

First function we'd want to define in this case is the one that computes one iteration step; if we have a guess and the value we want to draw the root from, what do we do?

Well, either we stop the iteration and return the result, or we go and do another iteration step.

```scala
if (isGoodEnough(guess, x)) guess
else sqrtIter(improve(guess, x), x)
```

The predicate that controls our iteration is called isGoodEnough - if our guess is good enough, we'll just return the guess.

If it's *not* good enough, we have to improve our guess - we'll do that with another function improve. We'll call sqrtIter again with the improved guess.

Note that sqrtIter is recursive - its right hand side calls itself. One pecularity in Scala - the return type of a recursive function *always needs to be defined* - for other functions it's optional. The reason being that to compute the return type of a recursive function, the Scala interpreter would have to look at the right hand side; because the sqrtIter function is recursive, it's stuck in a cycle.

