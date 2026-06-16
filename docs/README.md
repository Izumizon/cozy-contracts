# Cozy Contracts

Cozy Contracts is a cozy Forge 1.20.1 Minecraft mod where villages post daily community requests. Players complete cooking, farming, building, decorating, and gathering requests to earn Favour Tokens.

Long term, Cozy Contracts is planned to grow into a settlement-focused community economy mod with districts, themed shops, Community Kitchen systems, Prosperity, Community Projects, and resident food preferences.

## Status

Early prototype. Cozy Contracts has not been released yet.

## Current Prototype Features

- Community Board block
- Favour Token item
- 14 built-in vanilla JSON contracts
- Three active daily contracts per board
- Deterministic weighted selection based on board position and Minecraft day
- Persisted active contract IDs on the Community Board block entity
- Persisted completed contract IDs on the Community Board block entity
- Data-driven JSON/datapack contract loading
- Improved wooden Community Board model and polished GUI
- Community Board GUI with full-inventory request submission
- Favour Token reward shop with vanilla rewards
- Category-ready shop item metadata
- Settlement identity foundation for Community Boards
- Optional Farmer's Delight JSON contract support
- Shift-right-click held-item submission
- Full-inventory `/cozycontracts submit <slot>` and `/cc submit <slot>` commands

## Data-Driven Contracts

Contracts are loaded from JSON resources and datapacks. Built-in definitions live under `data/cozy_contracts/contracts/`, and datapacks can add contracts under their own namespaces. See [docs/CONTRACT_FORMAT.md](CONTRACT_FORMAT.md) for the authoring format.

## Documentation

- [Development Log](DEVELOPMENT_LOG.md)
- [Contract JSON Format](CONTRACT_FORMAT.md)
- [Project Roadmap](ROADMAP.md)
- [Village System Design](VILLAGE_SYSTEM_DESIGN.md)

## Planned Features

- Create: Food compatibility
- Themed village shops
- Community Kitchen and Standing Orders
- Resident Profiles and Taste Preferences
- Village Bond
- Community Projects
- Manual and assisted building
- Festivals

## Development Setup

Java 17 is required.

Build the project:

```powershell
.\gradlew.bat clean build
```

Launch the development client:

```powershell
.\gradlew.bat runClient
```
