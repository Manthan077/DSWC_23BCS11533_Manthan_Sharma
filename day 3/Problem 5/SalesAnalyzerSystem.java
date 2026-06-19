import java.util.*;

class Transaction {

    private String status;
    private String category;
    private double amount;

    public Transaction(String status,
                       String category,
                       double amount) {

        this.status = status;
        this.category = category;
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }
}

class SalesAnalyzer {

    public double calculateElectronicsRevenue(
            List<Transaction> transactions) {

        return transactions.stream()
                .filter(t -> t.getStatus().equals("COMPLETED"))
                .filter(t -> t.getCategory().equals("ELECTRONICS"))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
}

public class SalesAnalyzerSystem {

    public static void main(String[] args) {

        List<Transaction> transactions = Arrays.asList(

                new Transaction(
                        "COMPLETED",
                        "ELECTRONICS",
                        5000),

                new Transaction(
                        "PENDING",
                        "ELECTRONICS",
                        3000),

                new Transaction(
                        "COMPLETED",
                        "CLOTHING",
                        2000),

                new Transaction(
                        "COMPLETED",
                        "ELECTRONICS",
                        7000),

                new Transaction(
                        "COMPLETED",
                        "ELECTRONICS",
                        2500)
        );

        SalesAnalyzer analyzer =
                new SalesAnalyzer();

        double revenue =
                analyzer.calculateElectronicsRevenue(
                        transactions);

        System.out.println(
                "Total Electronics Revenue: " + revenue);
    }
}