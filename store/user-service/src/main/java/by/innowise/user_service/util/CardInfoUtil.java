package by.innowise.user_service.util;

import java.util.Random;

public class CardInfoUtil {

  public static String generateCardNumber() {
    Random random = new Random();
    StringBuilder sb = new StringBuilder(16);
    for (int i = 0; i < 16; i++) {
      sb.append(random.nextInt(10));
    }
    return sb.toString();
  }
}
