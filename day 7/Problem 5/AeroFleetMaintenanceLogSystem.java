import jakarta.persistence.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

@Embeddable
class MaintenanceLog {

    private String maintenanceType;

    private LocalDateTime maintenanceDate;

    private String remarks;

    public String getMaintenanceType() {
        return maintenanceType;
    }

    public LocalDateTime getMaintenanceDate() {
        return maintenanceDate;
    }
}

@Entity
@Table(name = "aircraft")
class Aircraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aircraftId;

    private String modelName;

    private boolean isGrounded;

    @ElementCollection
    @CollectionTable(
            name = "aircraft_maintenance_logs",
            joinColumns = @JoinColumn(name = "aircraft_id")
    )
    private List<MaintenanceLog> maintenanceLogs;

    public String getModelName() {
        return modelName;
    }

    public boolean isGrounded() {
        return isGrounded;
    }

    public List<MaintenanceLog> getMaintenanceLogs() {
        return maintenanceLogs;
    }
}

interface AircraftRepository
        extends JpaRepository<Aircraft, Long> {

    @Query("""
        SELECT DISTINCT a
        FROM Aircraft a
        JOIN a.maintenanceLogs m
        WHERE m.maintenanceType = :maintenanceType
        AND m.maintenanceDate BETWEEN :startDate AND :endDate
    """)
    Page<Aircraft> findAircraftByMaintenanceTypeAndDateRange(
            String maintenanceType,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );

    List<Aircraft>
    findByModelNameInAndIsGroundedTrue(
            List<String> modelNames
    );
}

public class AeroFleetMaintenanceLogSystem {

    public static void main(String[] args) {

        System.out.println(
                "AeroFleet Maintenance Log System");
    }
}