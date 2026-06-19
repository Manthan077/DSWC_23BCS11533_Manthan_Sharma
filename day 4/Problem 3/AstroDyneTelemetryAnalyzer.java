import java.util.*;

abstract class EngineLog {

    protected String timestamp;
    protected double coreTemperature;
    protected boolean isAnomaly;

    public EngineLog(String timestamp,
                     double coreTemperature,
                     boolean isAnomaly) {

        this.timestamp = timestamp;
        this.coreTemperature = coreTemperature;
        this.isAnomaly = isAnomaly;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public double getCoreTemperature() {
        return coreTemperature;
    }

    public boolean isAnomaly() {
        return isAnomaly;
    }
}

class NominalLog extends EngineLog {

    public NominalLog(String timestamp,
                      double coreTemperature,
                      boolean isAnomaly) {

        super(timestamp, coreTemperature, isAnomaly);
    }
}

class CriticalLog extends EngineLog {

    private String errorCode;

    public CriticalLog(String timestamp,
                       double coreTemperature,
                       boolean isAnomaly,
                       String errorCode) {

        super(timestamp, coreTemperature, isAnomaly);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

@FunctionalInterface
interface LogAuditor {
    boolean audit(EngineLog log);
}

@FunctionalInterface
interface HeatExtractor {
    double extract(EngineLog log);
}

class TelemetryProcessor {

    public double getPeakCriticalTemp(
            List<EngineLog> logs,
            LogAuditor auditor,
            HeatExtractor extractor) {

        return logs.stream()
                .filter(auditor::audit)
                .mapToDouble(extractor::extract)
                .max()
                .orElse(0.0);
    }
}

public class AstroDyneTelemetryAnalyzer {

    public static void main(String[] args) {

        List<EngineLog> logs = new ArrayList<>();

        logs.add(
                new NominalLog(
                        "2026-06-01 10:00",
                        350.5,
                        false));

        logs.add(
                new CriticalLog(
                        "2026-06-01 10:05",
                        890.0,
                        false,
                        "OVERHEAT"));

        logs.add(
                new CriticalLog(
                        "2026-06-01 10:10",
                        920.5,
                        true,
                        "TEMP_SPIKE"));

        logs.add(
                new NominalLog(
                        "2026-06-01 10:15",
                        400.0,
                        false));

        LogAuditor auditor = log -> {

            if (log.isAnomaly()) {
                return true;
            }

            if (log instanceof CriticalLog) {

                CriticalLog critical =
                        (CriticalLog) log;

                return critical.getErrorCode()
                        .equals("OVERHEAT");
            }

            return false;
        };

        HeatExtractor extractor =
                EngineLog::getCoreTemperature;

        TelemetryProcessor processor =
                new TelemetryProcessor();

        double peakTemperature =
                processor.getPeakCriticalTemp(
                        logs,
                        auditor,
                        extractor);

        System.out.println(
                "Peak Critical Temperature: "
                        + peakTemperature);
    }
}