import React,
{
  createContext,
  useContext,
  useState,
  useEffect,
  useMemo
} from "react";

const ProtocolContext =
  createContext();

function calculateAggregateRisk(
  patients
) {
  console.log(
    "Calculating Risk Score..."
  );

  return patients.reduce(
    (total, patient) =>
      total +
      patient.riskScore,
    0
  );
}

function PatientCard({
  patient
}) {

  const {
    hospitalCode
  } = useContext(
    ProtocolContext
  );

  return (
    <div
      className={
        hospitalCode ===
        "CODE_RED"
          ? "critical"
          : "normal"
      }
      style={{
        border:
          "1px solid gray",
        margin: "10px",
        padding: "10px"
      }}
    >
      <h3>
        {patient.name}
      </h3>

      <p>
        Risk Score:
        {patient.riskScore}
      </p>

      <p>
        Protocol:
        {hospitalCode}
      </p>
    </div>
  );
}

export default function TriageDashboard() {

  const [
    nurseNotes,
    setNurseNotes
  ] = useState("");

  const [
    hospitalCode
  ] = useState(
    "CODE_RED"
  );

  const [
    patients,
    setPatients
  ] = useState([
    {
      id: 1,
      name: "Patient A",
      riskScore: 85
    },
    {
      id: 2,
      name: "Patient B",
      riskScore: 60
    },
    {
      id: 3,
      name: "Patient C",
      riskScore: 40
    }
  ]);

  useEffect(() => {

    const timer =
      setInterval(() => {

        setPatients(
          prev => [...prev]
        );

      }, 5000);

    return () => {
      clearInterval(
        timer
      );
    };

  }, []);

  const totalRiskScore =
    useMemo(() => {

      return calculateAggregateRisk(
        patients
      );

    }, [patients]);

  return (
    <ProtocolContext.Provider
      value={{
        hospitalCode
      }}
    >
      <div>

        <h1>
          MedFlow Triage Board
        </h1>

        <h2>
          Total Risk Score:
          {totalRiskScore}
        </h2>

        <textarea
          placeholder="Nurse Notes"
          value={nurseNotes}
          onChange={e =>
            setNurseNotes(
              e.target.value
            )
          }
        />

        <h3>
          Notes:
          {nurseNotes}
        </h3>

        {patients.map(
          patient => (
            <PatientCard
              key={
                patient.id
              }
              patient={
                patient
              }
            />
          )
        )}

      </div>
    </ProtocolContext.Provider>
  );
}