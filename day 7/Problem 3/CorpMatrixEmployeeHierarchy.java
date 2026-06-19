import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    private String firstName;

    private String department;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @OneToMany(mappedBy = "manager")
    private List<Employee> directReports;

    public String getFirstName() {
        return firstName;
    }

    public String getDepartment() {
        return department;
    }

    public Employee getManager() {
        return manager;
    }
}

@Entity
class FullTimeEmployee extends Employee {

    private double salary;
}

@Entity
class Contractor extends Employee {

    private double hourlyRate;
}

class ManagerSpanDTO {

    private String managerName;
    private Long directReportCount;

    public ManagerSpanDTO(
            String managerName,
            Long directReportCount) {

        this.managerName = managerName;
        this.directReportCount = directReportCount;
    }

    @Override
    public String toString() {

        return managerName
                + " -> "
                + directReportCount;
    }
}

interface EmployeeRepository
        extends JpaRepository<Employee, Long> {

    @Query("""
        SELECT new ManagerSpanDTO(
            m.firstName,
            COUNT(e)
        )
        FROM Employee e
        INNER JOIN e.manager m
        GROUP BY m.firstName
    """)
    List<ManagerSpanDTO> getManagerSpanReport();

    List<Employee>
    findByManagerFirstNameAndDepartment(
            String managerFirstName,
            String department
    );
}

public class CorpMatrixEmployeeHierarchy {

    public static void main(String[] args) {

        System.out.println(
                "CorpMatrix Employee Hierarchy");
    }
}