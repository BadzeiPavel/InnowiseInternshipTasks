package salesandcustomeranalysis.entity;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import salesandcustomeranalysis.enums.OrderStatus;

@Data
@AllArgsConstructor
public class Order {

  private String orderId;
  private LocalDateTime orderDate;
  private Customer customer;
  private List<OrderItem> items;
  private OrderStatus status;
}