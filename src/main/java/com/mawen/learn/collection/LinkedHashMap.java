package com.mawen.learn.collection;

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
		new TreeNode<>(hash, key, value, next);

	}

}
