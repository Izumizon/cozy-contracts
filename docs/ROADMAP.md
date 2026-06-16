# Cozy Contracts Roadmap

## Current Prototype

Cozy Contracts currently has a complete first gameplay loop:

Players can open a Community Board, view daily contracts, submit requested items, earn Favour Tokens, and spend those tokens in a basic reward shop.

## Current Prototype Features

* Community Board block
* Favour Token item
* Data-driven JSON/datapack contract loading
* 14 built-in vanilla JSON contracts
* Optional Farmer's Delight JSON contracts
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
* Category-ready contract data
* Settlement Foundation Lite
* Improved Community Board model, texture, and GUI presentation
* Development log and design documentation

## Design Direction

The long-term direction is no longer just a simple contract board.

Cozy Contracts is becoming a settlement-focused community economy mod where the Community Board acts as the main interface to a larger village system.

Long-term systems should support:

* settlements instead of isolated boards
* multiple nearby boards sharing one settlement
* districts such as Farming, Kitchen, Builder, Mining, Scholar, Decorator, Market, Hunter, and Fishing
* themed village shops
* Community Kitchen and Standing Orders
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

Community Kitchen and Resident Profiles are future systems, not immediate MVP requirements.

The key design rule is:

Do not build the full village system now, but do not code the MVP in a way that blocks it later.

## Next Steps

### 1. Themed Shop Foundation

Begin separating shop stock by category.

Early version:

* Universal stock is always available.
* Category stock exists internally.
* Future settlement and district data can decide which category stock appears.
* Shop data stays ready for themed village shops later.

Do not add full rotating stock yet.

### 2. Community Kitchen Design/Foundation

Define the first small version of the future cooking-focused system.

Goals:

* decide whether the first version starts as a board tab or separate block later
* document Daily Menu and Standing Orders
* keep cooking distinct from normal board contracts
* prepare for food mod integration without adding full resident simulation yet

Do not make this an immediate MVP requirement.

### 3. Create: Food Compatibility Contracts

Add JSON contracts for Create: Food using `required_mods`.

Goals:

* food contracts using Create: Food items
* kitchen and farming categories
* safe skipping when the mod is missing
* future compatibility with Community Kitchen systems

### 4. MVP Polish and Release Testing

Before sharing a first test jar:

* test in a fresh world
* test in Survival mode
* test save/reload
* test `/reload`
* test without extra mods
* test with Farmer's Delight installed
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

Districts should affect contracts, shop stock, services, rewards, future Community Projects, and future Prosperity support.

### Community Kitchen and Standing Orders

A future cooking-focused system where players can deliver meals outside normal daily contracts.

Possible features:

* Daily Menu
* Standing Orders
* resident meal deliveries
* Feast Prep
* district meal support
* limited repeatable food deliveries
* Kitchen identity
* future Prosperity support
* Farmer's Delight and Create: Food integration

### Resident Profiles and Taste Preferences

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

### Community Supplies and Storehouse

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

Specialized settlements can support larger settlements or Capital Villages.

The network should encourage exploration and long-term world progression without becoming an industrial automation system.

### Capital Villages

Capital Villages should be expensive endgame settlements with multiple districts and strong services.

They should be powerful, but require significant support through projects, Prosperity, and linked villages.

### Festivals

Festivals can become future village events tied to seasons, food, districts, residents, Prosperity, and Community Projects.

## Not Planned for the Current MVP

Do not add these yet:

* full Prosperity system
* Community Kitchen gameplay
* Standing Orders
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
* complex villager AI
* full rotating shop stock
* shop JSON loading
* major balance pass

These systems should stay documented, but they should not be rushed into the current prototype.
