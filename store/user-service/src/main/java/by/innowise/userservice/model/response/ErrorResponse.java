package by.innowise.userservice.model.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

  private String title;
  private String name;
  private int status;
  private String message;
  private String path;
  private LocalDateTime timestamp;
}
