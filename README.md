# Cozy Contracts

Cozy Contracts is a cozy Forge 1.20.1 Minecraft mod where villages post daily community requests. Players complete requests, earn Favour Tokens, and spend those tokens through the Community Board.

## Status

0.3.0-alpha. Cozy Contracts is playable, but still in early development.

## Highlights

- Community Board block and GUI
- Daily request contracts
- Favour Tokens and a settlement supply-bundle shop
- Community Kitchen deliveries
- JSON/datapack contracts
- JSON/datapack Kitchen orders
- Optional Farmer's Delight contracts and Kitchen orders
- Optional Create: Food board contracts and Kitchen orders
- Standalone play with no optional food mods required

## Documentation

Full project documentation lives in [docs/README.md](docs/README.md).

Useful links:

- [0.3.0-alpha Release Notes](docs/RELEASE_NOTES_0.3.0_ALPHA.md)
- [0.2.0-alpha Release Notes](docs/RELEASE_NOTES_0.2.0_ALPHA.md)
- [0.1.0-alpha Release Notes](docs/RELEASE_NOTES_0.1.0_ALPHA.md)
- [Project Roadmap](docs/ROADMAP.md)
- [Development Diary](docs/DEVELOPMENT_DIARY.md)
- [Contract JSON Format](docs/CONTRACT_FORMAT.md)
- [Kitchen Order JSON Format](docs/KITCHEN_ORDER_FORMAT.md)
- [Village System Design](docs/VILLAGE_SYSTEM_DESIGN.md)

## Development

Java 17 is required.

~~~powershell
.\gradlew.bat clean build
.\gradlew.bat runClient
.\gradlew.bat runClient -PenableCreateFood=true
~~~
