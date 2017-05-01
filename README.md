# Ravr

_Bring Java developers to tears_

This is a partial port of the [Ramda.js functional programming library](http://ramdajs.com) to work with [vavr types](http://www.vavr.io).

## Curried functions & partial function application

Java8 already allows partial function application in an all-or-nothing way:

```java
List(1, 2, 3).map(x -> R.inc(x)); // [2,3,4]
List(1, 2, 3).map(R::inc); // [2,3,4]
```

Ravr provides two more ways to do partial application, either leave out parameters at the end or use a parameter placeholder:

```java
List(1, 2, 3).map(x -> R.add(2, x)); // [3, 4, 5]
List(1, 2, 3).map(R.add(2, __)); // [3, 4, 5]
List(1, 2, 3).map(R.add(2)); // [3, 4, 5]
```

The placeholder is rarely necessary because the parameters are usually arranged in a way that leans itself towards leaving out the last parameter. An example where it is needed is where functions mimic the parameter order of the corresponding operator, e.g. subtract:

```java
List(7, 8, 9).map(R.subtract(__, 2)); // [5, 6, 7]
```

## Function composition

You can compose functions in two ways.

### Compose

Similiar to mathematical composition. This is a vararg function.

```java
map(compose(add(1), multiply(2)), List(1, 2, 3));
// => [3, 5, 7]
```

### Pipe

Same as compose, but with the order inverted. This can be more readable when putting functions on separate lines.

```java
List("SIHT", "SI", "GNITSERETNI").map(pipe(
	reverse(),
	toLower(),
	join(" ... ")
));
// => "this ... is ... interesting"
```

## Functor, Applicative, Monad

Ravr provides map, ap, and flatMap.

Pattern | Standard name | Ramda name | Ravr name | Type signature
------- | ------------- | ---------- | --------- | --------------
Functor | map           | map        | map       | Functor m => (a -> b) -> m a -> m b
Applicative | apply     | ap         | ap        | Applicative m => m (a -> b) -> m a -> m b
Monad   | bind          | chain      | flatMap   | Monad m => (a -> m b) -> m a -> m b

Due to limits in Java's type system these currently only work on Lists, Options, Traversables and Futures.

## Lenses

Lenses only work on types that implement the included Copyable interface, which is a simplified version of the Cloneable interface. The simplest way to implement it is wrapping the clone function:

```Java
@Getters // lombok
@Setters // lombok
@AllArgsConstructor // lombok
class Thing implements Copyable<Thing> {
	String name;
	int quality;

	@Override
        public Thing copy() {
            return safeClone(() -> clone());
        }
}
```

_Note that your IDE might tell you to replace the lamda with a method reference. This might not work because of reasons._

You can create a lens using a pair of getters and setters and use it with get, set and over:

```java
Lens<Thing, String> name = lens(Thing::getName, Thing::setName);
Lens<Thing, Integer> quality = lens(Thing::getQuality, Thing::setQuality);

Thing lookingGlass = new Thing("looking glass", 5);

get(name, lookingGlass); // "looking glass"

Function1<Thing, Thing>
improve = pipe(
	set(name, "ten times better"),
	over(quality, multiply(10))
);

Thing betterThing = improve.apply(lookingGlass);

get(quality, betterThing); // 50
get(quality, lookingGlass); // 5 (not changed)
```

## Other functions

You can concatenate the content of a List<Option<X>> to List<X> with the catOptions function.

```java
List<String> strings = List(">>>>", "", "====");

pipe(
	head(),	// List(Option.some(">"), Option.none(), Option.some("="))
	catOptions(),	// List(">", "=")
	join(">")	// ">>="
).apply(strings);
```

## Future plans

Other Ramda functions will be added as needed. There probably needs to be a separate project for code generation for the parameter placeholder invocations.

## Contributing

The scope of this library is to port Ramda. Extra functions like catOptions should be the exception. Contact me on [Twitter](https://twitter.com/rednifre).

Licensed under the LGPL v3, see the LICENSE file for details.
