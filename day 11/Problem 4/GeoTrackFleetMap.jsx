import React,
{
  createContext,
  useContext,
  useState,
  useEffect,
  useMemo
} from "react";

const MetricsContext =
  createContext();

function calculateBoundingBox(
  trucks
) {
  console.log(
    "Calculating Bounding Box..."
  );

  if (trucks.length === 0) {
    return null;
  }

  const latitudes =
    trucks.map(
      truck => truck.lat
    );

  const longitudes =
    trucks.map(
      truck => truck.lng
    );

  return {
    minLat:
      Math.min(...latitudes),
    maxLat:
      Math.max(...latitudes),
    minLng:
      Math.min(...longitudes),
    maxLng:
      Math.max(...longitudes)
  };
}

function TruckTooltip({
  truck
}) {

  const { unit } =
    useContext(
      MetricsContext
    );

  return (
    <div
      style={{
        border:
          "1px solid gray",
        margin: "5px",
        padding: "5px"
      }}
    >
      <p>
        Truck:
        {truck.id}
      </p>

      <p>
        Speed:
        {truck.speed}
        {unit}
      </p>
    </div>
  );
}

export default function FleetMap() {

  const [unit] =
    useState("KM");

  const [
    zoomLevel,
    setZoomLevel
  ] = useState(5);

  const [
    trucks
  ] = useState([
    {
      id: 1,
      lat: 28.61,
      lng: 77.20,
      speed: 60
    },
    {
      id: 2,
      lat: 19.07,
      lng: 72.87,
      speed: 55
    },
    {
      id: 3,
      lat: 13.08,
      lng: 80.27,
      speed: 70
    }
  ]);

  useEffect(() => {

    const handleKeyDown =
      event => {

        if (
          event.key === "c" ||
          event.key === "C"
        ) {

          console.log(
            "Centering Map..."
          );
        }
      };

    window.addEventListener(
      "keydown",
      handleKeyDown
    );

    return () => {

      window.removeEventListener(
        "keydown",
        handleKeyDown
      );
    };

  }, []);

  const boundingBox =
    useMemo(() => {

      return calculateBoundingBox(
        trucks
      );

    }, [trucks]);

  return (
    <MetricsContext.Provider
      value={{ unit }}
    >
      <div>

        <h1>
          GeoTrack Fleet Map
        </h1>

        <label>
          Zoom Level:
          {zoomLevel}
        </label>

        <input
          type="range"
          min="1"
          max="20"
          value={zoomLevel}
          onChange={e =>
            setZoomLevel(
              Number(
                e.target.value
              )
            )
          }
        />

        <h3>
          Bounding Box
        </h3>

        <pre>
          {JSON.stringify(
            boundingBox,
            null,
            2
          )}
        </pre>

        {trucks.map(
          truck => (
            <TruckTooltip
              key={truck.id}
              truck={truck}
            />
          )
        )}

      </div>
    </MetricsContext.Provider>
  );
}