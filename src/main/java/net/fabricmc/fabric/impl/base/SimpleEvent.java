package net.fabricmc.fabric.impl.base;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public final class SimpleEvent<T> {
    private final List<T> listeners = new CopyOnWriteArrayList<>();

    public void register(T listener) {
        listeners.add(listener);
    }

    public void fire(Consumer<T> invoker) {
        for (T listener : listeners) {
            invoker.accept(listener);
        }
    }
}
