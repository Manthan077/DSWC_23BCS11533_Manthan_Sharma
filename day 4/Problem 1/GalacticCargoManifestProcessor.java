import java.util.*;
import java.util.stream.*;

abstract class Cargo {

    protected String containerId;
    protected double valueInCredits;
    protected boolean isHazardous;

    public Cargo(String containerId,
                 double valueInCredits,
                 boolean isHazardous) {

        this.containerId = containerId;
        this.valueInCredits = valueInCredits;
        this.isHazardous = isHazardous;
    }

    public String getContainerId() {
        return containerId;
    }

    public double getValueInCredits() {
        return valueInCredits;
    }

    public boolean isHazardous() {
        return isHazardous;
    }
}

class StandardCargo extends Cargo {

    public StandardCargo(String containerId,
                         double valueInCredits,
                         boolean isHazardous) {

        super(containerId, valueInCredits, isHazardous);
    }
}

class BiologicalCargo extends Cargo {

    private boolean isShielded;

    public BiologicalCargo(String containerId,
                           double valueInCredits,
                           boolean isHazardous,
                           boolean isShielded) {

        super(containerId, valueInCredits, isHazardous);
        this.isShielded = isShielded;
    }

    public boolean isShielded() {
        return isShielded;
    }
}

@FunctionalInterface
interface CargoInspector {
    boolean inspect(Cargo cargo);
}

@FunctionalInterface
interface CargoCompressor {
    String compress(Cargo cargo);
}

class ManifestProcessor {

    public List<String> processManifest(
            List<Cargo> manifest,
            CargoInspector inspector,
            CargoCompressor compressor) {

        return manifest.stream()
                .filter(inspector::inspect)
                .filter(c -> c.getValueInCredits() >= 1000.0)
                .map(compressor::compress)
                .collect(Collectors.toList());
    }
}

public class GalacticCargoManifestProcessor {

    public static void main(String[] args) {

        List<Cargo> manifest = new ArrayList<>();

        manifest.add(
                new StandardCargo(
                        "ALPHA-99",
                        5000.50,
                        false));

        manifest.add(
                new BiologicalCargo(
                        "BIO-101",
                        7000,
                        true,
                        false));

        manifest.add(
                new BiologicalCargo(
                        "BIO-202",
                        9000,
                        true,
                        true));

        manifest.add(
                new StandardCargo(
                        "GAMMA-11",
                        800,
                        false));

        CargoInspector inspector = cargo -> {

            if (cargo.isHazardous()
                    && cargo instanceof BiologicalCargo) {

                BiologicalCargo bio =
                        (BiologicalCargo) cargo;

                return bio.isShielded();
            }

            return true;
        };

        CargoCompressor compressor = cargo ->

                cargo.getContainerId().substring(0, 4)
                        + "-"
                        + (int) cargo.getValueInCredits();

        ManifestProcessor processor =
                new ManifestProcessor();

        List<String> result =
                processor.processManifest(
                        manifest,
                        inspector,
                        compressor);

        System.out.println("Compressed Cargo Data:");

        result.forEach(System.out::println);
    }
}