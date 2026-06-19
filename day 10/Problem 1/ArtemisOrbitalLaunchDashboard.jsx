import React, { useState, useEffect } from "react";

function DashboardPanel({ title, children }) {
  return (
    <div
      style={{
        border: "2px solid #444",
        padding: "15px",
        margin: "15px",
        borderRadius: "10px",
      }}
    >
      <h2>{title}</h2>
      {children}
    </div>
  );
}

function TelemetrySubsystem({
  fuelLevel,
  onAbortSequence,
}) {
  return (
    <DashboardPanel title="Telemetry Subsystem">
      <h3>Fuel Level: {fuelLevel}%</h3>

      {fuelLevel < 20 && (
        <h1 className="alert">
          CRITICAL FUEL
        </h1>
      )}

      <button onClick={onAbortSequence}>
        Manual Abort
      </button>
    </DashboardPanel>
  );
}

export default function LaunchCommander() {
  const [countdown, setCountdown] =
    useState(10);

  const [isAborted, setIsAborted] =
    useState(false);

  const [fuelLevel, setFuelLevel] =
    useState(100);

  useEffect(() => {
    if (isAborted) {
      return;
    }

    const interval = setInterval(() => {
      setCountdown(prev =>
        prev > 0 ? prev - 1 : 0
      );

      setFuelLevel(prev =>
        prev > 0 ? prev - 5 : 0
      );
    }, 1000);

    return () => {
      clearInterval(interval);
    };
  }, [isAborted]);

  const handleAbort = () => {
    setIsAborted(true);
  };

  return (
    <DashboardPanel
      title="Artemis Launch Commander"
    >
      <h1>
        {isAborted
          ? "MISSION ABORTED"
          : `T - ${countdown}`}
      </h1>

      <TelemetrySubsystem
        fuelLevel={fuelLevel}
        onAbortSequence={handleAbort}
      />
    </DashboardPanel>
  );
}