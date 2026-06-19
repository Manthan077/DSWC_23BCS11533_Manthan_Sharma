import java.util.*;

class Passenger {
    private String passportNumber;
    private String fullName;
    private String nationality;

    public Passenger(String passportNumber, String fullName, String nationality) {
        this.passportNumber = passportNumber;
        this.fullName = fullName;
        this.nationality = nationality;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public String getNationality() {
        return nationality;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        Passenger other = (Passenger) obj;

        return passportNumber.equals(other.passportNumber)
                && nationality.equals(other.nationality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passportNumber, nationality);
    }

    @Override
    public String toString() {
        return fullName + " (" + passportNumber + ")";
    }
}

class ManifestManager {

    private Set<Passenger> globalNoFlyList;
    private Map<String, List<Passenger>> flightRosters;
    private Map<Passenger, String> globalPassengerDirectory;

    public ManifestManager() {
        globalNoFlyList = new HashSet<>();
        flightRosters = new HashMap<>();
        globalPassengerDirectory = new HashMap<>();
    }

    public void addToNoFlyList(Passenger p) {
        globalNoFlyList.add(p);
    }

    public void checkInPassenger(String flightNumber, Passenger p) {
        flightRosters
                .computeIfAbsent(flightNumber, k -> new ArrayList<>())
                .add(p);
    }

    public boolean processCheckIn(String flightNumber, Passenger p) {

        if (globalNoFlyList.contains(p)) {
            return false;
        }

        checkInPassenger(flightNumber, p);

        globalPassengerDirectory.put(p, flightNumber);

        return true;
    }

    public String locatePassengerFlight(Passenger p) {
        return globalPassengerDirectory.get(p);
    }

    public void displayFlightRoster(String flightNumber) {

        List<Passenger> passengers = flightRosters.get(flightNumber);

        if (passengers == null || passengers.isEmpty()) {
            System.out.println("No passengers found.");
            return;
        }

        System.out.println("Passengers for Flight " + flightNumber + ":");

        for (Passenger p : passengers) {
            System.out.println(p);
        }
    }
}

public class AirlineManifestSystem {

    public static void main(String[] args) {

        ManifestManager manager = new ManifestManager();

        Passenger p1 = new Passenger(
                "P101",
                "Alice Johnson",
                "USA");

        Passenger p2 = new Passenger(
                "P102",
                "Bob Sharma",
                "India");

        Passenger p3 = new Passenger(
                "P103",
                "Charlie Brown",
                "Canada");

        manager.addToNoFlyList(p2);

        System.out.println(
                "Alice Check-In: "
                        + manager.processCheckIn("AI101", p1));

        System.out.println(
                "Bob Check-In: "
                        + manager.processCheckIn("AI101", p2));

        System.out.println(
                "Charlie Check-In: "
                        + manager.processCheckIn("AI101", p3));

        manager.displayFlightRoster("AI101");

        Passenger searchPassenger =
                new Passenger(
                        "P101",
                        "Alice Smith",
                        "USA");

        System.out.println(
                "\nLocated Flight: "
                        + manager.locatePassengerFlight(searchPassenger));
    }
}