# Cozy Contracts

Cozy Contracts is a cozy Forge 1.20.1 Minecraft mod where villages post daily community requests. Players complete cooking, farming, building, decorating, and gathering requests to earn Favour Tokens, then spend those tokens on useful village rewards.

Long term, Cozy Contracts is planned to grow into a settlement-focused community economy mod with districts, themed shops, Community Kitchen systems, Prosperity, Community Projects, and resident food preferences.

## Status

0.1.0-alpha release candidate. Cozy Contracts is playable, but still in early development.

## Alpha Status

The first alpha focuses on a complete core loop:

```text
Open Community Board -> complete requests -> earn Favour Tokens -> spend tokens
```

Balance, rewards, shop stock, contract pools, Kitchen orders, and future settlement systems may change during alpha development.

## Installation

1. Install Minecraft Forge for Minecraft 1.20.1.
2. Place the built Cozy Contracts jar in your `mods` folder.
3. Launch the game with Forge.

Optional mods are supported, but not required:

- Install Farmer's Delight to enable Farmer's Delight board contracts and Kitchen orders.
- Install Create and Create: Food to enable Create: Food board contracts.
- Install JEI if you want recipe viewing while playing or testing.

Cozy Contracts can run standalone with no optional food mods. Optional content is skipped safely when its required mod is missing.

## Current Features

- Community Board block and GUI
- Three active daily contracts per board
- Favour Token item and reward shop
- Full-inventory request submission from the GUI and commands
- Shift-right-click held-item hand-in
- Persistent active and completed board state
- JSON/datapack contract loading
- 14 built-in vanilla contracts
- Optional Farmer's Delight board contracts
- Optional Create: Food board contracts
- Community Kitchen tab with basic deliveries and daily caps
- JSON/datapack Kitchen order loading
- Optional Farmer's Delight Kitchen orders
- Scrollable Kitchen UI with compact rows and hover details
- Category-ready contract and shop data
- Category-aware shop stock foundation
- Settlement Foundation Lite for board settlement identity
- Debug commands for boards, contracts, Kitchen orders, shops, and settlements
- Improved wooden Community Board model and recipe

## Optional Mod Support

Farmer's Delight, Create: Food, Create, and JEI are optional. They are not mandatory dependencies for Cozy Contracts.

Farmer's Delight currently adds:

- Optional normal Community Board contracts
- Optional Community Kitchen orders

Create: Food currently adds:

- Optional normal Community Board contracts

Create: Food Kitchen orders are a possible future expansion. JEI is useful for recipe viewing, but Cozy Contracts does not require it.

## Known Limitations

- This is an alpha build; balance may change.
- The Community Kitchen is basic and does not yet include residents or taste preferences.
- Settlement Foundation Lite exists, but full districts and settlement progression are not implemented.
- Shop stock is category-ready, but not fully district-driven yet.
- Create: Food support is board contracts only for now.
- Advanced Prosperity, Storehouse, Village Bond, Community Projects, and Village Network systems are not implemented yet.
- Resident Profiles, Taste Preferences, Resident Memories, and Village Registry are future systems.

## Development Setup

Java 17 is required for development.

Build the project:

```powershell
.\gradlew.bat clean build
```

Launch the standalone development client:

```powershell
.\gradlew.bat runClient
```

Optional development runtime helpers can be enabled with Gradle properties. Do not also place those helper jars manually in `run/mods` when using these toggles.

```powershell
.\gradlew.bat runClient
.\gradlew.bat runClient -PenableFarmersDelight=true
.\gradlew.bat runClient -PenableCreateFood=true
.\gradlew.bat runClient -PenableJei=true
.\gradlew.bat runClient -PenableFarmersDelight=true -PenableCreateFood=true -PenableJei=true
```

## Documentation

- [Development Log](DEVELOPMENT_LOG.md)
- [Contract JSON Format](CONTRACT_FORMAT.md)
- [Kitchen Order JSON Format](KITCHEN_ORDER_FORMAT.md)
- [Project Roadmap](ROADMAP.md)
- [Village System Design](VILLAGE_SYSTEM_DESIGN.md)
- [0.1.0-alpha Release Notes](RELEASE_NOTES_0.1.0_ALPHA.md)

## Planned Future Systems

- Create: Food Kitchen orders
- Themed village shops
- Advanced Community Kitchen and Standing Orders
- Resident Profiles and Taste Preferences
- Prosperity and Storehouse support
- Village Bond
- Community Projects
- Manual and assisted building
- Village Network and Capital Villages
- Festivals
