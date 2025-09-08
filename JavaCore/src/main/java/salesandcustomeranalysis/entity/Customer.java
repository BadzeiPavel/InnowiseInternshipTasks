package salesandcustomeranalysis.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Customer {

  private String customerId;
  private String name;
  private String email;
  private LocalDateTime registeredAt;
  private int age;
  private String city;
}