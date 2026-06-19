import React, {
  useState,
  useEffect,
} from "react";

export default function RadarMap() {
  const [pings, setPings] =
    useState([]);

  const [isStealthMode,
    setIsStealthMode] =
    useState(false);

  useEffect(() => {
    if (isStealthMode) {
      return;
    }

    const handlePing = event => {
      setPings(prev => [
        ...prev,
        {
          x: event.clientX,
          y: event.clientY,
        },
      ]);
    };

    window.addEventListener(
      "click",
      handlePing
    );

    return () => {
      window.removeEventListener(
        "click",
        handlePing
      );
    };
  }, [isStealthMode]);

  return (
    <div
      style={{
        width: "100vw",
        height: "100vh",
        position: "relative",
        background: "#001f3f",
        color: "white",
      }}
    >
      <h1>
        Sonar Ping Tracker
      </h1>

      <button
        onClick={() =>
          setIsStealthMode(
            prev => !prev
          )
        }
      >
        {isStealthMode
          ? "Disable Stealth"
          : "Enable Stealth"}
      </button>

      <h3>
        Mode:
        {isStealthMode
          ? " STEALTH"
          : " ACTIVE"}
      </h3>

      {pings.map((ping, index) => (
        <div
          key={`${ping.x}-${ping.y}-${index}`}
          style={{
            position: "absolute",
            left: ping.x,
            top: ping.y,
            width: "10px",
            height: "10px",
            borderRadius: "50%",
            background: "red",
          }}
        />
      ))}
    </div>
  );
}