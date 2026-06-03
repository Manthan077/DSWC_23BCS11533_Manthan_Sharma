import java.util.concurrent.atomic.AtomicInteger;

public class DroneHiveSynchronization {

    static class DroneHive {

        private AtomicInteger
                totalDronesReturned =
                new AtomicInteger(0);

        private volatile boolean
                emergencyAbort = false;

        public void droneReturned() {

            totalDronesReturned
                    .incrementAndGet();
        }

        public int getReturnedCount() {

            return totalDronesReturned.get();
        }

        public void detectStorm() {

            emergencyAbort = true;
        }

        public boolean isEmergencyAbort() {

            return emergencyAbort;
        }
    }

    public static void main(String[] args) {

        DroneHive hive =
                new DroneHive();

        hive.droneReturned();
        hive.droneReturned();
        hive.droneReturned();

        System.out.println(
                "Returned Drones = "
                        + hive.getReturnedCount());

        hive.detectStorm();

        System.out.println(
                "Emergency Abort = "
                        + hive.isEmergencyAbort());
    }
}