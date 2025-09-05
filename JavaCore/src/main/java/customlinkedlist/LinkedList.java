package customlinkedlist;

import java.util.NoSuchElementException;

public class LinkedList<T> implements MyList<T> {

  private static class Node<T> {

    T value;
    Node<T> next;
    Node<T> prev;

    Node(T value) {
      this.value = value;
    }
  }

  private Node<T> head;
  private Node<T> tail;
  private int size;

  public int size() {
    return size;
  }

  public void addFirst(T element) {
    Node<T> newNode = new Node<>(element);
    if (head == null) {
      head = tail = newNode;
    } else {
      newNode.next = head;
      head.prev = newNode;
      head = newNode;
    }
    size++;
  }

  public void addLast(T element) {
    Node<T> newNode = new Node<>(element);
    if (tail == null) {
      head = tail = newNode;
    } else {
      tail.next = newNode;
      newNode.prev = tail;
      tail = newNode;
    }
    size++;
  }

  public void add(int index, T element) {
    checkPositionIndex(index);

    if (index == 0) {
      addFirst(element);
      return;
    }
    if (index == size) {
      addLast(element);
      return;
    }

    Node<T> current = getNode(index);
    Node<T> newNode = new Node<>(element);
    Node<T> prevNode = current.prev;

    newNode.next = current;
    newNode.prev = prevNode;
    prevNode.next = newNode;
    current.prev = newNode;

    size++;
  }

  public T getFirst() {
      if (head == null) {
          throw new NoSuchElementException("List is empty");
      }
    return head.value;
  }

  public T getLast() {
      if (tail == null) {
          throw new NoSuchElementException("List is empty");
      }
    return tail.value;
  }

  public T get(int index) {
    checkElementIndex(index);
    return getNode(index).value;
  }

  public T removeFirst() {
      if (head == null) {
          throw new NoSuchElementException("List is empty");
      }
    T value = head.value;
    head = head.next;
    if (head != null) {
      head.prev = null;
    } else {
      tail = null; // list became empty
    }
    size--;
    return value;
  }

  public T removeLast() {
      if (tail == null) {
          throw new NoSuchElementException("List is empty");
      }
    T value = tail.value;
    tail = tail.prev;
    if (tail != null) {
      tail.next = null;
    } else {
      head = null; // list became empty
    }
    size--;
    return value;
  }

  public T remove(int index) {
    checkElementIndex(index);

      if (index == 0) {
          return removeFirst();
      }
      if (index == size - 1) {
          return removeLast();
      }

    Node<T> current = getNode(index);
    Node<T> prevNode = current.prev;
    Node<T> nextNode = current.next;

    prevNode.next = nextNode;
    nextNode.prev = prevNode;

    size--;
    return current.value;
  }

  private Node<T> getNode(int index) {
    Node<T> current;
    if (index < size / 2) {
      current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
    } else {
      current = tail;
        for (int i = size - 1; i > index; i--) {
            current = current.prev;
        }
    }
    return current;
  }

  private void checkElementIndex(int index) {
      if (!(index >= 0 && index < size)) {
          throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
      }
  }

  private void checkPositionIndex(int index) {
    if (!(index >= 0 && index <= size))
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
  }
}
