abstract class DeliveryDrone {

    protected String droneId;

    public DeliveryDrone(String droneId) {
        this.droneId = droneId;
    }

    public abstract void deliverPackage();
}

interface Airborne {

    void flyToDestination();

    default void requestAirTrafficClearance() {
        System.out.println("Air traffic clearance requested.");
    }
}

interface GroundBased {
    void navigateSidewalks();
}

class Quadcopter extends DeliveryDrone implements Airborne {

    public Quadcopter(String droneId) {
        super(droneId);
    }

    @Override
    public void flyToDestination() {
        System.out.println("Quadcopter flying to destination.");
    }

    @Override
    public void deliverPackage() {
        requestAirTrafficClearance();
        flyToDestination();
        System.out.println("Package delivered by Quadcopter.");
    }
}

class CityRover extends DeliveryDrone implements GroundBased {

    public CityRover(String droneId) {
        super(droneId);
    }

    @Override
    public void navigateSidewalks() {
        System.out.println("City Rover navigating sidewalks.");
    }

    @Override
    public void deliverPackage() {
        navigateSidewalks();
        System.out.println("Package delivered by City Rover.");
    }
}

class HybridVTOL extends DeliveryDrone
        implements Airborne, GroundBased {

    public HybridVTOL(String droneId) {
        super(droneId);
    }

    @Override
    public void flyToDestination() {
        System.out.println("Hybrid VTOL flying.");
    }

    @Override
    public void navigateSidewalks() {
        System.out.println("Hybrid VTOL driving.");
    }

    @Override
    public void deliverPackage() {
        requestAirTrafficClearance();
        flyToDestination();
        navigateSidewalks();
        System.out.println("Package delivered by Hybrid VTOL.");
    }
}

public class AeroLogixDroneFleet {

    public static void main(String[] args) {

        DeliveryDrone d1 = new Quadcopter("Q101");
        DeliveryDrone d2 = new CityRover("R101");
        DeliveryDrone d3 = new HybridVTOL("H101");

        d1.deliverPackage();
        d2.deliverPackage();
        d3.deliverPackage();
    }
}