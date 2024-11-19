package com.mawen.learn.collection;

import java.util.Comparator;
import java.util.function.Consumer;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/11/17
 * @param <T> the type of elements returned by this Spliterator
 */
public interface Spliterator<T> {

	boolean tryAdvance(Consumer<? super T> action);

	default void forEachRemaining(Consumer<? super T> action) {
		do {

		} while (tryAdvance(action));
	}

	Spliterator<T> trySplit();

	long estimateSize();

	default long getExactSizeIfKnown() {
		return (characteristics() & SIZED) == 0 ? -1L : estimateSize();
	}

	long characteristics();

	default boolean hasCharacteristics(int characteristics) {
		return (characteristics() & characteristics) == characteristics;
	}

	default Comparator<? super T> getComparator() {
		throw new IllegalStateException();
	}

	public static final int ORDERED = 0x00000010;

	public static final int DISTINCT = 0x00000001;

	public static final int SORTED = 0x00000004;

	public static final int SIZED = 0x00000040;

	public static final int NONNULL = 0x00000400;

	public static final int CONCURRENT = 0x00001000;

	public static final int SUBSIZED = 0x00004000;
}
