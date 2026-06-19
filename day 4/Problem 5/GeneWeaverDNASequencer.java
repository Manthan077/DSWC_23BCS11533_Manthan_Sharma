import java.util.*;
import java.util.stream.*;

abstract class DNASample {

    protected String sampleId;
    protected double purityPercentage;

    public DNASample(String sampleId,
                     double purityPercentage) {

        this.sampleId = sampleId;
        this.purityPercentage = purityPercentage;
    }

    public String getSampleId() {
        return sampleId;
    }

    public double getPurityPercentage() {
        return purityPercentage;
    }
}

class HumanSample extends DNASample {

    private String bloodType;

    public HumanSample(String sampleId,
                       double purityPercentage,
                       String bloodType) {

        super(sampleId, purityPercentage);
        this.bloodType = bloodType;
    }

    public String getBloodType() {
        return bloodType;
    }
}

class AlienSample extends DNASample {

    private boolean isSiliconBased;

    public AlienSample(String sampleId,
                       double purityPercentage,
                       boolean isSiliconBased) {

        super(sampleId, purityPercentage);
        this.isSiliconBased = isSiliconBased;
    }

    public boolean isSiliconBased() {
        return isSiliconBased;
    }
}

@FunctionalInterface
interface ViabilityChecker {
    boolean check(DNASample sample);
}

@FunctionalInterface
interface GenomeMapper {
    String map(DNASample sample);
}

class Sequencer {

    public Map<String, List<String>> classifyGenomes(
            List<DNASample> samples,
            ViabilityChecker checker,
            GenomeMapper mapper) {

        return samples.stream()
                .filter(checker::check)
                .collect(Collectors.groupingBy(
                        sample -> sample.getClass().getSimpleName(),
                        Collectors.mapping(
                                mapper::map,
                                Collectors.toList()
                        )
                ));
    }
}

public class GeneWeaverDNASequencer {

    public static void main(String[] args) {

        List<DNASample> samples = Arrays.asList(

                new HumanSample(
                        "001",
                        95.0,
                        "O-"),

                new HumanSample(
                        "002",
                        75.0,
                        "A+"),

                new AlienSample(
                        "003",
                        88.0,
                        true),

                new AlienSample(
                        "004",
                        60.0,
                        false),

                new HumanSample(
                        "005",
                        91.0,
                        "B+")
        );

        ViabilityChecker checker =
                sample -> sample.getPurityPercentage() >= 80.0;

        GenomeMapper mapper = sample -> {

            if (sample instanceof HumanSample) {

                HumanSample human =
                        (HumanSample) sample;

                return "ID: "
                        + human.getSampleId()
                        + " (Type: "
                        + human.getBloodType()
                        + ")";
            }

            AlienSample alien =
                    (AlienSample) sample;

            return "ID: "
                    + alien.getSampleId()
                    + " (Silicon: "
                    + alien.isSiliconBased()
                    + ")";
        };

        Sequencer sequencer = new Sequencer();

        Map<String, List<String>> result =
                sequencer.classifyGenomes(
                        samples,
                        checker,
                        mapper);

        System.out.println("Classified Genomes:");

        result.forEach((key, value) -> {
            System.out.println(key + " -> " + value);
        });
    }
}