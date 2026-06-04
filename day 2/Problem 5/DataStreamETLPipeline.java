abstract class DataPayload {

    public abstract String getRawContent();
}

class JsonPayload extends DataPayload {

    private String content;

    public JsonPayload(String content) {
        this.content = content;
    }

    @Override
    public String getRawContent() {
        return content;
    }
}

class XmlPayload extends DataPayload {

    private String content;

    public XmlPayload(String content) {
        this.content = content;
    }

    @Override
    public String getRawContent() {
        return content;
    }
}

class PipelineProcessor<T extends DataPayload> {

    public void process(T payload) {

        System.out.println(
                "Processing Payload: "
                        + payload.getRawContent());
    }
}

public class DataStreamETLPipeline {

    public static void main(String[] args) {

        JsonPayload json =
                new JsonPayload("{\"name\":\"John\"}");

        XmlPayload xml =
                new XmlPayload("<name>John</name>");

        PipelineProcessor<JsonPayload> jsonProcessor =
                new PipelineProcessor<>();

        PipelineProcessor<XmlPayload> xmlProcessor =
                new PipelineProcessor<>();

        jsonProcessor.process(json);
        xmlProcessor.process(xml);
    }
}