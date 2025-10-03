package by.innowise.userservice.util;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ParserUtil {

  public static List<UUID> parseUUIDs(String ids) {
    try {
      return Arrays.stream(ids.split(","))
          .map(String::trim)
          .map(UUID::fromString)
          .collect(Collectors.toList());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid UUID format");
    }
  }
}
