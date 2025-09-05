package salesandcustomeranalysis;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import salesandcustomeranalysis.entity.Customer;
import salesandcustomeranalysis.entity.Order;
import salesandcustomeranalysis.entity.OrderItem;
import salesandcustomeranalysis.enums.OrderStatus;

public class OrderAnalytics {

    // List of unique cities where orders came from
    public static Set<String> getUniqueCities(List<Order> orders) {
        return orders.stream()
                .map(order -> order.getCustomer().getCity())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    // Total income for all completed orders (DELIVERED only)
    public static double getTotalIncome(List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                .flatMap(order -> order.getItems().stream())
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    // The most popular product by sales (by quantity)
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

    // Average check (avg order value) for delivered orders
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

    // Customers who have more than 5 orders
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
