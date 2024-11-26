package com.mawen.learn.collection;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/11/22
 */
public interface Deque<E> extends Queue<E> {

	void addFirst(E e);

	void addLast(E e);

	boolean offerFirst(E e);

	boolean offerLast(E e);

	E removeFirst();

	E removeLast();

	E pollLast();

	E getFirst();

	E getLast();

	E peekFirst();

	E peekLast();

	boolean removeFirstOccurrence(Object o);

	boolean removeLastOccurrence(Object o);

	boolean add(E e);

	boolean offer(E e);

	E remove();

	E poll();

	E element();

	E peek();

	void push(E e);

	E pop();

	boolean remove(Object o);

	boolean contains(Object o);

	int size();

	Iterator<E> iterator();

	Iterator<E> descendingIterator();
}
