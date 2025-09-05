import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;
import salesandcustomeranalysis.OrderAnalytics;
import salesandcustomeranalysis.entity.Customer;
import salesandcustomeranalysis.entity.Order;
import salesandcustomeranalysis.entity.OrderItem;
import salesandcustomeranalysis.enums.Category;
import salesandcustomeranalysis.enums.OrderStatus;

import static org.junit.jupiter.api.Assertions.*;

class OrderAnalyticsTest {

  private List<Order> orders;
  private Customer c1, c2, c3;

  @BeforeEach
  void setUp() {
    c1 = new Customer("c1", "Alice", "a@email.com", LocalDateTime.now(), 30, "New York");
    c2 = new Customer("c2", "Bob", "b@email.com", LocalDateTime.now(), 40, "London");
    c3 = new Customer("c3", "Charlie", "c@email.com", LocalDateTime.now(), 25, "Paris");

    OrderItem phone = new OrderItem("iPhone", 1, 1000, Category.ELECTRONICS);   // total 1000
    OrderItem shirts = new OrderItem("Shirt", 2, 50, Category.CLOTHING);        // total 100
    OrderItem books = new OrderItem("Book", 3, 20, Category.BOOKS);             // total 60

    orders = new ArrayList<>();
    orders.add(new Order("o1", LocalDateTime.now(), c1, List.of(phone), OrderStatus.DELIVERED));
    orders.add(new Order("o2", LocalDateTime.now(), c2, List.of(shirts, books), OrderStatus.DELIVERED));
    orders.add(new Order("o3", LocalDateTime.now(), c2, List.of(books), OrderStatus.NEW));
    orders.add(new Order("o4", LocalDateTime.now(), c3, List.of(new OrderItem("Shirt", 1, 50, Category.CLOTHING)), OrderStatus.DELIVERED));

    // add multiple small book orders for c1
    for (int i = 5; i <= 9; i++) {
      orders.add(new Order("o" + i, LocalDateTime.now(), c1,
          List.of(new OrderItem("Book", 3, 20, Category.BOOKS)), // total 60
          OrderStatus.DELIVERED));
    }
  }

  @Test
  void testUniqueCities() {
    Set<String> cities = OrderAnalytics.getUniqueCities(orders);
    assertEquals(Set.of("New York", "London", "Paris"), cities);
  }

  @Test
  void testTotalIncome() {
    double income = OrderAnalytics.getTotalIncome(orders);
    // Delivered orders:
    // o1: 1000
    // o2: 100 + 60 = 160
    // o4: 50
    // o5..o9: 5 × 60 = 300
    // total = 1000 + 160 + 50 + 300 = 1510
    assertEquals(1510, income, 0.001);
  }

  @Test
  void testMostPopularProduct() {
    Optional<String> product = OrderAnalytics.getMostPopularProduct(orders);
    assertTrue(product.isPresent());
    assertEquals("Book", product.get());
  }

  @Test
  void testAverageCheckForDelivered() {
    double avg = OrderAnalytics.getAverageCheckForDelivered(orders);
    // Delivered orders:
    // o1 = 1000
    // o2 = 160
    // o4 = 50
    // o5..o9 = 5 × 60 = 300
    // total = 1510, count = 8 orders
    // avg = 1510 / 8 = 188.75
    assertEquals(188.75, avg, 0.001);
  }

  @Test
  void testLoyalCustomers() {
    Set<Customer> loyal = OrderAnalytics.getLoyalCustomers(orders);
    assertEquals(1, loyal.size());
    assertTrue(loyal.contains(c1));
  }
}
