###The N-Queens Problem

So far, all the collections we've seen were sequences of some sort or another - there are two other fundamental classes of collection, sets and maps. This week we're going to look at sets.

###Sets

A set is written like a sequence:

```scala
val fruit = Set("apple", "banana", "pear")
val s = (1 to 6).toSet
```

Most operations on sequences are also available on sets

```scala
s map (_ + 2)
fruit filter (_.startsWith == "app")
```

###Sets vs Sequences

The principle differences between sets and sequences:

* sets are unordered; the elements of a set do not have a predefined order in which they appear in the set
* sets do not have duplicate elements
* the fundamental operation on sets is `contains`; the principle operation you can do with a set is asking if a given element is present within it: `s contains 5 // true`

###N-Queens

The eight queens problem is to place eight queens on a chessboard so that no queen is threatened by another. In other words, there can't be two queens in the same row, column, or diagonal.

We want to develop a solution for a chessboard of any size, not just 8.

One way to solve the problem is to place a queen on each row - and once we have placed k - 1 queens, we must place the kth queen in a column where it's not "in check" with any other queen on the board.

![img](http://i.imgur.com/Hqn1Iiv.png)

###Algorithm

We can solve this with a recursive algorithm! (It's a unix system... I know this!)

Suppose we have already generated all the solutions consiting of placing `k - 1` queens on a board of size `n`. Each solution is represented by a list (of length `k-1`) containing the numbers of columns (between 0 and `n-1`)

![img](http://i.imgur.com/qfZFo4j.png)

So, we number rows and columns from 0, and then the solution of our first three queens would be a list, starting with the last queen we placed `List(0,3,1)`; our complete solution would include our last queen. `List(2, 0, 3, 1)`

Of course, in general, there can be more solutions (or none at all). We're dealing here with not single solutions but *sets* of solutions.

Let's put this in an actual program!

```scala
  def queens(n: Int): Set[List[Int]] = {
  	def placeQueens(k: Int): Set[List[Int]] =
  		if (k == 0) Set(List())
  		else
  			for {
  				queens <- placeQueens(k - 1)
  				col <- 0 until n
  				if isSafe(col, queens)
  			} yield col :: queens
  	placeQueens(n)
  }
```

Here's a function to place all queens on a chessboard of `n` rows. The input of `queens` would be the number of rows, and the output would be a `Set` of solutions; each solution would be a `List[Int]`.

We use a recursive algorithm with an auxiliary method, `placeQueens`, which places a number `k` of queens on a board and produces the set of solutions. The initial call is `placeQueens(n)`, which means we want to place all `n` queens.

Now we've reduced the problem to how to implement `placeQueens`. We deal with the boundary case first - if `k` equals 0, we don't need to place any queens - what do we return? The empty set of solutions? That's actually not quite right... if we're not asked to do anything, then the solution is to not do anything. So we should return an empty `List` as our solution.

Now, in the case where `k` is greater than 0 we have to do some real work. In general, to place `k` queens, we have to solve the problem of placing `k - 1` queens. We'll let `queens` range over the set of our partial solutions returned by `placeQueens(k - 1)`.

Next, we have to put our `k` queen into a certain column. We can simply try all the possible columns - `col <- 0 until n`. We can't place the queen in any column we please, we need to make sure it doesn't threaten any other queen. So, we'll put a filter in there, that says that the column for the queen is safe with respect to the previous queens. (`isSafe(col, queens))`

If it is, then we can yield a new solution, which will be our partial solution `col`, augmented by the queen in the new column. So it would be `col :: queens`

OKAY. There's still one thing left to do - define that method `isSafe`.

```scala
  def isSafe(col: Int, queens: List[Int]): Boolean = {
  	val row = queens.length
  	val queensWithRow = (row - 1 to 0 by -1) zip queens
  	queensWithRow forall {
  		case (r, c) => col != c  && math.abs(col - c) != row - r
  	}
  }
```

The first thing we want to do is add rows to all the queens we look at here; the row of the queen to be placed will be just `queens.length`, since the other queens are in rows 0 to `n - 1`.

Next, we want to add a row to each of our previous queens, transforming our list of `Int`s into a list of pairs, of row and column. We've got a set of solutions, which is something like `List(0, 3, 1)`, with the columns of the previous queens. We want to transform that into a solution that adds the rows. The first element was actually the last row to be placed, so we'd get `List((2,0), (1,3), (0,1))`.

The idea here is to use a `zip` with a range - the range that we want to apply here is the range that goes from row - 1 to 0, by -1 steps. We zip that sequence with the list of our queens, and we call that `withRow`. So now we've got the partial solution of queens, represented with rows.

Now what we can do is simply check whether the ween at `row` and `column` is in check with any of our `queensWithRow`. `forall` of these `queensWithRow`, it must be that the new queen is not in check....

So we take the row and the column out of the pair with a `case` statement, and now comes our check. What do we need to check? First, we need to make sure that the current column is not the same as any of the previous queen's columns. `col != c`

We also need to make sure that the queen is not in check over any of the diagonals. Meaning, the absolute difference between the two columns (`math.abs(col - c)`) must not be the same as the absolute difference between the two rows (`row - r`).

If that predicate is true, then we know that the queen is not in check over any of the diagonals with the queen in `(r, c)`!
