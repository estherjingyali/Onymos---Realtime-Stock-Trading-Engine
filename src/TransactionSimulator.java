import model.Order;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TransactionSimulator {
    private static final int MAX_TICKERS = 1024;
    private static final int MAX_ORDERS = 1000;
    private static final int THREADS = 4;
    private final OrderBook orderBook = new OrderBook();
    private final OrderMatcher orderMatcher = new OrderMatcher();
    private final ExecutorService executor = Executors.newFixedThreadPool(THREADS);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Random random = new Random();

    private Order generateOrder(int orderId) {
        int tickerId = random.nextInt(MAX_TICKERS);
        boolean isBuy = random.nextBoolean();
        int price = 90 + random.nextInt(21);
        int quantity = 1 + random.nextInt(10);      //[1,10]
        return new Order(orderId, tickerId, isBuy, price, quantity);
    }

    public void executeSimulator() {
        for (int i = 0; i < MAX_ORDERS; i++) {
            int orderId = i + 1;

            executor.execute(() -> {
                Order order = generateOrder(orderId);
                orderBook.addOrder(order);
            });
        }

        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("Running periodic matchOrder...");
            for (int tickerId = 0; tickerId < 1024; tickerId++) {
                orderMatcher.matchOrder(orderBook, tickerId);
            }
        }, 0, 500, TimeUnit.MILLISECONDS);

        executor.shutdown();

        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                System.out.println("Forcing executor shutdown...");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        scheduler.shutdown();
    }

    public void awaitCompletion() throws InterruptedException {
        executor.shutdown();
        if (!executor.awaitTermination(20, TimeUnit.SECONDS)) {
            System.out.println("Forcing executor shutdown...");
            executor.shutdownNow();
        }

        scheduler.shutdown();
        if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
            System.out.println("Forcing scheduler shutdown...");
            scheduler.shutdownNow();
        }
    }

    public OrderBook getOrderBook() {
        return orderBook;
    }
}
