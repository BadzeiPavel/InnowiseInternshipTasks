import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import customlinkedlist.LinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;


class LinkedListTest {

  private LinkedList<Integer> list;

  @BeforeEach
  void setUp() {
    list = new LinkedList<>();
  }

  @Test
  void testAddFirstAndGetFirst() {
    list.addFirst(10);
    list.addFirst(20);
    assertEquals(20, list.getFirst());
    assertEquals(10, list.getLast());
    assertEquals(2, list.size());
  }

  @Test
  void testAddLastAndGetLast() {
    list.addLast(5);
    list.addLast(15);
    assertEquals(5, list.getFirst());
    assertEquals(15, list.getLast());
    assertEquals(2, list.size());
  }

  @Test
  void testAddByIndex() {
    list.addFirst(1);
    list.addLast(3);
    list.add(1, 2);
    assertEquals(3, list.size());
    assertEquals(2, list.get(1));
  }

  @Test
  void testGetByIndex() {
    list.addLast(100);
    list.addLast(200);
    list.addLast(300);
    assertEquals(200, list.get(1));
    assertEquals(300, list.get(2));
  }

  @Test
  void testRemoveFirst() {
    list.addLast(10);
    list.addLast(20);
    assertEquals(10, list.removeFirst());
    assertEquals(20, list.getFirst());
    assertEquals(1, list.size());
  }

  @Test
  void testRemoveLast() {
    list.addLast(1);
    list.addLast(2);
    list.addLast(3);
    assertEquals(3, list.removeLast());
    assertEquals(2, list.getLast());
    assertEquals(2, list.size());
  }

  @Test
  void testRemoveByIndex() {
    list.addLast(11);
    list.addLast(22);
    list.addLast(33);
    assertEquals(22, list.remove(1));
    assertEquals(2, list.size());
    assertEquals(33, list.get(1));
  }

  @Test
  void testExceptionsOnEmptyList() {
    assertThrows(NoSuchElementException.class, () -> list.getFirst());
    assertThrows(NoSuchElementException.class, () -> list.getLast());
    assertThrows(NoSuchElementException.class, () -> list.removeFirst());
    assertThrows(NoSuchElementException.class, () -> list.removeLast());
  }

  @Test
  void testIndexOutOfBounds() {
    list.addLast(42);
    assertThrows(IndexOutOfBoundsException.class, () -> list.get(5));
    assertThrows(IndexOutOfBoundsException.class, () -> list.add(2, 99));
    assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
  }
}
