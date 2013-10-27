##Pattern-Matching

###Reminder: Decomposition

Last time we were trying to find a general and convenient way to access objects in a extensible class hierarchy. We were using arithmetic expressions as our motivating example.

![Martin's handwriting is illegible](http://i.imgur.com/A7YUfhs.png)

We tried lotsa stuff - classification and access methods, which led to a quadratic explosion, type tests and casts which were unsafe and low level, and finally object oriented decomposition which doesn't always work and led to us needing to touch all classes to add a new method.

###Solution 2: Functional Decomposition with Pattern Matching

An important observation here is that the sole purpose of the test and accessor functions was to *reverse* the construction process; ie, when we construct a new node with `new Sum(e1, e2)`, we picked a particular class of node, `Sum`, and we picked the two arguments.

The purpose of decomposition is to recover what kind of constructor we used for the node, where it was a `Sum` or a `Number` say, and what the arguments were.

That kind of situation is very fundamental, and so common that many functional languages automate it. The technical term for this is *pattern matching*.

###Case Classes

We get pattern matching in scala with case classes - it's similar to a normal class definition:

```scala
trait Expr
case class Number(n: Int) extends Expr
case class Sum(e1: Expr, e2: Expr) extends Expr
```

Like before, here we've got a trait `Expr`, and two concrete subclasses `Number` and `Sum`.

When we define a case class, the scala compiler will implicitly define a companion object with a factory method `apply` which will construct that class directly:

```scala
object Number {
	def apply(n: Int) = new Number(n)
}

object Sum {
	def apply(e1: Expr, e2: Expr) = new Sum(e1, e2)
}
```

Now we can write `Number(1)` instead of `new Number(1)`. But the classes that we defined above are all empty - how can we access the components?

###Pattern Matching

One way to see pattern matching is as a generalization of `switch` from C/Java to class hierarchies. We express it in scala using the keyword `match`:

```scala
def eval(e: Expr): Int = e match {
	case Number(n) => n
	case Sum(e1, e2) => eval(e1) + eval(e2)
}
```

Basically, we're saying match the given expression with a number of patterns. The first pattern says if it's a number of sum given value n, return that value. The second pattern says if it's a sum with some operand e1 and another e2, then evaluate the two operands and form the sum.

###Match Syntax

* `match` is followed by a sequence of *cases*, `pat => expr`
* each case associates an *expressions* `expr` with a *pattern* `pat`
* a `MatchError` exception is thrown if no pattern matches the value of the selector.

###Forms of Patterns

What are patterns built from?

* constructors, eg `Number`, `Sum` - we've seen `Sum(n)`, where we refer to `n` in the expression. If we didn't care about the argument of `Sum` we could use the wildcard like so `Sum(_)`, meaning we don't care about that variable
* variables, eg `n`, `e1`, `e2`
* wildcard patterns _
* constants, eg `1`, `true`

We can take these building blocks and compose more complicated patterns from them:

```scala
match {
	case Sum(Number(1), Var(x))	=> x
}
```

would match objects which are sums, with left operands that are Numbers with a value of 1, and with a right node of type Var. The name field of the var can be anything, but then on the right hand side of the pattern we can refer to the variable.

The same variable name can appear only once in a pattern - so `Sum(x, x)` is not a legal pattern.

So here's some of the fine print: how do we distinguish a variable, such as `n`, which can match anything, from a constant, like `val N = 2`, which matches just the number 2 and nothing else? Syntactically, we need to find a way to distinguish one from the other.

The convention scala uses is that variables *always* must begin with a lower case letter, whereas constants should begin with a capital letter (excepting reserved words like `null`, `true`, `false`).

###Evaluating Match Expressions

An expression of the form

> e match { case p1 => e1 ... case pn => en }

matches the value of the slector `e` with the patterns `p1 ... pn` in the order in which they're written. The whole match ecpression is rewritten to the right hand side of the first case where the pattern matches the selector `e`. When we do that, references to pattern variables are replaced by the corresponding parts in the selector.

###What do Patterns Match?

What does it mean that a particular pattern matches an expression? Let's look at the possible forms of patterns to understand that.

If we have a constructor pattern, like `C(p1, ...., pn)`, it matches all the values of type `C` and its subtypes that have been constructed with arguments matching the patterns `p1, ..., pn`

A variable pattern `x` matches any value, and *binds* the name of the variable to this value, meaning in the associated expression we can then use `x` for the value that it matched.

A constant pattern `c` matches values that are equal to `c` (in the sense of ==)

Let's look at an application of our hot new pattern-matching evaluation function with a tree of `Sum(Number(1), Number(2))`:

```scala
eval(Sum(Number(1), Number(2)))
```

The first thing we would do as usual is rewrite that application with the body of eval, where the actual argument replaces the formal parameter, like this:

```scala
Sum(Number(1), Number(2)) match {
	case Number(n) => n
	case Sum(e1, e2) => eval(e1) + eval(e2)
}
```

Next, we have to evaluate the match expression. We have to match the selector expression against all the patterns. The first doesn't match because the constructor is different, but the second does. That means the two variables e1 and e2 will be bound to `Number(1)` and `Number(2)`.

After that, the whole expression will rewrite to the right hand side expression of the matched pattern.

```scala
eval(Number(1) + eval(Number(2)))
```

which gives us

```scala
Number(1) match {
	case Number(n) => n
	case Sum(e1, e2) => eval(e1) + eval(e2)
}
```

etc etc.

###Pattern Matching and Methods

It's totally possible to have pattern matching methods inside the class hierarchy too - we could just as easily have put eval inside of `Expr` like so:

```scala
trait Expr {
	def eval: Int = this match {
		case Number(n) => n
		case Sum(e1, e2) => eval(e1) + eval(e2)
	}
}
```

Once we've done that, we might ask what are the trade offs to doing it this way, versus the object oriented decomposition we saw earlier, where we had different implementations of eval in each class in our hiearchy? Some of the choice between these is a matter of style, but there are some differences that are important.

For example, are we more often creating new subclasses of `Expr`, or are we creating more methods? We're thinking here about the future extensibility and extention paths of our system.

If we're creating a lot of new subclasses, then the object oriented version has the upper hand. It's a very easy, local change to create a new subclass with a new `eval` method, where as in the functional solution we'd have to go back and change the code inside the `eval` method and add a new case to it.

On the otherhand, if what we're doing is creating lots of new methods, but the class hiearchy is kept relatively stable, then the pattern matching approach is advantageous. Each new method in the pattern matching approach is just a local change, whereas a new method, such as `show`, in the object oriented approach would require touching every subclass.

The problem of this two dimensional extensibility, where we want to add new classes to a hiearchy or new methods, or both, is called the *expression problem*