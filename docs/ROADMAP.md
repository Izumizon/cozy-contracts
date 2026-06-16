# Cozy Contracts Roadmap

## Current Prototype

Cozy Contracts currently has a complete first gameplay loop:

Players can open a Community Board, view daily contracts, submit requested items, earn Favour Tokens, and spend those tokens in a basic reward shop.

## Current Prototype Features

* Community Board block
* Favour Token item
* Data-driven JSON/datapack contract loading
* 14 built-in vanilla JSON contracts
* Three active daily contracts per board
* Weighted daily contract selection
* Persistent active contract IDs on the Community Board block entity
* Persistent completed contract IDs on the Community Board block entity
* Daily completion tracking
* Community Board GUI
* Full-inventory GUI request submission
* Shift-right-click held-item submission
* Command-based submission
* Debug commands for boards and contracts
* Basic Favour Token reward shop
* Category-ready shop item data
* Settlement Foundation Lite
* Improved Community Board model, texture, and GUI presentation
* Development log and documentation

## Design Direction

The long-term direction is no longer just a simple contract board.

Cozy Contracts is becoming a village-building and community economy mod where the Community Board acts as the interface to a larger settlement system.

Long-term systems should support:

* settlements instead of isolated boards
* multiple nearby boards sharing one settlement
* districts such as Farming, Kitchen, Builder, Mining, Scholar, Decorator, Market, Hunter, and Fishing
* Prosperity as a positive buff system, not upkeep
* Community Supplies
* themed shops
* Village Bond
* Community Projects
* player-founded villages
* Capital Villages
* Village Network progression

The key design rule is:

Do not build the full village system now, but do not code the MVP in a way that blocks it later.

## Next Steps

### 1. Themed Shop Foundation

Begin separating shop stock by category.

Early version:

* Universal stock is always available.
* Category stock exists internally but may not be fully used yet.
* Future settlement/district data can decide which category stock appears.

Do not add full rotating stock yet.

### 2. Farmer’s Delight Compatibility Contracts

Add JSON contracts for Farmer’s Delight using `required_mods`.

Goals:

* food contracts using Farmer’s Delight dishes
* kitchen/farming categories
* no hard dependency crash if Farmer’s Delight is missing
* contracts are skipped safely when the mod is not installed

### 3. Create: Food Compatibility Contracts

Add JSON contracts for Create: Food using `required_mods`.

Goals:

* food contracts using Create: Food items
* kitchen/farming categories
* safe skipping when the mod is missing

### 4. MVP Polish and Release Testing

Before sharing a first test jar:

* test in a fresh world
* test in Survival mode
* test save/reload
* test `/reload`
* test without extra mods
* test with Farmer’s Delight installed
* test with Create: Food installed if available
* test full inventory reward drops
* check screenshots
* update README
* update development log
* build final jar

## Future Features

These are planned future systems, not immediate MVP tasks.

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

Districts should affect contracts, shop stock, services, rewards, and future Community Projects.

### Prosperity

Prosperity should be a positive buff system.

Villages should never decay, lose progress, or punish the player for taking a break.

Community Supplies should improve Prosperity and provide bonuses such as:

* faster services
* better shop stock
* better rare stock chance
* stronger rewards
* improved project support

### Village Bond

Village Bond should represent the player’s relationship with a settlement.

It can later unlock:

* better shop items
* larger projects
* more contracts
* village services
* rare rewards
* district upgrades

### Community Projects

Community Projects should become the main way villages grow.

Players should choose where growth happens.

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

### Manual and Assisted Building

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

Specialised settlements can support larger settlements or Capital Villages.

The network should encourage exploration and long-term world progression without becoming an industrial automation system.

### Capital Villages

Capital Villages should be expensive endgame settlements with multiple districts and strong services.

They should be powerful, but require significant support through projects, Prosperity, and linked villages.

## Not Planned for the Current MVP

Do not add these yet:

* full Prosperity system
* Village Bond progression
* Community Projects
* Village Network
* Capital Villages
* work crews
* automatic building placement
* strict house schematics
* complex villager AI
* full rotating shop stock
* shop JSON loading
* major balance pass

These systems should stay documented, but they should not be rushed into the current prototype.
