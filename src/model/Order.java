package model;

public class Order {
    public final int orderId;
    public final int tickerId;   // range from 0 to 1023
    public final boolean isBuy;  // ture = buy, false = sell
    public final int price;
    public int quantity;
    public volatile Order next;

    public Order(int orderId, int tickerId, boolean isBuy, int price, int quantity) {
        this.orderId = orderId;
        this.tickerId = tickerId;
        this.isBuy = isBuy;
        this.price = price;
        this.quantity = quantity;
        this.next = null;
    }
}