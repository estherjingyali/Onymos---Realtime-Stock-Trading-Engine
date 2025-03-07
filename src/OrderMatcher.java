import model.Order;

class OrderMatcher {
    public synchronized void matchOrder(OrderBook orderBook, int tickerId) {
        LockFreeLinkedList buys = orderBook.getBuys(tickerId);
        LockFreeLinkedList sells = orderBook.getSells(tickerId);

        while (!buys.isEmpty() && !sells.isEmpty()) {
            Order buy = buys.getHead();
            Order sell = sells.getHead();

            if (buy == null || sell == null) {
                System.out.println("One of the orders is null, stopping matchOrder");
                break;
            }

            if (buy.price < sell.price) {
                System.out.println("No match found for TickerId: " + tickerId);
                sells.setHead(sell.next);  // checking next buy order
                continue;
            }

            int matchedQuantity = Math.min(buy.quantity, sell.quantity);
            buy.quantity -= matchedQuantity;
            sell.quantity -= matchedQuantity;

            System.out.println("Order Matched. TickerId: " + tickerId +
                    ", Matched Quantity: " + matchedQuantity +
                    ", Buy Price: " + buy.price +
                    ", Sell Price: " + sell.price);

            if (buy.quantity == 0) buys.setHead(buy.next);
            if (sell.quantity == 0) sells.setHead(sell.next);
        }
    }
}