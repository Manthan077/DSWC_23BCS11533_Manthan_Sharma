interface PaymentStrategy {
    boolean processPayment(double amount);
}

class CreditCardStrategy implements PaymentStrategy {

    @Override
    public boolean processPayment(double amount) {

        System.out.println(
                "Credit Card payment processed: $" + amount);

        return true;
    }
}

class CryptoStrategy implements PaymentStrategy {

    @Override
    public boolean processPayment(double amount) {

        System.out.println(
                "Crypto payment processed: $" + amount);

        return true;
    }
}

class TransactionProcessor {

    private PaymentStrategy strategy;

    public TransactionProcessor(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void executeTransaction(double amount) {

        if (strategy.processPayment(amount)) {
            System.out.println("Transaction Successful");
        } else {
            System.out.println("Transaction Failed");
        }
    }
}

public class FinTechRoutingEngine {

    public static void main(String[] args) {

        TransactionProcessor processor =
                new TransactionProcessor(
                        new CreditCardStrategy());

        processor.executeTransaction(1000);

        processor.setPaymentStrategy(
                new CryptoStrategy());

        processor.executeTransaction(500);
    }
}