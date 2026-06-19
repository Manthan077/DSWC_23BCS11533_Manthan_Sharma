import java.util.*;
import java.util.stream.*;

abstract class TemporalEntity {

    protected String entityName;
    protected int originYear;

    public TemporalEntity(String entityName,
                          int originYear) {

        this.entityName = entityName;
        this.originYear = originYear;
    }

    public String getEntityName() {
        return entityName;
    }

    public int getOriginYear() {
        return originYear;
    }
}

class HumanEntity extends TemporalEntity {

    public HumanEntity(String entityName,
                       int originYear) {

        super(entityName, originYear);
    }
}

class ArtifactEntity extends TemporalEntity {

    private boolean isRadioactive;

    public ArtifactEntity(String entityName,
                          int originYear,
                          boolean isRadioactive) {

        super(entityName, originYear);
        this.isRadioactive = isRadioactive;
    }

    public boolean isRadioactive() {
        return isRadioactive;
    }
}

class HistoricalEvent {

    private List<TemporalEntity> entities;
    private int eventYear;

    public HistoricalEvent(int eventYear,
                           List<TemporalEntity> entities) {

        this.eventYear = eventYear;
        this.entities = entities;
    }

    public int getEventYear() {
        return eventYear;
    }

    public List<TemporalEntity> getEntities() {
        return entities;
    }
}

@FunctionalInterface
interface ParadoxChecker {
    boolean check(TemporalEntity entity, int eventYear);
}

@FunctionalInterface
interface ThreatMapper {
    String map(TemporalEntity entity);
}

class ParadoxAnalyzer {

    public List<String> detectParadoxes(
            List<HistoricalEvent> timeline,
            ParadoxChecker checker,
            ThreatMapper mapper) {

        return timeline.stream()
                .flatMap(event ->
                        event.getEntities()
                                .stream()
                                .filter(entity ->
                                        checker.check(
                                                entity,
                                                event.getEventYear()))
                                .map(mapper::map))
                .collect(Collectors.toList());
    }
}

public class ChronoCorpParadoxDetector {

    public static void main(String[] args) {

        List<TemporalEntity> event1Entities =
                Arrays.asList(
                        new HumanEntity(
                                "John",
                                2050),

                        new ArtifactEntity(
                                "Ancient Device",
                                1800,
                                true));

        List<TemporalEntity> event2Entities =
                Arrays.asList(
                        new HumanEntity(
                                "Sarah",
                                1990),

                        new ArtifactEntity(
                                "Future Core",
                                3000,
                                false));

        HistoricalEvent event1 =
                new HistoricalEvent(
                        2000,
                        event1Entities);

        HistoricalEvent event2 =
                new HistoricalEvent(
                        2025,
                        event2Entities);

        List<HistoricalEvent> timeline =
                Arrays.asList(event1, event2);

        ParadoxChecker checker =
                (entity, eventYear) ->
                        entity.getOriginYear() > eventYear;

        ThreatMapper mapper =
                entity ->
                        entity.getEntityName()
                                + " detected out of time!";

        ParadoxAnalyzer analyzer =
                new ParadoxAnalyzer();

        List<String> paradoxes =
                analyzer.detectParadoxes(
                        timeline,
                        checker,
                        mapper);

        System.out.println("Detected Paradoxes:");

        paradoxes.forEach(System.out::println);
    }
}