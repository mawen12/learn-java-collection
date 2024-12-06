package com.mawen.custom.collection;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/11/28
 */
public class SinglyLinkedList<T> {

	private Node<T> head;

	private int size;

	public static class Node<T> {
		T data;

		Node<T> next;

		public Node(T data) {
			this.data = data;
			this.next = null;
		}
	}

	public SinglyLinkedList() {
		this.head = null;
		this.size = 0;
	}

	public int size() {
		return size;
	}

//	public int size() {
//		int size = 0;
//		Node<T> current = head;
//		while (current != null) {
//			size++;
//			current = current.next;
//		}
//		return size;
//	}

	public void insertAtBeginning(T data) {
		// TODO
	}

	public void insertAtEnd(T data) {
		// TODO
	}

	public Node<T> deleteFromBeginning() {
		if (size == 0) {
			return null;
		}
		Node<T> deletedNode = head;
		head = head.next;
		deletedNode.next = null;
		size--;
		return deletedNode;
	}

	public Node<T> deleteLast() {
		if (size == 0) {
			return null;
		}

		Node<T> current = head;
		if (current.next == null) {
			head = null;
		}
		else {
			Node<T> previous = null;
			while (current.next != null) {
				previous = current;
				current = current.next;
			}
			previous.next = null;
		}
		size--;
		return current;
	}
}
