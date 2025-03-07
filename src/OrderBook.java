import model.Order;

class OrderBook {
    private static final int MAX_TICKERS = 1024;
    private final LockFreeLinkedList[] buys = new LockFreeLinkedList[MAX_TICKERS];
    private final LockFreeLinkedList[] sells = new LockFreeLinkedList[MAX_TICKERS];
    private final OrderMatcher orderMatcher = new OrderMatcher();

    public OrderBook() {
        for (int i = 0; i < MAX_TICKERS; i++) {
            buys[i] = new LockFreeLinkedList();
            sells[i] = new LockFreeLinkedList();
        }
    }

    /* add order */
    public void addOrder(Order order) {
        int tickerId = order.tickerId;
        if (order.isBuy) {
            buys[tickerId].addOrder(order);
        } else {
            sells[tickerId].addOrder(order);
        }

        System.out.println("New Order Created: " + (order.isBuy ? "BUY" : "SELL") +
                " | TickerId: " + order.tickerId +
                " | Price: " + order.price +
                " | Quantity: " + order.quantity);
    }

    public LockFreeLinkedList getBuys(int tickerId) { return buys[tickerId]; }
    public LockFreeLinkedList getSells(int tickerId) { return sells[tickerId]; }

    public void printOrderBook(int tickerId) {
        orderMatcher.matchOrder(this, tickerId);
        System.out.println("---Buy Orders Book for TickerId: " + tickerId + "---");
        System.out.println("Buy Orders:");
        buys[tickerId].printOrders();
        System.out.println("Sell Orders:");
        sells[tickerId].printOrders();
    }
}