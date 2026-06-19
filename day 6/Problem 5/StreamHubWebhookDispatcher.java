import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

interface WebhookIntegration {

    void sendWebhook(String message);
}

@Component
class SlackIntegration
        implements WebhookIntegration {

    @Override
    public void sendWebhook(
            String message) {

        System.out.println(
                "Slack : " + message);
    }
}

@Component
class DiscordIntegration
        implements WebhookIntegration {

    @Override
    public void sendWebhook(
            String message) {

        System.out.println(
                "Discord : " + message);
    }
}

@Component
class EmailIntegration
        implements WebhookIntegration {

    @Override
    public void sendWebhook(
            String message) {

        System.out.println(
                "Email : " + message);
    }
}

@Component
public class StreamHubWebhookDispatcher
        implements ApplicationContextAware,
        SmartInitializingSingleton {

    private ApplicationContext
            applicationContext;

    @Override
    public void setApplicationContext(
            ApplicationContext applicationContext)
            throws BeansException {

        this.applicationContext =
                applicationContext;
    }

    @Override
    public void afterSingletonsInstantiated() {

        Map<String, WebhookIntegration>
                integrations =

                applicationContext
                        .getBeansOfType(
                                WebhookIntegration.class);

        System.out.println(
                "Routing Table Ready");

        System.out.println(
                "Total Integrations Loaded : "
                        + integrations.size());

        integrations.forEach(
                (name, bean) ->
                        System.out.println(
                                "Loaded : "
                                        + name));
    }

    public void dispatch(
            String message) {

        Map<String, WebhookIntegration>
                integrations =

                applicationContext
                        .getBeansOfType(
                                WebhookIntegration.class);

        integrations.values()
                .forEach(
                        integration ->
                                integration.sendWebhook(
                                        message));
    }
}