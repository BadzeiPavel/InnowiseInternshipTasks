package salesandcustomeranalysis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import salesandcustomeranalysis.enums.Category;

@Data
@AllArgsConstructor
public class OrderItem {

  private String productName;
  private int quantity;
  private double price;
  private Category category;
}