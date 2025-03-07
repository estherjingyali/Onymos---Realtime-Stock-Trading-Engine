import model.Order;
import utils.CAS;
import java.util.concurrent.atomic.AtomicReference;

class LockFreeLinkedList {
    private final AtomicReference<Order> head = new AtomicReference<>(null);

    public boolean isEmpty() {
        return head.get() == null;
    }

    public Order getHead() {
        return head.get();
    }

    public void setHead(Order order) {
        head.set(order);
    }

    public void addOrder(Order order) {
        while (true) {
            Order current = head.get();

            if (current == null || order.price >= current.price) {
                order.next = current;
                if (head.compareAndSet(current, order)) {
                    return;
                }
            } else {
                while (current.next != null && current.next.price > order.price) {
                    current = current.next;
                }

                order.next = current.next;

                if (current.next == null || current.next.price <= order.price) {
                    if (current.next == order) return;
                    if (current.next == null || current.next.price > order.price) {
                        if (CAS.compareAndSwapNext(current, current.next, order)) {
                            return;
                        }
                    }
                }
            }
        }
    }
    public void printOrders() {
        Order current = head.get();
        if (current == null) {
            System.out.println("No Orders");
            return;
        }
        while (current != null){
            System.out.println("Order: ID=" + current.orderId +
                    ", Price=" + current.price +
                    ", Quantity=" + current.quantity);
            current = current.next;
        }
    }
}