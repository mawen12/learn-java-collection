package com.mawen.learn.collection;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/11/17
 *
 * @param <E> the type of elements returned by this iterator
 */
public interface Iterator<E> {

	boolean hasNext();

	E next();

	default void remove() {
		throw new UnsupportedOperationException("remove");
	}

	default void forEachRemaining(Consumer<? super E> action) {
		Objects.requireNonNull(action);
		while (hasNext()) {
			action.accept(next());
		}
	}
}
