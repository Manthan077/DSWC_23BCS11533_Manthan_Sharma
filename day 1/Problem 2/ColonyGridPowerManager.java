public class ColonyGridPowerManager {

    static class PowerManager {

        private byte sectorStates = 0;

        public void turnOnSector(int sectorIndex) {

            sectorStates =
                    (byte)(sectorStates |
                            (1 << sectorIndex));
        }

        public void turnOffSector(int sectorIndex) {

            sectorStates =
                    (byte)(sectorStates &
                            ~(1 << sectorIndex));
        }

        public boolean isSectorOn(int sectorIndex) {

            return (sectorStates &
                    (1 << sectorIndex)) != 0;
        }
    }

    public static void main(String[] args) {

        PowerManager manager =
                new PowerManager();

        manager.turnOnSector(1);
        manager.turnOnSector(4);

        System.out.println(
                "Sector 1: "
                        + manager.isSectorOn(1));

        System.out.println(
                "Sector 4: "
                        + manager.isSectorOn(4));

        manager.turnOffSector(1);

        System.out.println(
                "Sector 1: "
                        + manager.isSectorOn(1));
    }
}