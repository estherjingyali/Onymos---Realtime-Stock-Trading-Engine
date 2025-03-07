package utils;

import model.Order;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class CAS {
    private static final VarHandle NEXT_HANDLE;

    static {
        try {
            NEXT_HANDLE = MethodHandles.lookup().findVarHandle(Order.class, "next", Order.class);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean compareAndSwapNext(Order node, Order expected, Order newNode) {
        if (node == null) {
            return false;
        }
        return NEXT_HANDLE.compareAndSet(node, expected, newNode);
    }
}

