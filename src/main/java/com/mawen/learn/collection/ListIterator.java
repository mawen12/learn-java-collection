package com.mawen.learn.collection;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/11/17
 */
public interface ListIterator<E> extends Iterator<E> {

	boolean hasNext();

	E next();

	boolean hasPrevious();

	E previous();

	int nextIndex();

	int previousIndex();

	void remove();

	void set(E e);

	void add(E e);
}
