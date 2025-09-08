import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import customlinkedlist.LinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

@DisplayName("LinkedList Tests")
class LinkedListTest {

  private LinkedList<Integer> list;

  @BeforeEach
  void setUp() {
    list = new LinkedList<>();
  }

  @Test
  @DisplayName("Should add elements to the front and retrieve them correctly")
  void testAddFirstAndGetFirst() {
    list.addFirst(10);
    list.addFirst(20);
    assertEquals(20, list.getFirst());
    assertEquals(10, list.getLast());
    assertEquals(2, list.size());
  }

  @Test
  @DisplayName("Should add elements to the end and retrieve them correctly")
  void testAddLastAndGetLast() {
    list.addLast(5);
    list.addLast(15);
    assertEquals(5, list.getFirst());
    assertEquals(15, list.getLast());
    assertEquals(2, list.size());
  }

  @Test
  @DisplayName("Should insert element at a specific index")
  void testAddByIndex() {
    list.addFirst(1);
    list.addLast(3);
    list.add(1, 2);
    assertEquals(3, list.size());
    assertEquals(2, list.get(1));
  }

  @Test
  @DisplayName("Should retrieve elements by index")
  void testGetByIndex() {
    list.addLast(100);
    list.addLast(200);
    list.addLast(300);
    assertEquals(200, list.get(1));
    assertEquals(300, list.get(2));
  }

  @Test
  @DisplayName("Should remove the first element correctly")
  void testRemoveFirst() {
    list.addLast(10);
    list.addLast(20);
    assertEquals(10, list.removeFirst());
    assertEquals(20, list.getFirst());
    assertEquals(1, list.size());
  }

  @Test
  @DisplayName("Should remove the last element correctly")
  void testRemoveLast() {
    list.addLast(1);
    list.addLast(2);
    list.addLast(3);
    assertEquals(3, list.removeLast());
    assertEquals(2, list.getLast());
    assertEquals(2, list.size());
  }

  @Test
  @DisplayName("Should remove element by index correctly")
  void testRemoveByIndex() {
    list.addLast(11);
    list.addLast(22);
    list.addLast(33);
    assertEquals(22, list.remove(1));
    assertEquals(2, list.size());
    assertEquals(33, list.get(1));
  }

  @Test
  @DisplayName("Should throw exceptions when operating on an empty list")
  void testExceptionsOnEmptyList() {
    assertThrows(NoSuchElementException.class, () -> list.getFirst());
    assertThrows(NoSuchElementException.class, () -> list.getLast());
    assertThrows(NoSuchElementException.class, () -> list.removeFirst());
    assertThrows(NoSuchElementException.class, () -> list.removeLast());
  }

  @Test
  @DisplayName("Should throw IndexOutOfBoundsException for invalid indices")
  void testIndexOutOfBounds() {
    list.addLast(42);
    assertThrows(IndexOutOfBoundsException.class, () -> list.get(5));
    assertThrows(IndexOutOfBoundsException.class, () -> list.add(2, 99));
    assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
  }
}
