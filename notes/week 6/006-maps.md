##Maps

Another funadmental collection type is the _map_.

A map of type `Map[Key, Value]` is a data structure that associates keys of type `Key` with values of type `Value`

```scala
val romanNumerals = Map("I" -> 1, "V" -> 5, "X" -> 10)

val campitalOfCountry = Map("US" -> "Washington", "Switzerland" -> "Bern")
```

###Maps are Iterables, Maps are Functions

Class `Map[Key, Value]` extends the collection type `Iterable[(Key, Value)]`

Therefore, maps support the same collection operations as other iterables. Ie,

```scala
val countryOfCapital = capitalOfCountry map {
    case(x,y) => (y, x)
}
```

Note that maps extend iterables of key/value _pairs_.

Infact, the syntax `key -> value` is just an alternative way to write the pair `(key, value)`

Class `Map[Key, Value]` extends the function type `Key => Value`, so maps can be used everywhere functions can. In particular, maps can be applied to key arguments. `capitalOfCountry("US")` is a well-formed application; looks like a function call which gives us back "Washington."

###Querying Map
Applying a map to a non-existing key gives an error. What can we do to query a map without knowing if it contains a given key or not?

Instead of having a simple function application, we can call a `get` method on the map

```scala
capitalOfCountry get "andorra"
```

What we get here is an `Option` value, and it read `None`, meaning "andorra" is not in the map.

If we were to go `capitalOfCountry.get "US"`, we'd get an option valut that read `Some(Washington)`. What are these things??!!!

###The Option Type
```scala
trait Option[+A]
case class Some[+A](value: A) extends Option[A]
object None extends Option[Nothing]
```

An `Option` value can be one of two things, so the expression `map get key` either returns:

* `None` if `map` does not contain the given key
* `Some(x)` if `map` associates the given `key` with the value x.

###Decomposing Option
Since options are defined as case classes, they can be decomposed using pattern matching:

```scala
def showCapital(country: String) = capitalOfCountry.get(country) match {
    case Some(capital) => capital
    case None => "missing data"
}
```

Options also support quite a few operations of the other collections. In particular, they support `map`, `flatMap`, and `filter`, so we can use them with for-expressions

###Sorted and GroupBy

Two useful operations of SQL queries in addition to for-expressions are `groupBy` and `orderBy`

`orderBy` on a collection can be expressed with `sortWith` and `sorted`, which is just a "natural" ordering:

```scala
val fruit = List("apple", "pear", "orange", "pineapple")
fruit sortWith(_.length < _.length) //List("pear", "apple", "orange", "pineapple")
fruit.sorted //List("apple", "orange", 'pear", "pineapple")
```

`groupBy` partitions a collection into a `map` of collections according to a _discriminator function_. 

```scala
fruit groupBy (_.head)  //|> Map(p -> List(pear, pineapple),
                        //       a -> List(apple))
```

`head` here is the first character that appears in each string - so what that gives us is a `map` that associates each head character with a list of all the fruit that have that character as the head. 

###Map Example
A polynomial can be seen as a map from exponents to coefficients. For instance, `x^3 - 2x + 5` can be represented as `Map(0 -> 5, 1 -> -2, 3 -> 1)`

Based on this observation, let's design a class `Polynom` that represents polynomials as maps!

###Default Values
So far, maps were _partial functions_: applying a map to a key value in `map(key)` could lead to an exception, if the key was not stored in the map. What if we could make maps total functions, that would never fail but that would give back a default value if some key wasn't found?

There's an operation for that! `withDefaultValue` turns a map into a total function:

```scala
val cap1 = capitalOfCountry withDefaultValue "<unknown>"
cap1("andorra")
```