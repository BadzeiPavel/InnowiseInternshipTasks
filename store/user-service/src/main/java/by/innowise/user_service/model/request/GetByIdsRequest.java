package by.innowise.user_service.model.request;

import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetByIdsRequest {

  private List<UUID> ids;
}
