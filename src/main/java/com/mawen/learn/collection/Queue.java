package com.mawen.learn.collection;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/11/22
 */
public interface Queue<E> extends Collection<E> {

	boolean add(E e);

	boolean offer(E e);

	E remove();

	E poll();

	E element();

	E peek();
}
