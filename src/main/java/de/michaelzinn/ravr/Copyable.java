package de.michaelzinn.ravr;

import javaslang.control.Try;

import java.util.function.Supplier;

/**
 * Better than Cloneable
 *
 * Created by michael on 27.04.17.
 */
public interface Copyable<Self extends Copyable<Self>> extends Cloneable {


    default Self safeClone(Try.CheckedSupplier<Object> lambdaThatCallsClone) {
        try {
            return (Self) lambdaThatCallsClone.get();
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    Self copy();
    /*
    default Self copy() {
        try {
            return (Self) this.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
    */
}
