package skynet.entity;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import skynet.entity.Factory;
import skynet.enums.PartType;

public class Faction implements Runnable {
  private final String name;
  private final AtomicInteger heads = new AtomicInteger(0);
  private final AtomicInteger torsos = new AtomicInteger(0);
  private final AtomicInteger hands = new AtomicInteger(0);
  private final AtomicInteger feet = new AtomicInteger(0);
  private final AtomicInteger robotsBuilt = new AtomicInteger(0);
  private final Factory factory;
  private final int maxCarryCapacity;
  private volatile boolean running = true;

  public Faction(String name, Factory factory, int maxCarryCapacity) {
    this.name = name;
    this.factory = factory;
    this.maxCarryCapacity = maxCarryCapacity;
  }

  @Override
  public void run() {
    while (running) {
      try {
        // Sleep to simulate night time (waiting for factory to produce)
        Thread.sleep(100);

        // Collect parts from factory
        collectParts();

        // Build robots from available parts
        buildRobots();

      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        running = false;
      }
    }
  }

  public void collectParts() {
    List<PartType> collectedParts = factory.takeParts(maxCarryCapacity, name);

    for (PartType part : collectedParts) {
      switch (part) {
        case HEAD -> heads.incrementAndGet();
        case TORSO -> torsos.incrementAndGet();
        case HAND -> hands.incrementAndGet();
        case FEET -> feet.incrementAndGet();
      }
    }

    if (!collectedParts.isEmpty()) {
      System.out.println(name + " collected " + collectedParts.size() + " parts: " + collectedParts);
    }
  }

  public void buildRobots() {
    int built = 0;
    while (canBuildRobot()) {
      heads.decrementAndGet();
      torsos.decrementAndGet();
      hands.decrementAndGet();
      hands.decrementAndGet(); // Need 2 hands
      feet.decrementAndGet();
      feet.decrementAndGet(); // Need 2 feet

      robotsBuilt.incrementAndGet();
      built++;
    }

    if (built > 0) {
      System.out.println(name + " built " + built + " robot(s)! Total: " + robotsBuilt.get());
    }
  }

  private boolean canBuildRobot() {
    return heads.get() >= 1 &&
        torsos.get() >= 1 &&
        hands.get() >= 2 &&
        feet.get() >= 2;
  }

  public int getRobotCount() {
    return robotsBuilt.get();
  }

  public void stop() {
    running = false;
  }

  public String getName() {
    return name;
  }

  public String getInventory() {
    return String.format("Heads: %d, Torsos: %d, Hands: %d, Feet: %d",
        heads.get(), torsos.get(), hands.get(), feet.get());
  }
}