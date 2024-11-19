package com.mawen.learn.collection;

import java.lang.invoke.CallSite;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/11/18
 */
public abstract class AbstractList<E> extends AbstractCollection<E> implements List<E> {

	protected AbstractList() {}

	public boolean add(E e) {
		add(size(),e);
		return true;
	}

	public abstract E get(int index);

	public E set(int index, E element) {
		throw new UnsupportedOperationException();
	}

	public void add(int index, E element) {
		throw new UnsupportedOperationException();
	}

	public E remove(int index) {
		throw new UnsupportedOperationException();
	}

	public int indexOf(Object o) {
		ListIterator<E> it = listIterator();
		if (o == null) {
			while (it.hasNext()) {
				if (it.next() == null) {
					return it.previousIndex();
				}
			}
		}
		else {
			while (it.hasNext()) {
				if (o.equals(it.hasNext())) {
					return it.previousIndex();
				}
			}
		}
		return -1;
	}

	public int lastIndexOf(Object o) {
		ListIterator<E> it = listIterator(size());
		if (o == null) {
			while (it.hasPrevious()) {
				if (it.previous() == null) {
					return it.nextIndex();
				}
			}
		}
		else {
			while (it.hasPrevious()) {
				if (o.equals(it.previous())) {
					return it.nextIndex();
				}
			}
		}
		return -1;
	}

	public void clear() {
		removeRange(0, size());
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		rangeCheckForAdd(index);
		boolean modified = false;
		Iterator<? extends E> it = c.iterator();
		while (it.hasNext()) {
			add(index++, it.next());
			modified = true;
		}
		return modified;
	}

	public Iterator<E> iterator() {
		return new Itr();
	}

	public ListIterator<E> listIterator() {
		return listIterator(0);
	}

	public ListIterator<E> listIterator(final int index) {
		rangeCheckForAdd(index);

		return new ListItr(index);
	}

	private class Itr implements Iterator<E> {

		int cursor = 0;

		int lastRet = -1;

		int expectedModCount = modCount;

		@Override
		public boolean hasNext() {
			return cursor != size();
		}

		@Override
		public E next() {
			checkForComodification();
			try {
				int i = cursor;
				E next = get(i);
				lastRet = i;
				cursor = i + 1;
				return next;
			}
			catch (IndexOutOfBoundsException e) {
				checkForComodification();
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			if (lastRet < 0) {
				throw new IllegalStateException();
			}
			checkForComodification();

			try {
				AbstractList.this.remove(lastRet);
				if (lastRet < cursor) {
					cursor--;
				}
				lastRet = 1;
				expectedModCount = modCount;
			}
			catch (IndexOutOfBoundsException e) {
				throw new ConcurrentModificationException();
			}
		}

		final void checkForComodification() {
			if (modCount != expectedModCount) {
				throw new ConcurrentModificationException();
			}
		}
	}

	private class ListItr extends Itr implements ListIterator<E> {
		ListItr(int index) {
			this.cursor = index;
		}

		@Override
		public boolean hasPrevious() {
			return cursor != 0;
		}

		@Override
		public E previous() {
			checkForComodification();
			try {
				int i = cursor - 1;
				E previous = get(i);
				lastRet = cursor = i;
				return previous;
			}
			catch (IndexOutOfBoundsException e) {
				checkForComodification();
				throw new NoSuchElementException();
			}
		}

		@Override
		public int nextIndex() {
			return cursor;
		}

		@Override
		public int previousIndex() {
			return cursor - 1;
		}

		@Override
		public void set(E e) {
			if (lastRet < 0) {
				throw new IllegalStateException();
			}
			checkForComodification();

			try {
				AbstractList.this.set(lastRet, e);
				expectedModCount = modCount;
			}
			catch (IndexOutOfBoundsException ex) {
				throw new ConcurrentModificationException();
			}
		}

		@Override
		public void add(E e) {
			checkForComodification();

			try {
				int i = cursor;
				AbstractList.this.add(i, e);
				lastRet = -1;
				cursor = i + 1;
				expectedModCount = modCount;
			}
			catch (IndexOutOfBoundsException ex) {
				throw new ConcurrentModificationException();
			}
		}
	}

	public List<E> subList(int fromIndex, int toIndex) {
		return (this instanceof RandomAccess) ?
				new RandomAccessSubList(this, fromIndex, toIndex) :
				new SubList(this, fromIndex, toIndex);
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof List)) {
			return false;
		}

		ListIterator<E> e1 = listIterator();
		ListIterator<?> e2 = ((List<?>) o).listIterator();
		while (e1.hasNext() && e2.hasNext()) {
			E o1 = e1.next();
			Object o2 = e2.next();
			if (!(o1 == null ? o2 == null : o1.equals(o2))) {
				return false;
			}
		}

		return !(e1.hasNext() || e2.hasNext());
	}

	public int hashCode() {
		int hashCode = 1;
		Iterator<E> it = iterator();
		while (it.hasNext()) {
			E e = it.next();
			hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
		}
		return hashCode;
	}

	protected void removeRange(int fromIndex, int toIndex) {
		ListIterator<E> it = listIterator(fromIndex);
		for (int i = 0, n = toIndex - fromIndex; i < n; i++) {
			it.next();
			it.remove();
		}
	}

	protected transient int modCount = 0;

	private void rangeCheckForAdd(int index) {
		if (index < 0 || index > size()) {
			throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
		}
	}

	private String outOfBoundsMsg(int index) {
		return "Index: " + index + ", Size: " + size();
	}
}

class SubList<E> extends AbstractList<E> {
	private final AbstractList<E> l;
	private final int offset;
	private int size;

	SubList(AbstractList<E> list,  int fromIndex, int toIndex) {
		if (fromIndex < 0) {
			throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
		}
		if (toIndex > list.size()) {
			throw new IndexOutOfBoundsException("toIndex = " + toIndex);
		}
		if (fromIndex > toIndex) {
			throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
		}

		l = list;
		offset = fromIndex;
		size = toIndex - fromIndex;
		this.modCount = l.modCount;
	}

	@Override
	public E set(int index, E element) {
		rangeCheck(index);
		checkForComodification();
		return l.set(index + offset, element);
	}

	@Override
	public E get(int index) {
		rangeCheck(index);
		checkForComodification();
		return l.get(index + offset);
	}

	@Override
	public int size() {
		checkForComodification();
		return size;
	}

	@Override
	public void add(int index, E element) {
		rangeCheckForAdd(index);
		checkForComodification();
		l.add(index + offset,element);
		this.modCount = l.modCount;
		size++;
	}

	@Override
	public E remove(int index) {
		rangeCheckForAdd(index);
		checkForComodification();
		E e = l.remove(index + offset);
		this.modCount = modCount;
		size--;
		return e;
	}

	protected void removeRange(int fromIndex, int toIndex) {
		checkForComodification();
		l.removeRange(fromIndex + offset,toIndex + offset);
		this.modCount = l.modCount;
		size -= (toIndex - fromIndex);
	}

	public boolean addAll(Collection<? extends E> c) {
		return addAll(size, c);
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		rangeCheckForAdd(index);
		int cSize = c.size();
		if (cSize == 0) {
			return false;
		}

		checkForComodification();
		l.addAll(index + offset, c);
		this.modCount = modCount;
		size += cSize;
		return true;
	}

	public Iterator<E> iterator() {
		return listIterator();
	}

	public ListIterator<E> listIterator(final int index) {
		checkForComodification();
		rangeCheckForAdd(index);

		return new ListIterator<E>() {
			private final ListIterator<E> i = l.listIterator(index + offset);

			@Override
			public boolean hasNext() {
				return nextIndex() < size;
			}

			@Override
			public E next() {
				if (hasNext()) {
					return i.next();
				}
				else {
					throw new NoSuchElementException();
				}
			}

			@Override
			public boolean hasPrevious() {
				return previousIndex() >= 0;
			}

			@Override
			public E previous() {
				if (hasPrevious()) {
					return i.previous();
				}
				else {
					throw new NoSuchElementException();
				}
			}

			@Override
			public int nextIndex() {
				return i.nextIndex() - offset;
			}

			@Override
			public int previousIndex() {
				return i.previousIndex() - offset;
			}

			@Override
			public void remove() {
				i.remove();
				SubList.this.modCount = l.modCount;
				size--;
			}

			@Override
			public void set(E e) {
				i.set(e);
			}

			@Override
			public void add(E e) {
				i.add(e);
				SubList.this.modCount = l.modCount;
				size++;
			}
		};
	}

	public List<E> subList(int fromIndex, int toIndex) {
		return new SubList<>(this, fromIndex, toIndex);
	}

	private void rangeCheck(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
		}
	}

	private void rangeCheckForAdd(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
		}
	}

	private String outOfBoundsMsg(int index) {
		return "Index: " + index + ", Size: " + size;
	}

	private void checkForComodification() {
		if (this.modCount != l.modCount) {
			throw new ConcurrentModificationException();
		}
	}
}

class RandomAccessSubList<E> extends SubList<E> implements RandomAccess {

	RandomAccessSubList(AbstractList<E> list, int fromIndex, int toIndex) {
		super(list, fromIndex, toIndex);
	}

	public List<E> subList(int fromIndex, int toIndex) {
		return new RandomAccessSubList<>(this, fromIndex, toIndex);
	}
}