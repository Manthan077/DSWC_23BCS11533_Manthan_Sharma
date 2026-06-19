import React,
{
  createContext,
  useContext,
  useState,
  useEffect,
  useMemo
} from "react";

const CurrencyContext =
  createContext();

const LivePriceFeed = {
  subscribe(callback) {
    const id = setInterval(() => {
      callback({
        BTC: 65000 +
          Math.floor(
            Math.random() * 1000
          ),
        ETH: 3500 +
          Math.floor(
            Math.random() * 100
          )
      });
    }, 1000);

    return id;
  },

  unsubscribe(id) {
    clearInterval(id);
  }
};

function calculateMassivePortfolioValue(
  transactions,
  livePrices,
  currency
) {
  console.log(
    "Heavy Calculation Running..."
  );

  return transactions.reduce(
    (total, tx) =>
      total +
      tx.amount *
        (livePrices[
          tx.symbol
        ] || 0),
    0
  );
}

function PortfolioMetrics({
  portfolioValue
}) {

  const {
    selectedCurrency
  } = useContext(
    CurrencyContext
  );

  const symbol =
    selectedCurrency === "EUR"
      ? "€"
      : selectedCurrency ===
        "JPY"
      ? "¥"
      : "$";

  return (
    <div>
      <h2>
        Portfolio Value:
        {symbol}
        {portfolioValue}
      </h2>
    </div>
  );
}

function DashboardContent({
  portfolioValue
}) {
  return (
    <PortfolioMetrics
      portfolioValue={
        portfolioValue
      }
    />
  );
}

export default function PortfolioDashboard() {

  const [
    selectedCurrency,
    setCurrency
  ] = useState("USD");

  const [
    livePrices,
    setLivePrices
  ] = useState({});

  const [
    darkMode,
    setDarkMode
  ] = useState(false);

  const [
    transactions
  ] = useState([
    {
      id: 1,
      symbol: "BTC",
      amount: 2
    },
    {
      id: 2,
      symbol: "ETH",
      amount: 5
    }
  ]);

  useEffect(() => {

    const handleNewPrices =
      prices => {
        setLivePrices(
          prices
        );
      };

    const connectionId =
      LivePriceFeed.subscribe(
        handleNewPrices
      );

    return () => {
      LivePriceFeed
        .unsubscribe(
          connectionId
        );
    };

  }, []);

  const portfolioValue =
    useMemo(() => {

      return calculateMassivePortfolioValue(
        transactions,
        livePrices,
        selectedCurrency
      );

    }, [
      transactions,
      livePrices,
      selectedCurrency
    ]);

  return (
    <CurrencyContext.Provider
      value={{
        selectedCurrency,
        setCurrency
      }}
    >
      <div
        style={{
          background:
            darkMode
              ? "#222"
              : "#fff",
          color:
            darkMode
              ? "#fff"
              : "#000",
          padding: "20px"
        }}
      >

        <h1>
          CoinStream Dashboard
        </h1>

        <button
          onClick={() =>
            setDarkMode(
              prev => !prev
            )
          }
        >
          Toggle Dark Mode
        </button>

        <select
          value={
            selectedCurrency
          }
          onChange={e =>
            setCurrency(
              e.target.value
            )
          }
        >
          <option value="USD">
            USD
          </option>
          <option value="EUR">
            EUR
          </option>
          <option value="JPY">
            JPY
          </option>
        </select>

        <DashboardContent
          portfolioValue={
            portfolioValue
          }
        />

      </div>
    </CurrencyContext.Provider>
  );
}