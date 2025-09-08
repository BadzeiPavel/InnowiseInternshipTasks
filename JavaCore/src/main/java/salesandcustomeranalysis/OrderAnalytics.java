package salesandcustomeranalysis;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import salesandcustomeranalysis.entity.Customer;
import salesandcustomeranalysis.entity.Order;
import salesandcustomeranalysis.entity.OrderItem;
import salesandcustomeranalysis.enums.OrderStatus;

public class OrderAnalytics {

  public static Set<String> getUniqueCities(List<Order> orders) {
    if (orders == null) {
      throw new IllegalArgumentException("orders must not be null");
    }

    return orders.stream()
        .map(order -> order.getCustomer().getCity())
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
  }

  public static double getTotalIncome(List<Order> orders) {
    return orders.stream()
        .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
        .flatMap(order -> order.getItems().stream())
        .mapToDouble(item -> item.getPrice() * item.getQuantity())
        .sum();
  }

  public static Optional<String> getMostPopularProduct(List<Order> orders) {
    return orders.stream()
        .flatMap(order -> order.getItems().stream())
        .collect(Collectors.groupingBy(
            OrderItem::getProductName,
            Collectors.summingInt(OrderItem::getQuantity)
        ))
        .entrySet()
        .stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey);
  }

  public static double getAverageCheckForDelivered(List<Order> orders) {
    return orders.stream()
        .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
        .mapToDouble(order -> order.getItems().stream()
            .mapToDouble(item -> item.getPrice() * item.getQuantity())
            .sum()
        )
        .average()
        .orElse(0.0);
  }

  public static Set<Customer> getLoyalCustomers(List<Order> orders) {
    return orders.stream()
        .collect(Collectors.groupingBy(Order::getCustomer, Collectors.counting()))
        .entrySet()
        .stream()
        .filter(entry -> entry.getValue() > 5)
        .map(Map.Entry::getKey)
        .collect(Collectors.toSet());
  }
}
