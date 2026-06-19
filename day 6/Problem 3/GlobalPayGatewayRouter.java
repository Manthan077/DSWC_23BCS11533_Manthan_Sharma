import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

interface PaymentProcessor {

    void processPayment(double amount);
}

@Component
@Primary
class StripeProcessor
        implements PaymentProcessor {

    @Override
    public void processPayment(
            double amount) {

        System.out.println(
                "Stripe Processing : "
                        + amount);
    }
}

@Component
class SquareProcessor
        implements PaymentProcessor {

    @Override
    public void processPayment(
            double amount) {

        System.out.println(
                "Square Processing : "
                        + amount);
    }
}

class BankXmlProcessor
        implements PaymentProcessor {

    private String endpoint;
    private String apiKey;
    private String version;

    public void setEndpoint(
            String endpoint) {

        this.endpoint = endpoint;
    }

    public void setApiKey(
            String apiKey) {

        this.apiKey = apiKey;
    }

    public void setVersion(
            String version) {

        this.version = version;
    }

    @Override
    public void processPayment(
            double amount) {

        System.out.println(
                "Bank XML Processing : "
                        + amount);
    }
}

@Component("bankXmlProcessor")
class BankXmlProcessorFactory
        implements FactoryBean<BankXmlProcessor> {

    @Override
    public BankXmlProcessor getObject()
            throws Exception {

        BankXmlProcessor processor =
                new BankXmlProcessor();

        processor.setEndpoint(
                "https://bank-api.com");

        processor.setApiKey(
                "BANK_KEY");

        processor.setVersion(
                "v1");

        return processor;
    }

    @Override
    public Class<?> getObjectType() {

        return BankXmlProcessor.class;
    }

    @Override
    public boolean isSingleton() {

        return true;
    }
}

@Component
public class GlobalPayGatewayRouter {

    private final PaymentProcessor
            defaultProcessor;

    private final PaymentProcessor
            bankProcessor;

    public GlobalPayGatewayRouter(

            PaymentProcessor defaultProcessor,

            @Qualifier("bankXmlProcessor")
            PaymentProcessor bankProcessor) {

        this.defaultProcessor =
                defaultProcessor;

        this.bankProcessor =
                bankProcessor;
    }

    public void checkout() {

        defaultProcessor
                .processPayment(5000);

        bankProcessor
                .processPayment(10000);
    }
}