import java.sql.*;

interface PortfolioManager {
    void restructurePortfolio(long investorId);
}

abstract class FinancialDatabaseConfig {

    private static final String URL =
            "jdbc:postgresql://localhost:5432/firedb";

    private static final String USER =
            "postgres";

    private static final String PASSWORD =
            "password";

    protected Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD);
    }
}

class PortfolioRepository
        extends FinancialDatabaseConfig
        implements PortfolioManager {

    private static final String AGGREGATE_QUERY =

            "SELECT h.asset_class, " +
            "SUM(h.current_value) AS total_value " +
            "FROM investors i " +
            "INNER JOIN holdings h " +
            "ON i.investor_id = h.investor_id " +
            "WHERE i.investor_id = ? " +
            "GROUP BY h.asset_class";

    private static final String DEBT_UPDATE =

            "UPDATE holdings " +
            "SET current_value = current_value - ? " +
            "WHERE investor_id = ? " +
            "AND asset_class = 'Debt'";

    private static final String EQUITY_UPDATE =

            "UPDATE holdings " +
            "SET current_value = current_value + ? " +
            "WHERE investor_id = ? " +
            "AND asset_class = 'Equity'";

    @Override
    public void restructurePortfolio(long investorId) {

        double shiftAmount = 5000.0;

        Connection conn = null;

        try {

            conn = getConnection();

            conn.setAutoCommit(false);

            try (
                    PreparedStatement selectStmt =
                            conn.prepareStatement(
                                    AGGREGATE_QUERY);

                    PreparedStatement debtStmt =
                            conn.prepareStatement(
                                    DEBT_UPDATE);

                    PreparedStatement equityStmt =
                            conn.prepareStatement(
                                    EQUITY_UPDATE)
            ) {

                selectStmt.setLong(
                        1,
                        investorId);

                ResultSet rs =
                        selectStmt.executeQuery();

                System.out.println(
                        "Current Portfolio:");

                while (rs.next()) {

                    System.out.println(
                            rs.getString("asset_class")
                                    + " : "
                                    + rs.getDouble(
                                    "total_value"));
                }

                debtStmt.setDouble(
                        1,
                        shiftAmount);

                debtStmt.setLong(
                        2,
                        investorId);

                debtStmt.executeUpdate();

                equityStmt.setDouble(
                        1,
                        shiftAmount);

                equityStmt.setLong(
                        2,
                        investorId);

                equityStmt.executeUpdate();

                conn.commit();

                System.out.println(
                        "Portfolio restructuring successful.");

            }

        } catch (Exception e) {

            try {

                if (conn != null) {
                    conn.rollback();
                }

            } catch (SQLException ex) {

                System.out.println(
                        ex.getMessage());
            }

            System.out.println(
                    "Transaction Failed: "
                            + e.getMessage());

        } finally {

            try {

                if (conn != null) {
                    conn.close();
                }

            } catch (SQLException e) {

                System.out.println(
                        e.getMessage());
            }
        }
    }
}

public class FIREPortfolioAggregator {

    public static void main(String[] args) {

        PortfolioManager manager =
                new PortfolioRepository();

        manager.restructurePortfolio(101);
    }
}