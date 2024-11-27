package com.mawen.learn.collection;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/11/26
 */
public class LinkedHashMap<K, V> extends HashMap<K, V> implements Map<K, V> {

	private static final long serialVersionUID = 4501806949900396568L;

	static class Entry<K, V> extends HashMap.Node<K, V> {
		Entry<K, V> before, after;

		Entry(int hash, K key, V value, Node<K, V> next) {
			super(hash, key, value, next);
		}
	}

	transient LinkedHashMap.Entry<K, V> head;

	transient LinkedHashMap.Entry<K, V> tail;

	final boolean accessOrder;

	private void linkNodeLast(LinkedHashMap.Entry<K, V> p) {
		LinkedHashMap.Entry<K, V> last = tail;
		tail = p;
		if (last == null) {
			head = p;
		}
		else {
			p.before = last;
			last.after = p;
		}
	}

	private void transferLinks(LinkedHashMap.Entry<K, V> src, LinkedHashMap.Entry<K, V> dst) {
		LinkedHashMap.Entry<K, V> b = dst.before = src.before;
		LinkedHashMap.Entry<K, V> a = dst.after = src.after;
		if (b == null) {
			head = dst;
		}
		else {
			b.after = dst;
		}
		if (a == null) {
			tail = dst;
		}
		else {
			a.before = dst;
		}
	}

	void reinitialize() {
		super.reinitialize();
		head = tail = null;
	}

	Node<K, V> newNode(int hash, K key, V value, Node<K, V> e) {
		LinkedHashMap.Entry<K, V> p = new LinkedHashMap.Entry<>(hash, key, value, e);
		linkNodeLast(p);
		return p;
	}

	Node<K, V> replacementNode(Node<K, V> p, Node<K, V> next) {
		LinkedHashMap.Entry<K, V> q = (Entry<K, V>) p;
		LinkedHashMap.Entry<K, V> t = new LinkedHashMap.Entry<>(q.hash, q.key, q.value, next);
		transferLinks(q, t);
		return t;
	}

	TreeNode<K, V> newTreeNode(int hash, K key, V value, Node<K, V> next) {
		TreeNode<K, V> p = new TreeNode<>(hash, key, value, next);
		linkNodeLast(p);
		return p;
	}

	TreeNode<K, V> replacementTreeNode(Node<K, V> p, Node<K, V> next) {
		LinkedHashMap.Entry<K, V> q = (LinkedHashMap.Entry<K, V>) p;
		TreeNode<K, V> t = new TreeNode<>(q.hash, q.key, q.value, next);
		transferLinks(q, t);
		return t;
	}

	void afterNodeRemoval(Node<K, V> e) {
		LinkedHashMap.Entry<K, V> p = (LinkedHashMap.Entry<K, V>) e, b = p.before, a = p.after;
		p.before = p.after = null;
		if (b == null) {
			head = a;
		}
		else {
			b.after = a;
		}
		if (a == null) {
			tail = b;
		}
		else {
			a.before = b;
		}
	}

	void afterNodeInsertion(boolean evict) {
		LinkedHashMap.Entry<K, V> first;
		if (evict && (first = head) != null && removeEldestEntry(first)) {
			K key = first.key;
			removeNode(hash(key), key, null, false, true);
		}
	}

	void afterNodeAccess(Node<K, V> e) {
		LinkedHashMap.Entry<K, V> last;
		if (accessOrder && (last = tail) != e) {
			LinkedHashMap.Entry<K, V> p = (LinkedHashMap.Entry<K, V>) e, b = p.before, a = p.after;
			p.after = null;
			if (b == null) {
				head = a;
			}
			else {
				b.after = a;
			}
			if (a != null) {
				a.before = b;
			}
			else {
				last = b;
			}
			if (last == null) {
				head = p;
			}
			else {
				p.before = last;
				last.after = p;
			}
			tail = p;
			++modCount;
		}
	}

	void internalWriteEntries(ObjectOutputStream s) throws IOException {
		LinkedHashMap.Entry<K, V> e = head;
		do {
			s.writeObject(e.key);
			s.writeObject(e.value);
		}
		while ((e = e.after) != null);
	}

	public LinkedHashMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
		accessOrder = false;
	}

	public LinkedHashMap(int initialCapacity) {
		super(initialCapacity);
		accessOrder = false;
	}

	public LinkedHashMap() {
		super();
		accessOrder = false;
	}

	public LinkedHashMap(Map<? extends K, ? extends V> m) {
		super();
		accessOrder = false;
		putMapEntries(m, false);
	}

	public LinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder) {
		super(initialCapacity, loadFactor);
		this.accessOrder = accessOrder;
	}

	public boolean containsValue(Object value) {
		LinkedHashMap.Entry<K, V> e = head;
		do {
			V v = e.value;
			if (v == value || (value != null && value.equals(v))) {
				return true;
			}
		}
		while ((e = e.after) != null);
		return false;
	}

	public V get(Object key) {
		Node<K, V> e;
		if ((e = getNode(hash(key), key)) == null) {
			return null;
		}
		if (accessOrder) {
			afterNodeAccess(e);
		}
		return e.value;
	}

	public V getOrDefault(Object key, V defaultValue) {
		Node<K, V> e;
		if ((e = getNode(hash(key), key)) == null) {
			return defaultValue;
		}
		if (accessOrder) {
			afterNodeAccess(e);
		}
		return e.value;
	}

	public void clear() {
		super.clear();
		head = tail = null;
	}

	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		return false;
	}

	public Set<K> keySet() {
		Set<K> ks = keySet;
		if (ks == null) {
			ks = new LinkedKeySet();
			keySet = ks;
		}
		return ks;
	}

	final class LinkedKeySet extends AbstractSet<K> {

		public final int size() {
			return size;
		}

		public final void clear() {
			LinkedHashMap.this.clear();
		}

		public final Iterator<K> iterator() {
			return new LinkedKeyIterator();
		}

		public final boolean contains(Object o) {
			return containsKey(o);
		}

		public final boolean remove(Object key) {
			return removeNode(hash(key), key, null, false, true) != null;
		}

		public final void forEach(Consumer<? super K> action) {
			if (action == null) {
				throw new NullPointerException();
			}
			int mc = modCount;
			LinkedHashMap.Entry<K, V> e = head;
			do {
				action.accept(e.key);
			}
			while ((e = e.after) != null);

			if (modCount != mc) {
				throw new ConcurrentModificationException();
			}
		}
	}

	public Collection<V> values() {
		Collection<V> vs = values;
		if (vs == null) {
			vs = new LinkedValues();
			values = vs;
		}
		return vs;
	}

	final class LinkedValues extends AbstractCollection<V> {
		public final int size() {
			return size;
		}

		public final void clear() {
			LinkedHashMap.this.clear();
		}

		public final Iterator<V> iterator() {
			return new LinkedValueIterator();
		}

		public final boolean contains(Object o) {
			return containsValue(o);
		}

		public final void forEach(Consumer<? super V> action) {
			if (action == null) {
				throw new NullPointerException();
			}

			int mc = modCount;
			LinkedHashMap.Entry<K, V> e = head;
			do {
				action.accept(e.value);
			}
			while ((e = e.after) != null);

			if (modCount != mc) {
				throw new ConcurrentModificationException();
			}
		}
	}

	public Set<Map.Entry<K, V>> entrySet() {
		Set<Map.Entry<K, V>> es;
		return (es = entrySet) == null ? (entrySet = new LinkedEntrySet()) : es;
	}

	final class LinkedEntrySet extends AbstractSet<Map.Entry<K, V>> {
		public final int size() {
			return size;
		}

		public final void clear() {
			LinkedHashMap.this.clear();
		}

		public final Iterator<Map.Entry<K, V>> iterator() {
			return new LinkedEntryIterator();
		}

		public final boolean contains(Object o) {
			if (!(o instanceof Map.Entry)) {
				return false;
			}
			Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
			Object key = e.getKey();
			Node<K, V> candidate = getNode(hash(key), key);
			return candidate != null && candidate.equals(e);
		}

		public final boolean remove(Object o) {
			if (o instanceof Map.Entry) {
				Map.Entry<? , ?> e = (Map.Entry<?, ?>) o;
				Object key = e.getKey();
				Object value = e.getValue();
				return removeNode(hash(key), key, value, true, true) != null;
			}
			return false;
		}

		public final void forEach(Consumer<? super Map.Entry<K, V>> action) {
			if (action == null) {
				throw new NullPointerException();
			}
			int mc = modCount;
			LinkedHashMap.Entry<K, V> e = head;
			do {
				action.accept(e);
			}
			while ((e = e.after) != null);
		}
	}

	public void forEach(BiConsumer<? super K, ? super V> action) {
		if (action == null) {
			throw new NullPointerException();
		}
		int mc = modCount;
		LinkedHashMap.Entry<K, V> e = head;
		do {
			action.accept(e.key, e.value);
		}
		while ((e = e.after) != null);
		if (modCount != mc) {
			throw new ConcurrentModificationException();
		}
	}

	public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
		if (function == null) {
			throw new NullPointerException();
		}
		int mc = modCount;
		LinkedHashMap.Entry<K, V> e = head;
		do {
			e.value = function.apply(e.key, e.value);
		}
		while ((e = e.after) != null);
	}

	abstract class LinkedHashIterator {
		LinkedHashMap.Entry<K, V> next;
		LinkedHashMap.Entry<K, V> current;
		int expectedModCount;

		LinkedHashIterator() {
			next = head;
			expectedModCount = modCount;
			current = null;
		}

		public final boolean hasNext() {
			return next != null;
		}

		final LinkedHashMap.Entry<K, V> nextNode() {
			LinkedHashMap.Entry<K, V> e = next;
			if (modCount != expectedModCount) {
				throw new ConcurrentModificationException();
			}
			if (e == null) {
				throw new NoSuchElementException();
			}
			current = e;
			next = e.after;
			return e;
		}

		public final void remove() {
			Node<K, V> p = current;
			if (p == null) {
				throw new IllegalStateException();
			}
			if (modCount != expectedModCount) {
				throw new ConcurrentModificationException();
			}
			current = null;
			K key = p.key;
			removeNode(hash(key), key, null, false, false);
			expectedModCount = modCount;
		}
	}

	final class LinkedKeyIterator extends LinkedHashIterator implements Iterator<K> {
		@Override
		public K next() {
			return nextNode().getKey();
		}
	}

	final class LinkedValueIterator extends LinkedHashIterator implements Iterator<V> {
		@Override
		public V next() {
			return nextNode().value;
		}
	}

	final class LinkedEntryIterator extends LinkedHashIterator implements Iterator<Map.Entry<K, V>> {
		@Override
		public Map.Entry<K, V> next() {
			return nextNode();
		}
	}
}