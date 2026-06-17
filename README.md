# Cozy Contracts

Cozy Contracts is a cozy Forge 1.20.1 Minecraft mod where villages post daily community requests. Players complete requests, earn Favour Tokens, and spend those tokens through the Community Board.

## Status

0.1.0-alpha release candidate. Cozy Contracts is playable, but still in early development.

## Highlights

- Community Board block and GUI
- Daily request contracts
- Favour Tokens and reward shop
- Community Kitchen deliveries
- JSON/datapack contracts
- JSON/datapack Kitchen orders
- Optional Farmer's Delight contracts and Kitchen orders
- Optional Create: Food board contracts
- Standalone play with no optional food mods required

## Documentation

Full project documentation lives in [`docs/README.md`](docs/README.md).

Useful links:

- [`docs/RELEASE_NOTES_0.1.0_ALPHA.md`](docs/RELEASE_NOTES_0.1.0_ALPHA.md)
- [`docs/ROADMAP.md`](docs/ROADMAP.md)
- [`docs/CONTRACT_FORMAT.md`](docs/CONTRACT_FORMAT.md)
- [`docs/KITCHEN_ORDER_FORMAT.md`](docs/KITCHEN_ORDER_FORMAT.md)
- [`docs/VILLAGE_SYSTEM_DESIGN.md`](docs/VILLAGE_SYSTEM_DESIGN.md)

## Development

Java 17 is required.

```powershell
.\gradlew.bat clean build
.\gradlew.bat runClient
```
