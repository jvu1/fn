package com.iodesystems.fn;

public abstract class Transformer<A, B> implements From<A, B> {

    public static <A, B> Transformer<A, B> starting(final From<A, B> from) {
        return new Transformer<A, B>() {
            @Override
            public B from(A a) {
                return from.from(a);
            }
        };
    }

    public <C> Transformer<A, C> then(final From<B, C> next) {
        return new Transformer<A, C>() {
            @Override
            public C from(A a) {
                return next.from(Transformer.this.from(a));
            }
        };
    }
}
