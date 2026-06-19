import java.sql.*;

interface ReportGenerator {
    void printDelayedReport();
}

abstract class DatabaseRepository {

    private static final String URL =
            "jdbc:postgresql://localhost:5432/cargologix";

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

class LogisticsRepository
        extends DatabaseRepository
        implements ReportGenerator {

    private static final String QUERY =

            "SELECT s.shipment_id, " +
            "c.company_name, " +
            "s.dispatch_date " +
            "FROM shipments s " +
            "INNER JOIN couriers c " +
            "ON s.courier_id = c.courier_id " +
            "WHERE s.status = ? " +
            "ORDER BY s.dispatch_date DESC";

    @Override
    public void printDelayedReport() {

        try (
                Connection conn = getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(QUERY)
        ) {

            ps.setString(1, "DELAYED");

            try (ResultSet rs = ps.executeQuery()) {

                System.out.println(
                        "Delayed Shipment Report");

                while (rs.next()) {

                    long shipmentId =
                            rs.getLong("shipment_id");

                    String companyName =
                            rs.getString("company_name");

                    Timestamp dispatchDate =
                            rs.getTimestamp(
                                    "dispatch_date");

                    System.out.println(
                            shipmentId
                                    + " | "
                                    + companyName
                                    + " | "
                                    + dispatchDate);
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Database Error: "
                            + e.getMessage());
        }
    }
}

public class CargoLogixSystem {

    public static void main(String[] args) {

        LogisticsRepository repository =
                new LogisticsRepository();

        repository.printDelayedReport();
    }
}