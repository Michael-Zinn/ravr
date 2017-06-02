package de.michaelzinn.ravr;

import io.vavr.*;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Traversable;
import io.vavr.control.Either;
import io.vavr.control.Option;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.*;

/**
 * Created by michael on 16.04.17.
 */
public class Ravr {

    // Ramda

    public static Integer add(Integer x, Integer y) {
        return x + y;
    }

    public static <A>
    List<A> adjust(Function<A, A> f, Integer index, List<A> list) {
        return list.update(index, f);
    }

    public static <A>
    boolean all(Predicate<A> predicate, List<A> list) {
        return list.forAll(predicate);
    }

    public static <A, B>
    A always(A a, B ignore) {
        return a;
    }


    public static <A>
    boolean any(Predicate<A> predicate, List<A> list) {
        // TODO is there really no direct function for this in vavr?
        return list.find(predicate).isDefined();
    }

    public static <A, B>
    Option<B> ap(Option<? extends Function<? super A, ? extends B>> of, Option<A> oa) {
        return of.flatMap(oa::map);
    }

    public static <A, B>
    List<B> ap(List<? extends Function<? super A, ? extends B>> fs, List<A> as) {
        return (List<B>) ap((Traversable) fs, (Traversable) as);
    }

    private static <A, B, T extends Traversable>
    T ap(T afxy, T ax) {
        Traversable<Function<A, B>> fs = (Traversable<Function<A, B>>) afxy;
        Traversable<A> xs = (Traversable<A>) ax;
        return (T) fs.flatMap(f -> xs.map(f::apply));
    }


    public static <L, R, T>
    T applyTuple(BiFunction<L, R, T> f, Tuple2<L, R> tuple) {
        return tuple.apply(f);
    }

    public static <T1, T2, T3, R>
    R applyTuple(Function3<? super T1, ? super T2, ? super T3, ? extends R> f, Tuple3<T1, T2, T3> tuple) {
        return tuple.apply(f);
    }

    public static <A>
    Predicate<A> complement(Predicate<A> predicate) {
        return a -> !predicate.test(a);
    }

    public static <A>
    List<A> concat(List<A> list1, List<A> list2) {
        return list1.appendAll(list2);
    }

    public static <A>
    String concat(String string1, String string2) {
        return string1 + string2;
    }

    public static <A>
    List<A> concatOptions(List<Option<A>> list) {
        return list.filter(Option::isDefined).map(Option::get);
    }

    // TODO hotfix, because this doesn't get generated right now
    public static <A>
    Function<List<Option<A>>, List<A>> concatOptions() {
        return Ravr::concatOptions;
    }

    public static <A>
    boolean contains(A a, List<A> list) {
        return list.contains(a);
    }

    public static <A>
    int count(Predicate<? super A> predicate, List<A> list) {
        return list.count(predicate);
    }

    public static Integer dec(Integer x) {
        return --x;
    }

    public static <A>
    A defaultTo(A a, Option<A> optionA) {
        return optionA.getOrElse(a);
    }

    public static <A>
    boolean eq(A a1, A a2) {
        return a1.equals(a2);
    }

    public static <A>
    List<A> filter(Predicate<A> p, List<A> list) {
        return list.filter(p);
    }


    public static <A>
    int findIndex(Predicate<A> predicate, List<A> list) {
        return list.indexWhere(predicate);
    }

    public static <A, B>
    List<B> flatMap(Function<A, List<B>> f, List<A> ma) {
        return ma.flatMap(f);
    }

    /*
    public static <A, B, T extends Traversable>
    Function1<T, T> flatMap(Function1<A,T> f) {
        return ma -> {
            if(ma instanceof List) {
                return (T) flatMap((Function1<A,List<B>>) f, (List<A>) ma);
            } else if(ma instanceof Option) {
                return (T) flatMap((Function1<A,Option<B>>) f, (Option<A>) ma);
            }
        }
    }
    */

    public static <A, B>
    Function1<List<A>, List<B>> flatMapList(Function<A, List<B>> f) {
        return ma -> flatMap(f, ma);
    }

    public static <A, B>
    Option<B> flatMap(Function<A, Option<B>> f, Option<A> ma) {
        return ma.flatMap(f);
    }

    public static <A, B>
    Function1<Option<A>, Option<B>> flatMapOption(Function<A, Option<B>> f) {
        return ma -> flatMap(f, ma);
    }


    public static <A>
    List<A> forEach(Consumer<A> consumer, List<A> list) {
        list.forEach(consumer);
        return list;
    }

    public static <S, A>
    A get(Lens<S, A> lens, S s) {
        return lens.getter.apply(s);
    }

    public static <A, B>
    Map<B, List<A>> groupBy(Function<? super A, ? extends B> classifier, List<A> list) {
        return list.groupBy(classifier);
    }

    public static <A>
    Option<A> head(List<A> list) {
        return list.headOption();
    }

    public static <A>
    A identity(A a) {
        return a;
    }

    public static <A, B>
    B ifElse(Predicate<A> predicate, Function<A, B> then, Function<A, B> els, A value) {
        return predicate.test(value) ? then.apply(value) : els.apply(value);
    }

    public static Integer inc(Integer x) {
        return ++x;
    }

    public static String init(String string) {
        return string.substring(0, string.length() - 1);
    }

    public static <A>
    boolean isNone(Option<A> any) {
        return any.isEmpty();
    }

    public static <A>
    boolean isSome(Option<A> any) {
        return any.isDefined();
    }

    public static <A>
    String join(String joiner, List<A> list) {
        return list
                .map(Object::toString)
                .reduceOption((l, r) -> l + joiner + r)
                .getOrElse("");
    }


    public static <A>
    Option<String> joinOption(String joiner, List<A> list) {
        return list
                .map(Object::toString)
                .reduceOption((l, r) -> l + joiner + r);
    }

    public static <S, A>
    Lens<S, A> lens(Function<S, A> getter, BiConsumer<S, A> setter) {
        return new Lens<>(getter, setter);
    }


    public static <A, B>
    List<B> map(Function<A, B> f, List<A> value) {
        return value.map(f);
    }

    public static <A, B>
    Option<B> map_Option(Function<A, B> f, Option<A> value) {
        return value.map(f);
    }

    public static <L, R, Z>
    Either<L, Z> map_Either(Function<R, Z> f, Either<L, R> either) {
        return either.map(f);
    }

    public static <L, R, Z>
    Either<Z, R> map_Either_left(Function<L, Z> f, Either<L, R> either) {
        return either.mapLeft(f);
    }

    public static Integer modulo(Integer a, Integer n) {
        return a % n;
    }

    public static AtomicInteger multiply(AtomicInteger a, AtomicInteger b) {
        return new AtomicInteger(a.get() * b.get());
    }
        /*
            AtomicInteger,
            AtomicLong,
            BigDecimal,
            BigInteger,
            Byte,
            Double,
            Float,
            Integer,
            Long,
            Short
         */

    public static AtomicLong multiply(AtomicLong a, AtomicLong b) {
        return new AtomicLong(a.get() * b.get());
    }

    public static Byte multiply(Byte a, Byte b) {
        return (Byte) (byte) (a * b);
    }

    public static Double multiply(Double a, Double b) {
        return a * b;
    }

    public static Float multiply(Float a, Float b) {
        return a * b;
    }

    public static BigDecimal multiply(BigDecimal a, BigDecimal b) {
        return a.multiply(b);
    }

    public static BigInteger multiply(BigInteger a, BigInteger b) {
        return a.multiply(b);
    }

    public static Integer multiply(Integer a, Integer b) {
        return a * b;
    }

    public static Long multiply(Long a, Long b) {
        return a * b;
    }

    public static Short multiply(Short a, Short b) {
        return (short) (a * b);
    }

    public static <A>
    boolean none(Predicate<A> predicate, List<A> list) {
        return list.find(predicate).isEmpty();
    }

    public static boolean not(boolean b) {
        return !b;
    }

    public static <T>
    T nullTo(T fallback, T nullable) {
        return nullable != null ? nullable : fallback;
    }

    public static <T>
    Function<T, T> nullTo(T fallback) {
        return nullable -> nullTo(fallback, nullable);
    }

    public static <S extends Copyable<S>, A>
    S over(Lens<S, A> lens, Function<A, A> f, S x) {
        S copy = x.copy();
        lens.setter.accept(copy, f.apply(lens.getter.apply(x)));
        return copy;
    }

    public static <A>
    List<A> reverse(List<A> list) {
        return list.reverse();
    }

    public static <S extends Copyable<S>, A>
    S set(Lens<S, A> lens, A value, S object) {
        S copy = object.copy();
        lens.setter.accept(copy, value);
        return copy;
    }

    public static <A, C extends Comparable<? super C>>
    List<A> sortBy(Function<? super A, ? extends C> by, List<A> list) {
        return list.sortBy(by);
    }

    public static Integer subtract(Integer x, Integer y) {
        return x - y;
    }

    public static <T> List<T> tail(List<T> list) {
        return list.tail();
    }

    public static <T> T tap(Consumer<T> block, T value) {
        block.accept(value);
        return value;
    }

    public static <T> Function<T, T> tap(Consumer<T> block) {
        return value -> tap(block, value);
    }

    /*
    public static <T> T tap(Runnable block, T value) {
        block.run();
        return value;
    }

    public static <T> Function<T, T> tap(Runnable block) {
        return value -> tap(block, value);
    }
    */

    public static String toLower(String s) {
        return s.toLowerCase();
    }

    public static String toUpper(String s) {
        return s.toUpperCase();
    }

    public static <A>
    List<A> without(List<A> remove, List<A> list) {
        return list.removeAll(remove);
    }

    public static <A, B>
    List<Tuple2<A, B>> zip(List<A> a, List<B> b) {
        return a.zip(b);
    }

    public static <T, U, R>
    List<R> zipWith(BiFunction<T, U, R> biFunction, List<T> t, List<U> u) {
        return t.zipWith(u, biFunction);
    }


    // PARTIAL APPLICATIONS ////////////////////////////////////////////////////////////////////////////////////////////////


    public static Function1<Integer, Integer> add(Placeholder _x, Integer y) {
        return (x) -> add(x, y);
    }

    public static Function1<Integer, Integer> add(Integer x, Placeholder _y) {
        return (y) -> add(x, y);
    }

    public static Function2<Integer, Integer, Integer> add(Placeholder _x, Placeholder _y) {
        return Ravr::add;
    }

    public static Function1<Integer, Integer> add(Integer x) {
        return (y) -> add(x, y);
    }

    public static Function2<Integer, Integer, Integer> add(Placeholder _x) {
        return Ravr::add;
    }

    public static Function2<Integer, Integer, Integer> add() {
        return Ravr::add;
    }

    public static <A>
    Function1<Function<A, A>, List<A>> adjust(Placeholder _f, Integer index, List<A> list) {
        return (f) -> adjust(f, index, list);
    }

    public static <A>
    Function1<Integer, List<A>> adjust(Function<A, A> f, Placeholder _index, List<A> list) {
        return (index) -> adjust(f, index, list);
    }

    public static <A>
    Function2<Function<A, A>, Integer, List<A>> adjust(Placeholder _f, Placeholder _index, List<A> list) {
        return (f, index) -> adjust(f, index, list);
    }

    public static <A>
    Function1<List<A>, List<A>> adjust(Function<A, A> f, Integer index, Placeholder _list) {
        return (list) -> adjust(f, index, list);
    }

    public static <A>
    Function2<Function<A, A>, List<A>, List<A>> adjust(Placeholder _f, Integer index, Placeholder _list) {
        return (f, list) -> adjust(f, index, list);
    }

    public static <A>
    Function2<Integer, List<A>, List<A>> adjust(Function<A, A> f, Placeholder _index, Placeholder _list) {
        return (index, list) -> adjust(f, index, list);
    }

    public static <A>
    Function3<Function<A, A>, Integer, List<A>, List<A>> adjust(Placeholder _f, Placeholder _index, Placeholder _list) {
        return Ravr::adjust;
    }

    public static <A>
    Function1<List<A>, List<A>> adjust(Function<A, A> f, Integer index) {
        return (list) -> adjust(f, index, list);
    }

    public static <A>
    Function2<Function<A, A>, List<A>, List<A>> adjust(Placeholder _f, Integer index) {
        return (f, list) -> adjust(f, index, list);
    }

    public static <A>
    Function2<Integer, List<A>, List<A>> adjust(Function<A, A> f, Placeholder _index) {
        return (index, list) -> adjust(f, index, list);
    }

    public static <A>
    Function3<Function<A, A>, Integer, List<A>, List<A>> adjust(Placeholder _f, Placeholder _index) {
        return Ravr::adjust;
    }

    public static <A>
    Function2<Integer, List<A>, List<A>> adjust(Function<A, A> f) {
        return (index, list) -> adjust(f, index, list);
    }

    public static <A>
    Function3<Function<A, A>, Integer, List<A>, List<A>> adjust(Placeholder _f) {
        return Ravr::adjust;
    }

    public static <A>
    Function3<Function<A, A>, Integer, List<A>, List<A>> adjust() {
        return Ravr::adjust;
    }

    public static <A>
    Predicate<Predicate<A>> all(Placeholder _predicate, List<A> list) {
        return (predicate) -> all(predicate, list);
    }

    public static <A>
    Predicate<List<A>> all(Predicate<A> predicate, Placeholder _list) {
        return (list) -> all(predicate, list);
    }

    public static <A>
    Function2<Predicate<A>, List<A>, Boolean> all(Placeholder _predicate, Placeholder _list) {
        return Ravr::all;
    }

    public static <A>
    Predicate<List<A>> all(Predicate<A> predicate) {
        return (list) -> all(predicate, list);
    }

    public static <A>
    Function2<Predicate<A>, List<A>, Boolean> all(Placeholder _predicate) {
        return Ravr::all;
    }

    public static <A>
    Function2<Predicate<A>, List<A>, Boolean> all() {
        return Ravr::all;
    }

    public static <A, B>
    Function1<A, A> always(Placeholder _a, B ignore) {
        return (a) -> always(a, ignore);
    }

    public static <A, B>
    Function1<B, A> always(A a, Placeholder _ignore) {
        return (ignore) -> always(a, ignore);
    }

    public static <A, B>
    Function1<B, A> always(A a) {
        return (ignore) -> always(a, ignore);
    }

    public static <A>
    Predicate<Predicate<A>> any(Placeholder _predicate, List<A> list) {
        return (predicate) -> any(predicate, list);
    }

    public static <A>
    Predicate<List<A>> any(Predicate<A> predicate, Placeholder _list) {
        return (list) -> any(predicate, list);
    }

    public static <A>
    Function2<Predicate<A>, List<A>, Boolean> any(Placeholder _predicate, Placeholder _list) {
        return Ravr::any;
    }

    public static <A>
    Predicate<List<A>> any(Predicate<A> predicate) {
        return (list) -> any(predicate, list);
    }

    public static <A>
    Function2<Predicate<A>, List<A>, Boolean> any(Placeholder _predicate) {
        return Ravr::any;
    }

    public static <A>
    Function2<Predicate<A>, List<A>, Boolean> any() {
        return Ravr::any;
    }

    public static <A, B>
    Function1<List<? extends Function<? super A, ? extends B>>, List<B>> ap(Placeholder _fs, List<A> as) {
        return (fs) -> ap(fs, as);
    }

    public static <A, B>
    Function1<List<A>, List<B>> ap(List<? extends Function<? super A, ? extends B>> fs, Placeholder _as) {
        return (as) -> ap(fs, as);
    }

    public static <A, B>
    Function1<List<A>, List<B>> ap(List<? extends Function<? super A, ? extends B>> fs) {
        return (as) -> ap(fs, as);
    }

    public static <L, R, T>
    Function1<BiFunction<L, R, T>, T> applyTuple(Placeholder _f, Tuple2<L, R> tuple) {
        return (f) -> applyTuple(f, tuple);
    }

    public static <L, R, T>
    Function1<Tuple2<L, R>, T> applyTuple(BiFunction<L, R, T> f, Placeholder _tuple) {
        return (tuple) -> applyTuple(f, tuple);
    }

    public static <L, R, T>
    Function1<Tuple2<L, R>, T> applyTuple(BiFunction<L, R, T> f) {
        return (tuple) -> applyTuple(f, tuple);
    }

    public static <T1, T2, T3, R>
    Function1<Function3<? super T1, ? super T2, ? super T3, ? extends R>, R> applyTuple(Placeholder _f, Tuple3<T1, T2, T3> tuple) {
        return (f) -> applyTuple(f, tuple);
    }

    public static <T1, T2, T3, R>
    Function1<Tuple3<T1, T2, T3>, R> applyTuple(Function3<? super T1, ? super T2, ? super T3, ? extends R> f, Placeholder _tuple) {
        return (tuple) -> applyTuple(f, tuple);
    }

    public static <T1, T2, T3, R>
    Function1<Tuple3<T1, T2, T3>, R> applyTuple(Function3<? super T1, ? super T2, ? super T3, ? extends R> f) {
        return (tuple) -> applyTuple(f, tuple);
    }

    public static <A>
    Function1<Predicate<A>, Predicate<A>> complement(Placeholder _predicate) {
        return Ravr::complement;
    }

    public static <A>
    Function1<Predicate<A>, Predicate<A>> complement() {
        return Ravr::complement;
    }

    public static <A>
    Function1<List<A>, List<A>> concat(Placeholder _list1, List<A> list2) {
        return (list1) -> concat(list1, list2);
    }

    public static <A>
    Function1<List<A>, List<A>> concat(List<A> list1, Placeholder _list2) {
        return (list2) -> concat(list1, list2);
    }

    public static <A>
    Function1<List<A>, List<A>> concat(List<A> list1) {
        return (list2) -> concat(list1, list2);
    }

    public static Function1<String, String> concat(Placeholder _string1, String string2) {
        return (string1) -> concat(string1, string2);
    }

    public static Function1<String, String> concat(String string1, Placeholder _string2) {
        return (string2) -> concat(string1, string2);
    }

    public static Function1<String, String> concat(String string1) {
        return (string2) -> concat(string1, string2);
    }


    public static <A>
    Predicate<A> contains(Placeholder _a, List<A> list) {
        return (a) -> contains(a, list);
    }

    public static <A>
    Predicate<List<A>> contains(A a, Placeholder _list) {
        return (list) -> contains(a, list);
    }

    public static <A>
    Function2<A, List<A>, Boolean> contains(Placeholder _a, Placeholder _list) {
        return Ravr::contains;
    }

    public static <A>
    Predicate<List<A>> contains(A a) {
        return (list) -> contains(a, list);
    }

    public static <A>
    Function2<A, List<A>, Boolean> contains(Placeholder _a) {
        return Ravr::contains;
    }

    public static <A>
    Function2<A, List<A>, Boolean> contains() {
        return Ravr::contains;
    }

    public static <A>
    Function1<Predicate<? super A>, Integer> count(Placeholder _predicate, List<A> list) {
        return (predicate) -> count(predicate, list);
    }

    public static <A>
    Function1<List<A>, Integer> count(Predicate<? super A> predicate, Placeholder _list) {
        return (list) -> count(predicate, list);
    }

    public static <A>
    Function2<Predicate<? super A>, List<A>, Integer> count(Placeholder _predicate, Placeholder _list) {
        return Ravr::count;
    }

    public static <A>
    Function1<List<A>, Integer> count(Predicate<? super A> predicate) {
        return (list) -> count(predicate, list);
    }

    public static <A>
    Function2<Predicate<? super A>, List<A>, Integer> count(Placeholder _predicate) {
        return Ravr::count;
    }

    public static <A>
    Function2<Predicate<? super A>, List<A>, Integer> count() {
        return Ravr::count;
    }

    public static Function1<Integer, Integer> dec(Placeholder _x) {
        return Ravr::dec;
    }

    public static Function1<Integer, Integer> dec() {
        return Ravr::dec;
    }

    public static <A>
    Function1<A, A> defaultTo(Placeholder _a, Option<A> optionA) {
        return (a) -> defaultTo(a, optionA);
    }

    public static <A>
    Function1<Option<A>, A> defaultTo(A a, Placeholder _optionA) {
        return (optionA) -> defaultTo(a, optionA);
    }

    public static <A>
    Function2<A, Option<A>, A> defaultTo(Placeholder _a, Placeholder _optionA) {
        return Ravr::defaultTo;
    }

    public static <A>
    Function1<Option<A>, A> defaultTo(A a) {
        return (optionA) -> defaultTo(a, optionA);
    }

    public static <A>
    Function2<A, Option<A>, A> defaultTo(Placeholder _a) {
        return Ravr::defaultTo;
    }

    public static <A>
    Function2<A, Option<A>, A> defaultTo() {
        return Ravr::defaultTo;
    }

    public static <A>
    Predicate<A> eq(Placeholder _a1, A a2) {
        return (a1) -> eq(a1, a2);
    }

    public static <A>
    Predicate<A> eq(A a1, Placeholder _a2) {
        return (a2) -> eq(a1, a2);
    }

    public static <A>
    Function2<A, A, Boolean> eq(Placeholder _a1, Placeholder _a2) {
        return Ravr::eq;
    }

    public static <A>
    Predicate<A> eq(A a1) {
        return (a2) -> eq(a1, a2);
    }

    public static <A>
    Function2<A, A, Boolean> eq(Placeholder _a1) {
        return Ravr::eq;
    }

    public static <A>
    Function2<A, A, Boolean> eq() {
        return Ravr::eq;
    }

    public static <A>
    Function1<Predicate<A>, List<A>> filter(Placeholder _p, List<A> list) {
        return (p) -> filter(p, list);
    }

    public static <A>
    Function1<List<A>, List<A>> filter(Predicate<A> p, Placeholder _list) {
        return (list) -> filter(p, list);
    }

    public static <A>
    Function2<Predicate<A>, List<A>, List<A>> filter(Placeholder _p, Placeholder _list) {
        return Ravr::filter;
    }

    public static <A>
    Function1<List<A>, List<A>> filter(Predicate<A> p) {
        return (list) -> filter(p, list);
    }

    public static <A>
    Function2<Predicate<A>, List<A>, List<A>> filter(Placeholder _p) {
        return Ravr::filter;
    }

    public static <A>
    Function2<Predicate<A>, List<A>, List<A>> filter() {
        return Ravr::filter;
    }

    public static <A>
    Function1<Predicate<A>, Integer> findIndex(Placeholder _predicate, List<A> list) {
        return (predicate) -> findIndex(predicate, list);
    }

    public static <A>
    Function1<List<A>, Integer> findIndex(Predicate<A> predicate, Placeholder _list) {
        return (list) -> findIndex(predicate, list);
    }

    public static <A>
    Function2<Predicate<A>, List<A>, Integer> findIndex(Placeholder _predicate, Placeholder _list) {
        return Ravr::findIndex;
    }

    public static <A>
    Function1<List<A>, Integer> findIndex(Predicate<A> predicate) {
        return (list) -> findIndex(predicate, list);
    }

    public static <A>
    Function2<Predicate<A>, List<A>, Integer> findIndex(Placeholder _predicate) {
        return Ravr::findIndex;
    }

    public static <A>
    Function2<Predicate<A>, List<A>, Integer> findIndex() {
        return Ravr::findIndex;
    }

    public static <A, B>
    Function1<Function<A, List<B>>, List<B>> flatMap(Placeholder _f, List<A> ma) {
        return (f) -> flatMap(f, ma);
    }

    public static <A, B>
    Function1<List<A>, List<B>> flatMap(Function<A, List<B>> f, Placeholder _ma) {
        return (ma) -> flatMap(f, ma);
    }

    public static <A, B>
    Function2<Function<A, List<B>>, List<A>, List<B>> flatMap(Placeholder _f, Placeholder _ma) {
        return Ravr::flatMap;
    }

    public static <A, B>
    Function1<List<A>, List<B>> flatMap(Function<A, List<B>> f) {
        return (ma) -> flatMap(f, ma);
    }

    public static <A, B>
    Function2<Function<A, List<B>>, List<A>, List<B>> flatMap(Placeholder _f) {
        return Ravr::flatMap;
    }

    public static <A, B>
    Function2<Function<A, List<B>>, List<A>, List<B>> flatMap() {
        return Ravr::flatMap;
    }

    public static <A>
    Function1<Consumer<A>, List<A>> forEach(Placeholder _consumer, List<A> list) {
        return (consumer) -> forEach(consumer, list);
    }

    public static <A>
    Function1<List<A>, List<A>> forEach(Consumer<A> consumer, Placeholder _list) {
        return (list) -> forEach(consumer, list);
    }

    public static <A>
    Function2<Consumer<A>, List<A>, List<A>> forEach(Placeholder _consumer, Placeholder _list) {
        return Ravr::forEach;
    }

    public static <A>
    Function1<List<A>, List<A>> forEach(Consumer<A> consumer) {
        return (list) -> forEach(consumer, list);
    }

    public static <A>
    Function2<Consumer<A>, List<A>, List<A>> forEach(Placeholder _consumer) {
        return Ravr::forEach;
    }

    public static <A>
    Function2<Consumer<A>, List<A>, List<A>> forEach() {
        return Ravr::forEach;
    }

    public static <S, A>
    Function1<Lens<S, A>, A> get(Placeholder _lens, S s) {
        return (lens) -> get(lens, s);
    }

    public static <S, A>
    Function1<S, A> get(Lens<S, A> lens, Placeholder _s) {
        return (s) -> get(lens, s);
    }

    public static <S, A>
    Function2<Lens<S, A>, S, A> get(Placeholder _lens, Placeholder _s) {
        return Ravr::get;
    }

    public static <S, A>
    Function1<S, A> get(Lens<S, A> lens) {
        return (s) -> get(lens, s);
    }

    public static <S, A>
    Function2<Lens<S, A>, S, A> get(Placeholder _lens) {
        return Ravr::get;
    }

    public static <S, A>
    Function2<Lens<S, A>, S, A> get() {
        return Ravr::get;
    }

    public static <A, B>
    Function1<Function<? super A, ? extends B>, Map<B, List<A>>> groupBy(Placeholder _classifier, List<A> list) {
        return (classifier) -> groupBy(classifier, list);
    }

    public static <A, B>
    Function1<List<A>, Map<B, List<A>>> groupBy(Function<? super A, ? extends B> classifier, Placeholder _list) {
        return (list) -> groupBy(classifier, list);
    }

    public static <A, B>
    Function2<Function<? super A, ? extends B>, List<A>, Map<B, List<A>>> groupBy(Placeholder _classifier, Placeholder _list) {
        return Ravr::groupBy;
    }

    public static <A, B>
    Function1<List<A>, Map<B, List<A>>> groupBy(Function<? super A, ? extends B> classifier) {
        return (list) -> groupBy(classifier, list);
    }

    public static <A, B>
    Function2<Function<? super A, ? extends B>, List<A>, Map<B, List<A>>> groupBy(Placeholder _classifier) {
        return Ravr::groupBy;
    }

    public static <A, B>
    Function2<Function<? super A, ? extends B>, List<A>, Map<B, List<A>>> groupBy() {
        return Ravr::groupBy;
    }

    public static <A>
    Function1<List<A>, Option<A>> head(Placeholder _list) {
        return Ravr::head;
    }

    public static <A>
    Function1<List<A>, Option<A>> head() {
        return Ravr::head;
    }

    public static <A>
    Function1<A, A> identity(Placeholder _a) {
        return Ravr::identity;
    }

    public static <A>
    Function1<A, A> identity() {
        return Ravr::identity;
    }

    public static <A, B>
    Function1<Predicate<A>, B> ifElse(Placeholder _predicate, Function<A, B> then, Function<A, B> els, A value) {
        return (predicate) -> ifElse(predicate, then, els, value);
    }

    public static <A, B>
    Function1<Function<A, B>, B> ifElse(Predicate<A> predicate, Placeholder _then, Function<A, B> els, A value) {
        return (then) -> ifElse(predicate, then, els, value);
    }

    public static <A, B>
    Function2<Predicate<A>, Function<A, B>, B> ifElse(Placeholder _predicate, Placeholder _then, Function<A, B> els, A value) {
        return (predicate, then) -> ifElse(predicate, then, els, value);
    }

    public static <A, B>
    Function1<Function<A, B>, B> ifElse(Predicate<A> predicate, Function<A, B> then, Placeholder _els, A value) {
        return (els) -> ifElse(predicate, then, els, value);
    }

    public static <A, B>
    Function2<Predicate<A>, Function<A, B>, B> ifElse(Placeholder _predicate, Function<A, B> then, Placeholder _els, A value) {
        return (predicate, els) -> ifElse(predicate, then, els, value);
    }

    public static <A, B>
    Function2<Function<A, B>, Function<A, B>, B> ifElse(Predicate<A> predicate, Placeholder _then, Placeholder _els, A value) {
        return (then, els) -> ifElse(predicate, then, els, value);
    }

    public static <A, B>
    Function3<Predicate<A>, Function<A, B>, Function<A, B>, B> ifElse(Placeholder _predicate, Placeholder _then, Placeholder _els, A value) {
        return (predicate, then, els) -> ifElse(predicate, then, els, value);
    }

    public static <A, B>
    Function1<A, B> ifElse(Predicate<A> predicate, Function<A, B> then, Function<A, B> els, Placeholder _value) {
        return (value) -> ifElse(predicate, then, els, value);
    }

    public static <A, B>
    Function2<Predicate<A>, A, B> ifElse(Placeholder _predicate, Function<A, B> then, Function<A, B> els, Placeholder _value) {
        return (predicate, value) -> ifElse(predicate, then, els, value);
    }

    public static <A, B>
    Function2<Function<A, B>, A, B> ifElse(Predicate<A> predicate, Placeholder _then, Function<A, B> els, Placeholder _value) {
        return (then, value) -> ifElse(predicate, then, els, value);
    }

    public static <A, B>
    Function3<Predicate<A>, Function<A, B>, A, B> ifElse(Placeholder _predicate, Placeholder _then, Function<A, B> els, Placeholder _value) {
        return (predicate, then, value) -> ifElse(predicate, then, els, value);
    }

    public static <A, B>
    Function2<Function<A, B>, A, B> ifElse(Predicate<A> predicate, Function<A, B> then, Placeholder _els, Placeholder _value) {
        return (els, value) -> ifElse(predicate, then, els, value);
    }

    public static <A, B>
    Function3<Predicate<A>, Function<A, B>, A, B> ifElse(Placeholder _predicate, Function<A, B> then, Placeholder _els, Placeholder _value) {
        return (predicate, els, value) -> ifElse(predicate, then, els, value);
    }

    public static <A, B>
    Function3<Function<A, B>, Function<A, B>, A, B> ifElse(Predicate<A> predicate, Placeholder _then, Placeholder _els, Placeholder _value) {
        return (then, els, value) -> ifElse(predicate, then, els, value);
    }

    public static <A, B>
    Function4<Predicate<A>, Function<A, B>, Function<A, B>, A, B> ifElse(Placeholder _predicate, Placeholder _then, Placeholder _els, Placeholder _value) {
        return Ravr::ifElse;
    }

    public static <A, B>
    Function1<A, B> ifElse(Predicate<A> predicate, Function<A, B> then, Function<A, B> els) {
        return (value) -> ifElse(predicate, then, els, value);
    }

    public static <A, B>
    Function2<Predicate<A>, A, B> ifElse(Placeholder _predicate, Function<A, B> then, Function<A, B> els) {
        return (predicate, value) -> ifElse(predicate, then, els, value);
    }

    public static <A, B>
    Function2<Function<A, B>, A, B> ifElse(Predicate<A> predicate, Placeholder _then, Function<A, B> els) {
        return (then, value) -> ifElse(predicate, then, els, value);
    }

    public static <A, B>
    Function3<Predicate<A>, Function<A, B>, A, B> ifElse(Placeholder _predicate, Placeholder _then, Function<A, B> els) {
        return (predicate, then, value) -> ifElse(predicate, then, els, value);
    }

    public static <A, B>
    Function2<Function<A, B>, A, B> ifElse(Predicate<A> predicate, Function<A, B> then, Placeholder _els) {
        return (els, value) -> ifElse(predicate, then, els, value);
    }

    public static <A, B>
    Function3<Predicate<A>, Function<A, B>, A, B> ifElse(Placeholder _predicate, Function<A, B> then, Placeholder _els) {
        return (predicate, els, value) -> ifElse(predicate, then, els, value);
    }

    public static <A, B>
    Function3<Function<A, B>, Function<A, B>, A, B> ifElse(Predicate<A> predicate, Placeholder _then, Placeholder _els) {
        return (then, els, value) -> ifElse(predicate, then, els, value);
    }

    public static <A, B>
    Function4<Predicate<A>, Function<A, B>, Function<A, B>, A, B> ifElse(Placeholder _predicate, Placeholder _then, Placeholder _els) {
        return Ravr::ifElse;
    }

    public static <A, B>
    Function2<Function<A, B>, A, B> ifElse(Predicate<A> predicate, Function<A, B> then) {
        return (els, value) -> ifElse(predicate, then, els, value);
    }

    public static <A, B>
    Function3<Predicate<A>, Function<A, B>, A, B> ifElse(Placeholder _predicate, Function<A, B> then) {
        return (predicate, els, value) -> ifElse(predicate, then, els, value);
    }

    public static <A, B>
    Function3<Function<A, B>, Function<A, B>, A, B> ifElse(Predicate<A> predicate, Placeholder _then) {
        return (then, els, value) -> ifElse(predicate, then, els, value);
    }

    public static <A, B>
    Function4<Predicate<A>, Function<A, B>, Function<A, B>, A, B> ifElse(Placeholder _predicate, Placeholder _then) {
        return Ravr::ifElse;
    }

    public static <A, B>
    Function3<Function<A, B>, Function<A, B>, A, B> ifElse(Predicate<A> predicate) {
        return (then, els, value) -> ifElse(predicate, then, els, value);
    }

    public static <A, B>
    Function4<Predicate<A>, Function<A, B>, Function<A, B>, A, B> ifElse(Placeholder _predicate) {
        return Ravr::ifElse;
    }

    public static <A, B>
    Function4<Predicate<A>, Function<A, B>, Function<A, B>, A, B> ifElse() {
        return Ravr::ifElse;
    }

    public static Function1<Integer, Integer> inc(Placeholder _x) {
        return Ravr::inc;
    }

    public static Function1<Integer, Integer> inc() {
        return Ravr::inc;
    }

    public static <A>
    Predicate<Option<A>> isNone(Placeholder _any) {
        return Ravr::isNone;
    }

    public static <A>
    Predicate<Option<A>> isNone() {
        return Ravr::isNone;
    }

    public static <A>
    Predicate<Option<A>> isSome(Placeholder _any) {
        return Ravr::isSome;
    }

    public static <A>
    Predicate<Option<A>> isSome() {
        return Ravr::isSome;
    }

    public static <A>
    Function1<String, String> join(Placeholder _joiner, List<A> list) {
        return (joiner) -> join(joiner, list);
    }

    public static <A>
    Function1<List<A>, String> join(String joiner, Placeholder _list) {
        return (list) -> join(joiner, list);
    }

    public static <A>
    Function2<String, List<A>, String> join(Placeholder _joiner, Placeholder _list) {
        return Ravr::join;
    }

    public static <A>
    Function1<List<A>, String> join(String joiner) {
        return (list) -> join(joiner, list);
    }

    public static <A>
    Function2<String, List<A>, String> join(Placeholder _joiner) {
        return Ravr::join;
    }

    public static <A>
    Function2<String, List<A>, String> join() {
        return Ravr::join;
    }

    public static <A>
    Function1<String, Option<String>> joinOption(Placeholder _joiner, List<A> list) {
        return (joiner) -> joinOption(joiner, list);
    }

    public static <A>
    Function1<List<A>, Option<String>> joinOption(String joiner, Placeholder _list) {
        return (list) -> joinOption(joiner, list);
    }

    public static <A>
    Function2<String, List<A>, Option<String>> joinOption(Placeholder _joiner, Placeholder _list) {
        return Ravr::joinOption;
    }

    public static <A>
    Function1<List<A>, Option<String>> joinOption(String joiner) {
        return (list) -> joinOption(joiner, list);
    }

    public static <A>
    Function2<String, List<A>, Option<String>> joinOption(Placeholder _joiner) {
        return Ravr::joinOption;
    }

    public static <A>
    Function2<String, List<A>, Option<String>> joinOption() {
        return Ravr::joinOption;
    }


    public static <A, B>
    Function1<Function<A, B>, List<B>> map(Placeholder _f, List<A> value) {
        return (f) -> map(f, value);
    }

    public static <A, B>
    Function1<List<A>, List<B>> map(Function<A, B> f, Placeholder _value) {
        return (value) -> map(f, value);
    }

    public static <A, B>
    Function2<Function<A, B>, List<A>, List<B>> map(Placeholder _f, Placeholder _value) {
        return Ravr::map;
    }

    public static <A, B>
    Function1<List<A>, List<B>> map(Function<A, B> f) {
        return (value) -> map(f, value);
    }

    public static <A, B>
    Function2<Function<A, B>, List<A>, List<B>> map(Placeholder _f) {
        return Ravr::map;
    }

    public static <A, B>
    Function2<Function<A, B>, List<A>, List<B>> map() {
        return Ravr::map;
    }

    public static <A, B>
    Function1<Function<A, B>, Option<B>> map_Option(Placeholder _f, Option<A> value) {
        return (f) -> map_Option(f, value);
    }

    public static <A, B>
    Function1<Option<A>, Option<B>> map_Option(Function<A, B> f, Placeholder _value) {
        return (value) -> map_Option(f, value);
    }

    public static <A, B>
    Function2<Function<A, B>, Option<A>, Option<B>> map_Option(Placeholder _f, Placeholder _value) {
        return Ravr::map_Option;
    }

    public static <A, B>
    Function1<Option<A>, Option<B>> map_Option(Function<A, B> f) {
        return (value) -> map_Option(f, value);
    }

    public static <A, B>
    Function2<Function<A, B>, Option<A>, Option<B>> map_Option(Placeholder _f) {
        return Ravr::map_Option;
    }

    public static <A, B>
    Function2<Function<A, B>, Option<A>, Option<B>> map_Option() {
        return Ravr::map_Option;
    }

    public static <L, R, Z>
    Function1<Function<R, Z>, Either<L, Z>> map_Either(Placeholder _f, Either<L, R> value) {
        return (f) -> map_Either(f, value);
    }

    public static <L, R, Z>
    Function1<Either<L, R>, Either<L, Z>> map_Either(Function<R, Z> f, Placeholder _value) {
        return (value) -> map_Either(f, value);
    }

    public static <L, R, Z>
    Function2<Function<R, Z>, Either<L, R>, Either<L, Z>> map_Either(Placeholder _f, Placeholder _value) {
        return Ravr::map_Either;
    }

    public static <L, R, Z>
    Function1<Either<L, R>, Either<L, Z>> map_Either(Function<R, Z> f) {
        return (value) -> map_Either(f, value);
    }

    public static <L, R, Z>
    Function2<Function<R, Z>, Either<L, R>, Either<L, Z>> map_Either(Placeholder _f) {
        return Ravr::map_Either;
    }

    public static <L, R, Z>
    Function2<Function<R, Z>, Either<L, R>, Either<L, Z>> map_Either() {
        return Ravr::map_Either;
    }

    public static <L, R, Z>
    Function1<Function<L, Z>, Either<Z, R>> map_Either_left(Placeholder _f, Either<L, R> value) {
        return (f) -> map_Either_left(f, value);
    }

    public static <L, R, Z>
    Function1<Either<L, R>, Either<Z, R>> map_Either_left(Function<L, Z> f, Placeholder _value) {
        return (value) -> map_Either_left(f, value);
    }

    public static <L, R, Z>
    Function2<Function<L, Z>, Either<L, R>, Either<Z, R>> map_Either_left(Placeholder _f, Placeholder _value) {
        return Ravr::map_Either_left;
    }

    public static <L, R, Z>
    Function1<Either<L, R>, Either<Z, R>> map_Either_left(Function<L, Z> f) {
        return (value) -> map_Either_left(f, value);
    }

    public static <L, R, Z>
    Function2<Function<L, Z>, Either<L, R>, Either<Z, R>> map_Either_left(Placeholder _f) {
        return Ravr::map_Either_left;
    }

    public static <L, R, Z>
    Function2<Function<L, Z>, Either<L, R>, Either<Z, R>> map_Either_left() {
        return Ravr::map_Either_left;
    }

    public static <A>
    Predicate<Predicate<A>> none(Placeholder _predicate, List<A> list) {
        return (predicate) -> none(predicate, list);
    }

    public static <A>
    Predicate<List<A>> none(Predicate<A> predicate, Placeholder _list) {
        return (list) -> none(predicate, list);
    }

    public static <A>
    Function2<Predicate<A>, List<A>, Boolean> none(Placeholder _predicate, Placeholder _list) {
        return Ravr::none;
    }

    public static <A>
    Predicate<List<A>> none(Predicate<A> predicate) {
        return (list) -> none(predicate, list);
    }

    public static <A>
    Function2<Predicate<A>, List<A>, Boolean> none(Placeholder _predicate) {
        return Ravr::none;
    }

    public static <A>
    Function2<Predicate<A>, List<A>, Boolean> none() {
        return Ravr::none;
    }

    public static Predicate<Boolean> not(Placeholder _b) {
        return Ravr::not;
    }

    public static Predicate<Boolean> not() {
        return Ravr::not;
    }


    public static <S extends Copyable<S>, A>
    Function1<Lens<S, A>, S> over(Placeholder _lens, Function<A, A> f, S x) {
        return (lens) -> over(lens, f, x);
    }

    public static <S extends Copyable<S>, A>
    Function1<Function<A, A>, S> over(Lens<S, A> lens, Placeholder _f, S x) {
        return (f) -> over(lens, f, x);
    }

    public static <S extends Copyable<S>, A>
    Function2<Lens<S, A>, Function<A, A>, S> over(Placeholder _lens, Placeholder _f, S x) {
        return (lens, f) -> over(lens, f, x);
    }

    public static <S extends Copyable<S>, A>
    Function1<S, S> over(Lens<S, A> lens, Function<A, A> f, Placeholder _x) {
        return (x) -> over(lens, f, x);
    }

    public static <S extends Copyable<S>, A>
    Function2<Lens<S, A>, S, S> over(Placeholder _lens, Function<A, A> f, Placeholder _x) {
        return (lens, x) -> over(lens, f, x);
    }

    public static <S extends Copyable<S>, A>
    Function2<Function<A, A>, S, S> over(Lens<S, A> lens, Placeholder _f, Placeholder _x) {
        return (f, x) -> over(lens, f, x);
    }

    public static <S extends Copyable<S>, A>
    Function3<Lens<S, A>, Function<A, A>, S, S> over(Placeholder _lens, Placeholder _f, Placeholder _x) {
        return Ravr::over;
    }

    public static <S extends Copyable<S>, A>
    Function1<S, S> over(Lens<S, A> lens, Function<A, A> f) {
        return (x) -> over(lens, f, x);
    }

    public static <S extends Copyable<S>, A>
    Function2<Lens<S, A>, S, S> over(Placeholder _lens, Function<A, A> f) {
        return (lens, x) -> over(lens, f, x);
    }

    public static <S extends Copyable<S>, A>
    Function2<Function<A, A>, S, S> over(Lens<S, A> lens, Placeholder _f) {
        return (f, x) -> over(lens, f, x);
    }

    public static <S extends Copyable<S>, A>
    Function3<Lens<S, A>, Function<A, A>, S, S> over(Placeholder _lens, Placeholder _f) {
        return Ravr::over;
    }

    public static <S extends Copyable<S>, A>
    Function2<Function<A, A>, S, S> over(Lens<S, A> lens) {
        return (f, x) -> over(lens, f, x);
    }

    public static <S extends Copyable<S>, A>
    Function3<Lens<S, A>, Function<A, A>, S, S> over(Placeholder _lens) {
        return Ravr::over;
    }

    public static <S extends Copyable<S>, A>
    Function3<Lens<S, A>, Function<A, A>, S, S> over() {
        return Ravr::over;
    }

    public static <A>
    Function1<List<A>, List<A>> reverse(Placeholder _list) {
        return Ravr::reverse;
    }

    public static <A>
    Function1<List<A>, List<A>> reverse() {
        return Ravr::reverse;
    }

    public static <S extends Copyable<S>, A>
    Function1<Lens<S, A>, S> set(Placeholder _lens, A value, S object) {
        return (lens) -> set(lens, value, object);
    }

    public static <S extends Copyable<S>, A>
    Function1<A, S> set(Lens<S, A> lens, Placeholder _value, S object) {
        return (value) -> set(lens, value, object);
    }

    public static <S extends Copyable<S>, A>
    Function2<Lens<S, A>, A, S> set(Placeholder _lens, Placeholder _value, S object) {
        return (lens, value) -> set(lens, value, object);
    }

    public static <S extends Copyable<S>, A>
    Function1<S, S> set(Lens<S, A> lens, A value, Placeholder _object) {
        return (object) -> set(lens, value, object);
    }

    public static <S extends Copyable<S>, A>
    Function2<Lens<S, A>, S, S> set(Placeholder _lens, A value, Placeholder _object) {
        return (lens, object) -> set(lens, value, object);
    }

    public static <S extends Copyable<S>, A>
    Function2<A, S, S> set(Lens<S, A> lens, Placeholder _value, Placeholder _object) {
        return (value, object) -> set(lens, value, object);
    }

    public static <S extends Copyable<S>, A>
    Function3<Lens<S, A>, A, S, S> set(Placeholder _lens, Placeholder _value, Placeholder _object) {
        return Ravr::set;
    }

    public static <S extends Copyable<S>, A>
    Function1<S, S> set(Lens<S, A> lens, A value) {
        return (object) -> set(lens, value, object);
    }

    public static <S extends Copyable<S>, A>
    Function2<Lens<S, A>, S, S> set(Placeholder _lens, A value) {
        return (lens, object) -> set(lens, value, object);
    }

    public static <S extends Copyable<S>, A>
    Function2<A, S, S> set(Lens<S, A> lens, Placeholder _value) {
        return (value, object) -> set(lens, value, object);
    }

    public static <S extends Copyable<S>, A>
    Function3<Lens<S, A>, A, S, S> set(Placeholder _lens, Placeholder _value) {
        return Ravr::set;
    }

    public static <S extends Copyable<S>, A>
    Function2<A, S, S> set(Lens<S, A> lens) {
        return (value, object) -> set(lens, value, object);
    }

    public static <S extends Copyable<S>, A>
    Function3<Lens<S, A>, A, S, S> set(Placeholder _lens) {
        return Ravr::set;
    }

    public static <S extends Copyable<S>, A>
    Function3<Lens<S, A>, A, S, S> set() {
        return Ravr::set;
    }

    public static <A, C extends Comparable<? super C>>
    Function1<Function<? super A, ? extends C>, List<A>> sortBy(Placeholder _by, List<A> list) {
        return (by) -> sortBy(by, list);
    }

    public static <A, C extends Comparable<? super C>>
    Function1<List<A>, List<A>> sortBy(Function<? super A, ? extends C> by, Placeholder _list) {
        return (list) -> sortBy(by, list);
    }

    public static <A, C extends Comparable<? super C>>
    Function2<Function<? super A, ? extends C>, List<A>, List<A>> sortBy(Placeholder _by, Placeholder _list) {
        return Ravr::sortBy;
    }

    public static <A, C extends Comparable<? super C>>
    Function1<List<A>, List<A>> sortBy(Function<? super A, ? extends C> by) {
        return (list) -> sortBy(by, list);
    }

    public static <A, C extends Comparable<? super C>>
    Function2<Function<? super A, ? extends C>, List<A>, List<A>> sortBy(Placeholder _by) {
        return Ravr::sortBy;
    }

    public static <A, C extends Comparable<? super C>>
    Function2<Function<? super A, ? extends C>, List<A>, List<A>> sortBy() {
        return Ravr::sortBy;
    }

    public static <A>
    Function1<List<A>, List<A>> tail(Placeholder _list) {
        return Ravr::tail;
    }

    public static <A>
    Function1<List<A>, List<A>> tail() {
        return Ravr::tail;
    }

    public static Function1<String, String> toLower(Placeholder _s) {
        return Ravr::toLower;
    }

    public static Function1<String, String> toLower() {
        return Ravr::toLower;
    }

    public static Function1<String, String> toUpper(Placeholder _s) {
        return Ravr::toUpper;
    }

    public static Function1<String, String> toUpper() {
        return Ravr::toUpper;
    }

    public static <A>
    Function1<List<A>, List<A>> without(Placeholder _remove, List<A> list) {
        return (remove) -> without(remove, list);
    }

    public static <A>
    Function1<List<A>, List<A>> without(List<A> remove, Placeholder _list) {
        return (list) -> without(remove, list);
    }

    public static <A>
    Function2<List<A>, List<A>, List<A>> without(Placeholder _remove, Placeholder _list) {
        return Ravr::without;
    }

    public static <A>
    Function1<List<A>, List<A>> without(List<A> remove) {
        return (list) -> without(remove, list);
    }

    public static <A>
    Function2<List<A>, List<A>, List<A>> without(Placeholder _remove) {
        return Ravr::without;
    }

    public static <A>
    Function2<List<A>, List<A>, List<A>> without() {
        return Ravr::without;
    }

    public static <A, B>
    Function1<List<A>, List<Tuple2<A, B>>> zip(Placeholder _a, List<B> b) {
        return (a) -> zip(a, b);
    }

    public static <A, B>
    Function1<List<B>, List<Tuple2<A, B>>> zip(List<A> a, Placeholder _b) {
        return (b) -> zip(a, b);
    }

    public static <A, B>
    Function2<List<A>, List<B>, List<Tuple2<A, B>>> zip(Placeholder _a, Placeholder _b) {
        return Ravr::zip;
    }

    public static <A, B>
    Function1<List<B>, List<Tuple2<A, B>>> zip(List<A> a) {
        return (b) -> zip(a, b);
    }

    public static <A, B>
    Function2<List<A>, List<B>, List<Tuple2<A, B>>> zip(Placeholder _a) {
        return Ravr::zip;
    }

    public static <A, B>
    Function2<List<A>, List<B>, List<Tuple2<A, B>>> zip() {
        return Ravr::zip;
    }

    public static <T, U, R>
    Function1<BiFunction<T, U, R>, List<R>> zipWith(Placeholder _biFunction, List<T> t, List<U> u) {
        return (biFunction) -> zipWith(biFunction, t, u);
    }

    public static <T, U, R>
    Function1<List<T>, List<R>> zipWith(BiFunction<T, U, R> biFunction, Placeholder _t, List<U> u) {
        return (t) -> zipWith(biFunction, t, u);
    }

    public static <T, U, R>
    Function2<BiFunction<T, U, R>, List<T>, List<R>> zipWith(Placeholder _biFunction, Placeholder _t, List<U> u) {
        return (biFunction, t) -> zipWith(biFunction, t, u);
    }

    public static <T, U, R>
    Function1<List<U>, List<R>> zipWith(BiFunction<T, U, R> biFunction, List<T> t, Placeholder _u) {
        return (u) -> zipWith(biFunction, t, u);
    }

    public static <T, U, R>
    Function2<BiFunction<T, U, R>, List<U>, List<R>> zipWith(Placeholder _biFunction, List<T> t, Placeholder _u) {
        return (biFunction, u) -> zipWith(biFunction, t, u);
    }

    public static <T, U, R>
    Function2<List<T>, List<U>, List<R>> zipWith(BiFunction<T, U, R> biFunction, Placeholder _t, Placeholder _u) {
        return (t, u) -> zipWith(biFunction, t, u);
    }

    public static <T, U, R>
    Function3<BiFunction<T, U, R>, List<T>, List<U>, List<R>> zipWith(Placeholder _biFunction, Placeholder _t, Placeholder _u) {
        return Ravr::zipWith;
    }

    public static <T, U, R>
    Function1<List<U>, List<R>> zipWith(BiFunction<T, U, R> biFunction, List<T> t) {
        return (u) -> zipWith(biFunction, t, u);
    }

    public static <T, U, R>
    Function2<BiFunction<T, U, R>, List<U>, List<R>> zipWith(Placeholder _biFunction, List<T> t) {
        return (biFunction, u) -> zipWith(biFunction, t, u);
    }

    public static <T, U, R>
    Function2<List<T>, List<U>, List<R>> zipWith(BiFunction<T, U, R> biFunction, Placeholder _t) {
        return (t, u) -> zipWith(biFunction, t, u);
    }

    public static <T, U, R>
    Function3<BiFunction<T, U, R>, List<T>, List<U>, List<R>> zipWith(Placeholder _biFunction, Placeholder _t) {
        return Ravr::zipWith;
    }

    public static <T, U, R>
    Function2<List<T>, List<U>, List<R>> zipWith(BiFunction<T, U, R> biFunction) {
        return (t, u) -> zipWith(biFunction, t, u);
    }

    public static <T, U, R>
    Function3<BiFunction<T, U, R>, List<T>, List<U>, List<R>> zipWith(Placeholder _biFunction) {
        return Ravr::zipWith;
    }

    public static <T, U, R>
    Function3<BiFunction<T, U, R>, List<T>, List<U>, List<R>> zipWith() {
        return Ravr::zipWith;
    }


// TYPE ALIGNED SEQUENCE FUNCTIONS /////////////////////////////////////////////////////////////////////////////////////


    public static <A, B>
    Function<A, B> compose(
            Function<? super A, B> f_A_B
    ) {
        return value -> f_A_B.apply(value);
    }

    public static <A, B, C>
    Function<A, C> compose(
            Function<? super B, C> f_B_C,
            Function<? super A, B> f_A_B
    ) {
        return value -> f_B_C.apply(f_A_B.apply(value));
    }

    public static <A, B, C, D>
    Function<A, D> compose(
            Function<? super C, D> f_C_D,
            Function<? super B, C> f_B_C,
            Function<? super A, B> f_A_B
    ) {
        return value -> f_C_D.apply(f_B_C.apply(f_A_B.apply(value)));
    }

    public static <A, B, C, D, E>
    Function<A, E> compose(
            Function<? super D, E> f_D_E,
            Function<? super C, D> f_C_D,
            Function<? super B, C> f_B_C,
            Function<? super A, B> f_A_B
    ) {
        return value -> f_D_E.apply(f_C_D.apply(f_B_C.apply(f_A_B.apply(value))));
    }

    public static <A, B, C, D, E, F>
    Function<A, F> compose(
            Function<? super E, F> f_E_F,
            Function<? super D, E> f_D_E,
            Function<? super C, D> f_C_D,
            Function<? super B, C> f_B_C,
            Function<? super A, B> f_A_B
    ) {
        return value -> f_E_F.apply(f_D_E.apply(f_C_D.apply(f_B_C.apply(f_A_B.apply(value)))));
    }

    public static <A, B, C, D, E, F, G>
    Function<A, G> compose(
            Function<? super F, G> f_F_G,
            Function<? super E, F> f_E_F,
            Function<? super D, E> f_D_E,
            Function<? super C, D> f_C_D,
            Function<? super B, C> f_B_C,
            Function<? super A, B> f_A_B
    ) {
        return value -> f_F_G.apply(f_E_F.apply(f_D_E.apply(f_C_D.apply(f_B_C.apply(f_A_B.apply(value))))));
    }

    public static <A, B, C, D, E, F, G, H>
    Function<A, H> compose(
            Function<? super G, H> f_G_H,
            Function<? super F, G> f_F_G,
            Function<? super E, F> f_E_F,
            Function<? super D, E> f_D_E,
            Function<? super C, D> f_C_D,
            Function<? super B, C> f_B_C,
            Function<? super A, B> f_A_B
    ) {
        return value -> f_G_H.apply(f_F_G.apply(f_E_F.apply(f_D_E.apply(f_C_D.apply(f_B_C.apply(f_A_B.apply(value)))))));
    }

    public static <A, B, C, D, E, F, G, H, I>
    Function<A, I> compose(
            Function<? super H, I> f_H_I,
            Function<? super G, H> f_G_H,
            Function<? super F, G> f_F_G,
            Function<? super E, F> f_E_F,
            Function<? super D, E> f_D_E,
            Function<? super C, D> f_C_D,
            Function<? super B, C> f_B_C,
            Function<? super A, B> f_A_B
    ) {
        return value -> f_H_I.apply(f_G_H.apply(f_F_G.apply(f_E_F.apply(f_D_E.apply(f_C_D.apply(f_B_C.apply(f_A_B.apply(value))))))));
    }

    public static <A, B, C, D, E, F, G, H, I, J>
    Function<A, J> compose(
            Function<? super I, J> f_I_J,
            Function<? super H, I> f_H_I,
            Function<? super G, H> f_G_H,
            Function<? super F, G> f_F_G,
            Function<? super E, F> f_E_F,
            Function<? super D, E> f_D_E,
            Function<? super C, D> f_C_D,
            Function<? super B, C> f_B_C,
            Function<? super A, B> f_A_B
    ) {
        return value -> f_I_J.apply(f_H_I.apply(f_G_H.apply(f_F_G.apply(f_E_F.apply(f_D_E.apply(f_C_D.apply(f_B_C.apply(f_A_B.apply(value)))))))));
    }

    public static <A, B, C, D, E, F, G, H, I, J, K>
    Function<A, K> compose(
            Function<? super J, K> f_J_K,
            Function<? super I, J> f_I_J,
            Function<? super H, I> f_H_I,
            Function<? super G, H> f_G_H,
            Function<? super F, G> f_F_G,
            Function<? super E, F> f_E_F,
            Function<? super D, E> f_D_E,
            Function<? super C, D> f_C_D,
            Function<? super B, C> f_B_C,
            Function<? super A, B> f_A_B
    ) {
        return value -> f_J_K.apply(f_I_J.apply(f_H_I.apply(f_G_H.apply(f_F_G.apply(f_E_F.apply(f_D_E.apply(f_C_D.apply(f_B_C.apply(f_A_B.apply(value))))))))));
    }


    public static <A, B>
    Function<A, B> pipe(
            Function<? super A, B> f_A_B
    ) {
        return value -> f_A_B.apply(value);
    }

    public static <A, B, C>
    Function<A, C> pipe(
            Function<? super A, B> f_A_B,
            Function<? super B, C> f_B_C
    ) {
        return value -> f_B_C.apply(f_A_B.apply(value));
    }

    public static <A, B, C, D>
    Function<A, D> pipe(
            Function<? super A, B> f_A_B,
            Function<? super B, C> f_B_C,
            Function<? super C, D> f_C_D
    ) {
        return value -> f_C_D.apply(f_B_C.apply(f_A_B.apply(value)));
    }

    public static <A, B, C, D, E>
    Function<A, E> pipe(
            Function<? super A, B> f_A_B,
            Function<? super B, C> f_B_C,
            Function<? super C, D> f_C_D,
            Function<? super D, E> f_D_E
    ) {
        return value -> f_D_E.apply(f_C_D.apply(f_B_C.apply(f_A_B.apply(value))));
    }

    public static <A, B, C, D, E, F>
    Function<A, F> pipe(
            Function<? super A, B> f_A_B,
            Function<? super B, C> f_B_C,
            Function<? super C, D> f_C_D,
            Function<? super D, E> f_D_E,
            Function<? super E, F> f_E_F
    ) {
        return value -> f_E_F.apply(f_D_E.apply(f_C_D.apply(f_B_C.apply(f_A_B.apply(value)))));
    }

    public static <A, B, C, D, E, F, G>
    Function<A, G> pipe(
            Function<? super A, B> f_A_B,
            Function<? super B, C> f_B_C,
            Function<? super C, D> f_C_D,
            Function<? super D, E> f_D_E,
            Function<? super E, F> f_E_F,
            Function<? super F, G> f_F_G
    ) {
        return value -> f_F_G.apply(f_E_F.apply(f_D_E.apply(f_C_D.apply(f_B_C.apply(f_A_B.apply(value))))));
    }

    public static <A, B, C, D, E, F, G, H>
    Function<A, H> pipe(
            Function<? super A, B> f_A_B,
            Function<? super B, C> f_B_C,
            Function<? super C, D> f_C_D,
            Function<? super D, E> f_D_E,
            Function<? super E, F> f_E_F,
            Function<? super F, G> f_F_G,
            Function<? super G, H> f_G_H
    ) {
        return value -> f_G_H.apply(f_F_G.apply(f_E_F.apply(f_D_E.apply(f_C_D.apply(f_B_C.apply(f_A_B.apply(value)))))));
    }

    public static <A, B, C, D, E, F, G, H, I>
    Function<A, I> pipe(
            Function<? super A, B> f_A_B,
            Function<? super B, C> f_B_C,
            Function<? super C, D> f_C_D,
            Function<? super D, E> f_D_E,
            Function<? super E, F> f_E_F,
            Function<? super F, G> f_F_G,
            Function<? super G, H> f_G_H,
            Function<? super H, I> f_H_I
    ) {
        return value -> f_H_I.apply(f_G_H.apply(f_F_G.apply(f_E_F.apply(f_D_E.apply(f_C_D.apply(f_B_C.apply(f_A_B.apply(value))))))));
    }

    public static <A, B, C, D, E, F, G, H, I, J>
    Function<A, J> pipe(
            Function<? super A, B> f_A_B,
            Function<? super B, C> f_B_C,
            Function<? super C, D> f_C_D,
            Function<? super D, E> f_D_E,
            Function<? super E, F> f_E_F,
            Function<? super F, G> f_F_G,
            Function<? super G, H> f_G_H,
            Function<? super H, I> f_H_I,
            Function<? super I, J> f_I_J
    ) {
        return value -> f_I_J.apply(f_H_I.apply(f_G_H.apply(f_F_G.apply(f_E_F.apply(f_D_E.apply(f_C_D.apply(f_B_C.apply(f_A_B.apply(value)))))))));
    }

    public static <A, B, C, D, E, F, G, H, I, J, K>
    Function<A, K> pipe(
            Function<? super A, B> f_A_B,
            Function<? super B, C> f_B_C,
            Function<? super C, D> f_C_D,
            Function<? super D, E> f_D_E,
            Function<? super E, F> f_E_F,
            Function<? super F, G> f_F_G,
            Function<? super G, H> f_G_H,
            Function<? super H, I> f_H_I,
            Function<? super I, J> f_I_J,
            Function<? super J, K> f_J_K
    ) {
        return value -> f_J_K.apply(f_I_J.apply(f_H_I.apply(f_G_H.apply(f_F_G.apply(f_E_F.apply(f_D_E.apply(f_C_D.apply(f_B_C.apply(f_A_B.apply(value))))))))));
    }


    public static <A, B>
    Predicate<A> pipe_Predicate(
            Function<? super A, B> f_A_B,
            Predicate<B> predicate
    ) {
        return value -> predicate.test(f_A_B.apply(value));
    }

    public static <A, B, C>
    Predicate<A> pipe_Predicate(
            Function<? super A, B> f_A_B,
            Function<? super B, C> f_B_C,
            Predicate<C> predicate
    ) {
        return value -> predicate.test(f_B_C.apply(f_A_B.apply(value)));
    }

    public static <A, B, C, D>
    Predicate<A> pipe_Predicate(
            Function<? super A, B> f_A_B,
            Function<? super B, C> f_B_C,
            Function<? super C, D> f_C_D,
            Predicate<D> predicate
    ) {
        return value -> predicate.test(f_C_D.apply(f_B_C.apply(f_A_B.apply(value))));
    }

    public static <A, B, C, D, E>
    Predicate<A> pipe_Predicate(
            Function<? super A, B> f_A_B,
            Function<? super B, C> f_B_C,
            Function<? super C, D> f_C_D,
            Function<? super D, E> f_D_E,
            Predicate<E> predicate
    ) {
        return value -> predicate.test(f_D_E.apply(f_C_D.apply(f_B_C.apply(f_A_B.apply(value)))));
    }

    public static <A, B, C, D, E, F>
    Predicate<A> pipe_Predicate(
            Function<? super A, B> f_A_B,
            Function<? super B, C> f_B_C,
            Function<? super C, D> f_C_D,
            Function<? super D, E> f_D_E,
            Function<? super E, F> f_E_F,
            Predicate<F> predicate
    ) {
        return value -> predicate.test(f_E_F.apply(f_D_E.apply(f_C_D.apply(f_B_C.apply(f_A_B.apply(value))))));
    }

    public static <A, B, C, D, E, F, G>
    Predicate<A> pipe_Predicate(
            Function<? super A, B> f_A_B,
            Function<? super B, C> f_B_C,
            Function<? super C, D> f_C_D,
            Function<? super D, E> f_D_E,
            Function<? super E, F> f_E_F,
            Function<? super F, G> f_F_G,
            Predicate<G> predicate
    ) {
        return value -> predicate.test(f_F_G.apply(f_E_F.apply(f_D_E.apply(f_C_D.apply(f_B_C.apply(f_A_B.apply(value)))))));
    }

    public static <A, B, C, D, E, F, G, H>
    Predicate<A> pipe_Predicate(
            Function<? super A, B> f_A_B,
            Function<? super B, C> f_B_C,
            Function<? super C, D> f_C_D,
            Function<? super D, E> f_D_E,
            Function<? super E, F> f_E_F,
            Function<? super F, G> f_F_G,
            Function<? super G, H> f_G_H,
            Predicate<H> predicate
    ) {
        return value -> predicate.test(f_G_H.apply(f_F_G.apply(f_E_F.apply(f_D_E.apply(f_C_D.apply(f_B_C.apply(f_A_B.apply(value))))))));
    }

    public static <A, B, C, D, E, F, G, H, I>
    Predicate<A> pipe_Predicate(
            Function<? super A, B> f_A_B,
            Function<? super B, C> f_B_C,
            Function<? super C, D> f_C_D,
            Function<? super D, E> f_D_E,
            Function<? super E, F> f_E_F,
            Function<? super F, G> f_F_G,
            Function<? super G, H> f_G_H,
            Function<? super H, I> f_H_I,
            Predicate<I> predicate
    ) {
        return value -> predicate.test(f_H_I.apply(f_G_H.apply(f_F_G.apply(f_E_F.apply(f_D_E.apply(f_C_D.apply(f_B_C.apply(f_A_B.apply(value)))))))));
    }

    public static <A, B, C, D, E, F, G, H, I, J>
    Predicate<A> pipe_Predicate(
            Function<? super A, B> f_A_B,
            Function<? super B, C> f_B_C,
            Function<? super C, D> f_C_D,
            Function<? super D, E> f_D_E,
            Function<? super E, F> f_E_F,
            Function<? super F, G> f_F_G,
            Function<? super G, H> f_G_H,
            Function<? super H, I> f_H_I,
            Function<? super I, J> f_I_J,
            Predicate<J> predicate
    ) {
        return value -> predicate.test(f_I_J.apply(f_H_I.apply(f_G_H.apply(f_F_G.apply(f_E_F.apply(f_D_E.apply(f_C_D.apply(f_B_C.apply(f_A_B.apply(value))))))))));
    }

    public static <A, B, C, D, E, F, G, H, I, J, K>
    Predicate<A> pipe_Predicate(
            Function<? super A, B> f_A_B,
            Function<? super B, C> f_B_C,
            Function<? super C, D> f_C_D,
            Function<? super D, E> f_D_E,
            Function<? super E, F> f_E_F,
            Function<? super F, G> f_F_G,
            Function<? super G, H> f_G_H,
            Function<? super H, I> f_H_I,
            Function<? super I, J> f_I_J,
            Function<? super J, K> f_J_K,
            Predicate<K> predicate
    ) {
        return value -> predicate.test(f_J_K.apply(f_I_J.apply(f_H_I.apply(f_G_H.apply(f_F_G.apply(f_E_F.apply(f_D_E.apply(f_C_D.apply(f_B_C.apply(f_A_B.apply(value)))))))))));
    }


    public static <A, B>
    B doWith(
            A value,
            Function<? super A, B> f_A_B
    ) {
        return f_A_B.apply(value);
    }

    public static <A, B, C>
    C doWith(
            A value,
            Function<? super A, B> f_A_B,
            Function<? super B, C> f_B_C
    ) {
        return f_B_C.apply(f_A_B.apply(value));
    }

    public static <A, B, C, D>
    D doWith(
            A value,
            Function<? super A, B> f_A_B,
            Function<? super B, C> f_B_C,
            Function<? super C, D> f_C_D
    ) {
        return f_C_D.apply(f_B_C.apply(f_A_B.apply(value)));
    }

    public static <A, B, C, D, E>
    E doWith(
            A value,
            Function<? super A, B> f_A_B,
            Function<? super B, C> f_B_C,
            Function<? super C, D> f_C_D,
            Function<? super D, E> f_D_E
    ) {
        return f_D_E.apply(f_C_D.apply(f_B_C.apply(f_A_B.apply(value))));
    }

    public static <A, B, C, D, E, F>
    F doWith(
            A value,
            Function<? super A, B> f_A_B,
            Function<? super B, C> f_B_C,
            Function<? super C, D> f_C_D,
            Function<? super D, E> f_D_E,
            Function<? super E, F> f_E_F
    ) {
        return f_E_F.apply(f_D_E.apply(f_C_D.apply(f_B_C.apply(f_A_B.apply(value)))));
    }

    public static <A, B, C, D, E, F, G>
    G doWith(
            A value,
            Function<? super A, B> f_A_B,
            Function<? super B, C> f_B_C,
            Function<? super C, D> f_C_D,
            Function<? super D, E> f_D_E,
            Function<? super E, F> f_E_F,
            Function<? super F, G> f_F_G
    ) {
        return f_F_G.apply(f_E_F.apply(f_D_E.apply(f_C_D.apply(f_B_C.apply(f_A_B.apply(value))))));
    }

    public static <A, B, C, D, E, F, G, H>
    H doWith(
            A value,
            Function<? super A, B> f_A_B,
            Function<? super B, C> f_B_C,
            Function<? super C, D> f_C_D,
            Function<? super D, E> f_D_E,
            Function<? super E, F> f_E_F,
            Function<? super F, G> f_F_G,
            Function<? super G, H> f_G_H
    ) {
        return f_G_H.apply(f_F_G.apply(f_E_F.apply(f_D_E.apply(f_C_D.apply(f_B_C.apply(f_A_B.apply(value)))))));
    }

    public static <A, B, C, D, E, F, G, H, I>
    I doWith(
            A value,
            Function<? super A, B> f_A_B,
            Function<? super B, C> f_B_C,
            Function<? super C, D> f_C_D,
            Function<? super D, E> f_D_E,
            Function<? super E, F> f_E_F,
            Function<? super F, G> f_F_G,
            Function<? super G, H> f_G_H,
            Function<? super H, I> f_H_I
    ) {
        return f_H_I.apply(f_G_H.apply(f_F_G.apply(f_E_F.apply(f_D_E.apply(f_C_D.apply(f_B_C.apply(f_A_B.apply(value))))))));
    }

    public static <A, B, C, D, E, F, G, H, I, J>
    J doWith(
            A value,
            Function<? super A, B> f_A_B,
            Function<? super B, C> f_B_C,
            Function<? super C, D> f_C_D,
            Function<? super D, E> f_D_E,
            Function<? super E, F> f_E_F,
            Function<? super F, G> f_F_G,
            Function<? super G, H> f_G_H,
            Function<? super H, I> f_H_I,
            Function<? super I, J> f_I_J
    ) {
        return f_I_J.apply(f_H_I.apply(f_G_H.apply(f_F_G.apply(f_E_F.apply(f_D_E.apply(f_C_D.apply(f_B_C.apply(f_A_B.apply(value)))))))));
    }

    public static <A, B, C, D, E, F, G, H, I, J, K>
    K doWith(
            A value,
            Function<? super A, B> f_A_B,
            Function<? super B, C> f_B_C,
            Function<? super C, D> f_C_D,
            Function<? super D, E> f_D_E,
            Function<? super E, F> f_E_F,
            Function<? super F, G> f_F_G,
            Function<? super G, H> f_G_H,
            Function<? super H, I> f_H_I,
            Function<? super I, J> f_I_J,
            Function<? super J, K> f_J_K
    ) {
        return f_J_K.apply(f_I_J.apply(f_H_I.apply(f_G_H.apply(f_F_G.apply(f_E_F.apply(f_D_E.apply(f_C_D.apply(f_B_C.apply(f_A_B.apply(value))))))))));
    }


    public static <A, B>
    Function<A, List<B>> pipeK_List(
            Function<? super A, List<B>> f_A_B
    ) {
        return value -> f_A_B.apply(value);
    }

    public static <A, B, C>
    Function<A, List<C>> pipeK_List(
            Function<? super A, List<B>> f_A_B,
            Function<? super B, List<C>> f_B_C
    ) {
        return value -> f_A_B.apply(value).flatMap(f_B_C);
    }

    public static <A, B, C, D>
    Function<A, List<D>> pipeK_List(
            Function<? super A, List<B>> f_A_B,
            Function<? super B, List<C>> f_B_C,
            Function<? super C, List<D>> f_C_D
    ) {
        return value -> f_A_B.apply(value).flatMap(f_B_C).flatMap(f_C_D);
    }

    public static <A, B, C, D, E>
    Function<A, List<E>> pipeK_List(
            Function<? super A, List<B>> f_A_B,
            Function<? super B, List<C>> f_B_C,
            Function<? super C, List<D>> f_C_D,
            Function<? super D, List<E>> f_D_E
    ) {
        return value -> f_A_B.apply(value).flatMap(f_B_C).flatMap(f_C_D).flatMap(f_D_E);
    }

    public static <A, B, C, D, E, F>
    Function<A, List<F>> pipeK_List(
            Function<? super A, List<B>> f_A_B,
            Function<? super B, List<C>> f_B_C,
            Function<? super C, List<D>> f_C_D,
            Function<? super D, List<E>> f_D_E,
            Function<? super E, List<F>> f_E_F
    ) {
        return value -> f_A_B.apply(value).flatMap(f_B_C).flatMap(f_C_D).flatMap(f_D_E).flatMap(f_E_F);
    }

    public static <A, B, C, D, E, F, G>
    Function<A, List<G>> pipeK_List(
            Function<? super A, List<B>> f_A_B,
            Function<? super B, List<C>> f_B_C,
            Function<? super C, List<D>> f_C_D,
            Function<? super D, List<E>> f_D_E,
            Function<? super E, List<F>> f_E_F,
            Function<? super F, List<G>> f_F_G
    ) {
        return value -> f_A_B.apply(value).flatMap(f_B_C).flatMap(f_C_D).flatMap(f_D_E).flatMap(f_E_F).flatMap(f_F_G);
    }

    public static <A, B, C, D, E, F, G, H>
    Function<A, List<H>> pipeK_List(
            Function<? super A, List<B>> f_A_B,
            Function<? super B, List<C>> f_B_C,
            Function<? super C, List<D>> f_C_D,
            Function<? super D, List<E>> f_D_E,
            Function<? super E, List<F>> f_E_F,
            Function<? super F, List<G>> f_F_G,
            Function<? super G, List<H>> f_G_H
    ) {
        return value -> f_A_B.apply(value).flatMap(f_B_C).flatMap(f_C_D).flatMap(f_D_E).flatMap(f_E_F).flatMap(f_F_G).flatMap(f_G_H);
    }

    public static <A, B, C, D, E, F, G, H, I>
    Function<A, List<I>> pipeK_List(
            Function<? super A, List<B>> f_A_B,
            Function<? super B, List<C>> f_B_C,
            Function<? super C, List<D>> f_C_D,
            Function<? super D, List<E>> f_D_E,
            Function<? super E, List<F>> f_E_F,
            Function<? super F, List<G>> f_F_G,
            Function<? super G, List<H>> f_G_H,
            Function<? super H, List<I>> f_H_I
    ) {
        return value -> f_A_B.apply(value).flatMap(f_B_C).flatMap(f_C_D).flatMap(f_D_E).flatMap(f_E_F).flatMap(f_F_G).flatMap(f_G_H).flatMap(f_H_I);
    }

    public static <A, B, C, D, E, F, G, H, I, J>
    Function<A, List<J>> pipeK_List(
            Function<? super A, List<B>> f_A_B,
            Function<? super B, List<C>> f_B_C,
            Function<? super C, List<D>> f_C_D,
            Function<? super D, List<E>> f_D_E,
            Function<? super E, List<F>> f_E_F,
            Function<? super F, List<G>> f_F_G,
            Function<? super G, List<H>> f_G_H,
            Function<? super H, List<I>> f_H_I,
            Function<? super I, List<J>> f_I_J
    ) {
        return value -> f_A_B.apply(value).flatMap(f_B_C).flatMap(f_C_D).flatMap(f_D_E).flatMap(f_E_F).flatMap(f_F_G).flatMap(f_G_H).flatMap(f_H_I).flatMap(f_I_J);
    }

    public static <A, B, C, D, E, F, G, H, I, J, K>
    Function<A, List<K>> pipeK_List(
            Function<? super A, List<B>> f_A_B,
            Function<? super B, List<C>> f_B_C,
            Function<? super C, List<D>> f_C_D,
            Function<? super D, List<E>> f_D_E,
            Function<? super E, List<F>> f_E_F,
            Function<? super F, List<G>> f_F_G,
            Function<? super G, List<H>> f_G_H,
            Function<? super H, List<I>> f_H_I,
            Function<? super I, List<J>> f_I_J,
            Function<? super J, List<K>> f_J_K
    ) {
        return value -> f_A_B.apply(value).flatMap(f_B_C).flatMap(f_C_D).flatMap(f_D_E).flatMap(f_E_F).flatMap(f_F_G).flatMap(f_G_H).flatMap(f_H_I).flatMap(f_I_J).flatMap(f_J_K);
    }


    public static <A, B>
    Function<A, Option<B>> pipeK_Option(
            Function<? super A, Option<B>> f_A_B
    ) {
        return value -> f_A_B.apply(value);
    }

    public static <A, B, C>
    Function<A, Option<C>> pipeK_Option(
            Function<? super A, Option<B>> f_A_B,
            Function<? super B, Option<C>> f_B_C
    ) {
        return value -> f_A_B.apply(value).flatMap(f_B_C);
    }

    public static <A, B, C, D>
    Function<A, Option<D>> pipeK_Option(
            Function<? super A, Option<B>> f_A_B,
            Function<? super B, Option<C>> f_B_C,
            Function<? super C, Option<D>> f_C_D
    ) {
        return value -> f_A_B.apply(value).flatMap(f_B_C).flatMap(f_C_D);
    }

    public static <A, B, C, D, E>
    Function<A, Option<E>> pipeK_Option(
            Function<? super A, Option<B>> f_A_B,
            Function<? super B, Option<C>> f_B_C,
            Function<? super C, Option<D>> f_C_D,
            Function<? super D, Option<E>> f_D_E
    ) {
        return value -> f_A_B.apply(value).flatMap(f_B_C).flatMap(f_C_D).flatMap(f_D_E);
    }

    public static <A, B, C, D, E, F>
    Function<A, Option<F>> pipeK_Option(
            Function<? super A, Option<B>> f_A_B,
            Function<? super B, Option<C>> f_B_C,
            Function<? super C, Option<D>> f_C_D,
            Function<? super D, Option<E>> f_D_E,
            Function<? super E, Option<F>> f_E_F
    ) {
        return value -> f_A_B.apply(value).flatMap(f_B_C).flatMap(f_C_D).flatMap(f_D_E).flatMap(f_E_F);
    }

    public static <A, B, C, D, E, F, G>
    Function<A, Option<G>> pipeK_Option(
            Function<? super A, Option<B>> f_A_B,
            Function<? super B, Option<C>> f_B_C,
            Function<? super C, Option<D>> f_C_D,
            Function<? super D, Option<E>> f_D_E,
            Function<? super E, Option<F>> f_E_F,
            Function<? super F, Option<G>> f_F_G
    ) {
        return value -> f_A_B.apply(value).flatMap(f_B_C).flatMap(f_C_D).flatMap(f_D_E).flatMap(f_E_F).flatMap(f_F_G);
    }

    public static <A, B, C, D, E, F, G, H>
    Function<A, Option<H>> pipeK_Option(
            Function<? super A, Option<B>> f_A_B,
            Function<? super B, Option<C>> f_B_C,
            Function<? super C, Option<D>> f_C_D,
            Function<? super D, Option<E>> f_D_E,
            Function<? super E, Option<F>> f_E_F,
            Function<? super F, Option<G>> f_F_G,
            Function<? super G, Option<H>> f_G_H
    ) {
        return value -> f_A_B.apply(value).flatMap(f_B_C).flatMap(f_C_D).flatMap(f_D_E).flatMap(f_E_F).flatMap(f_F_G).flatMap(f_G_H);
    }

    public static <A, B, C, D, E, F, G, H, I>
    Function<A, Option<I>> pipeK_Option(
            Function<? super A, Option<B>> f_A_B,
            Function<? super B, Option<C>> f_B_C,
            Function<? super C, Option<D>> f_C_D,
            Function<? super D, Option<E>> f_D_E,
            Function<? super E, Option<F>> f_E_F,
            Function<? super F, Option<G>> f_F_G,
            Function<? super G, Option<H>> f_G_H,
            Function<? super H, Option<I>> f_H_I
    ) {
        return value -> f_A_B.apply(value).flatMap(f_B_C).flatMap(f_C_D).flatMap(f_D_E).flatMap(f_E_F).flatMap(f_F_G).flatMap(f_G_H).flatMap(f_H_I);
    }

    public static <A, B, C, D, E, F, G, H, I, J>
    Function<A, Option<J>> pipeK_Option(
            Function<? super A, Option<B>> f_A_B,
            Function<? super B, Option<C>> f_B_C,
            Function<? super C, Option<D>> f_C_D,
            Function<? super D, Option<E>> f_D_E,
            Function<? super E, Option<F>> f_E_F,
            Function<? super F, Option<G>> f_F_G,
            Function<? super G, Option<H>> f_G_H,
            Function<? super H, Option<I>> f_H_I,
            Function<? super I, Option<J>> f_I_J
    ) {
        return value -> f_A_B.apply(value).flatMap(f_B_C).flatMap(f_C_D).flatMap(f_D_E).flatMap(f_E_F).flatMap(f_F_G).flatMap(f_G_H).flatMap(f_H_I).flatMap(f_I_J);
    }

    public static <A, B, C, D, E, F, G, H, I, J, K>
    Function<A, Option<K>> pipeK_Option(
            Function<? super A, Option<B>> f_A_B,
            Function<? super B, Option<C>> f_B_C,
            Function<? super C, Option<D>> f_C_D,
            Function<? super D, Option<E>> f_D_E,
            Function<? super E, Option<F>> f_E_F,
            Function<? super F, Option<G>> f_F_G,
            Function<? super G, Option<H>> f_G_H,
            Function<? super H, Option<I>> f_H_I,
            Function<? super I, Option<J>> f_I_J,
            Function<? super J, Option<K>> f_J_K
    ) {
        return value -> f_A_B.apply(value).flatMap(f_B_C).flatMap(f_C_D).flatMap(f_D_E).flatMap(f_E_F).flatMap(f_F_G).flatMap(f_G_H).flatMap(f_H_I).flatMap(f_I_J).flatMap(f_J_K);
    }

}
