import React,
{
  createContext,
  useContext,
  useState,
  useEffect,
  useMemo
} from "react";

const RegionContext =
  createContext();

function sortResultsByRelevance(
  results
) {
  console.log(
    "Sorting Results..."
  );

  return [...results].sort(
    (a, b) =>
      b.relevanceScore -
      a.relevanceScore
  );
}

function ResultItem({
  item
}) {

  const { region } =
    useContext(
      RegionContext
    );

  const currency =
    region === "EU"
      ? "€"
      : "$";

  return (
    <div
      style={{
        border:
          "1px solid gray",
        margin: "10px",
        padding: "10px"
      }}
    >
      <h3>
        {item.name}
      </h3>

      <p>
        Price:
        {currency}
        {item.price}
      </p>

      <p>
        Score:
        {item.relevanceScore}
      </p>
    </div>
  );
}

export default function OmniSearch() {

  const [region] =
    useState("EU");

  const [
    searchTerm,
    setSearchTerm
  ] = useState("");

  const [
    results,
    setResults
  ] = useState([]);

  const [
    isFilterMenuOpen,
    setIsFilterMenuOpen
  ] = useState(false);

  useEffect(() => {

    if (!searchTerm) {
      setResults([]);
      return;
    }

    const controller =
      new AbortController();

    const fetchResults =
      async () => {

        try {

          const response =
            await fetch(
              `https://api.example.com/search?q=${searchTerm}`,
              {
                signal:
                  controller.signal
              }
            );

          const data =
            await response.json();

          setResults(data);

        } catch (error) {

          if (
            error.name !==
            "AbortError"
          ) {

            console.log(
              error
            );
          }
        }
      };

    fetchResults();

    return () => {

      controller.abort();
    };

  }, [searchTerm]);

  const sortedResults =
    useMemo(() => {

      return sortResultsByRelevance(
        results
      );

    }, [results]);

  return (
    <RegionContext.Provider
      value={{
        region
      }}
    >
      <div>

        <h1>
          OmniSearch
        </h1>

        <input
          type="text"
          placeholder="Search..."
          value={
            searchTerm
          }
          onChange={e =>
            setSearchTerm(
              e.target.value
            )
          }
        />

        <button
          onClick={() =>
            setIsFilterMenuOpen(
              prev => !prev
            )
          }
        >
          Toggle Filters
        </button>

        <h3>
          Filters:
          {isFilterMenuOpen
            ? " Open"
            : " Closed"}
        </h3>

        {sortedResults.map(
          item => (
            <ResultItem
              key={item.id}
              item={item}
            />
          )
        )}

      </div>
    </RegionContext.Provider>
  );
}