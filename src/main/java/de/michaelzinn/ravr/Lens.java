package de.michaelzinn.ravr;

import javaslang.Function1;
import javaslang.Function2;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by michael on 27.04.17.
 */
public class Lens<S, A> {

    public final Function1<S, A> getter;
    public final BiConsumer<S, A> setter;

    public Lens(Function1<S, A> getter, BiConsumer<S, A> setter) {
        this.getter = getter;
        this.setter = setter;
    }

}
