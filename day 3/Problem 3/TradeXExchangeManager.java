import java.util.*;
import java.util.concurrent.*;

class Order {

    private int orderId;
    private double amount;

    public Order(int orderId, double amount) {
        this.orderId = orderId;
        this.amount = amount;
    }

    public int getOrderId() {
        return orderId;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Order ID: " + orderId +
                ", Amount: " + amount;
    }
}

class ExchangeManager {

    private ConcurrentHashMap<String,
            CopyOnWriteArrayList<Order>> orderBook;

    public ExchangeManager() {
        orderBook = new ConcurrentHashMap<>();
    }

    public void placeOrder(String ticker, Order order) {

        orderBook
                .computeIfAbsent(
                        ticker,
                        k -> new CopyOnWriteArrayList<>())
                .add(order);
    }

    public List<Order> getOrders(String ticker) {

        return orderBook.getOrDefault(
                ticker,
                new CopyOnWriteArrayList<>());
    }

    public void displayOrders(String ticker) {

        List<Order> orders = getOrders(ticker);

        System.out.println("Orders for " + ticker + ":");

        for (Order order : orders) {
            System.out.println(order);
        }
    }
}

public class TradeXExchangeManager {

    public static void main(String[] args) {

        ExchangeManager manager = new ExchangeManager();

        manager.placeOrder(
                "BTC",
                new Order(101, 50000));

        manager.placeOrder(
                "BTC",
                new Order(102, 51000));

        manager.placeOrder(
                "ETH",
                new Order(201, 3000));

        manager.placeOrder(
                "ETH",
                new Order(202, 3200));

        manager.displayOrders("BTC");

        System.out.println();

        manager.displayOrders("ETH");
    }
}