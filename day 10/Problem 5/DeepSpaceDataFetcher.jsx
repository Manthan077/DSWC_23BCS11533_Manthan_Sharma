import React,
{
  useState,
  useEffect
} from "react";

export default function DeepSpaceDataFetcher() {

  const [activePlanet,
    setActivePlanet] =
    useState("Mars");

  const [data,
    setData] =
    useState(null);

  const [isLoading,
    setIsLoading] =
    useState(false);

  const [error,
    setError] =
    useState("");

  useEffect(() => {

    let isCurrentRequest = true;

    const fetchPlanetData =
      async () => {

        try {

          setIsLoading(true);
          setError("");

          const response =
            await fetch(
              `https://api.example.com/planets/${activePlanet}`
            );

          if (!response.ok) {
            throw new Error(
              "Failed to fetch data"
            );
          }

          const result =
            await response.json();

          if (isCurrentRequest) {

            setData(result);
          }

        } catch (err) {

          if (isCurrentRequest) {

            setError(
              err.message
            );
          }

        } finally {

          if (isCurrentRequest) {

            setIsLoading(false);
          }
        }
      };

    fetchPlanetData();

    return () => {

      isCurrentRequest = false;
    };

  }, [activePlanet]);

  return (
    <div>

      <h1>
        Deep Space Encyclopedia
      </h1>

      <button
        onClick={() =>
          setActivePlanet(
            "Mars"
          )
        }
      >
        Mars
      </button>

      <button
        onClick={() =>
          setActivePlanet(
            "Jupiter"
          )
        }
      >
        Jupiter
      </button>

      <button
        onClick={() =>
          setActivePlanet(
            "Saturn"
          )
        }
      >
        Saturn
      </button>

      <hr />

      {isLoading && (
        <h2>
          Loading...
        </h2>
      )}

      {error && (
        <h2
          style={{
            color: "red"
          }}
        >
          Error:
          {error}
        </h2>
      )}

      {!isLoading &&
        !error &&
        data && (
          <div>

            <h2>
              Planet:
              {activePlanet}
            </h2>

            <pre>
              {JSON.stringify(
                data,
                null,
                2
              )}
            </pre>

          </div>
        )}
    </div>
  );
}