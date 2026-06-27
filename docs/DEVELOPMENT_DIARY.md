# Cozy Contracts Development Diary

Project: Cozy Contracts  
Technology: Minecraft Forge 1.20.1, Java 17, Gradle  
Started: June 2026  
Status: Public alpha development

## 1. Project Overview

Cozy Contracts is a Forge Minecraft mod about villages asking for help through a Community Board. Players complete daily requests, earn Favour Tokens, and spend those tokens on supplies that help them keep building, farming, cooking, and decorating.

The project started as a simple bounty board idea. The first version only needed to prove one tiny loop: right-click a board, see a request, hand in an item, and receive a reward. That was enough to make the concept playable, but it also revealed a larger direction. The board was not just a quest block. It wanted to become the front desk for a community.

Over time, Cozy Contracts evolved into a settlement-focused community economy mod. The Community Board became the main interface. Favour Tokens became the village economy. The shop changed from a generic reward list into a settlement supply-bundle market. The Community Kitchen became a separate cooking loop. Settlement Foundation Lite introduced the idea that the board is the interface, but the settlement is the system behind it.

The current design is moving toward villages that grow through player effort rather than abstract menus. Buildable Community Projects are an important part of that direction. Project Markers are planning tools, not completed projects by themselves. The player still needs to build in the world, then register the finished work as a Registered Improvement for the settlement. A completed marker can remain as a Project Site, but the settlement progress lives in saved settlement data.

The long-term version of Cozy Contracts may include project families, residents, taste preferences, Prosperity, Storehouse support, Village Bond, districts, Village Network, Livestock Crates, and deeper optional food-mod compatibility. The challenge is keeping the current alpha focused while making sure the architecture does not block those future systems.

## 2. Development Goals

The main goal is to make village support feel cozy, useful, and player-shaped.

The project goals are:

- create a readable village request loop
- make Favour Tokens useful without making them overpowered
- support cooking as its own playstyle
- support food mods through optional compatibility instead of hard dependencies
- make villages feel like they grow through player effort
- avoid stressful upkeep or punishment for taking breaks
- keep systems modular and future-proof
- use datapacks and JSON where practical
- give players reasons to build farms, kitchens, gardens, yards, barns, and community spaces
- keep the Community Board central while allowing future systems to become tabs or dedicated blocks

One important design goal is emotional as much as technical: the mod should encourage players to care about a settlement without making it feel needy. A village should benefit from support, not collapse without it.

## 3. Technical Approach

Cozy Contracts is built with Minecraft Forge 1.20.1 and Java 17. Gradle handles development builds, runtime helpers, and packaging.

The main player-facing object is the Community Board. It is a block with a BlockEntity, a client screen, server-side interaction logic, and network packets for actions such as submitting requests, buying shop items, delivering Kitchen orders, and validating Projects.

The project uses a server-authoritative approach. The client can request an action, but the server checks the board position, player distance, current board state, inventory contents, shop availability, Kitchen delivery limits, and project validation. This matters because Minecraft clients should not be trusted to decide rewards or consume items.

Contracts are data-driven through JSON files under the mod's data resources. Each contract has a stable ID, title, requester, category, difficulty, weight, requirement, and reward. The registry supports datapack-style loading and falls back safely when needed.

Kitchen orders follow a similar data direction. They use JSON/datapack resources, stable IDs, order types, requirements, rewards, daily limits, and optional `required_mods` gating. This lets Farmer's Delight and Create: Food content exist without adding Java imports or mandatory dependencies.

Favour Tokens are the central economy item. They are earned through contracts and Kitchen deliveries, then spent in the supply shop. Purchases are validated on the server, token removal is count-first, and rewards are dropped near the player if the inventory is full.

Settlement Foundation Lite stores the first layer of settlement identity. Nearby boards can resolve to the same settlement, which prevents each board from becoming a separate reward farm. This also prepares the project for settlement-level progress such as Community Improvements.

Project Marker and Community Project foundations are the newest direction. A marker identifies where a build is located, while settlement saved data stores active assignments, completed project IDs, completed site locations, and lightweight improvement counts.

## 4. Design Philosophy

The most important design rule is:

```text
The board is the interface; the settlement is the system behind it.
```

That rule changed how I think about the mod. A Community Board is convenient and readable, but it should not permanently be the entire village. It should point to a settlement identity that can later own projects, shops, districts, residents, Prosperity, supplies, and memories.

Other design rules:

- Villages should grow through player choice.
- Prosperity should be a positive buff system, not upkeep.
- The mod should not punish players for taking breaks.
- Shops should sell useful project-support supplies, not random filler.
- Projects should become real player-built improvements, not abstract material dumps.
- Validation should guide, not judge.
- Cozy systems should reduce friction without becoming overpowered.
- Optional mod support should remain optional.
- Future systems should be prepared carefully, not rushed into the MVP.

I rejected nearby-block detection as a shop unlock system because it encouraged the wrong behaviour. If placing a composter next to the board unlocked farming stock, players would be rewarded for block spam instead of meaningful settlement development. Community Projects and settlement development tracks are a better long-term unlock model.

## 5. Challenges and Solutions

### Avoiding Infinite Contract Reward Farming

The earliest request system could have become an unlimited token source. Daily completion tracking solved this by recording completed contract IDs and resetting progress on the next Minecraft day.

The lesson was that even cozy systems need clear limits. A reward loop can feel generous without being infinite.

### Moving Contracts from Java to JSON

The first contracts were hardcoded in Java because that was the fastest way to prove the idea. Once the loop worked, hardcoded content became limiting.

Moving contracts into JSON made the system easier to expand, easier to debug, and friendlier to future datapacks. It also forced the contract model to become more explicit about IDs, categories, difficulty, weight, requirements, and rewards.

### Optional Farmer's Delight and Create: Food Support

Food mods are a natural fit for Cozy Contracts, but they should not be required. The solution was `required_mods` gating in JSON data. If a required mod is missing, the content is skipped safely.

This kept Cozy Contracts standalone while still allowing richer food content when optional mods are installed.

### Designing Kitchen Orders

Kitchen deliveries needed to feel useful without becoming an infinite token farm. The solution was small rewards, daily limits, and deterministic daily selection. The Kitchen registry can contain many orders, but each board only shows a small active set.

The Kitchen became a separate loop from normal board contracts. That gave cooking its own identity instead of making every meal request compete with the three daily board requests.

### Replacing the Generic Shop

The first shop worked technically, but it was weak as a design. It sold a small set of generic rewards that did not strongly support the settlement fantasy.

The 0.3.0-alpha shop rework changed it into a supply-bundle market. Tokens now buy practical farming, Kitchen, building, decorating, and universal supplies. This made the economy feel more connected to building a village.

### Avoiding Nearby-Block Spam

The shop needed categories, but nearby block detection was the wrong unlock method. It would reward players for clustering signal blocks around the board instead of building meaningful farms, kitchens, yards, or gardens.

The better solution is future settlement development: projects, improvement counts, and deliberate unlocks.

### Deciding How Project Markers Should Work

Project Markers needed to define where a project build happens without becoming a magic completion block. The marker is only the planning point. The player still builds the field, garden, or yard around it.

This keeps Community Projects grounded in the world.

### Separating Active Markers from Completed Improvements

An assigned Project Marker is not the same as a completed settlement improvement. The active marker is temporary work-in-progress. The completed project is settlement-level progress.

Keeping these separate prevents the system from confusing "I placed a marker" with "I built something useful." It also lets completed markers become optional Project Sites without making the settlement lose progress if the physical marker is later removed.

### Balancing Player Freedom with Validation

Community Projects should not require exact blueprints, but they still need to know whether a build roughly satisfies the idea.

The current plan uses loose functional checks. Village Fields looks for farmland, crops, and water. Garden Corner looks for flowers, natural decoration, and light. Builder's Yard looks for work, storage, materials, and light.

The validation message should say what is still missing, not tell the player their build is invalid.

## 6. Features Implemented

### Community Board

- Placeable wooden Community Board block
- BlockEntity-backed state
- GUI with Requests, Shop, Kitchen, and Projects tabs
- Server-side interaction handling
- Shift-right-click held-item submission
- Screen refresh after server actions

### Contracts

- Daily active contracts per board
- Weighted deterministic selection
- Exact item and tag requirements
- Completion tracking
- JSON/datapack loading
- Fallback prototype data
- Debug commands

### Favour Tokens

- Reward currency for community work
- Inventory-safe reward delivery
- Used by the settlement supply shop

### Shop

- Supply-bundle market
- Category-ready shop metadata
- Category filter tabs
- Server-authoritative purchases
- Full-inventory reward dropping

### Kitchen

- Community Kitchen tab
- Daily Menu, Standing Order, and Feast Prep order types
- Food deliveries from player inventory
- Daily delivery limits
- JSON/datapack Kitchen orders
- Deterministic daily selection
- Optional Farmer's Delight and Create: Food order support

### Optional Mod Support

- Farmer's Delight board contracts
- Farmer's Delight Kitchen orders
- Create: Food board contracts
- Create: Food Kitchen orders
- Development runtime toggles
- No hard Java dependency on optional food mods

### Settlement Foundation

- Settlement identity for boards
- Nearby boards can share a settlement
- SavedData foundation for future settlement-level systems

### Debug Commands

- Board debugging
- Contract debugging
- Shop debugging
- Kitchen debugging
- Settlement debugging
- Project debugging

### Project Marker and Community Projects Work-in-Progress

- Project Marker block
- Projects tab foundation
- Starter projects: Village Fields, Garden Corner, Builder's Yard
- Loose validation around a marker
- Settlement-level completed project IDs
- Completed Project Site location tracking
- Farming, Builder, and Decor Community Improvement counts

## 7. What I Learned

I learned that scope control is not the same as thinking small. Cozy Contracts has a large long-term vision, but each step needs to prove one useful thing. The best milestones were the ones that made a system playable without pretending the whole future system already existed.

I also learned that data-driven design becomes valuable once the gameplay loop is clear. Starting with hardcoded contracts was the right choice because it kept the first test small. Moving to JSON was the right next step because the model had become stable enough to deserve data files.

Server-authoritative gameplay is especially important in a Minecraft mod. The client should show information and request actions, but the server needs to decide whether items are consumed, rewards are granted, projects are completed, and purchases are valid.

UI readability mattered more than I expected. The Community Board grew from text messages into a real screen, and each new tab created layout pressure. Kitchen orders needed scrolling, compact progress text, and hover details. The shop needed category tabs. The lesson was that a working system still needs a comfortable interface.

Optional compatibility taught me to separate content from dependency. Farmer's Delight and Create: Food make the mod richer, but Cozy Contracts needs to stand alone. `required_mods` gating is a clean way to support both.

Economy balance also became a design problem, not just a numbers problem. If the shop sells an item that can be turned into more tokens than it costs, the loop breaks. If rewards are too random, tokens feel pointless. The supply-bundle market was a better fit because it supports what players are already doing.

The biggest design lesson was that player experience matters more than pure logic. A validation system could be strict and technically correct, but it would feel bad if it judged creative builds harshly. A cozy mod should guide the player toward what is missing.

Finally, I learned to separate MVP systems from future systems. It is tempting to build residents, districts, Prosperity, Storehouse, Village Bond, and project chains immediately. The better path is to leave room for them while finishing the small playable foundations first.

## 8. Future Improvements

Future improvements may include:

- Project Marker lifecycle polish and completed-site presentation
- clearer completed project sites
- project plaques or completion markers
- project families such as farms, gardens, kitchens, yards, houses, barns, mines, and wells
- Community Pantry projects
- Animal Pen and Community Barn projects
- Farmhouse projects
- Resident Profiles
- Taste Preferences
- Resident Memories
- Village Registry
- Storehouse support
- Prosperity as a positive buff system
- Village Bond
- Village Network
- Livestock Crates as future farming utility
- shop unlocks through settlement development tracks and Community Projects
- deeper Farmer's Delight support
- more optional Create: Food support
- support for additional food and farming mods
- better release screenshots and player-facing tutorials

Livestock Crates are a good example of the kind of future feature that should be added carefully. They should be farming and village-building tools, not universal mob capture items. They should preserve animal data correctly and probably begin with a small safe set of supported livestock.

## 9. Portfolio Reflection

Cozy Contracts is useful as a portfolio project because it is not only a list of features. It shows an evolving design process, a real mod architecture, data-driven content, optional compatibility, UI iteration, server-authoritative gameplay, persistence, and release planning.

The project demonstrates:

- Java and Forge mod development
- Minecraft registry and resource work
- JSON/datapack-style content loading
- client/server packet design
- saved world data
- gameplay economy design
- optional mod compatibility
- UI layout iteration
- debugging commands and release validation
- player-focused design decisions
- long-term roadmap planning

It also shows that I can change direction when an early idea is weak. The shop was redesigned because the first version did not support the settlement fantasy well enough. Nearby block detection was rejected because it encouraged spam. Community Projects are being designed carefully because they need to respect player creativity.

That kind of iteration is the strongest part of the project.

## 10. Development Milestones

### June 2026 - Initial Forge Setup

What changed:  
I set up the Forge 1.20.1 project, configured Java 17, replaced the example mod identity, and registered the first Cozy Contracts content.

Why the change was needed:  
The project needed a stable technical foundation before any gameplay systems could be tested.

Technical/design challenge:  
The starter project contained inherited example names and resources that could cause confusion.

Solution:  
I cleaned the mod identity, package, registries, and basic metadata before adding gameplay.

What I learned:  
A clean foundation prevents small naming mistakes from spreading through the whole project.

### June 2026 - First Community Board Interaction

What changed:  
The Community Board became a real interactive block that could respond to right-clicks.

Why the change was needed:  
The board needed to become the entry point for the request system.

Technical/design challenge:  
Messages needed to be sent server-side to avoid duplicate client output.

Solution:  
The block handled right-clicks and returned a successful interaction while keeping logic server-side.

What I learned:  
Even tiny interactions need correct client/server boundaries in Forge.

### June 2026 - Temporary Contracts

What changed:  
I added the first temporary contract model and displayed three hardcoded requests from the board.

Why the change was needed:  
The project needed to prove that the board could present multiple requests before completion logic existed.

Technical/design challenge:  
It was too early to build the full contract system.

Solution:  
I used a small Java prototype source and kept the UI text-only.

What I learned:  
Temporary data is useful when it helps prove a flow without pretending to be final architecture.

### June 2026 - Held-Item Submission and Favour Tokens

What changed:  
Shift-right-click submission consumed requested held items and rewarded Favour Tokens.

Why the change was needed:  
The mod needed its first complete loop: request, submit, reward.

Technical/design challenge:  
Sneaking interactions initially allowed vanilla eating or block placement to continue.

Solution:  
The board consumed the interaction and kept item removal and reward logic server-side.

What I learned:  
Interaction priority matters. A feature can technically work while still feeling broken if vanilla behaviour leaks through.

### June 2026 - Weighted Daily Contracts

What changed:  
The contract pool expanded and boards began selecting weighted daily requests based on board position and Minecraft day.

Why the change was needed:  
Static requests made the board feel like a demo instead of a daily village system.

Technical/design challenge:  
Selections needed to vary without changing every time the screen opened.

Solution:  
I used deterministic selection so the same board keeps the same contracts during the same day.

What I learned:  
Random-feeling systems are easier to test when they are deterministic under the hood.

### June 2026 - Persistent Board State

What changed:  
The Community Board gained a BlockEntity that stores active contract IDs and completed contract IDs.

Why the change was needed:  
Board progress needed to survive save/reload and avoid repeated rewards.

Technical/design challenge:  
Saved state should reference contracts by ID, not by fragile object data.

Solution:  
The board stores ResourceLocation IDs and resolves them through registries.

What I learned:  
Stable IDs are the backbone of persistence.

### June 2026 - JSON Contract Loading

What changed:  
Contracts moved from Java prototype data into JSON/datapack resources.

Why the change was needed:  
The contract pool needed to grow without recompiling Java.

Technical/design challenge:  
Invalid JSON or missing optional mods should not break the whole registry.

Solution:  
The loader skips invalid entries with warnings and supports `required_mods`.

What I learned:  
Good data loading needs failure paths, not only happy paths.

### June 2026 - Community Board GUI

What changed:  
The board moved from chat previews to a dedicated screen.

Why the change was needed:  
Chat messages were not enough once requests had completion state, rewards, and later tabs.

Technical/design challenge:  
The client screen needed authoritative data without trusting the client.

Solution:  
The server sends a compact screen packet with display entries.

What I learned:  
A GUI is not just presentation. It changes what data needs to be synchronized.

### June 2026 - GUI Submission

What changed:  
Submit buttons were added to the board GUI and later updated to submit from the player's full inventory.

Why the change was needed:  
Held-item submission was good for testing, but tedious once the GUI existed.

Technical/design challenge:  
Inventory submission needed to remove exact counts across matching stacks without partial failure.

Solution:  
The service counts first, removes only after confirming enough items, and reuses server-side reward logic.

What I learned:  
Convenience features still need careful failure handling.

### June 2026 - Shop Foundation

What changed:  
Favour Tokens became spendable through a shop tab.

Why the change was needed:  
Tokens needed a purpose beyond collecting currency.

Technical/design challenge:  
Purchases needed to be server-authoritative and safe when inventories were full.

Solution:  
The server removes tokens only after confirming enough exist, inserts a fresh reward stack, and drops leftovers.

What I learned:  
Reward delivery bugs are serious because they can make players lose currency.

### June 2026 - Settlement Foundation

What changed:  
Boards began resolving to settlement identities, allowing nearby boards to share a settlement.

Why the change was needed:  
The mod needed to stop treating every board as a separate village.

Technical/design challenge:  
The first settlement system needed to stay small.

Solution:  
Settlement Foundation Lite only answers which settlement a board belongs to.

What I learned:  
A foundation should solve one clear problem and leave room for later systems.

### June 2026 - Farmer's Delight Contracts

What changed:  
Optional Farmer's Delight contracts were added through JSON.

Why the change was needed:  
Food mods fit the cozy village request fantasy.

Technical/design challenge:  
Cozy Contracts should not require Farmer's Delight to launch.

Solution:  
The contracts use `required_mods` and no Farmer's Delight Java imports.

What I learned:  
Optional compatibility is strongest when it is data-driven.

### June 2026 - Community Kitchen Foundation

What changed:  
The board gained a Kitchen tab with Daily Menu, Standing Order, and Feast Prep concepts.

Why the change was needed:  
Cooking needed a home outside normal daily contracts.

Technical/design challenge:  
The first version needed to show the direction without adding too much gameplay at once.

Solution:  
The Kitchen started as a focused board tab, then gained deliveries once the display structure was stable.

What I learned:  
A future system can begin as presentation before becoming gameplay.

### June 2026 - Kitchen Deliveries

What changed:  
Kitchen orders became deliverable from the player's inventory with daily limits and small token rewards.

Why the change was needed:  
The Kitchen needed a playable loop.

Technical/design challenge:  
Kitchen rewards needed to avoid becoming an infinite token source.

Solution:  
Each order has a daily limit, modest rewards, and server-side item removal.

What I learned:  
Separate loops need separate balance assumptions.

### June 2026 - Kitchen JSON Orders

What changed:  
Kitchen orders moved into JSON/datapack resources.

Why the change was needed:  
The Kitchen needed the same extensibility as contracts.

Technical/design challenge:  
The format had to support optional mods safely.

Solution:  
Kitchen order loading supports required fields, requirement validation, and `required_mods`.

What I learned:  
Once one data-driven system works, similar systems can reuse the same design principles.

### June 2026 - Farmer's Delight Kitchen Orders

What changed:  
Optional Farmer's Delight Kitchen orders were added.

Why the change was needed:  
Farmer's Delight makes the Kitchen feel much richer.

Technical/design challenge:  
The larger order pool made the Kitchen tab overflow.

Solution:  
The Kitchen tab became scrollable, compact, and tooltip-friendly.

What I learned:  
Content expansion often exposes UI assumptions.

### June 2026 - 0.1.0-alpha Release

What changed:  
The first public alpha gathered the Community Board, contracts, Favour Tokens, shop, Kitchen deliveries, JSON support, and optional food-mod content into a playable release.

Why the change was needed:  
The project had reached a complete first gameplay loop worth packaging.

Technical/design challenge:  
Release documentation needed to be honest about what was implemented and what remained future work.

Solution:  
I wrote release notes, known limitations, and setup documentation.

What I learned:  
Public release preparation is part of development, not a separate afterthought.

### June 2026 - Create: Food Kitchen Orders

What changed:  
Optional Create: Food Kitchen order support expanded the cooking pool.

Why the change was needed:  
Create: Food is another natural compatibility target for a cooking-focused mod.

Technical/design challenge:  
Create: Food needs its own optional runtime handling and safe JSON gating.

Solution:  
Orders use `required_mods`, development runtime toggles, and no hard Java imports.

What I learned:  
Compatibility work needs careful boundaries so it does not make the base mod fragile.

### June 2026 - Daily Kitchen Selection

What changed:  
Boards began selecting a smaller daily active set from the loaded Kitchen order registry.

Why the change was needed:  
Optional food mods can add many orders, and showing everything at once became noisy.

Technical/design challenge:  
Selection needed to be stable for a board/day but vary across boards and days.

Solution:  
The Kitchen uses deterministic weighted selection and stores active Kitchen order IDs.

What I learned:  
The registry should be the pool, not necessarily the whole UI.

### June 2026 - 0.2.0-alpha Release

What changed:  
The Kitchen expansion and daily selection work became the second public alpha direction.

Why the change was needed:  
Cooking had become a meaningful part of the mod rather than a side note.

Technical/design challenge:  
The release needed to explain that optional food content extends the Kitchen without becoming mandatory.

Solution:  
Documentation framed food mods as optional compatibility.

What I learned:  
Each release should have a clear theme.

### June 2026 - Shop Supply-Bundle Rework

What changed:  
The shop was redesigned into a practical settlement supply-bundle market.

Why the change was needed:  
The first generic shop was functional but not very exciting or useful.

Technical/design challenge:  
Some shop items overlap with contract or Kitchen inputs, which can create token loops.

Solution:  
The supply bundles were priced conservatively and organized by category.

What I learned:  
Economy design is system design. Items, prices, and contracts all affect each other.

### June 2026 - Shop Category Tabs

What changed:  
The Shop tab gained category filters for All, Village, Farming, Kitchen, Builder, and Decor.

Why the change was needed:  
The larger supply shop needed better browsing.

Technical/design challenge:  
Filtering should be client-side convenience without changing server purchase validation.

Solution:  
The UI filters visible entries by existing category metadata while packet IDs and server validation remain unchanged.

What I learned:  
Readability polish can make a system feel much more complete without changing gameplay.

### June 2026 - 0.3.0-alpha Release

What changed:  
The supply-bundle shop and category browsing became the focus of the third alpha.

Why the change was needed:  
Favour Tokens needed to support settlement building more directly.

Technical/design challenge:  
The release needed to describe the shop as useful support, not a creative inventory.

Solution:  
Documentation emphasized practical supplies, conservative pricing, and future settlement unlocks.

What I learned:  
A good reward shop should reinforce the main fantasy of the mod.

### June 2026 - Buildable Community Projects Foundation Lite

What changed:  
Project Markers, a Projects tab, starter projects, loose validation, Registered Improvements, and completed Project Sites were added as 0.4.0-alpha work-in-progress.

Why the change was needed:  
The mod needed a first step toward villages growing through player-built world improvements.

Technical/design challenge:  
The system had to avoid abstract item donations, auto-building, strict blueprints, and harsh validation.

Solution:  
Project Markers identify where a build is located. The board assigns and validates projects. Completion registers settlement-level improvements and can leave the marker behind as a Project Site. Breaking that site later should not undo the settlement record.

What I learned:  
World-building systems need trust in the player's creativity. The mod should ask for meaningful features, not control the whole shape of the build.

## End of Development Diary
