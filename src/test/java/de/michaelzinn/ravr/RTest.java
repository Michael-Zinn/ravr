package de.michaelzinn.ravr;

import javaslang.Function1;
import javaslang.collection.List;
import javaslang.control.Option;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

import static de.michaelzinn.ravr.Placeholder.__;
import static de.michaelzinn.ravr.R.*;
import static java.util.Objects.nonNull;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by michael on 16.04.17.
 */
public class RTest {

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
                        join(".."),
                        Option::get
                ).apply(List.of(1, 2, 3))
        );
    }

    // Ramda

    @Test
    public void testAdd() {
        assertEquals((Integer) 3, R.add(1, 2));
        assertEquals(List.of(2, 3, 4), List.of(1, 2, 3).map(R.add(1)));
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
                join(""),
                defaultTo("nope")
        );

        assertEquals(
                "13",
                f.apply(List.of(1, 2, 3))
        );
    }


    @Test
    public void testJoin() {
        assertEquals(
                Option.some("hey ho"),
                join(" ", List.of("hey", "ho")));

        assertEquals(
                Option.some("1:2:3"),
                join(":", List.of(1, 2, 3))
        );

        assertEquals(
                Option.some("noJoiner"),
                join("notUsed", List.of("noJoiner"))
        );

        assertThat(
                join(" ", List.of()),
                is(Option.none())
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
            return safeClone(() -> clone());
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
                        .map(over(name, toUpper())) ,
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
                multiply( new AtomicInteger(2), new AtomicInteger(3) ).get(),
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
        List<String> words = List.of("SIHT", "SI", "GNITSERETNI");

        assertThat(
                words.map(R.pipe(
                        R::reverse,
                        R::toLower
                )),
                is(List.of("this", "is", "interesting"))
        );

        /*
        assertThat(
                add(1),
        );
        */
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


    // Haskell

    @Test
    public void testCatOptions() {

        assertThat(
                catOptions(List.of(Option.some("he"), Option.none(), Option.some("hm"))),
                is(List.of("he", "hm"))
        );

        Function1<String, Option<String>> removeFoo = s ->
                s.equals("foo") ? Option.none() : Option.some(s);

        assertThat(
                pipe(
                        map(removeFoo),
                        catOptions()
                ).apply(List.of("Hey", "foo", "keep")),
                is(List.of("Hey", "keep"))
        );

    }
}
