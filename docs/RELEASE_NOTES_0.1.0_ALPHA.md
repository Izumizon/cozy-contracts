# Cozy Contracts 0.1.0-alpha

Cozy Contracts 0.1.0-alpha is the first playable alpha of the mod. It includes the Community Board, daily request contracts, Favour Tokens, a reward shop, Community Kitchen deliveries, JSON/datapack support, and optional Farmer's Delight and Create: Food content.

This is an early alpha release candidate. The core loop is playable, but balance and content may change in later versions.

## Current Features

- Community Board block and GUI
- Three active daily contracts per board
- Daily completion tracking
- Persistent board state for active and completed contracts
- Full-inventory request submission from the GUI and commands
- Shift-right-click held-item hand-in
- Favour Tokens
- Basic reward shop
- JSON/datapack contract loading
- Built-in vanilla contract pool
- Community Kitchen tab
- Basic Kitchen deliveries with daily caps
- JSON/datapack Kitchen order loading
- Scrollable Kitchen UI with compact rows and hover details
- Settlement Foundation Lite
- Category-ready contract and shop metadata
- Category-aware shop stock foundation
- Debug commands for development and testing

## Optional Mod Support

Cozy Contracts can run standalone with no optional mods.

Optional content is skipped safely when its required mod is missing.

- Farmer's Delight adds optional board contracts and Kitchen orders.
- Create: Food adds optional board contracts.
- Create is required when using Create: Food.
- JEI is optional and useful for recipe viewing.

Create: Food Kitchen orders are not included yet and may be added in a future update.

## Installation

1. Install Minecraft Forge for Minecraft 1.20.1.
2. Place the Cozy Contracts jar in your `mods` folder.
3. Launch the game with Forge.

Optional setup:

- Add Farmer's Delight for Farmer's Delight contracts and Kitchen orders.
- Add Create and Create: Food for Create: Food contracts.
- Add JEI for recipe viewing.

Do not install optional mods unless you want their optional content.

## Known Limitations

- Alpha build; balance and content may change.
- Community Kitchen is basic and does not yet include residents or taste preferences.
- Settlement Foundation Lite exists, but full districts and progression are not implemented.
- Shop stock is not fully district-driven yet.
- Create: Food support is board contracts only for now.
- No advanced Prosperity system yet.
- No Storehouse yet.
- No Village Bond yet.
- No Community Projects yet.
- No Village Network yet.
- No Resident Profiles, Taste Preferences, Resident Memories, or Village Registry yet.

## Testing Checklist

Recommended release testing:

- Launch standalone Forge 1.20.1 with only Cozy Contracts.
- Craft the Community Board.
- Place and open the Community Board.
- Complete a normal request.
- Confirm Favour Tokens are rewarded.
- Buy an item from the reward shop.
- Complete a Kitchen delivery.
- Save and reload the world.
- Confirm completed board and Kitchen state persists for the same Minecraft day.
- Test with Farmer's Delight installed.
- Test with Create and Create: Food installed.
- Test with Farmer's Delight, Create, Create: Food, and JEI installed.
- Confirm optional contracts are skipped safely when optional mods are missing.

## Future Plans

Likely future work includes:

- Create: Food Kitchen orders
- advanced Community Kitchen progression
- advanced Standing Orders
- resident meal deliveries
- Resident Profiles and Taste Preferences
- district-aware contracts and shops
- Prosperity and Storehouse support
- Village Bond
- Community Projects
- Village Network and Capital Villages
- festivals
