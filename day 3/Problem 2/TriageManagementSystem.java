import java.util.*;

enum TriageLevel {
    CRITICAL,
    URGENT,
    STABLE
}

class Patient implements Comparable<Patient> {

    private String name;
    private TriageLevel severity;
    private long arrivalTime;

    public Patient(String name, TriageLevel severity, long arrivalTime) {
        this.name = name;
        this.severity = severity;
        this.arrivalTime = arrivalTime;
    }

    public String getName() {
        return name;
    }

    public TriageLevel getSeverity() {
        return severity;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    @Override
    public int compareTo(Patient other) {

        if (this.severity != other.severity) {
            return Integer.compare(
                    this.severity.ordinal(),
                    other.severity.ordinal());
        }

        return Long.compare(
                this.arrivalTime,
                other.arrivalTime);
    }

    @Override
    public String toString() {
        return "Patient{name='" + name +
                "', severity=" + severity +
                ", arrivalTime=" + arrivalTime + "}";
    }
}

class TriageManager {

    private PriorityQueue<Patient> waitingRoom;

    public TriageManager() {
        waitingRoom = new PriorityQueue<>();
    }

    public void admitPatient(Patient p) {
        waitingRoom.offer(p);
    }

    public Patient getNextPatient() {
        return waitingRoom.poll();
    }

    public boolean isEmpty() {
        return waitingRoom.isEmpty();
    }
}

public class TriageManagementSystem {

    public static void main(String[] args) {

        TriageManager manager = new TriageManager();

        manager.admitPatient(
                new Patient(
                        "John",
                        TriageLevel.URGENT,
                        100));

        manager.admitPatient(
                new Patient(
                        "Alice",
                        TriageLevel.CRITICAL,
                        200));

        manager.admitPatient(
                new Patient(
                        "Bob",
                        TriageLevel.CRITICAL,
                        50));

        manager.admitPatient(
                new Patient(
                        "David",
                        TriageLevel.STABLE,
                        300));

        System.out.println("Treatment Order:");

        while (!manager.isEmpty()) {
            System.out.println(
                    manager.getNextPatient());
        }
    }
}