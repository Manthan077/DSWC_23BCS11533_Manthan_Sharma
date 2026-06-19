import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "patients")
class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;

    private String patientName;

    @OneToMany(
            mappedBy = "patient",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<MedicalRecord> medicalRecords;

    @OneToOne(
            mappedBy = "patient",
            cascade = CascadeType.ALL
    )
    private BillingAccount billingAccount;

    public Long getPatientId() {
        return patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public BillingAccount getBillingAccount() {
        return billingAccount;
    }
}

@Entity
@Table(name = "medical_records")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        name = "record_type",
        discriminatorType = DiscriminatorType.STRING
)
abstract class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    public Patient getPatient() {
        return patient;
    }
}

@Entity
@DiscriminatorValue("PRESCRIPTION")
class PrescriptionRecord extends MedicalRecord {

    private String medicationName;

    public String getMedicationName() {
        return medicationName;
    }
}

@Entity
@DiscriminatorValue("LAB_RESULT")
class LabResultRecord extends MedicalRecord {

    private String labName;

    public String getLabName() {
        return labName;
    }
}

@Entity
@Table(name = "billing_accounts")
class BillingAccount {

    @Id
    private Long patientId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "patient_id")
    private Patient patient;

    private BigDecimal currentBalance;

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }
}

class PatientBillingSummaryDTO {

    private String patientName;
    private String recordType;
    private BigDecimal currentBalance;

    public PatientBillingSummaryDTO(
            String patientName,
            String recordType,
            BigDecimal currentBalance) {

        this.patientName = patientName;
        this.recordType = recordType;
        this.currentBalance = currentBalance;
    }

    @Override
    public String toString() {
        return patientName + " "
                + recordType + " "
                + currentBalance;
    }
}

interface PatientRepository
        extends JpaRepository<Patient, Long> {

    @Query("""
        SELECT new PatientBillingSummaryDTO(
            p.patientName,
            TYPE(m).name,
            b.currentBalance
        )
        FROM Patient p
        JOIN p.medicalRecords m
        JOIN p.billingAccount b
    """)
    List<PatientBillingSummaryDTO>
    getPatientBillingSummary();

    List<Patient>
    findDistinctByBillingAccountCurrentBalanceGreaterThanAndMedicalRecordsMedicationName(
            BigDecimal amount,
            String medicationName
    );
}

public class HealthSyncPatientRecordSystem {

    public static void main(String[] args) {

        System.out.println(
                "HealthSync Patient Record System");
    }
}