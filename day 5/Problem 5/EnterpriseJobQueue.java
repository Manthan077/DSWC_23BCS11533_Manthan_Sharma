import java.sql.*;

interface QueueWorker {
    void processNextJob();
}

abstract class EnterpriseConnectionFactory {

    private static final String URL =
            "jdbc:postgresql://localhost:5432/enterprise";

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

class JobQueueRepository
        extends EnterpriseConnectionFactory
        implements QueueWorker {

    private static final String FETCH_JOB =

            "SELECT bj.job_id, d.dept_name " +
            "FROM background_jobs bj " +
            "INNER JOIN departments d " +
            "ON bj.dept_id = d.dept_id " +
            "WHERE bj.status = ? " +
            "AND d.dept_name = ? " +
            "ORDER BY bj.created_at ASC " +
            "LIMIT 1 " +
            "FOR UPDATE SKIP LOCKED";

    private static final String UPDATE_JOB =

            "UPDATE background_jobs " +
            "SET status = ? " +
            "WHERE job_id = ?";

    @Override
    public void processNextJob() {

        Connection conn = null;

        try {

            conn = getConnection();

            conn.setAutoCommit(false);

            long jobId = -1;

            try (
                    PreparedStatement selectStmt =
                            conn.prepareStatement(
                                    FETCH_JOB)
            ) {

                selectStmt.setString(
                        1,
                        "PENDING");

                selectStmt.setString(
                        2,
                        "Engineering");

                try (
                        ResultSet rs =
                                selectStmt.executeQuery()
                ) {

                    if (rs.next()) {

                        jobId =
                                rs.getLong(
                                        "job_id");

                        System.out.println(
                                "Picked Job ID: "
                                        + jobId);
                    }
                }
            }

            if (jobId != -1) {

                try (
                        PreparedStatement updateStmt =
                                conn.prepareStatement(
                                        UPDATE_JOB)
                ) {

                    updateStmt.setString(
                            1,
                            "PROCESSING");

                    updateStmt.setLong(
                            2,
                            jobId);

                    updateStmt.executeUpdate();
                }

                conn.commit();

                System.out.println(
                        "Job moved to PROCESSING.");
            }
            else {

                conn.rollback();

                System.out.println(
                        "No pending jobs found.");
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
                    "Error: "
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

public class EnterpriseJobQueue {

    public static void main(String[] args) {

        QueueWorker worker =
                new JobQueueRepository();

        worker.processNextJob();
    }
}