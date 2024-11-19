package com.mawen.learn.collection;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Implementing this interface allows an object to be the target of the "for-each loop"
 * statement.
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/11/17
 *
 * @param <T> the type of elements returned by the iterator
 */
public interface Iterable<T> {

	/**
	 * Returns an iterator over elements of type {@code T}.
	 *
	 * @return an Iterator.
	 */
	Iterator<T> iterator();
}
