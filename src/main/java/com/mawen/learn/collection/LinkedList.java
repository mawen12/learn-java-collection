package com.mawen.learn.collection;

import java.io.Serializable;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/11/22
 */
public class LinkedList<E> extends AbstractSequentialList<E>
	implements List<E>, Deque<E>, Cloneable, Serializable {

	transient int size = 0;

	transient Node<E> first;

	transient Node<E> last;

	public LinkedList() {

	}

	public LinkedList(Collection<? extends E> c) {
		this();
		addAll(c);
	}

	private void linkFirst(E e) {
		final Node<E> f = first;
		final Node<E> newNode = new Node<>(null, e, f);
		first = newNode;
		if (f == null) {
			last = newNode;
		}
		else {
			f.prev = newNode;
		}
		size++;
		modCount++;
	}

	private static class Node<E> {
		E item;
		Node<E> next;
		Node<E> prev;

		Node(Node<E> prev, E element, Node<E> next) {
			this.item = element;
			this.prev = prev;
			this.next = next;
		}
	}

	TODO
}
