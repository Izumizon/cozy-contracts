# Cozy Contracts Roadmap

## Current Alpha

Cozy Contracts 0.2.0-alpha has a complete first gameplay loop:

```text
Open Community Board -> view daily requests -> submit items -> earn Favour Tokens -> buy rewards
```

The current alpha also includes the first playable Community Kitchen delivery loop. This is still early and intentionally simple, with vanilla, Farmer's Delight, and optional Create: Food orders using the same JSON order system.

## Current Alpha Features

* Community Board block
* Favour Token item
* Data-driven JSON/datapack contract loading
* 14 built-in vanilla JSON contracts
* Optional Farmer's Delight board contracts
* Optional Create: Food board contracts
* Optional Create: Food Kitchen orders
* Three active daily contracts per board
* Weighted daily contract selection
* Persistent active contract IDs on the Community Board block entity
* Persistent completed contract IDs on the Community Board block entity
* Daily completion tracking
* Community Board GUI
* Full-inventory GUI request submission
* Shift-right-click held-item submission
* Command-based submission
* Debug commands for boards, contracts, Kitchen orders, shops, and settlements
* Basic Favour Token reward shop
* Category-ready shop item data
* Category-ready contract data
* Category-aware shop stock foundation
* Settlement Foundation Lite
* Community Kitchen tab
* Basic Community Kitchen deliveries
* Basic Standing Orders as Kitchen order types
* JSON/datapack Kitchen order loading
* Optional Farmer's Delight Kitchen orders
* Scrollable Kitchen UI with compact rows and hover details
* Optional dev runtime toggles for Farmer's Delight, Create: Food, and JEI
* Improved Community Board model, texture, GUI, and crafting recipe
* Development log, design documentation, and release notes

## Design Direction

Cozy Contracts is becoming a settlement-focused community economy mod where the Community Board acts as the main interface to a larger village system.

Long-term systems should support:

* settlements instead of isolated boards
* multiple nearby boards sharing one settlement
* districts such as Farming, Kitchen, Builder, Mining, Scholar, Decorator, Market, Hunter, and Fishing
* themed village shops
* advanced Community Kitchen progression
* resident meal deliveries
* Resident Profiles and Taste Preferences
* Resident Memories
* Community Supplies and Storehouse
* Prosperity as a positive buff system, not upkeep
* Village Bond
* Community Projects
* player-founded villages
* Capital Villages
* Village Network progression
* festivals

The key design rule is:

Do not build the full village system now, but do not code the alpha in a way that blocks it later.

## Next Steps

### 1. MVP Release Testing

Verify the 0.2.0-alpha candidate in normal play conditions.

Goals:

* test a fresh world
* test Survival mode
* test save and reload
* test `/reload`
* test request completion and reward delivery
* test Kitchen deliveries and daily caps
* test shop purchases with normal and full inventories

### 2. Final Documentation And Release Notes

Prepare the release-facing documentation for players and pack makers.

Goals:

* finalize README wording
* update release notes
* verify known limitations
* verify optional mod support wording
* make sure future systems are clearly marked as future

### 3. Fresh Forge Instance Testing

Test the built jar outside the development workspace.

Recommended setups:

* standalone Forge 1.20.1
* Forge 1.20.1 with Farmer's Delight
* Forge 1.20.1 with Create and Create: Food
* Forge 1.20.1 with Farmer's Delight, Create, Create: Food, and JEI

### 4. GitHub Alpha Release

Prepare the GitHub release page and attach the alpha jar.

Recommended checks:

* jar filename
* version number
* release notes
* known limitations
* optional dependency notes
* screenshots or short clips if available

### 5. Modrinth / CurseForge Alpha Release

Publish on mod platforms if desired.

Recommended checks:

* loader and Minecraft version metadata
* optional dependencies marked correctly
* screenshots
* feature summary
* alpha status clearly visible

### 6. Later Systems

After the alpha, likely next work includes:

* advanced Community Kitchen progression
* advanced Standing Orders
* resident meal deliveries
* Resident Profiles and Taste Preferences
* district-aware contracts and shops
* Prosperity and Storehouse support
* Village Bond and Community Projects
* Village Network and Capital Villages

## Future Features

These are planned future systems, not part of the 0.2.0-alpha release.

### Districts

Villages can grow through multiple districts, such as:

* Farming District
* Kitchen District
* Builder District
* Mining District
* Scholar District
* Decorator District
* Market District
* Hunter District
* Fishing District

Districts should affect contracts, shop stock, services, rewards, future Community Projects, and future Prosperity support.

### Advanced Community Kitchen And Standing Orders

The current alpha has basic Kitchen deliveries and Standing Order order types. Future versions can expand this into a deeper cooking playstyle.

Possible future features:

* resident meal deliveries
* Feast Prep gameplay
* district meal support
* advanced Standing Orders
* Kitchen identity
* future Prosperity support
* deeper Farmer's Delight Kitchen support

### Resident Profiles And Taste Preferences

Residents can eventually have names, roles, homes, personalities, relationship levels, hidden Taste Profiles, and personal food preferences.

Players can discover loved dishes, liked food categories, favourite meal types, and disliked categories by cooking for residents. This should make food mods feel more personal and give cooking a stronger identity.

### Village Registry

A future UI, block, or board tab for tracking:

* residents
* homes
* roles
* relationship levels
* known preferences
* memories
* personal requests

### Resident Memories

Important events and loved meals can create memories.

Memories should make villages feel alive without requiring complex simulation.

### Community Supplies And Storehouse

Community Supplies can support districts and improve Prosperity.

A future Storehouse can help manage supplied items. It should make support easier, not create stressful upkeep.

### Prosperity

Prosperity should be a positive buff system.

Villages should never decay, lose progress, or punish the player for taking a break.

Community Supplies, food support, and projects can improve Prosperity and provide bonuses such as:

* faster services
* better shop stock
* better rare stock chance
* stronger rewards
* improved project support

### Village Bond

Village Bond should represent the player's relationship with a settlement.

It can later unlock:

* better shop items
* larger projects
* more contracts
* village services
* rare rewards
* district upgrades

### Community Projects

Community Projects should become the main way villages grow. Players should choose where growth happens.

Future project types may include:

* gardens
* kitchens
* markets
* wells
* paths
* houses
* workshops
* community hearths
* festival grounds

### Manual And Assisted Building

Both playstyles should be supported.

Manual building:

* player builds the structure themselves
* mod validates flexible requirements

Assisted building:

* player provides materials and Favour Tokens
* structure appears gradually or through a controlled system

The mod should not randomly place buildings without player control.

### Village Network

The Village Network should connect multiple settlements.

Specialized settlements can support larger settlements or Capital Villages. The network should encourage exploration and long-term world progression without becoming an industrial automation system.

### Capital Villages

Capital Villages should be expensive endgame settlements with multiple districts and strong services.

They should be powerful, but require significant support through projects, Prosperity, and linked villages.

### Festivals

Festivals can become future village events tied to seasons, food, districts, residents, Prosperity, and Community Projects.

## Not Planned For The Current MVP

Do not add these yet:

* full Prosperity system
* advanced Community Kitchen progression
* resident meal deliveries
* Feast Prep gameplay
* advanced Standing Orders
* district-aware Kitchen support
* Resident Profiles
* Taste Preferences
* Resident Memories
* Village Registry
* Village Bond progression
* Community Projects
* Village Network
* Capital Villages
* work crews
* automatic building placement
* strict house schematics
* complex villager behavior
* full rotating shop stock
* shop JSON loading
* major balance pass

These systems should stay documented, but they should not be rushed into the current alpha.
