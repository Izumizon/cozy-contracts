# Cozy Contracts Village System Design

## Purpose

This document defines the long-term village and settlement direction for Cozy Contracts.

Cozy Contracts started as a Community Board contract system, but the design has grown into a broader village-building and community economy mod. The Community Board remains the main player-facing block, but the long-term system should be based around settlements, districts, Prosperity, Favour Tokens, and player-shaped village growth.

The goal is to keep the first MVP simple while avoiding code and design choices that would block future settlement systems.

## Core Design Statement

Cozy Contracts is a Minecraft mod where the Community Board becomes the heart of a growing village or settlement. Players complete cozy contracts, earn Favour Tokens, and use those tokens to buy themed goods, unlock services, support projects, and help the village grow.

Villages can develop multiple districts such as Farming, Kitchen, Mining, Builder, Scholar, Decorator, Market, Hunter, and Fishing. Each district affects contracts, shop stock, services, and rewards.

Large villages can become powerful, but they require Community Supplies to raise Prosperity and operate at full efficiency. Villages never decay or punish the player when unsupported; instead, supplies provide positive buffs such as faster production, better shop stock, stronger rewards, and improved services.

Players can specialise villages or build expensive Capital Villages, while the Village Network remains valuable by linking specialised settlements together.

## Main Design Rules

1. Villages can have multiple districts.
2. Districts affect contracts, shop stock, services, and future rewards.
3. Prosperity is a positive buff system, not upkeep.
4. Villages should never decay, lose bond, or become useless when unsupported.
5. Super villages are allowed as expensive endgame Capital Villages.
6. Specialised villages should remain efficient and useful.
7. The Village Network should remain valuable.
8. Nearby Community Boards should share settlement state instead of creating separate reward farms.
9. Player-founded villages should be possible later.
10. Players should stay in control of village growth.
11. Random uncontrolled building placement should be avoided.
12. Manual building and assisted building should both be supported later.
13. Favour Tokens should become a central village economy currency.
14. Shops should eventually be themed, not generic.

## Board vs Settlement

The Community Board is the player-facing block and interface.

The settlement is the larger system behind the board.

This distinction is important.

A Community Board should not permanently be treated as the entire village. Long term, a board should point to settlement data. The settlement should store things like contracts, shop state, districts, Prosperity, Village Bond, projects, and village network links.

Future architecture should move toward:

```text
Community Board
↓
Settlement ID / Settlement Center
↓
Settlement Data
↓
Contracts, shop, districts, Prosperity, bond, projects, network
```

For the MVP, the Community Board can still act as the settlement center. However, future code should be structured so that the board can later become an interface to a settlement rather than the settlement itself.

## Settlement Foundation Lite

The first settlement implementation should be intentionally small.

It should only answer one question:

```text
When this board is used, what settlement does it belong to?
```

It should not try to implement the full village system immediately.

### First version goals

* Each board can resolve to a settlement.
* Each settlement has an ID.
* Each settlement has a center position.
* Nearby boards can resolve to the same settlement.
* The first board in an area can act as the settlement center.
* Future systems can add districts, Prosperity, Village Bond, projects, and network data later.

### What not to include yet

Settlement Foundation Lite should not include:

* full districts
* Prosperity
* Village Bond
* Village Network
* project markers
* storehouses
* work crews
* house validation
* Capital Villages
* generated buildings
* automatic structure placement
* supply automation

The first version should stay boring, small, and future-proof.

## Board Spam Prevention

Players should be allowed to place multiple Community Boards.

The mod should avoid a frustrating rule like:

```text
You cannot place another board here.
```

Instead, nearby boards should connect to the same settlement.

Better behaviour:

```text
This board is connected to the nearby settlement.
```

This solves the board-spam exploit without blocking player creativity.

If a player places ten boards in one settlement area, those boards should act as access points to the same settlement state instead of creating ten separate daily reward systems.

### Suggested future rule

Boards within a configurable radius, such as 64 blocks, should share the same settlement.

Example:

```text
If another settlement center exists within 64 blocks, this board joins that settlement.
Otherwise, this board becomes a new settlement center.
```

This keeps decoration and convenience possible while preventing reward duplication.

## Village Stages

The board is the block. The settlement or village is the system behind it.

Suggested long-term stages:

| Stage            | Meaning                                            |
| ---------------- | -------------------------------------------------- |
| Outpost          | A new or small settlement with basic functionality |
| Settlement       | Has basic population/support structures            |
| Village          | Has districts and clearer identity                 |
| Thriving Village | High Prosperity and stronger services              |
| Capital Village  | Endgame multi-district hub                         |

These stages should not be rushed into the MVP. They are long-term progression targets.

## Districts

Villages are not locked to one strict theme. Instead, they can grow through districts.

A village might contain:

* Farming District
* Kitchen District
* Mining District
* Builder District
* Scholar District
* Decorator District
* Market District
* Hunter District
* Fishing District

Districts should be shaped by what the player builds, supports, and upgrades.

Example:

If the player builds farms, composters, barns, and crop fields, the village gains Farming identity.

If the player builds smokers, kitchens, pantries, and bakeries, the village gains Kitchen identity.

If the player builds mine entrances, stonecutters, blacksmith areas, and tool storage, the village gains Mining or Builder identity.

The village identity should feel player-shaped, not randomly assigned.

## District Effects

Each district can eventually affect:

* contract weighting
* available contract categories
* shop stock
* rare shop stock
* services
* Prosperity supply needs
* Community Projects
* work crew options
* special rewards
* festivals

Example district behaviour:

| District  | Possible Effects                                                 |
| --------- | ---------------------------------------------------------------- |
| Farming   | crop contracts, seed shop stock, bone meal rewards, animal goods |
| Kitchen   | food contracts, ingredient shop stock, recipe rewards            |
| Mining    | ore/stone contracts, mining supplies, mining crew services       |
| Builder   | construction contracts, assisted building, block supply stock    |
| Scholar   | books, maps, recipes, enchantments, paper contracts              |
| Decorator | flowers, dyes, candles, wool, clay, cozy blocks                  |
| Market    | rotating stock, trade, rare goods                                |
| Hunter    | mob drops, bows, leather, string                                 |
| Fishing   | fish, kelp, barrels, water-related goods                         |

## Contracts and Districts

The current contract system already has categories. This should become the foundation for district-based request selection.

Long term, a district should affect which contracts are more likely to appear.

Example:

A Kitchen District should increase the chance of:

* cooking contracts
* food contracts
* ingredient requests
* Farmer’s Delight requests
* Create: Food requests

A Builder District should increase the chance of:

* wood contracts
* stone contracts
* glass contracts
* lantern contracts
* construction supply requests

The MVP can keep three active daily contracts per board for now. Later, the GUI can support category or district views.

Possible future GUI structure:

```text
Requests
- General
- Kitchen
- Farming
- Builder
- Decorator
- Scholar
```

Each unlocked district could have its own small set of contracts.

## Favour Tokens

Favour Tokens should be central to the Cozy Contracts economy.

They can be used for:

* buying themed shop items
* buying rare ingredients
* buying saplings
* buying livestock vouchers
* paying for assisted building
* starting work crew tasks
* refreshing special stock
* unlocking services
* supporting projects
* buying recipe rewards
* buying convenience items

The token economy should reward helpful community activity without becoming an infinite exploit.

## Shops

The current MVP shop is a basic vanilla reward shop. This is useful for proving the gameplay loop:

```text
Complete contracts → earn Favour Tokens → spend Favour Tokens
```

Long term, shops should become themed.

A Farming District shop should not sell the same items as a Scholar District shop.

Example shop themes:

### Farming Shop

* seeds
* crops
* bone meal
* hoes
* livestock vouchers
* saplings
* beehives

### Kitchen Shop

* eggs
* milk
* sugar
* cocoa
* honey
* fruit saplings
* cooking tools
* recipes

### Builder Shop

* logs
* stone
* glass
* lanterns
* scaffolding
* construction blocks

### Scholar Shop

* books
* paper
* maps
* candles
* lapis
* enchanted books
* recipe unlocks

### Mining Shop

* stone
* coal
* copper
* iron nuggets
* pickaxes
* torches
* rails

The shop system should eventually support categories or district tags internally.

## Prosperity

Prosperity replaces the idea of upkeep.

The village should not punish the player for forgetting supplies. Instead, Community Supplies should raise Prosperity and provide positive bonuses.

Bad feeling:

```text
Your village is under-supplied. Production reduced.
```

Better feeling:

```text
Your village is well supplied. Production boosted.
```

Prosperity should never cause:

* decay
* lost buildings
* lost Village Bond
* villagers leaving
* deleted progress
* disabled core contracts
* disabled basic shops

Villages should always have baseline function.

## Baseline Village Function

A village should always keep its basic functionality.

Baseline systems should include:

* basic daily contracts
* basic shop
* basic Favour Token rewards
* village identity
* completed progress
* buildings and districts already created

The player should never feel punished for taking a break.

## Prosperity Buffs

Prosperity can improve:

* work crew speed
* production output
* shop quality
* rare stock chance
* token rewards
* number of available contracts
* project assistance
* festival rewards
* service speed

Example production framing:

| Prosperity Level | Mining Crew Time |
| ---------------- | ---------------- |
| Stable           | 3 days           |
| Supplied         | 2.5 days         |
| Well Supplied    | 2 days           |
| Thriving         | 1.5 days         |

This should feel like a bonus, not a penalty.

## Community Supplies

Community Supplies are items the player provides to improve Prosperity.

Supplies should depend on the district.

Examples:

| District  | Example Supplies                               |
| --------- | ---------------------------------------------- |
| Farming   | seeds, bone meal, hoes, compost, water buckets |
| Kitchen   | wheat, eggs, milk, sugar, fuel                 |
| Mining    | pickaxes, torches, food, logs, rails           |
| Scholar   | paper, ink, books, candles, lapis              |
| Decorator | flowers, dyes, candles, wool, clay             |
| Builder   | logs, stone, glass, lanterns, scaffolding      |
| Fishing   | fish, kelp, barrels, boats, string             |
| Hunter    | arrows, leather, string, bones, food           |

Supplying a village should feel like helping it thrive.

## Large Villages and Capital Villages

A player should be allowed to create a very large village.

Large villages should not be forbidden, but they should require more support to reach full power.

Specialised villages are efficient and easier to support.

Capital Villages are powerful endgame settlements with many districts, but they require significant investment.

Possible Capital Village requirements:

* high Village Bond
* multiple completed Community Projects
* several built houses or districts
* strong Prosperity support
* links to other villages
* a final Capital Project

Possible Capital Village benefits:

* more district slots
* larger shop
* better services
* more contracts
* stronger rare stock
* regional trade
* festival hub
* specialist services
* stronger Village Network benefits

Capital Villages should be long-term goals, not early-game defaults.

## Village Network

The Village Network should remain part of the long-term design.

Its purpose is not simply:

```text
I maxed lots of villages.
```

Instead, it should become:

```text
I connected specialised villages together to support a thriving region.
```

Example network support:

| Linked Village  | Supports With                |
| --------------- | ---------------------------- |
| Farming Village | crops, eggs, milk, bone meal |
| Mining Village  | coal, stone, copper, iron    |
| Forest Village  | logs, saplings, berries      |
| Scholar Village | paper, ink, books            |
| Garden Village  | flowers, dyes, honey         |
| Fishing Village | fish, kelp, barrels          |

The Village Network makes specialised settlements valuable even when Capital Villages exist.

## Player-Founded Villages

The player should eventually be able to found a village from nothing.

A Community Board can become the center of a new settlement.

It might begin as:

* an outpost
* a shack
* a camp
* a small shelter
* a community hearth

Then it can grow into:

* Hamlet
* Settlement
* Village
* Thriving Village
* Capital Village

This means Cozy Contracts is not only about improving vanilla villages. It is also about building new communities.

## Player Control and Building

Village growth should not randomly place buildings everywhere.

Random uncontrolled structure placement can damage player builds and feel un-Minecraft-like.

Instead, village growth should happen through:

* Project Markers
* player-built structures
* assisted building
* district projects
* house validation
* optional construction animations

The player should choose where things go.

## Manual and Assisted Building

Both manual and assisted building should exist later.

| Method         | How It Works                                                         |
| -------------- | -------------------------------------------------------------------- |
| Manual Build   | Player builds the structure themselves                               |
| Assisted Build | Player provides materials/tokens and the structure appears gradually |
| Validation     | Mod checks whether the structure meets requirements                  |

This supports different playstyles.

A cooking-focused player should not be forced to become a builder.

A builder should not be forced to use auto-building.

## Role-Based Play

Players should be able to specialise in the activities they enjoy.

Possible player roles:

* cooking
* farming
* building
* mining
* decorating
* hunting
* exploring
* trading

Favour Tokens and village systems should help players access support for activities they enjoy less.

Example:

A cooking-focused player can complete Kitchen contracts, earn Favour Tokens, buy seeds and ingredients, improve farms and kitchens, unlock better food contracts, and support a Kitchen/Farming village.

## MVP Scope

The MVP should not include the full settlement system.

The MVP should include:

* Community Board block
* daily contracts
* JSON/datapack contract loading
* Favour Tokens
* contract completion tracking
* persistent board state
* GUI request display
* GUI submit buttons
* full-inventory request submission
* basic reward shop
* category-ready contracts
* category-ready shop data
* simple settlement foundation if needed

The MVP should prove the core loop:

```text
Open board → complete requests → earn Favour Tokens → spend tokens
```

## Future Scope

Future versions may include:

* Settlement Foundation Lite
* full districts
* Prosperity
* Community Supplies
* Community Storehouse
* Project Markers
* Village Bond
* Community Projects
* work crews
* Village Network
* Capital Villages
* player-founded village progression
* assisted building
* manual structure validation
* themed shops
* Farmer’s Delight contracts
* Create: Food contracts
* festivals

## Development Priority

Current recommended order:

1. Document the village system.
2. Add Settlement Foundation Lite.
3. Make shop items category-ready.
4. Add Farmer’s Delight JSON contracts.
5. Add Create: Food JSON contracts.
6. Add district-aware contract weighting.
7. Add Prosperity later.
8. Add Village Bond later.
9. Add Community Projects later.
10. Add Village Network later.

## Non-Goals for Now

Do not implement these yet:

* full Prosperity
* Village Bond
* Community Projects
* Village Network
* Capital Villages
* district buildings
* work crews
* storehouses
* automatic construction
* random building placement
* house validation
* full settlement simulation

These systems should be documented and planned, but not rushed into the current MVP.

## Final Design Principle

Do not build the whole village system now, but do not code the MVP in a way that blocks it later.

The Community Board should remain simple and playable now, while the architecture gradually moves toward settlements, districts, Prosperity, and village networks.
