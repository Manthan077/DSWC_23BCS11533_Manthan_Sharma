import React,
{
  createContext,
  useContext,
  useState,
  useEffect,
  useMemo
} from "react";

const PlaybackContext =
  createContext();

function generateWaveformVisuals(
  buffers
) {
  console.log(
    "Generating Waveform..."
  );

  return buffers.map(
    buffer => ({
      id: buffer.id,
      waveform:
        `Waveform-${buffer.id}`
    })
  );
}

function AudioTrack({
  track
}) {

  const { isMuted } =
    useContext(
      PlaybackContext
    );

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
        {track.name}
      </h3>

      {isMuted ? (
        <p
          style={{
            color: "red"
          }}
        >
          MUTED
        </p>
      ) : (
        <p>
          Playing
        </p>
      )}
    </div>
  );
}

export default function MixingBoard() {

  const [
    isMuted,
    setIsMuted
  ] = useState(false);

  const [
    panBalance,
    setPanBalance
  ] = useState(0);

  const [
    tracks
  ] = useState([
    {
      id: 1,
      name: "Drums"
    },
    {
      id: 2,
      name: "Bass"
    },
    {
      id: 3,
      name: "Guitar"
    }
  ]);

  const [
    buffers
  ] = useState([
    { id: 1 },
    { id: 2 },
    { id: 3 }
  ]);

  useEffect(() => {

    document.title =
      `Tracks: ${tracks.length}`;

  }, [tracks.length]);

  const waveformData =
    useMemo(() => {

      return generateWaveformVisuals(
        buffers
      );

    }, [buffers]);

  return (
    <PlaybackContext.Provider
      value={{
        isMuted
      }}
    >
      <div>

        <h1>
          SoundWave Audio Mixer
        </h1>

        <button
          onClick={() =>
            setIsMuted(
              prev => !prev
            )
          }
        >
          {isMuted
            ? "Unmute"
            : "Mute"}
        </button>

        <br />
        <br />

        <label>
          Pan Balance:
          {panBalance}
        </label>

        <input
          type="range"
          min="-100"
          max="100"
          value={panBalance}
          onChange={e =>
            setPanBalance(
              Number(
                e.target.value
              )
            )
          }
        />

        <h2>
          Waveform Data
        </h2>

        <pre>
          {JSON.stringify(
            waveformData,
            null,
            2
          )}
        </pre>

        {tracks.map(
          track => (
            <AudioTrack
              key={track.id}
              track={track}
            />
          )
        )}

      </div>
    </PlaybackContext.Provider>
  );
}