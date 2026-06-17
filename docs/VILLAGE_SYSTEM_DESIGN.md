# Cozy Contracts Village System Design

## Purpose

This document defines the long-term village and settlement direction for Cozy Contracts.

Cozy Contracts began as a Community Board contract system, but the broader design is a settlement-focused community economy mod. The Community Board remains the main player-facing block and interface, while the systems behind it can grow into settlements, districts, Prosperity, Community Supplies, Favour Tokens, resident relationships, and village networks.

The goal is to keep the MVP playable and focused while avoiding design choices that would block future settlement systems.

## Core Design Statement

Cozy Contracts is a Minecraft mod where the Community Board becomes the heart of a growing village or settlement. Players complete requests, earn Favour Tokens, spend those tokens on useful rewards, and gradually support the identity and growth of the community.

Long term, villages can develop multiple districts such as Farming, Kitchen, Mining, Builder, Scholar, Decorator, Market, Hunter, and Fishing. These districts can affect contracts, shop stock, services, rewards, Community Supplies, and future village progression.

Prosperity should be a positive buff system, not upkeep. Villages should never decay, lose progress, or punish the player for taking a break. Well-supported villages should receive bonuses, while unsupported villages keep their baseline function.

The long-term design also supports larger settlements, Capital Villages, player-founded villages, manual and assisted building, and a Village Network that makes specialized settlements valuable.

## Main Design Rules

1. The Community Board is the main settlement interface.
2. The settlement is the system behind the board.
3. Nearby boards should share settlement state instead of creating reward farms.
4. Villages can have multiple districts.
5. Districts should be player-shaped, not randomly assigned.
6. Prosperity is a positive buff system, not upkeep.
7. Villages should never decay, lose bond, or become useless when unsupported.
8. Large villages and Capital Villages are allowed, but they require more support.
9. Specialized villages should remain efficient and useful.
10. The Village Network should remain valuable.
11. Player-founded villages should be possible later.
12. Players should stay in control of village growth.
13. Manual building and assisted building should both be supported later.
14. Favour Tokens should remain central to the village economy.
15. Cooking should become a real playstyle, not only another contract category.

## Board vs Settlement

The Community Board is the player-facing block and interface.

The settlement is the larger system behind the board.

A Community Board should not permanently be treated as the entire village. Long term, a board should point to settlement data. The settlement should store contracts, shop state, districts, Prosperity, Village Bond, projects, resident information, Community Supplies, and village network links.

Future architecture should move toward:

```text
Community Board
-> Settlement ID / Settlement Center
-> Settlement Data
-> Contracts, shops, districts, Prosperity, residents, bond, projects, network
```

For the MVP, the Community Board can still act as the settlement center. Future code should continue moving toward the board as an interface to settlement state rather than the entire settlement itself.

## Settlement Foundation Lite

The first settlement implementation should stay intentionally small.

It should answer one question:

```text
When this board is used, what settlement does it belong to?
```

It should not try to implement the full village system immediately.

### First Version Goals

* Each board can resolve to a settlement.
* Each settlement has an ID.
* Each settlement has a center position.
* Nearby boards can resolve to the same settlement.
* The first board in an area can act as the settlement center.
* Future systems can add districts, Prosperity, Village Bond, residents, projects, supplies, and network data later.

### What Not To Include Yet

Settlement Foundation Lite should not include:

* full districts
* Prosperity
* Village Bond
* Village Network
* resident simulation
* project markers
* storehouses
* work crews
* house validation
* Capital Villages
* generated buildings
* automatic structure placement
* supply automation

The first version should stay small, readable, and future-proof.

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

This solves the board-spam exploit without blocking player creativity. If a player places several boards in one settlement area, those boards should act as access points to the same settlement state instead of creating separate daily reward systems.

### Suggested Future Rule

Boards within a configurable radius, such as 64 blocks, should share the same settlement.

```text
If another settlement center exists within 64 blocks, this board joins that settlement.
Otherwise, this board becomes a new settlement center.
```

This keeps decoration and convenience possible while preventing reward duplication.

## Village Stages

The board is the block. The settlement or village is the system behind it.

Suggested long-term stages:

| Stage | Meaning |
| --- | --- |
| Outpost | A new or small settlement with basic functionality |
| Settlement | Has basic population or support structures |
| Village | Has districts and clearer identity |
| Thriving Village | High Prosperity and stronger services |
| Capital Village | Endgame multi-district hub |

These stages are long-term progression targets, not immediate MVP requirements.

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

Examples:

* Farms, composters, barns, and crop fields can build Farming identity.
* Smokers, kitchens, pantries, bakeries, and meal counters can build Kitchen identity.
* Mine entrances, stonecutters, blacksmith areas, and tool storage can build Mining or Builder identity.

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

| District | Possible Effects |
| --- | --- |
| Farming | crop contracts, seed shop stock, bone meal rewards, animal goods |
| Kitchen | food contracts, ingredient stock, Community Kitchen support, recipe rewards |
| Mining | ore and stone contracts, mining supplies, mining crew services |
| Builder | construction contracts, assisted building, block supply stock |
| Scholar | books, maps, recipes, enchantments, paper contracts |
| Decorator | flowers, dyes, candles, wool, clay, cozy blocks |
| Market | rotating stock, trade, rare goods |
| Hunter | mob drops, bows, leather, string |
| Fishing | fish, kelp, barrels, water-related goods |

## Contracts and Districts

The current contract system already has categories. This should become the foundation for district-based request selection.

Long term, a district should affect which contracts are more likely to appear.

A Kitchen District should increase the chance of:

* cooking contracts
* food contracts
* ingredient requests
* Farmer's Delight requests
* Create: Food requests
* Community Kitchen requests

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

Each unlocked district could have its own small set of requests.

## Community Kitchen

The Community Kitchen is a cooking-focused system that begins in the 0.1.0-alpha as a Community Board tab with basic food deliveries, daily limits, and JSON-loaded Kitchen orders.

It gives players somewhere to deliver meals outside normal daily contracts. This lets cooking become a real playstyle instead of only another board contract category.

The current version is intentionally small. Later, the Kitchen may become its own block or interface, such as a Community Kitchen Counter or Meal Counter.

The Kitchen should complement the Community Board, not replace it. The board remains the primary settlement interface, while Kitchen and Residents may become tabs or dedicated blocks as the design grows.

The Kitchen already supports vanilla Kitchen orders and optional Farmer's Delight Kitchen orders. Create: Food Kitchen orders, resident preferences, meal requests, and settlement support are possible future expansions.

Possible Kitchen tabs:

* Daily Menu
* Standing Orders
* Residents
* Feast Prep

## Daily Menu

The Daily Menu exists in basic form as a Kitchen order type. Future versions can expand it into a richer set of village meal requests.

It is different from normal board contracts because it focuses specifically on cooking. A menu might request breakfast, lunch, dinner, dessert, drinks, or comfort meals.

The Daily Menu can reward:

* Favour Tokens
* Kitchen identity
* known resident preferences
* future Prosperity support
* future festival or feast progress

Daily Menu rewards should have limits to avoid infinite farming. The system should encourage cooking as a daily role without turning meals into an unlimited token source.

## Standing Orders

Standing Orders exist in basic form as Kitchen order types with daily delivery limits. Future versions can expand them into repeatable or semi-repeatable food needs for a settlement or district.

Examples:

* Miner's Lunchbox
* Farmhand Breakfast
* School Meal
* Builder's Packed Lunch

Standing Orders should support cooking as an ongoing role. They can let a player keep helping the settlement even after daily contracts are completed.

They should not give unlimited full rewards. The first few deliveries can give useful rewards, while extra deliveries may provide smaller support value, Kitchen identity, relationship hints, or future Prosperity bonuses.

## Resident Profiles and Taste Preferences

As villages grow, residents can move in.

Residents can have:

* names
* roles
* homes
* personalities
* relationship levels
* hidden Taste Profiles
* memories
* personal requests

Players can discover food preferences by cooking for residents. This creates personal attachment and gives food mods more purpose.

Example profile:

```text
Name: Mira
Role: Baker
Home: Bakery Cottage
Relationship: Acquaintance
Likes: Baked goods
Loves: Apple Pie
Dislikes: Unknown
Memories: Loved the Apple Pie delivered on Day 42
```

This system is inspired by the kind of personal attachment that can come from discovering a resident's likes, dislikes, habits, and memories over time.

## Taste Profile Design

Taste Profiles should use layered preferences instead of one flat favorite food.

Possible layers:

* loved exact dish
* liked food category
* liked ingredient
* disliked category
* favourite meal type
* favourite drink

Important rules:

* Experimentation should not be punishing.
* Disliked food should not heavily reduce relationship.
* A disliked food should mostly reveal information and give little or no bonus.
* Resident reactions should stay soft and cozy, not insulting.
* Residents should only meaningfully react to a limited number of meals per day.

The goal is discovery and attachment, not optimization pressure.

## Resident Memories

Loved dishes or important village events can create memories.

Memories help the village feel alive. They can reference meaningful moments without requiring complex simulation.

Example:

```text
Tomas still talks about the Beef Stew you made after the mine opened.
```

Memories can appear in a future Village Registry, Resident Profile UI, or Community Board resident tab.

## Village Registry

The Village Registry is a future UI, block, or board tab for tracking residents.

It can show:

* residents
* homes
* roles
* relationship level
* known preferences
* memories
* personal requests

The Registry should help players understand the community they are building without requiring spreadsheets or external notes.

## Food, Districts, and Prosperity

Cooking can support districts.

Giving a miner a loved hearty meal could later provide a small temporary Mining service bonus. Feeding builders might support construction work. Preparing meals for a festival could support village-wide Prosperity.

These should be optional bonuses, not mandatory upkeep.

Important rules:

* Do not force players to feed every resident daily.
* Do not reduce core village function when meals are missing.
* Loved meals can provide small temporary district or Prosperity bonuses.
* Food systems should reward attention without creating chores.

## Community Supplies and Storehouse

Community Supplies are items the player provides to support districts and improve Prosperity.

Supplies should depend on the district.

Examples:

| District | Example Supplies |
| --- | --- |
| Farming | seeds, bone meal, hoes, compost, water buckets |
| Kitchen | wheat, eggs, milk, sugar, fuel, cooked meals |
| Mining | pickaxes, torches, food, logs, rails |
| Scholar | paper, ink, books, candles, lapis |
| Decorator | flowers, dyes, candles, wool, clay |
| Builder | logs, stone, glass, lanterns, scaffolding |
| Fishing | fish, kelp, barrels, boats, string |
| Hunter | arrows, leather, string, bones, food |

A future Storehouse can store these supplies and make settlement support easier to manage.

The Storehouse should not become stressful upkeep. Missing supplies should not punish the player. Supplied villages should receive bonuses, better services, stronger stock, or smoother project support.

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

The current MVP shop is a basic reward shop. It proves the gameplay loop:

```text
Complete requests -> earn Favour Tokens -> spend Favour Tokens
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

The shop system should eventually support categories, district tags, and settlement identity internally.

## Prosperity

Prosperity replaces the idea of upkeep.

The village should not punish the player for forgetting supplies. Instead, Community Supplies, meals, projects, and resident support can raise Prosperity and provide positive bonuses.

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
* district meal bonuses

Example production framing:

| Prosperity Level | Mining Crew Time |
| --- | --- |
| Stable | 3 days |
| Supplied | 2.5 days |
| Well Supplied | 2 days |
| Thriving | 1.5 days |

This should feel like a bonus, not a penalty.

## Large Villages and Capital Villages

A player should be allowed to create a very large village.

Large villages should not be forbidden, but they should require more support to reach full power.

Specialized villages are efficient and easier to support.

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
I connected specialized villages together to support a thriving region.
```

Example network support:

| Linked Village | Supports With |
| --- | --- |
| Farming Village | crops, eggs, milk, bone meal |
| Mining Village | coal, stone, copper, iron |
| Forest Village | logs, saplings, berries |
| Scholar Village | paper, ink, books |
| Garden Village | flowers, dyes, honey |
| Fishing Village | fish, kelp, barrels |

The Village Network makes specialized settlements valuable even when Capital Villages exist.

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

Random uncontrolled structure placement can damage player builds and feel unlike Minecraft.

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

| Method | How It Works |
| --- | --- |
| Manual Build | Player builds the structure themselves |
| Assisted Build | Player provides materials or tokens and the structure appears gradually |
| Validation | Mod checks whether the structure meets flexible requirements |

This supports different playstyles.

A cooking-focused player should not be forced to become a builder. A builder should not be forced to use auto-building.

## Role-Based Play

Players should be able to specialize in the activities they enjoy.

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

A cooking-focused player can complete Kitchen requests, prepare Community Kitchen meals, discover resident taste preferences, earn Favour Tokens, buy ingredients, improve farms and kitchens, unlock better food requests, and support a Kitchen/Farming village.

## MVP Scope

The 0.1.0-alpha MVP should not include the full settlement system.

The MVP should include:

* Community Board block
* daily contracts
* JSON/datapack contract loading
* Favour Tokens
* reward shop
* contract completion tracking
* persistent board state
* GUI request display
* GUI submit buttons
* full-inventory request submission
* category-ready contracts
* category-ready shop data
* category-aware shop foundation
* Settlement Foundation Lite
* Community Kitchen tab
* vanilla Kitchen deliveries
* JSON/datapack Kitchen order loading
* optional Farmer's Delight board contracts
* optional Farmer's Delight Kitchen orders
* optional Create: Food board contracts
* optional dev runtime helpers for testing
* release documentation

The MVP should prove the core loop:

```text
Open board -> complete requests -> earn Favour Tokens -> spend tokens
```

## Future Scope

Future versions may include:

* full districts
* advanced Community Kitchen progression
* resident meal deliveries
* advanced Standing Orders
* Feast Prep gameplay
* district-aware Kitchen support
* Resident Profiles
* Taste Preferences
* Resident Memories
* Village Registry
* Prosperity
* Community Supplies
* Storehouse
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
* deeper Farmer's Delight integration
* Create: Food Kitchen orders
* festivals

## Development Priority

Current recommended order:

1. Finish release documentation.
2. Test release jar in a fresh Forge instance.
3. Release 0.1.0-alpha.
4. Later: Create: Food Kitchen orders.
5. Later: district-aware contracts and shops.
6. Later: Resident Profiles and Taste Preferences.
7. Later: Prosperity, Storehouse, Village Bond, Community Projects, and Village Network.

## Non-Goals for Now

Do not implement these yet:

* advanced Community Kitchen progression
* resident meal deliveries
* advanced Standing Orders
* Feast Prep gameplay
* Resident Profiles
* Taste Preferences
* Resident Memories
* Village Registry
* full Prosperity
* Storehouse
* Village Bond
* Community Projects
* Village Network
* Capital Villages
* work crews
* automatic building placement
* strict house schematics
* complex villager behavior
* full rotating shop stock
* shop JSON loading

These systems should be documented and planned, but not rushed into the current alpha.

## Final Design Principle

Do not build the whole village system now, but do not code the MVP in a way that blocks it later.

The Community Board should remain simple and playable now, while the architecture gradually moves toward settlements, districts, Prosperity, resident attachment, food-focused play, and village networks.
