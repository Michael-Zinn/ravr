package de.michaelzinn.ravr;

import io.vavr.Function1;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

import static de.michaelzinn.ravr.Placeholder.__;
import static de.michaelzinn.ravr.Ravr.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by michael on 16.04.17.
 */
public class RavrTest {

    @Test
    public void complexExample() {
        assertEquals(
                List.of(0, 1, 2),
                map(subtract(__, 1), List.of(1, 2, 3))
        );

        /*
        doPipe(
                List.of(1, 2, 3),
                map(subtract(__, 1))
        );
        */

        pipe(
                map(subtract(__, 1)),
                join("..")
        ).apply(List.of(1, 2, 3));

        assertEquals(
                "0..1..2",
                pipe(
                        map(subtract(__, 1)),
                        join("..")
                ).apply(List.of(1, 2, 3))
        );
    }

    // Ramda

    @Test
    public void testAdd() {
        assertEquals((Integer) 3, Ravr.add(1, 2));
        assertEquals(List.of(2, 3, 4), List.of(1, 2, 3).map(Ravr.add(1)));
    }

    @Test
    public void apList() {
        String a = "a";
        Integer aLength = 1;
        Integer aHash = a.hashCode();

        String b = "bbb";
        Integer bLength = 3;
        Integer bHash = b.hashCode();

        List<Function1<String, Integer>> fs = List.of(String::length, String::hashCode);
        List<String> strings = List.of(a, b);

        List<Integer> result = ap(fs, strings);

        assertEquals(List.of(aLength, bLength, aHash, bHash), result);
    }

    @Test
    public void apOption() {
        Option<Function1<String, Integer>>
                f = Option.some(String::length);
        Option<String>
                s = Option.some("hey");

        assertEquals(Option.some(3), ap(f, s));
        assertEquals(Option.none(), ap(Option.none(), s));
        assertEquals(Option.none(), ap(f, Option.none()));
        assertEquals(Option.none(), ap(Option.none(), Option.none()));
    }


    @Test
    public void testDefaultTo() {

        assertThat(defaultTo(5, Option.none()), is(5));
        assertThat(defaultTo("nope", Option.some("yes")), is("yes"));

        assertThat(
                map(defaultTo("null"), List.of(Option.some("y"), Option.none())),
                is(List.of("y", "null"))
        );

    }


    @Test
    public void testFilter() {

        Predicate<Integer> isEven = x -> x % 2 == 0;

        assertEquals(
                List.of(2, 4),
                filter(isEven, List.range(1, 5))
        );

        Function1<List<Integer>, String> f = pipe(
                filter(complement(isEven)),
                join("")
        );

        assertEquals(
                "13",
                f.apply(List.of(1, 2, 3))
        );
    }



    @Test
    public void testFlatMap() {
        Function1<String, Option<Integer>> length = pipe(
                String::length,
                Option::some
        );

        assertThat(
                flatMap(length, Option.some("hey")),
                is(Option.some(3))
        );
        assertThat(
                flatMapOption(length).apply(Option.some("hey")),
                is(Option.some(3))
        );


        Function1<String, List<String>> twice = s -> List.of(s, s);

        assertThat(
                flatMap(twice, List.of("a", "b")),
                is(List.of("a", "a", "b", "b"))
        );

        assertThat(
                flatMapList(twice).apply(List.of("a", "b")),
                is(List.of("a", "a", "b", "b"))
        );

    }


    @Test
    public void testHead() {
        assertThat(
                head(List.of("Hey", "ho")),
                is(Option.of("Hey"))
        );

        assertThat(
                head(List.of()),
                is(Option.none())
        );

        assertThat(
                head("Hey"),
                is(Option.some('H'))
        );

        assertThat(
                head(""),
                is(Option.none())
        );
    }


    @Test
    public void testJoin() {
        assertThat(
                join(" ", List.of("hey", "ho")),
                is("hey ho")
        );

        assertThat(
                join(":", List.of(1, 2, 3)),
                is("1:2:3")
        );

        assertThat(
                join("notUsed", List.of("noJoiner")),
                is("noJoiner")
        );

        assertThat(
                join(" ", List.of()),
                is("")
        );
    }

    class Person implements Copyable<Person> {
        private int id;
        private String name;

        Person(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Person person = (Person) o;

            if (id != person.id) return false;
            return name != null ? name.equals(person.name) : person.name == null;
        }

        @Override
        public int hashCode() {
            int result = id;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            return result;
        }

        @Override
        public Person copy() {
            return safeClone(Try.ofCallable(() -> clone()));
        }
    }

    @Test
    public void testLens() {
        Person bob = new Person(1, "Bob");
        Person originalBob = new Person(1, "Bob");

        assertThat(bob, is(originalBob));

        Lens<Person, Integer> id = lens(Person::getId, Person::setId);
        Lens<Person, String> name = lens(Person::getName, Person::setName);

        assertThat(get(id, bob), is(1));
        assertThat(get(name, bob), is("Bob"));

        Person clone = set(id, 2, bob);
        assertThat(bob, is(originalBob));
        assertThat(clone, is(new Person(2, "Bob")));

        Person BOB = over(name, toUpper(), bob);
        assertThat(BOB, is(new Person(1, "BOB")));
        assertThat(bob, is(originalBob));
        assertThat(over(id, add(2), BOB), is(new Person(3, "BOB")));
        assertThat(BOB, is(new Person(1, "BOB")));


        Person alice = new Person(10, "Alice");
        List<Person> persons = List.of(alice, bob);

        assertThat(
                persons
                        .map(over(id, add(9000)))
                        .map(over(name, toUpper())),
                is(List.of(
                        new Person(9010, "ALICE"),
                        new Person(9001, "BOB")
                ))
        );

    }

    @Test
    public void testModulo() {
        assertThat(modulo(10, 3), is(1));
        assertThat(modulo(-5, 3), is(-2));
        assertThat(modulo(10).apply(3), is(1));
    }


    @Test
    public void testMultiply() {

        /* All direct subtypes of Number:
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

        assertThat(
                multiply(new AtomicInteger(2), new AtomicInteger(3)).get(),
                is(new AtomicInteger(6).get())
        );

        assertThat(
                multiply(new AtomicLong(2), new AtomicLong(3)).get(),
                is(new AtomicLong(6).get())
        );

        assertThat(
                multiply(BigDecimal.TEN, BigDecimal.TEN),
                is(BigDecimal.TEN.multiply(BigDecimal.TEN))
        );

        assertThat(
                multiply(BigInteger.TEN, BigInteger.TEN),
                is(new BigInteger("100"))
        );

        assertThat(
                multiply((byte) 2, (byte) 3),
                is((byte) 6)
        );

        assertThat(
                multiply(2.0, 3.2),
                is(6.4)
        );

        assertThat(
                multiply(2f, 3.2f),
                is(6.4f)
        );

        assertThat(
                multiply(2, 3),
                is(6)
        );

        assertThat(
                multiply(2L, 3L),
                is(6L)
        );

        assertThat(
                multiply((short) 2, (short) 3),
                is((short) 6)
        );

    }


    @Test
    public void testPipe() {
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

    }

    @Test
    public void testReverse() {
        assertThat(
                reverse(List.of(1, 2, 3)),
                is(List.of(3, 2, 1))
        );

        assertThat(
                reverse("inventor"),
                is("rotnevni")
        );
    }

    @Test
    public void testSubtract() {
        assertThat(subtract(8, 3), is(5));

        assertThat(
                List.of(3, 4, 5).map(subtract(__, 2)),
                is(List.of(1, 2, 3))
        );
    }


    class Ref<T> {
        public T t;
    }

    @Test
    public void testTap() {
        Ref<String> s = new Ref<>();
        Ref<String> y = new Ref<>();

        String hey = tap(x -> s.t = x, "hey");
        String yay = tap((String x) -> y.t = x).apply("yay");

        assertThat(hey, is("hey"));
        assertThat(s.t, is("hey"));

        assertThat(yay, is("yay"));
        assertThat(y.t, is("yay"));


    }


    @Test
    public void testZip() {
        assertThat(
                zip(List.of(1, 2, 3), List.of("one", "two", "three")),
                is(List.of(
                        new Tuple2(1, "one"),
                        new Tuple2(2, "two"),
                        new Tuple2(3, "three")
                ))
        );
    }


    @Test
    public void testZipWith() {
        assertThat(
                zipWith(Ravr::add, List.of(1,2,3), List.of(4,5,6)),
                is(List.of(5,7,9))
        );
    }

    // Haskell

    @Test
    public void testConcatOptions() {

        assertThat(
                concatOptions(List.of(Option.some("he"), Option.none(), Option.some("hm"))),
                is(List.of("he", "hm"))
        );

        Function1<String, Option<String>> removeFoo = s ->
                s.equals("foo") ? Option.none() : Option.some(s);

        assertThat(
                pipe(
                        map(removeFoo),
                        concatOptions()
                ).apply(List.of("Hey", "foo", "keep")),
                is(List.of("Hey", "keep"))
        );

    }
}
