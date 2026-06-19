import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

interface PIIProcessor {
}

@Component
class TransactionService
        implements PIIProcessor {

    public void processTransaction() {

        System.out.println(
                "Processing Sensitive Transaction");
    }
}

@Component
class PublicCurrencyService {

    public void getExchangeRates() {

        System.out.println(
                "Fetching Public Exchange Rates");
    }
}

@Component
class ComplianceAuditProcessor
        implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(
            Object bean,
            String beanName)
            throws BeansException {

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(
            Object bean,
            String beanName)
            throws BeansException {

        if (bean instanceof PIIProcessor) {

            System.out.println(
                    "[AUDIT] Securely Initialized PII Bean : "
                            + beanName);
        }

        return bean;
    }
}

public class SecureBankComplianceAuditor {

    public static void main(String[] args) {

        System.out.println(
                "Spring Container Startup...");
    }
}