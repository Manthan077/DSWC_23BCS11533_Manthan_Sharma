import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

interface TradingStrategy {
    void executeTrade();
}

abstract class AbstractStrategy implements TradingStrategy {

    protected String assetClass;

    public String getAssetClass() {
        return assetClass;
    }
}

@Component
class MomentumStrategy extends AbstractStrategy {

    public MomentumStrategy() {
        this.assetClass = "Equity";
    }

    @Override
    public void executeTrade() {
        System.out.println(
                "Executing Momentum Strategy");
    }
}

@Component
class ArbitrageStrategy extends AbstractStrategy {

    public ArbitrageStrategy() {
        this.assetClass = "Forex";
    }

    @Override
    public void executeTrade() {
        System.out.println(
                "Executing Arbitrage Strategy");
    }
}

@Component
class MarketDataService {

    public void loadMarketData() {

        System.out.println(
                "Market Data Loaded");
    }
}

@Component
class AlertService {

    public void sendAlert(String message) {

        System.out.println(
                "ALERT : " + message);
    }
}

@Component
public class QuantifyXTradingEngine
        implements BeanNameAware,
        InitializingBean {

    private final MarketDataService marketDataService;

    private final List<TradingStrategy> strategies;

    private AlertService alertService;

    private String beanName;

    public QuantifyXTradingEngine(
            MarketDataService marketDataService,
            List<TradingStrategy> strategies) {

        this.marketDataService =
                marketDataService;

        this.strategies =
                strategies;
    }

    @Autowired(required = false)
    public void setAlertService(
            AlertService alertService) {

        this.alertService =
                alertService;
    }

    @Override
    public void setBeanName(
            String beanName) {

        this.beanName =
                beanName;

        System.out.println(
                "Bean Name : "
                        + beanName);
    }

    @PostConstruct
    public void warmUpCache() {

        System.out.println(
                "Warming Market Cache...");

        marketDataService
                .loadMarketData();
    }

    @Override
    public void afterPropertiesSet()
            throws Exception {

        System.out.println(
                "Performing Safety Validation...");

        if (marketDataService == null) {

            throw new IllegalStateException(
                    "MarketDataService missing");
        }

        if (strategies == null
                || strategies.isEmpty()) {

            throw new IllegalStateException(
                    "No Trading Strategies Loaded");
        }

        System.out.println(
                "Validation Successful");
    }

    public void startTrading() {

        System.out.println(
                "Trading Engine Started");

        for (TradingStrategy strategy
                : strategies) {

            strategy.executeTrade();
        }

        if (alertService != null) {

            alertService.sendAlert(
                    "Trading Engine Active");
        }
    }

    @PreDestroy
    public void closePositions() {

        System.out.println(
                "Closing Open Positions...");
    }
}