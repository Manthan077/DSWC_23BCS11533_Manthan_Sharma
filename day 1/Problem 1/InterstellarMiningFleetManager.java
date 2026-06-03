public class InterstellarMiningFleetManager {

    static abstract class SpaceVessel {

        protected short shipId;
        protected boolean operational;
        protected char fleetClass;

        public SpaceVessel(short shipId,
                           boolean operational,
                           char fleetClass) {

            this.shipId = shipId;
            this.operational = operational;
            this.fleetClass = fleetClass;
        }
    }

    static class MiningShip extends SpaceVessel {

        private float[][] cargoHold;

        public MiningShip(short shipId,
                          boolean operational,
                          char fleetClass,
                          float[][] cargoHold) {

            super(shipId, operational, fleetClass);
            this.cargoHold = cargoHold;
        }

        public float calculateTotalOreWeight() {

            float total = 0;

            for (float[] bay : cargoHold) {

                for (float weight : bay) {

                    total += weight;
                }
            }

            return total;
        }

        public float findHeaviestContainer() {

            float maxWeight = Float.MIN_VALUE;

            for (float[] bay : cargoHold) {

                for (float weight : bay) {

                    maxWeight =
                            Math.max(maxWeight,
                                    weight);
                }
            }

            return maxWeight;
        }
    }

    public static void main(String[] args) {

        SpaceVessel[] fleet =
                new SpaceVessel[2];

        float[][] cargo = {
                {1200.5f, 2200.0f},
                {5500.75f, 3100.25f}
        };

        fleet[0] =
                new MiningShip(
                        (short)101,
                        true,
                        'A',
                        cargo);

        MiningShip ship =
                (MiningShip) fleet[0];

        System.out.println(
                "Total Ore Weight = "
                        + ship.calculateTotalOreWeight());

        System.out.println(
                "Heaviest Container = "
                        + ship.findHeaviestContainer());
    }
}