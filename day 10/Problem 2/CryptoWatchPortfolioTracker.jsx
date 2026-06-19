import React, { useState } from "react";

function AssetRow({
  asset,
  onBuy,
}) {
  return (
    <div
      style={{
        border: "1px solid gray",
        margin: "10px",
        padding: "10px",
      }}
    >
      <h3>{asset.name}</h3>

      <p>
        Amount: {asset.amount}
      </p>

      <p>
        Price: $
        {asset.price.toLocaleString()}
      </p>

      <button
        onClick={() =>
          onBuy(asset.id)
        }
      >
        Buy 1
      </button>
    </div>
  );
}

export default function Portfolio() {
  const [assets, setAssets] =
    useState([
      {
        id: "btc",
        name: "Bitcoin",
        amount: 0,
        price: 50000,
      },
      {
        id: "eth",
        name: "Ethereum",
        amount: 0,
        price: 3000,
      },
      {
        id: "sol",
        name: "Solana",
        amount: 0,
        price: 150,
      },
    ]);

  const handleBuy = assetId => {
    setAssets(prevAssets =>
      prevAssets.map(asset =>
        asset.id === assetId
          ? {
              ...asset,
              amount:
                asset.amount + 1,
            }
          : asset
      )
    );
  };

  const totalPortfolioValue =
    assets.reduce(
      (total, asset) =>
        total +
        asset.amount *
          asset.price,
      0
    );

  return (
    <div>
      <h1>
        CryptoWatch Portfolio
      </h1>

      <h2>
        Total Portfolio Value:
        $
        {totalPortfolioValue.toLocaleString()}
      </h2>

      {assets.map(asset => (
        <AssetRow
          key={asset.id}
          asset={asset}
          onBuy={handleBuy}
        />
      ))}
    </div>
  );
}