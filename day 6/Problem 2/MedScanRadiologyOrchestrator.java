import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Lazy
class ImageRenderingEngine {

    public ImageRenderingEngine() {

        System.out.println(
                "Heavy ImageRenderingEngine Initialized");
    }

    public void renderMRI() {

        System.out.println(
                "Rendering MRI Scan...");
    }
}

@Component
@Scope("prototype")
class PatientContext {

    private String patientId;

    public void setPatientId(
            String patientId) {

        this.patientId =
                patientId;
    }

    public String getPatientId() {

        return patientId;
    }
}

@Component
public class MedScanRadiologyOrchestrator {

    private final ImageRenderingEngine
            imageRenderingEngine;

    private final ObjectProvider<PatientContext>
            patientContextProvider;

    public MedScanRadiologyOrchestrator(

            ImageRenderingEngine imageRenderingEngine,

            ObjectProvider<PatientContext>
                    patientContextProvider) {

        this.imageRenderingEngine =
                imageRenderingEngine;

        this.patientContextProvider =
                patientContextProvider;
    }

    public void processScan(
            String patientId) {

        PatientContext context =
                patientContextProvider
                        .getObject();

        context.setPatientId(
                patientId);

        System.out.println(
                "Processing Scan For : "
                        + context.getPatientId());

        imageRenderingEngine
                .renderMRI();
    }
}