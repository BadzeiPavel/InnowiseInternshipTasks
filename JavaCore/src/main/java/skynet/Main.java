package skynet;

import java.util.concurrent.*;
import skynet.entity.Faction;
import skynet.entity.Factory;

public class Main {
  public static void main(String[] args) {
    System.out.println("Starting Robot Army Simulation...");

    Factory factory = new Factory(10);

    Faction worldFaction = new Faction("World", factory, 5);
    Faction wednesdayFaction = new Faction("Wednesday", factory, 5);

    ExecutorService executor = Executors.newFixedThreadPool(3);

    executor.execute(factory);
    executor.execute(worldFaction);
    executor.execute(wednesdayFaction);

    try {
      while (factory.getDayCount() < 100) {
        Thread.sleep(100);
      }

      factory.stop();
      worldFaction.stop();
      wednesdayFaction.stop();

      executor.shutdown();
      executor.awaitTermination(2, TimeUnit.SECONDS);

    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    System.out.println("\n=== FINAL RESULTS AFTER 100 DAYS ===");
    System.out.println(worldFaction.getName() + ": " + worldFaction.getRobotCount() + " robots - " + worldFaction.getInventory());
    System.out.println(wednesdayFaction.getName() + ": " + wednesdayFaction.getRobotCount() + " robots - " + wednesdayFaction.getInventory());

    if (worldFaction.getRobotCount() > wednesdayFaction.getRobotCount()) {
      System.out.println("~~~ WORLD faction has the strongest army! ~~~");
    } else if (wednesdayFaction.getRobotCount() > worldFaction.getRobotCount()) {
      System.out.println("~~~ WEDNESDAY faction has the strongest army! ~~~");
    } else {
      System.out.println("~~~ It's a tie! Both factions have equal armies. ~~~");
    }

    System.out.println("Factory storage remaining: " + factory.getStorageSize() + " parts");
  }
}