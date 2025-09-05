package skynet.entity;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import skynet.enums.PartType;

public class Factory implements Runnable {
    private final BlockingQueue<PartType> partsStorage = new LinkedBlockingQueue<>();
    private final Random random = new Random();
    private final int maxDailyProduction;
    private final AtomicInteger dayCount = new AtomicInteger(0);
    private volatile boolean running = true;
    private final Object lock = new Object();

    public Factory(int maxDailyProduction) {
        this.maxDailyProduction = maxDailyProduction;
    }

    @Override
    public void run() {
        while (running && dayCount.get() < 100) {
            try {
                // Sleep to simulate a day passing
                Thread.sleep(200);

                // Produce parts for the day
                produceParts();

                // Increment day counter
                dayCount.incrementAndGet();
                System.out.println("--- Day " + dayCount.get() + " completed ---");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                running = false;
            }
        }

        System.out.println("Factory stopped after " + dayCount.get() + " days");
        running = false;
    }

    public void produceParts() {
        synchronized (lock) {
            int partsToProduce = random.nextInt(maxDailyProduction) + 1;

            for (int i = 0; i < partsToProduce; i++) {
                PartType part = PartType.values()[random.nextInt(PartType.values().length)];
                partsStorage.offer(part);
            }

            System.out.println("Factory produced " + partsToProduce + " parts. Total in storage: " + partsStorage.size());
        }
    }

    public List<PartType> takeParts(int maxAmount, String factionName) {
        synchronized (lock) {
            List<PartType> takenParts = new ArrayList<>();
            int amountToTake = Math.min(maxAmount, partsStorage.size());

            for (int i = 0; i < amountToTake; i++) {
                PartType part = partsStorage.poll();
                if (part != null) {
                    takenParts.add(part);
                }
            }

            return takenParts;
        }
    }

    public void stop() {
        running = false;
    }

    public int getDayCount() {
        return dayCount.get();
    }

    public int getStorageSize() {
        synchronized (lock) {
            return partsStorage.size();
        }
    }
}