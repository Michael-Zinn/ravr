![ravr logo](ravr.png)

This is a partial port of the [Ramda.js functional programming library](http://ramdajs.com) to work with [vavr types](http://www.vavr.io).

_v0.0.7 (experimental, incomplete)_

## Adding it to your project

### Maven

It's not on Maven Central yet, but you can [find it in JCenter](https://bintray.com/rednifre/ravr/ravr). First, add JCenter to the repositories in your pom:

```xml
    <repositories>
        <repository>
            <id>jcenter</id>
            <url>http://jcenter.bintray.com </url>
        </repository>
    </repositories>
```

Then you can import it as usual:

```xml
    <dependencies>
        <dependency>
            <groupId>de.michaelzinn.ravr</groupId>
            <artifactId>ravr</artifactId>
            <version>0.0.7</version>
        </dependency>
    </dependencies>
```

### Gradle

```gradle
compile 'de.michaelzinn.ravr:ravr:0.0.7'
```

## Curried functions & partial function application

Ravr provides two ways to do partial application: Either leave out parameters at the end or use a parameter placeholder:

```java

// All the same
List.of(1, 2, 3).map(x -> add(2, x));
List.of(1, 2, 3).map(add(2, __)); // works but isn't idiomatic
List.of(1, 2, 3).map(add(2)); // use this one instead
// => [3, 4, 5]

// The placeholder should only be used when necessary.
List.of(7, 8, 9).map(subtract(__, 2));
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

## Lenses (experimental)

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

## What's included

| Icon | Meaning |
|:----:|:--------|
| :white_check_mark: | Works |
| :neutral_face: | Works with issues |
|    | Not implemented |
| :heavy_multiplication_x: | Not planned |
| :diamond_shape_with_a_dot_inside: | Bonus function not originally included in Ramda.js |

This list is generated automatically and slightly inaccurate right now.

| Status | Function | Note |
|:----:|:--------|:-----|
| :white_check_mark: | add | |
|    | addIndex |   |
| :white_check_mark: | adjust | |
| :white_check_mark: | all | |
|    | allPass |   |
| :white_check_mark: | always | |
|    | and |   |
| :white_check_mark: | any | |
|    | anyPass |   |
| :neutral_face: | ap | Only works on Lists.|
|    | aperture |   |
|    | append |   |
|    | apply |   |
|    | applySpec |   |
| :neutral_face: | applyTuple | Only works on Tuples of size 2 and 3. Will be fixed.|
|    | ascend |   |
|    | assoc |   |
| :heavy_multiplication_x: | assocPath |   |
|    | binary |   |
|    | bind |   |
|    | both |   |
|    | call |   |
|    | chain |   |
|    | clamp |   |
| :heavy_multiplication_x: | clone |   |
|    | comparator |   |
| :white_check_mark: | complement | |
|    | compose |   |
|    | composeK |   |
|    | composeP |   |
| :white_check_mark: | concat | |
| :diamond_shape_with_a_dot_inside: | concatOptions | |
|    | cond |   |
|    | construct |   |
|    | constructN |   |
| :white_check_mark: | contains | |
|    | converge |   |
| :diamond_shape_with_a_dot_inside: | count | |
|    | countBy |   |
|    | curry |   |
|    | curryN |   |
|    | dec |   |
| :white_check_mark: | defaultTo | |
|    | descend |   |
|    | difference |   |
|    | differenceWith |   |
|    | dissoc |   |
|    | dissocPath |   |
|    | divide |   |
|    | drop |   |
|    | dropLast |   |
|    | dropLastWhile |   |
|    | dropRepeats |   |
|    | dropRepeatsWith |   |
|    | dropWhile |   |
|    | either |   |
|    | empty |   |
|    | endsWith |   |
| :white_check_mark: | eq | |
|    | eqBy |   |
|    | eqProps |   |
|    | equals |   |
|    | evolve |   |
|    | F |   |
| :white_check_mark: | filter | |
|    | find |   |
| :white_check_mark: | findIndex | |
|    | findLast |   |
|    | findLastIndex |   |
| :neutral_face: | flatMap | Only for lists?|
|    | flatten |   |
|    | flip |   |
| :white_check_mark: | forEach | |
|    | forEachObjIndexed |   |
|    | fromPairs |   |
| :white_check_mark: | get | |
| :white_check_mark: | groupBy | |
|    | groupWith |   |
|    | gt |   |
|    | gte |   |
|    | has |   |
|    | hasIn |   |
| :white_check_mark: | head | |
|    | identical |   |
| :white_check_mark: | identity | |
| :white_check_mark: | ifElse | |
|    | inc |   |
|    | indexBy |   |
|    | indexOf |   |
|    | init |   |
|    | innerJoin |   |
|    | insert |   |
|    | insertAll |   |
|    | intersection |   |
|    | intersectionWith |   |
|    | intersperse |   |
|    | into |   |
|    | invert |   |
|    | invertObj |   |
|    | invoker |   |
|    | is |   |
|    | isEmpty |   |
|    | isNil |   |
| :white_check_mark: | isNone | Replacement for isNil, returns true for Option.none().|
| :white_check_mark: | isSome | Replacement for complement(isNil), returns true for Option.some("whatever").|
| :white_check_mark: | join | |
| :diamond_shape_with_a_dot_inside: | joinOption | Like join, except that it returns nothing when joining empty lists.|
|    | juxt |   |
|    | keys |   |
|    | keysIn |   |
|    | last |   |
|    | lastIndexOf |   |
|    | length |   |
| :white_check_mark: | lens | |
|    | lensIndex |   |
|    | lensPath |   |
|    | lensProp |   |
|    | lift |   |
|    | liftN |   |
|    | lt |   |
|    | lte |   |
| :white_check_mark: | map | |
|    | mapAccum |   |
|    | mapAccumRight |   |
|    | mapObjIndexed |   |
|    | match |   |
|    | mathMod |   |
|    | max |   |
|    | maxBy |   |
|    | mean |   |
|    | median |   |
|    | memoize |   |
|    | memoizeWith |   |
|    | merge |   |
|    | mergeAll |   |
|    | mergeDeepLeft |   |
|    | mergeDeepRight |   |
|    | mergeDeepWith |   |
|    | mergeDeepWithKey |   |
|    | mergeWith |   |
|    | mergeWithKey |   |
|    | min |   |
|    | minBy |   |
|    | modulo |   |
|    | multiply |   |
|    | nAry |   |
|    | negate |   |
| :white_check_mark: | none | |
| :white_check_mark: | not | |
|    | nth |   |
|    | nthArg |   |
| :white_check_mark: | nullTo | |
|    | o |   |
|    | objOf |   |
|    | of |   |
|    | omit |   |
|    | once |   |
|    | or |   |
| :white_check_mark: | over | |
|    | pair |   |
|    | partial |   |
|    | partialRight |   |
|    | partition |   |
|    | path |   |
|    | pathEq |   |
|    | pathOr |   |
|    | pathSatisfies |   |
|    | pick |   |
|    | pickAll |   |
|    | pickBy |   |
|    | pipe |   |
|    | pipeK |   |
|    | pipeP |   |
|    | pluck |   |
|    | prepend |   |
|    | product |   |
|    | project |   |
|    | prop |   |
|    | propEq |   |
|    | propIs |   |
|    | propOr |   |
|    | props |   |
|    | propSatisfies |   |
|    | range |   |
|    | reduce |   |
|    | reduceBy |   |
|    | reduced |   |
|    | reduceRight |   |
|    | reduceWhile |   |
|    | reject |   |
|    | remove |   |
|    | repeat |   |
|    | replace |   |
| :white_check_mark: | reverse | |
|    | scan |   |
|    | sequence |   |
| :white_check_mark: | set | |
|    | slice |   |
|    | sort |   |
|    | sortBy |   |
|    | sortWith |   |
|    | split |   |
|    | splitAt |   |
|    | splitEvery |   |
|    | splitWhen |   |
|    | startsWith |   |
|    | subtract |   |
|    | sum |   |
|    | symmetricDifference |   |
|    | symmetricDifferenceWith |   |
|    | T |   |
| :white_check_mark: | tail | |
|    | take |   |
|    | takeLast |   |
|    | takeLastWhile |   |
|    | takeWhile |   |
|    | tap |   |
|    | test |   |
|    | times |   |
| :white_check_mark: | toLower | |
|    | toPairs |   |
|    | toPairsIn |   |
|    | toString |   |
| :white_check_mark: | toUpper | |
|    | transduce |   |
|    | transpose |   |
|    | traverse |   |
|    | trim |   |
|    | tryCatch |   |
| :heavy_multiplication_x: | type |   |
|    | unapply |   |
|    | unary |   |
|    | uncurryN |   |
|    | unfold |   |
|    | union |   |
|    | unionWith |   |
|    | uniq |   |
|    | uniqBy |   |
|    | uniqWith |   |
|    | unless |   |
|    | unnest |   |
|    | until |   |
|    | update |   |
|    | useWith |   |
|    | values |   |
|    | valuesIn |   |
|    | view |   |
|    | when |   |
|    | where |   |
|    | whereEq |   |
|    | without |   |
|    | xprod |   |
| :white_check_mark: | zip | |
|    | zipObj |   |
| :white_check_mark: | zipWith | |
|    | _isArrayLike |   |

## Future plans

Other Ramda functions will be added as needed. 

## Contributing

The scope of this library is to port Ramda. Extra functions like concatOptions should be the exception. Contact me on [Twitter](https://twitter.com/rednifre).

Licensed under the LGPL v3, see the LICENSE file for details.
