package de.michaelzinn.ravr;

import io.vavr.Function1;
import java.util.function.BiConsumer;

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
