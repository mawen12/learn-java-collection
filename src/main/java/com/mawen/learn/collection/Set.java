package com.mawen.learn.collection;

/**
 * A collection that contains no duplicate elements. More formally,
 * sets contain no pair of elements {@code e1} and {@code e2} such that
 * {@code e1.equals(e2)}, and at most one null element. As implied by its name,
 * this interface models the mathematical set abstraction.
 * <p>
 * The set interface places additional stipulations, beyond those inherited
 * from the Collection interface, on the contracts of all constructors and
 * on the contracts of the add, equals and hashCode methods. Declarations
 * other inherited methods are also included here for convenience.
 * <p>
 * Note: Great care must be exercised if mutable objects are used as set elements.
 * The behavior of a set is not specified if the value of an object is changed in
 * a manner that affects equals comparisons while the object is an element in the set.
 * A special case of this prohibition is that it is not permissible for a set to contain
 * itself as an element.
 * <p>
 * This interface is a member of the {@code Java Collections Framework}.
 *
 * @param <E> the type of elements maintained by this set
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/11/17
 */
public interface Set<E> extends Collection<E> {

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

	boolean retainAll(Collection<?> c);

	boolean removeAll(Collection<?> c);

	void clear();

	boolean equals(Object o);

	int hashCode();
}
