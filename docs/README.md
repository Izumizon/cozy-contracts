# Cozy Contracts

Cozy Contracts is a cozy Forge 1.20.1 Minecraft mod where villages post daily community requests. Players complete cooking, farming, building, decorating, and gathering requests to earn Favour Tokens.

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
- Right-click read-only Community Board GUI
- Shift-right-click held-item submission
- `/cozycontracts submit <slot>`
- `/cc submit <slot>`

## Data-Driven Contracts

Contracts are loaded from JSON resources and datapacks. Built-in definitions live under `data/cozy_contracts/contracts/`, and datapacks can add contracts under their own namespaces. See [docs/CONTRACT_FORMAT.md](docs/CONTRACT_FORMAT.md) for the authoring format.

## Documentation

- [Development Log](docs/DEVELOPMENT_LOG.md)
- [Contract JSON Format](docs/CONTRACT_FORMAT.md)
- [Project Roadmap](docs/ROADMAP.md)

## Planned Features

- GUI submit buttons
- Better Community Board visuals
- Farmer's Delight and Create: Food compatibility
- Themed village shops
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
