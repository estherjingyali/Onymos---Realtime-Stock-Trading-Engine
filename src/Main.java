public class Main {
    public static void main(String[] args) {
        System.out.println("----------Start Engine----------");
        TransactionSimulator simulator = new TransactionSimulator();
        simulator.executeSimulator();

        try {
            simulator.awaitCompletion();
        } catch (InterruptedException e) {
            System.out.println("Simulator was interrupted.");
        }

        for (int tickerId = 0; tickerId < 100; tickerId++) {
            simulator.getOrderBook().printOrderBook(tickerId);
        }

        System.out.println("----------Close Engine----------");
    }
}