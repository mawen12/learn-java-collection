package com.mawen.learn.collection;

import java.io.Serializable;
import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/11/22
 */
public class Vector<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, Serializable {

	private static final long serialVersionUID = -722141386479315565L;

	protected Object[] elementData;

	protected int elementCount;

	protected int capacityIncrement;

	public Vector(int initialCapacity, int capacityIncrement) {
		super();
		if (initialCapacity < 0) {
			throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
		}

		this.elementData = new Object[initialCapacity];
		this.capacityIncrement = capacityIncrement;
	}

	public Vector(int initialCapacity) {
		this(initialCapacity, 0);
	}

	public Vector() {
		this(10);
	}

	public Vector(Collection<? extends E> c) {
		elementData = c.toArray();
		elementCount = elementData.length;
		if (elementData.getClass() != Object[].class) {
			elementData = Arrays.copyOf(elementData, elementCount, Object[].class);
		}
	}

	public synchronized void copyInto(Object[] anArray) {
		System.arraycopy(elementData, 0, anArray, 0, elementCount);
	}

	public synchronized void trimToSize() {
		modCount++;
		int oldCapacity = elementData.length;
		if (elementCount < oldCapacity) {
			elementData = Arrays.copyOf(elementData, elementCount);
		}
	}

	public synchronized void ensureCapacity(int minCapacity) {
		if (minCapacity > 0) {
			modCount++;
			ensureCapacityHelper(minCapacity);
		}
	}

	private void ensureCapacityHelper(int minCapacity) {
		if (minCapacity - elementData.length > 0) {
			grow(minCapacity);
		}
	}

	private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

	private void grow(int minCapacity) {
		int oldCapacity = elementData.length;
		int newCapacity = oldCapacity + ((capacityIncrement > 0) ? capacityIncrement : oldCapacity);
		if (newCapacity - minCapacity < 0) {
			newCapacity = minCapacity;
		}
		if (newCapacity - MAX_ARRAY_SIZE > 0) {
			newCapacity = hugeCapacity(minCapacity);
		}
		elementData = Arrays.copyOf(elementData, newCapacity);
	}

	private static int hugeCapacity(int minCapacity) {
		if (minCapacity < 0) {
			throw new OutOfMemoryError();
		}
		return minCapacity > MAX_ARRAY_SIZE ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
	}

	public synchronized void setSize(int newSize) {
		modCount++;
		if (newSize > elementCount) {
			ensureCapacityHelper(newSize);
		}
		else {
			for (int i = newSize; i < elementCount; i++) {
				elementData[i] = null;
			}
		}
		elementCount = newSize;
	}

	public synchronized int capacity() {
		return elementData.length;
	}

	public synchronized int size() {
		return elementCount;
	}

	public synchronized boolean isEmpty() {
		return elementCount == 0;
	}

	public Enumeration<E> elements() {
		return new Enumeration<E>() {
			int count = 0;

			@Override
			public boolean hasMoreElements() {
				return count < elementCount;
			}

			@Override
			public E nextElement() {
				synchronized (Vector.class) {
					if (count < elementCount) {
						return elementData(count++);
					}
				}
				throw new NoSuchElementException("Vector Enumeration");
			}
		};
	}

	public boolean contains(Object o) {
		return indexOf(o, 0) >= 0;
	}

	public int indexOf(Object o) {
		return indexOf(o, 0);
	}

	public synchronized int indexOf(Object o, int index) {
		if (o == null) {
			for (int i = index; i < elementCount; i++) {
				if (elementData[i] == null) {
					return i;
				}
			}
		}
		else {
			for (int i = index; i < elementCount; i++) {
				if (o.equals(elementData[i])) {
					return i;
				}
			}
		}
		return -1;
	}

	public synchronized int lastIndexOf(Object o) {
		return lastIndexOf(o, elementCount - 1);
	}

	public synchronized int lastIndexOf(Object o, int index) {
		if (index >= elementCount) {
			throw new IndexOutOfBoundsException(index + " >= " + elementCount);
		}

		if (o == null) {
			for (int i = index; i >= 0; i--) {
				if (elementData[i] == null) {
					return i;
				}
			}
		}
		else {
			for (int i = index; i >= 0; i--) {
				if (o.equals(elementData[i])) {
					return i;
				}
			}
		}
		return -1;
	}

	public synchronized E elementAt(int index) {
		if (index >= elementCount) {
			throw new ArrayIndexOutOfBoundsException(index + " >= " + elementCount);
		}

		return elementData(index);
	}

	public synchronized E firstElement() {
		if (elementCount == 0) {
			throw new NoSuchElementException();
		}
		return elementData(0);
	}

	public synchronized E lastElement() {
		if (elementCount == 0) {
			throw new NoSuchElementException();
		}
		return elementData(elementCount - 1);
	}

	public synchronized void setElementAt(E obj, int index) {
		if (index >= elementCount) {
			throw new ArrayIndexOutOfBoundsException(index + " >= " + elementCount);
		}
		elementData[index] = obj;
	}

	public synchronized void removeElementAt(int index) {
		modCount++;
		if (index >= elementCount) {
			throw new ArrayIndexOutOfBoundsException(index + " >= " + elementCount);
		}
		else if (index < 0) {
			throw new ArrayIndexOutOfBoundsException(index);
		}

		int j = elementCount - index - 1;
		if (j > 0) {
			System.arraycopy(elementData, index + 1, elementData, index, j);
		}
		elementCount--;
		elementData[elementCount] = null;
	}

	public synchronized void insertElementAt(E obj, int index) {
		modCount++;
		if (index > elementCount) {
			throw new ArrayIndexOutOfBoundsException(index + " > " + elementCount);
		}
		ensureCapacityHelper(elementCount + 1);
		System.arraycopy(elementData, index, elementData, index + 1, elementCount - index);
		elementData[index] = obj;
		elementCount++;
	}

	public synchronized void addElement(E obj) {
		modCount++;
		ensureCapacityHelper(elementCount + 1);
		elementData[elementCount++] = obj;
	}

	public synchronized boolean removeElement(Object obj) {
		modCount++;
		int i = indexOf(obj);
		if (i >= 0) {
			removeElementAt(i);
			return true;
		}
		return false;
	}

	public synchronized void removeAllElements() {
		modCount++;
		for (int i = 0; i < elementCount; i++) {
			elementData[i] = null;
		}
		elementCount = 0;
	}

	public synchronized Object clone() {
		try {
			Vector<E> v = (Vector<E>) super.clone();
			v.elementData = Arrays.copyOf(elementData, elementCount);
			v.modCount = 0;
			return v;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	public synchronized Object[] toArray() {
		return Arrays.copyOf(elementData, elementCount);
	}

	public synchronized <T> T[] toArray(T[] a) {
		if (a.length < elementCount) {
			return (T[]) Arrays.copyOf(elementData, a.length, a.getClass());
		}
		System.arraycopy(elementData, 0, a, 0, elementCount);

		if (a.length > elementCount) {
			a[elementCount] = null;
		}
		return a;
	}

	E elementData(int index) {
		return (E) elementData[index];
	}

	public synchronized E get(int index) {
		if (index >= elementCount) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
		return elementData(index);
	}

	public void add(int index, E element) {
		insertElementAt(element, index);
	}

	public synchronized E remove(int index) {
		modCount++;
		if (index >= elementCount) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
		E oldValue = elementData(index);

		int numMoved = elementCount - index - 1;
		if (numMoved > 0) {
			System.arraycopy(elementData, index + 1, elementData, index, numMoved);
		}
		elementData[--elementCount] = null;

		return oldValue;
	}

	public void clear() {
		removeAllElements();
	}

	public synchronized boolean contains(Collection<?> c) {
		return super.contains(c);
	}

	public synchronized boolean addAll(Collection<? extends E> c) {
		modCount++;
		Object[] a = c.toArray();
		int numNew = a.length;
		ensureCapacityHelper(elementCount + numNew);
		System.arraycopy(a, 0, elementData, elementCount, numNew);
		elementCount += numNew;
		return numNew != 0;
	}

	public synchronized boolean removeAll(Collection<?> c) {
		return super.removeAll(c);
	}

	public synchronized boolean retainAll(Collection<?> c) {
		return super.retainAll(c);
	}

	TODO
}
