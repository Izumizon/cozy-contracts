# Changelog

## 0.1.0-alpha - Release Candidate

### Added

- Forge 1.20.1 project foundation using Java 17
- Community Board block and GUI
- Favour Token item
- Daily request contracts
- JSON/datapack contract loading
- Built-in vanilla contract pool
- Optional Farmer's Delight board contracts
- Optional Create: Food board contracts
- Persistent Community Board block entity state
- Active and completed contract tracking per board
- Full-inventory GUI and command request submission
- Shift-right-click held-item request submission
- Favour Token reward shop
- Category-ready contract and shop metadata
- Category-aware shop stock foundation
- Settlement Foundation Lite
- Community Kitchen tab
- JSON/datapack Kitchen order loading
- Basic Kitchen deliveries with per-board daily limits
- Optional Farmer's Delight Kitchen orders
- Scrollable Kitchen UI with compact rows and hover details
- Debug commands for contracts, Kitchen orders, shops, boards, and settlements
- Optional development runtime toggles for Farmer's Delight, Create: Food, and JEI
- Community Board model, texture, crafting recipe, and interaction polish
- Contract and Kitchen order authoring documentation
- 0.1.0-alpha release notes

### Fixed

- Fixed Community Board recipe ingredient using an empty `minecraft:sticks` tag; the recipe now uses `minecraft:stick`.
- Fixed shop reward delivery when player inventories are full by dropping leftover purchased rewards.
- Fixed sneaking board interaction so held food and placeable blocks are not consumed by vanilla behavior.
- Fixed Kitchen refresh behavior so deliveries keep the player on the Kitchen tab.

### Known Limitations

- Alpha balance may change.
- Settlement Foundation Lite is present, but full districts and progression are future systems.
- Community Kitchen is basic and does not yet include residents or taste preferences.
- Create: Food support is board contracts only for now.
- Shop stock is not fully district-driven yet.
- Prosperity, Storehouse, Village Bond, Community Projects, Village Network, Resident Profiles, and Taste Preferences are not implemented yet.
