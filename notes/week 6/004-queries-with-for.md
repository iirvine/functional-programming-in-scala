##Queries With For

The `for` notation is essentially equivalent to the common operation of query languages for databases....

```scala
case class Book(title: String, authors: List[String])
```

Suppose we've got a database of books, represented as a list of books.

![img](http://i.imgur.com/HJL1yNJ.png)

###Some Queries

To find the titles of bboks whose author's name is "Bird":


```scala
for (b <- books; a <- b.authors if a startsWith "Bird")
yield b.title
```

To find all the books which have the word "Program" in the tiel

```scala
for (b <- books if b.title indexOf "Program" >= 0)
yield b.title
```

###Another query

To find the names of all authors who have written at least two books:

```scala
for {
	b1 <- books
	b2 <- books
	if b1 != b2
	a1 <- b1.authors
	a2 <- b2.authors
	if a1 == a2
} yield a1
```

The way to do this is to have two iterators ranging over the database, `b1` and `b2`; we demand that `b1` and `b2` are different. Now we have pairs of different books; we let `a1` and `a2` range over the authors of these pairs. If we find a match, ie, an author that appears in the authors lists of both `b1` and `b2`, then we've found an author that's published at least two books.

If we run this, we end up getting the right results, but twice. Whaaaa?

The reason is that we have two generators that both range over `books`, so each pair of book will show up twice, once with the arguments swapped.

IE, we'll get this:

![img](http://i.imgur.com/w6WvEmE.png)

How can we avoid this? Well, an easy way would be to instead of just demanding that the two books are different, we can demand that the title of the first book must be lexicographically smaller than the title of the second book.

```scala
for {
	b1 <- books
	b2 <- books
	if b1.title < b2.title
	a1 <- b1.authors
	a2 <- b2.authors
	if a1 == a2
} yield a1
```

But what happens if an author has published three books? The author is printed three times! oh man.....

Even with this added condition, we have three possible pairs of books

![img](http://i.imgur.com/JeI8BAt.png)

We have three possible pairs out of two possible books out of these three; for each of the three possibilities, the same author will be printed. What can we do????

One solution would be to remove duplicate authors from the result list. There's a function for this that works on all sequences, called `distinct`:

```scala
{ for {
		b1 <- books
		b2 <- books
		if b1.title < b2.title
		a1 <- b1.authors
		a2 <- b2.authors
		if a1 == a2
	} yield a1
}.distinct
```

That'd do the trick, but on the other hand, maybe these problems are a sign that we've started off with the wrong data structure. We have a database that's a list of books - in actual databases, the order in which the rows appear shouldn't matter. Databases are much more sets of rows instead of lists rows. Sets have the advantage that duplicates are eliminated by design.

So let's make `books` a set of rows! That gets the job done.