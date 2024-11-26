package com.mawen.learn.collection;

import java.util.NoSuchElementException;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/11/22
 */
public abstract class AbstractSequentialList<E> extends AbstractList<E> {

	protected AbstractSequentialList(){}

	public E get(int index) {
		try {
			return listIterator(index).next();
		}
		catch (NoSuchElementException e) {
			throw new IndexOutOfBoundsException("Index: " + index);
		}
	}

	public E set(int index, E element) {
		try {
			ListIterator<E> e = listIterator(index);
			E oldVal = e.next();
			e.set(element);
			return oldVal;
		}
		catch (NoSuchElementException e) {
			throw new IndexOutOfBoundsException("Index: " + index);
		}
	}

	public void add(int index, E element) {
		try {
			listIterator(index).add(element);
		}
		catch (NoSuchElementException e) {
			throw new IndexOutOfBoundsException("Index: " + index);
		}
	}

	public E remove(int index) {
		try {
			ListIterator<E> e = listIterator(index);
			E outCast = e.next();
			e.remove();
			return outCast;
		}
		catch (NoSuchElementException e) {
			throw new IndexOutOfBoundsException("Index: " + index);
		}
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		try {
			boolean modified = false;
			ListIterator<E> e1 = listIterator(index);
			Iterator<? extends E> e2 = c.iterator();
			while (e2.hasNext()) {
				e1.add(e2.next());
				modified = true;
			}
			return modified;
		}
		catch (NoSuchElementException e) {
			throw new IndexOutOfBoundsException("Index: " + index);
		}
	}

	public Iterator<E> iterator() {
		return listIterator();
	}

	public abstract ListIterator<E> listIterator(int index);
}
