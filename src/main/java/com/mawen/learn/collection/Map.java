package com.mawen.learn.collection;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/11/17
 */
public interface Map<K, V> {

	int size();

	boolean isEmpty();

	boolean containsKey(Object key);

	boolean containsValue(Object value);

	V get(Object key);

	V put(K key, V value);

	V remove(Object key);

	void putAll(Map<? extends K, ? extends V> m);

	void clear();

	Set<K> keySet();

	Collection<V> values();

	Set<Entry<K, V>> entrySet();

	interface Entry<K, V> {
		K getKey();

		V getValue();

		V setValue(V value);

		boolean equals(Object o);

		static <K extends Comparable<? super K>, V>Comparator<Entry<K, V>> comparingByKey() {
			return (c1, c2) -> c1.getKey().compareTo(c2.getKey());
		}

		static <K, V extends Comparable<? super V>> Comparator<Entry<K, V>> comparingByValue() {
			return (c1, c2) -> c1.getValue().compareTo(c2.getValue());
		}

		static <K, V> Comparator<Entry<K, V>> comparingByKey(Comparator<? super K> cmp) {
			Objects.requireNonNull(cmp);
			return (c1, c2) -> cmp.compare(c1.getKey(), c2.getKey());
		}

		static <K, V> Comparator<Entry<K, V>> comparingByValue(Comparator<? super V> cmp) {
			Objects.requireNonNull(cmp);
			return (c1, c2) -> cmp.compare(c1.getValue(), c2.getValue());
		}
	}

	boolean equals(Object o);

	int hashCode();

	default V getOrDefault(Object key, V defaultValue) {
		V v;
		// Because value may be null
		return (((v = get(key)) != null) || containsKey(key)) ? v : defaultValue;
	}

	default V putIfAbsent(K key, V value) {
		V v = get(key);
		if (v == null) {
			v = put(key, value);
		}
		return v;
	}

	default boolean remove(Object key, Object value) {
		Object curValue = get(key);
		if (!Objects.equals(curValue, value) || (curValue == null && !containsKey(key))) {
			return false;
		}
		remove(key);
		return true;
	}

	default boolean replace(K key, V oldValue, V newValue) {
		Object curValue = get(key);
		if (!Objects.equals(curValue, oldValue) || (curValue == null && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	default V replace(K key, V value) {
		V curValue;
		if (((curValue = get(key)) != null) || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}

	default V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		V v;
		if ((v = get(key)) == null) {
			V newValue;
			if ((newValue = mappingFunction.apply(key)) != null) {
				put(key, newValue);
				return newValue;
			}
		}
		return v;
	}

	default V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		Objects.requireNonNull(remappingFunction);
		V oldValue;
		if ((oldValue = get(key)) != null) {
			V newValue = remappingFunction.apply(key, oldValue);
			if (newValue != null) {
				put(key, newValue);
				return newValue;
			}
			else {
				remove(key);
				return null;
			}
		}
		else {
			return null;
		}
	}

	default V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		Objects.requireNonNull(remappingFunction);
		V oldValue = get(key);

		V newValue = remappingFunction.apply(key, oldValue);
		if (newValue == null) {
			if (oldValue != null || containsKey(key)) {
				remove(key);
				return null;
			}
			else {
				return null;
			}
		}
		else {
			put(key, newValue);
			return newValue;
		}
	}

	default V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
		Objects.requireNonNull(remappingFunction);
		Objects.requireNonNull(value);

		V oldValue = get(key);
		V newValue = oldValue == null ? value : remappingFunction.apply(oldValue, value);
		if (newValue == null) {
			remove(key);
		}
		else {
			put(key, newValue);
		}
		return newValue;
	}
}
