package com.mawen.learn.collection;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * An ordered collection (also known as a sequence). The user of this interface
 * has precise control over where in the list each element is inserted.The user
 * can access elements by their integer index (position in the list), and search
 * for elements in the list.
 * <p>
 * Unlike sets, lists typically allow duplicate elements. More formally, lists
 * typically allow pairs of elements {@code e1} and {@code e2} such that
 * {@code e1.equals(e2)}, and they typically allow multiple null elements
 * if they allow null elements at all. It is not inconceivable that someone
 * might wish to implement a list that prohibits duplicates, by throwing runtime
 * exceptions when the user attempts to insert them, but we except this usage
 * to be rare.
 * <p>
 * The List interface places additional stipulations, beyond those specified
 * in the Collection interface, on the contracts of the iterator, add, remove,
 * equals, and hashCode methods. Declarations for other inherited methods are
 * also included here for convenience.
 * <p>
 * The List interface provides four methods for positional (indexed) access to
 * list elements. Lists (like Java arrays) are zero based. Note that these
 * operations may execute in time proportional to the index value for some
 * implementations (the LinkedList class, for example). Thus, iterating over
 * the elements in a list is typically preferable to indexing through it if
 * the caller does not know the implementation.
 * <p>
 * The List interface provides a special iterator, called a {@code ListIterator},
 * that allows element insertion and replacement, and bidirectional access in
 * addition to the normal operations that the {@code Iterator} interface
 * provides. A method is provided to obtain a list iterator that starts at
 * a specified position int the list.
 * <p>
 * The List interface provides two methods to search for a specified object.
 * From a performance standpoint, these methods should be used with caution.
 * In many implementations they will perform costly linear searches.
 * <p>
 * The List interface provides two methods to efficiently insert and remove
 * multiple elements at an arbitrary point in the list.
 * <p>
 * Note: While it is permissible for lists to contain themselves as elements,
 * extreme caution is advised: the equals and hashCode methods are no longer
 * well defined on such a list.
 * <p>
 * This interface is a member of the {@code Java Collections Framework}.
 *
 * @param <E> the type of elements maintained by this list
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/11/17
 */
public interface List<E> extends Collection<E> {

	int size();

	boolean isEmpty();

	boolean contains(Object o);

	Iterator<E> iterator();

	Object[] toArray();

	<T> T[] toArray(T[] a);

	boolean add(E e);

	boolean remove(Object o);

	boolean containsAll(Collection<?> c);

	boolean addAll(Collection<? extends E> c);

	boolean addAll(int index, Collection<? extends E> c);

	boolean removeAll(Collection<?> c);

	boolean retainAll(Collection<?> c);

	default void replaceAll(UnaryOperator<E> operator) {
		Objects.requireNonNull(operator);
		ListIterator<E> li = listIterator();
		while (li.hasNext()) {
			li.set(operator.apply(li.next()));
		}
	}

	default void sort(Comparator<? super E> c) {
		Object[] a = toArray();
		Arrays.sort(a, (Comparator) c);
		ListIterator<E> i = listIterator();
		for (Object e : a) {
			i.next();
			i.set((E)e);
		}
	}

	void clear();

	int hashCode();

	E get(int index);

	E set(int index, E element);

	void add(int index, E element);

	E remove(int index);

	int indexOf(Object o);

	int lastIndexOf(Object o);

	ListIterator<E> listIterator();

	ListIterator<E> listIterator(int index);

	List<E> subList(int fromIndex, int toIndex);
}
