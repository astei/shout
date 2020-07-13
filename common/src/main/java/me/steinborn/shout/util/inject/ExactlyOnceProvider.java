package me.steinborn.shout.util.inject;

import com.google.inject.Provider;

public abstract class ExactlyOnceProvider<T> implements Provider<T> {
    private static final Object NOT_YET_INVOKED = new Object();
    private volatile Object constructed;

    public ExactlyOnceProvider() {
        this.constructed = NOT_YET_INVOKED;
    }

    @Override
    public T get() {
        if (this.constructed == NOT_YET_INVOKED) {
            synchronized (this) {
                if (this.constructed == NOT_YET_INVOKED) {
                    this.constructed = this.actualProvide();
                }
            }
        }
        return (T) this.constructed;
    }

    protected abstract T actualProvide();

}
