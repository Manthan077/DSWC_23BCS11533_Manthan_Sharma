public class DeepSeaTelemetryErrorHandling {

    static class HardwareLockException
            extends Exception {

        public HardwareLockException(
                String message) {

            super(message);
        }
    }

    static class SensorCorruptionException
            extends RuntimeException {

        public SensorCorruptionException(
                String message) {

            super(message);
        }
    }

    static class TelemetryStream
            implements AutoCloseable {

        public void readData() {

            System.out.println(
                    "Reading telemetry...");
        }

        @Override
        public void close() {

            System.out.println(
                    "Telemetry stream closed.");
        }
    }

    static class TelemetryParser {

        public void parse()
                throws HardwareLockException {

            try (TelemetryStream stream =
                         new TelemetryStream()) {

                stream.readData();

            }
        }
    }

    public static void main(String[] args) {

        TelemetryParser parser =
                new TelemetryParser();

        try {

            parser.parse();

        } catch (HardwareLockException e) {

            System.out.println(
                    e.getMessage());
        }
    }
}