package de.michaelzinn.ravr;

import javaslang.Function1;
import javaslang.Function2;
import javaslang.collection.List;
import javaslang.collection.Traversable;
import javaslang.control.Option;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by michael on 16.04.17.
 */
public class R {

    // Ramda

    public static Integer add(Integer x, Integer y) {
        return x + y;
    }

    public static Function1<Integer, Integer> add(Integer x) {
        return y -> x + y;
    }


    public static <A, B>
    Option<B> ap(Option<Function1<A, B>> of, Option<A> oa) {
        return of.flatMap(oa::map);
    }

    public static <A, B>
    List<B> ap(List<Function1<A, B>> fs, List<A> as) {
        return (List<B>) ap((Traversable) fs, (Traversable) as);
    }

    public static <A, B, T extends Traversable>
    T ap(T afxy, T ax) {
        Traversable<Function1<A, B>> fs = (Traversable<Function1<A, B>>) afxy;
        Traversable<A> xs = (Traversable<A>) ax;
        return (T) fs.flatMap(f -> xs.map(f::apply));
    }


    public static <A>
    Predicate<A> complement(Predicate<A> predicate) {
        return a -> !predicate.test(a);
    }


    public static <A>
    A defaultTo(A a, Option<A> optionA) {
        return optionA.getOrElse(a);
    }

    public static <A>
    Function1<Option<A>, A> defaultTo(A a) {
        return optionA -> optionA.getOrElse(a);
    }

    public static <A>
    Function1<A, A> defaultTo(Placeholder __, Option<A> optionA) {
        return optionA::getOrElse;
    }


    public static <A>
    List<A> filter(Predicate<A> p, List<A> list) {
        return list.filter(p);
    }

    public static <A>
    Function1<List<A>, List<A>> filter(Predicate<A> p) {
        return list -> list.filter(p);
    }


    public static <A>
    Option<String> join(String joiner, List<A> list) {
        return list
                .map(Object::toString)
                .reduceOption((l, r) -> l + joiner + r);
    }

    public static <A>
    Function1<List<A>, Option<String>> join(String joiner) {
        return list -> join(joiner, list);
    }


    public static <S, A>
    Lens<S, A> lens(Function1<S, A> getter, BiConsumer<S, A> setter) {
        return new Lens<>(getter, setter);
    }


    public static <A, B>
    List<B> map(Function1<A, B> f, List<A> value) {
        return value.map(f);
    }

    public static <A, B>
    Function1<List<A>, List<B>> map(Function1<A, B> f) {
        return (List<A> list) -> (List<B>) list.map(f);
    }


    public static Integer modulo(Integer a, Integer n) {
        return a % n;
    }

    public static Function1<Integer, Integer> modulo(Placeholder __, Integer n) {
        return a -> modulo(a, n);
    }

    public static Function1<Integer, Integer> modulo(Integer a) {
        return n -> a % n;
    }


    public static <S extends Copyable<S>, A>
    S over(Lens<S, A> lens, Function1<A, A> f, S x) {
        S copy = x.copy();
        lens.setter.accept(copy, f.apply(lens.getter.apply(x)));
        return copy;
    }

    public static <S extends Copyable<S>, A>
    Function1<S, S> over(Lens<S, A> lens, Function1<A, A> f) {
        return x -> over(lens, f, x);
    }

    public static <S extends Copyable<S>, A>
    Function1<Function1<A, A>, Function1<S, S>> over(Lens<S, A> lens) {
        return f -> (x -> over(lens, f, x));
    }


    public static <A>
    Function1<A, A> pipe() {
        return a -> a;
    }

    public static <A, B>
    Function1<A, B> pipe(
            Function1<A, B> ab
    ) {
        return ab;
    }

    public static <A, B, C>
    Function1<A, C> pipe(
            Function1<A, B> ab,
            Function1<B, C> bc
    ) {
        return a -> bc.apply(ab.apply(a));
    }

    public static <A, B, C, D>
    Function1<A, D> pipe(
            Function1<A, B> ab,
            Function1<B, C> bc,
            Function1<C, D> cd
    ) {
        return a -> cd.apply(bc.apply(ab.apply(a)));
    }

    public static <A, B, C, D, E>
    Function1<A, E> pipe(
            Function1<A, B> ab,
            Function1<B, C> bc,
            Function1<C, D> cd,
            Function1<D, E> de
    ) {
        return a -> de.apply(cd.apply(bc.apply(ab.apply(a))));
    }

    public static <A, B, C, D, E, F>
    Function1<A, F> pipe(
            Function1<A, B> ab,
            Function1<B, C> bc,
            Function1<C, D> cd,
            Function1<D, E> de,
            Function1<E, F> ef
    ) {
        return a -> ef.apply(de.apply(cd.apply(bc.apply(ab.apply(a)))));
    }


    public static <S extends Copyable<S>, A>
    S set(Lens<S, A> lens, A value, S object) {
        S copy = object.copy();
        lens.setter.accept(copy, value);
        return copy;
    }

    public static <S extends Copyable<S>, A>
    Function1<S, S> set(Lens<S, A> lens, A value) {
        return object -> set(lens, value, object);
    }

    public static <S extends Copyable<S>, A>
    Function1<A, Function1<S,S>> set(Lens<S, A> lens) {
        return value -> object -> set(lens, value, object);
    }

    public static <S extends Copyable<S>, A>
    Function1<Lens<S, A>, Function1<A, Function1<S, S>>> set() {
        return lens -> value -> object -> set(lens, value, object);
    }


    public static Integer subtract(Integer x, Integer y) {
        return x - y;
    }

    public static Function1<Integer, Integer> subtract(Integer x) {
        return y -> x - y;
    }

    public static Function1<Integer, Integer> subtract(Placeholder __, Integer y) {
        return x -> x - y;
    }

    public static Function1<Integer, Function1<Integer, Integer>> subtract() {
        return x -> y -> x - y;
    }


    public static String toLower(String s) {
        return s.toLowerCase();
    }

    public static Function1<String, String> toLower() {
        return R::toLower;
    }


    public static String toUpper(String s) {
        return s.toUpperCase();
    }

    public static Function1<String, String> toUpper() {
        return R::toUpper;
    }


    public static <S, A>
    A get(Lens<S, A> lens, S s) {
        return lens.getter.apply(s);
    }

    public static <S, A>
    Function1<S, A> get(Lens<S, A> lens) {
        return s -> get(lens, s);
    }


    // Haskell

    public static <A>
    List<A> catOptions(List<Option<A>> list) {
        return list.filter(Option::isDefined).map(Option::get);
    }

    public static <A>
    Function1<List<Option<A>>, List<A>> catOptions() {
        return R::catOptions;
    }

}
