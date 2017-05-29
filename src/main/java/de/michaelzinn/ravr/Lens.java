package de.michaelzinn.ravr;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by michael on 27.04.17.
 */
public class Lens<S, A> {

    public final Function<S, A> getter;
    public final BiConsumer<S, A> setter;

    public Lens(Function<S, A> getter, BiConsumer<S, A> setter) {
        this.getter = getter;
        this.setter = setter;
    }

}
