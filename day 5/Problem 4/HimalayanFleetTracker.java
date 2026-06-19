import java.sql.*;
import java.time.LocalDateTime;

interface TelemetryService {
    void printLatestLocations();
}

abstract class FleetDatabaseConnection {

    private static final String URL =
            "jdbc:postgresql://localhost:5432/fleetdb";

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

class FleetRepository
        extends FleetDatabaseConnection
        implements TelemetryService {

    private static final String QUERY =

            "SELECT rider_name, " +
            "bike_model, " +
            "latitude, " +
            "longitude, " +
            "recorded_at " +
            "FROM (" +
            "SELECT r.rider_name, " +
            "r.bike_model, " +
            "g.latitude, " +
            "g.longitude, " +
            "g.recorded_at, " +
            "ROW_NUMBER() OVER (" +
            "PARTITION BY g.rider_id " +
            "ORDER BY g.recorded_at DESC" +
            ") rn " +
            "FROM riders r " +
            "INNER JOIN gps_pings g " +
            "ON r.rider_id = g.rider_id" +
            ") t " +
            "WHERE rn = 1";

    @Override
    public void printLatestLocations() {

        try (
                Connection conn = getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(
                                QUERY);

                ResultSet rs =
                        ps.executeQuery()
        ) {

            System.out.println(
                    "Latest Rider Locations");

            while (rs.next()) {

                String riderName =
                        rs.getString(
                                "rider_name");

                String bikeModel =
                        rs.getString(
                                "bike_model");

                double latitude =
                        rs.getDouble(
                                "latitude");

                double longitude =
                        rs.getDouble(
                                "longitude");

                LocalDateTime recordedAt =
                        rs.getObject(
                                "recorded_at",
                                LocalDateTime.class);

                System.out.println(
                        riderName
                                + " | "
                                + bikeModel
                                + " | "
                                + latitude
                                + ", "
                                + longitude
                                + " | "
                                + recordedAt);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Database Error: "
                            + e.getMessage());
        }
    }
}

public class HimalayanFleetTracker {

    public static void main(String[] args) {

        TelemetryService service =
                new FleetRepository();

        service.printLatestLocations();
    }
}