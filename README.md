![ravr logo](ravr.png)

This is a partial port of the [Ramda.js functional programming library](http://ramdajs.com) to work with [vavr types](http://www.vavr.io).

## Curried functions & partial function application

Ravr provides two ways to do partial application: Either leave out parameters at the end or use a parameter placeholder:

```java

// All the same
List.of(1, 2, 3).map(x -> add(2, x));
List.of(1, 2, 3).map(add(2, __)); // works but isn't idiomatic
List.of(1, 2, 3).map(add(2)); // use this one instead
// => [3, 4, 5]

// The placeholder should only be used when necessary.
List(7, 8, 9).map(subtract(__, 2));
// => [5, 6, 7]

```

## Function composition

You can compose functions in two ways.

### Compose

Similiar to mathematical composition. This is a vararg function.

```java

// classic Java
map(add(1).compose(multiply(2)).compose(subtract(__, 1)), List(2, 3, 4));
// => [3, 5, 7]

// Ravr style
map(compose(add(1), multiply(2), subtract(__, 1)), List(1, 2, 3));
// => [3, 5, 7]

```

### Pipe

Same as compose, but with the order inverted. This can be more readable when putting functions on separate lines.

```java

List<String> words = List.of("xSIHTx", "xSIx", "xGNITSERETNIx");

assertThat(
	join("... ", words.map(pipe(
		Ravr::reverse,
		Ravr::toLower,
		Ravr::init,
		Ravr::tail
	))),
	is("this... is... interesting")
);

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

_Note: Your IDE might tell you to replace the lamda with a method reference. This might not work because of reasons._

You can create a lens using a pair of getters and setters. You can use the lens in combination with get, set and over:

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

You can concatenate the content of a List\<Option\<X\>\> to List\<X\> with the concatOptions function.

```java
List<String> strings = List(">>>>", "", "====");

pipe(
	head(),	// List.of(Option.some(">"), Option.none(), Option.some("="))
	concatOptions(),	// List.of(">", "=")
	join(">")	// ">>="
).apply(strings);
```

## Future plans

Other Ramda functions will be added as needed. There probably needs to be a separate project for code generation for the parameter placeholder invocations.

## Contributing

The scope of this library is to port Ramda. Extra functions like catOptions should be the exception. Contact me on [Twitter](https://twitter.com/rednifre).

Licensed under the LGPL v3, see the LICENSE file for details.
