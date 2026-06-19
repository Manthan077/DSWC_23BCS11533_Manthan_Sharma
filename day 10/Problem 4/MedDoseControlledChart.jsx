import React, { useState } from "react";

function DosageInput({
  currentDose,
  onDoseChange,
  isOverLimit,
}) {
  return (
    <div
      style={{
        border: isOverLimit
          ? "2px solid red"
          : "2px solid green",
        padding: "15px",
        margin: "15px",
      }}
    >
      <label>
        Dosage (mg):
      </label>

      <input
        type="number"
        value={currentDose}
        onChange={e =>
          onDoseChange(
            Number(e.target.value)
          )
        }
        disabled={isOverLimit}
      />
    </div>
  );
}

export default function MedicalChart() {
  const [currentDose,
    setCurrentDose] =
    useState(0);

  const isOverLimit =
    currentDose > 500;

  return (
    <div>
      <h1>
        MedDose Controlled Chart
      </h1>

      <DosageInput
        currentDose={currentDose}
        onDoseChange={setCurrentDose}
        isOverLimit={isOverLimit}
      />

      {isOverLimit ? (
        <h2
          style={{
            color: "red",
          }}
        >
          WARNING:
          Maximum safe dosage
          exceeded!
        </h2>
      ) : (
        <h2
          style={{
            color: "green",
          }}
        >
          Dosage within safe limit
        </h2>
      )}
    </div>
  );
}