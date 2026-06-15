# Cozy Contracts Development Log

Cozy Contracts is a Forge 1.20.1 Minecraft mod focused on cozy village requests, daily contracts, Favour Tokens, and future cooking and village progression systems. This log follows the project from its initial Forge setup through the current early prototype.

## Milestone 1 — Project Setup and Forge Foundation

### Goal

Establish a clean Forge 1.20.1 development environment and create the basic identity and registries for Cozy Contracts.

### What was implemented

* Set up the Forge 1.20.1 MDK with Forge 47.4.20.
* Configured Java 17 for development and Gradle builds.
* Removed the MDK's example mod references and replaced them with the `cozy_contracts` mod identity.
* Created the `CozyContracts` main mod class.
* Registered the first Favour Token item.
* Registered the Community Board block and its block item.
* Added a Cozy Contracts creative mode tab.
* Verified the project with a successful clean build and development client launch.

### Why it mattered

This milestone established a reliable technical foundation. It proved that the project could register custom content, build successfully, and launch in Minecraft before gameplay systems were introduced.

### Challenges / fixes

* Cleaned inherited example names and resources to prevent registration and namespace confusion.
* Confirmed the project used the correct Java version required by the Forge toolchain.

### Next step

Give the Community Board its first custom player interaction.

## Milestone 2 — Community Board Interaction

### Goal

Turn the Community Board from a decorative block into the entry point for the contract system.

### What was implemented

* Added a dedicated `CommunityBoardBlock` class.
* Added server-side right-click handling.
* Displayed the first system message: "The Community Board is ready for requests."
* Ensured the interaction returned a successful result without duplicate client messages.

### Why it mattered

The Community Board became a functional gameplay object rather than only registered content. This provided a small, testable foundation for all later request previews and submissions.

### Challenges / fixes

* Kept message delivery server-side to avoid duplicate output.

### Next step

Represent temporary contracts in code and display them through the board.

## Milestone 3 — Temporary Contract Preview

### Goal

Prove that the board could present multiple village requests before implementing completion logic.

### What was implemented

* Added the first `Contract` data model.
* Added `TemporaryContracts` as a prototype contract source.
* Created three hardcoded requests: Baker's Morning Rush, Flower Table, and Roof Repairs.
* Updated normal right-click to display the requests as readable chat messages.

### Why it mattered

This separated contract data from block interaction code and established the first complete request-preview flow.

### Challenges / fixes

* Kept the preview text-only so the data model could develop before committing to a GUI architecture.

### Next step

Add a temporary submission path that consumes requested items and grants rewards.

## Milestone 4 — Held-Item Submission and Favour Token Rewards

### Goal

Prove the core gameplay loop: view a request, hand in an item, and receive Favour Tokens.

### What was implemented

* Added shift-right-click submission while preserving normal right-click preview.
* Limited submission checks to the player's held main-hand stack.
* Consumed the requested quantity when a requirement was satisfied.
* Granted Favour Tokens and dropped them near the player if the inventory was full.
* Supported exact bread requirements and tag-based flower and log requirements.
* Added clear success, insufficient quantity, and wrong-item messages.

### Why it mattered

The project gained its first playable reward loop without introducing inventory menus or broad inventory scanning.

### Challenges / fixes

* Fixed an interaction priority issue where bread could be eaten and placeable items could be placed.
* Ensured board interactions always consumed the event while sneaking.
* Kept all item removal and reward logic server-side.

### Next step

Move requirement details into reusable contract data instead of special-case submission code.

## Milestone 5 — Contract Model Expansion

### Goal

Prepare contracts for selection, persistence, debugging, and eventual JSON loading.

### What was implemented

* Added stable `ResourceLocation` contract IDs.
* Added category, difficulty, and selection weight fields.
* Added `ContractRequirement` with exact-item and item-tag requirement types.
* Stored requirement ID, count, and display text in the contract model.
* Updated preview, matching, missing-item messages, consumption, and rewards to derive from contract data.

### Why it mattered

Contracts became reusable data records rather than isolated gameplay branches. Stable IDs enabled persistence, while categories, difficulties, and weights prepared the model for themed content and weighted selection.

### Challenges / fixes

* Refactored submission matching without changing the established in-game behavior.

### Next step

Expand the prototype pool and select a daily set of contracts for each board.

## Milestone 6 — Weighted Daily Board Contracts

### Goal

Make Community Boards feel varied while keeping selections stable and reproducible.

### What was implemented

* Expanded the temporary pool to 14 vanilla-item contracts.
* Added `TemporaryContractSelector`.
* Added weighted selection using each contract's configured weight.
* Made selection deterministic from the Minecraft day and board position.
* Allowed different Community Boards to show different request sets.
* Kept the same board's contracts stable during the same day.

### Why it mattered

The board began behaving like a daily village system rather than a fixed demonstration. Deterministic selection also made behavior easier to test and persist.

### Challenges / fixes

* Balanced predictable board state with enough variation between locations and days.

### Next step

Track completed requests and prevent repeated rewards during the same board day.

## Milestone 7 — Daily Completion Tracking

### Goal

Stop unlimited Favour Token farming while clearly communicating completed work.

### What was implemented

* Recorded completed contract IDs per Community Board and Minecraft day.
* Prevented a completed contract from rewarding the player again that day.
* Added a completed marker to board previews.
* Reset completion state when a new Minecraft day began.

### Why it mattered

Rewards gained a meaningful daily limit, and the board could communicate progress across its active request set.

### Challenges / fixes

* Ensured repeat submissions did not consume items or grant additional tokens.

### Next step

Persist active and completed contract state through world saves and reloads.

## Milestone 8 — Persistent Community Board State

### Goal

Make each board retain its daily contracts and progress reliably across save and reload.

### What was implemented

* Added `CommunityBoardBlockEntity`.
* Stored active contract IDs on the block entity.
* Stored completed contract IDs on the block entity.
* Added NBT save and load support.
* Preserved the same board state through world save and reload during the same day.
* Refreshed active and completed state when the Minecraft day changed.

### Why it mattered

The board became a persistent world object with its own identity and progress, which is essential for future GUI, shop, and village progression features.

### Challenges / fixes

* Kept saved state ID-based so contracts could be resolved independently of their source.

### Next step

Add command-based submission and centralize submission behavior for future interfaces.

## Milestone 9 — Command System and Shared Submission Backend

### Goal

Provide a precise development interface and prevent submission logic from being duplicated.

### What was implemented

* Added `/cozycontracts submit <slot>`.
* Added the shorter `/cc submit <slot>` alias.
* Added `ContractSubmissionService` as the shared submission backend.
* Routed both commands and shift-right-click through the same completion rules.
* Added preview and board/contract debug commands.

### Why it mattered

A shared service keeps command and world interaction behavior consistent. It also provides the backend that future GUI submit buttons can call without rewriting contract rules.

### Challenges / fixes

* Preserved held-item-only submission while allowing a specific active contract slot to be selected by command.

### Next step

Replace prototype-only contract sourcing with a data-driven JSON registry.

## Milestone 10 — JSON/Datapack Contract Loading

### Goal

Allow contracts to be authored as data so the contract pool can grow without hardcoding Java objects.

### What was implemented

* Loaded contract JSON files from `data/<namespace>/contracts/*.json`.
* Added 14 built-in vanilla JSON contracts.
* Added `ContractRegistry` as the central source for contract lookup and listing.
* Retained `TemporaryContracts` as a safe fallback when no valid JSON contracts load.
* Added optional `required_mods` support for future compatibility packs.
* Skipped contracts with missing required mods.
* Skipped invalid contract entries with warnings instead of failing the full reload.
* Added commands for inspecting the registry and individual contracts.
* Documented the authoring format in `docs/CONTRACT_FORMAT.md`.

### Why it mattered

The core contract system became data-driven and ready for datapacks and future food-mod compatibility without adding hard dependencies.

### Challenges / fixes

* Added safe validation and fallback behavior so malformed or unavailable content did not leave boards unusable.
* Preserved board selection and submission behavior while changing the backing data source.

### Next step

Present the current board state through a focused in-game screen.

## Milestone 11 — Read-Only Community Board GUI

### Goal

Replace normal right-click chat spam with a clear visual summary of the current board's requests.

### What was implemented

* Added a client-side Community Board screen opened by normal right-click.
* Added minimal server-to-client networking for authoritative board display data.
* Displayed the current Minecraft day and three active contracts.
* Displayed each contract's requirement, reward, and available/completed status.
* Kept the screen read-only with no menu, inventory slots, or submit buttons.
* Preserved command submission and shift-right-click held-item submission.
* Kept `/cc preview` and its full command alias available as text previews.

### Why it mattered

This first GUI phase made board information easier to scan while keeping gameplay logic in the existing server-side services. It also established a client-safe route for future board interactions.

### Challenges / fixes

* Kept client-only screen classes isolated to avoid dedicated-server class-loading problems.
* Sent only the display data needed by the read-only screen.
* Corrected packet collection serialization during build verification.

### Screenshots

![Community Board GUI showing available requests](screenshots/10-board-gui-phase-1/board-gui-available-requests.png)

![Command submission and chat preview](screenshots/10-board-gui-phase-1/command-submit-and-chat-preview.png)

![Contract completion reward message](screenshots/10-board-gui-phase-1/contract-completion-reward-message.png)

![Community Board GUI showing completed request](screenshots/10-board-gui-phase-1/board-gui-completed-request.png)


### Next step

Add server-authoritative submit controls to the board GUI while continuing to use the shared submission backend.

## Milestone 12 — GUI Submit Buttons

### Goal

Allow players to submit a specific active request directly from the Community Board GUI while keeping all contract processing authoritative on the server.

### What was implemented

* Added one Submit button to each active contract entry.
* Disabled the button when its contract is already completed.
* Added a client-to-server packet containing the board position and zero-based contract slot.
* Validated the player, board position, interaction distance, and selected slot on the server.
* Reused `ContractSubmissionService` for held-item validation, consumption, rewards, and completion state.
* Refreshed the GUI with current board data after a valid submission request.
* Preserved command and shift-right-click submission.

### Why it mattered

The GUI now supports the complete request-and-reward loop without duplicating gameplay rules or trusting client-provided results. Commands, world interaction, and GUI controls all use the same submission backend.

### Challenges / fixes

* Kept the buttons lightweight without introducing menu or inventory slots.
* Added proximity and block identity checks before accepting a client request.
* Reused the existing screen packet to refresh completed status instead of adding a larger synchronization system.

### Screenshots

Screenshots will be added after in-game testing.

### Next step

Improve the Community Board's presentation and visual assets before expanding into reward-shop features.

## Milestone 13 — Inventory-Based GUI Submission

### Goal

Make GUI and command submission convenient by finding requested items across the player's inventory while preserving shift-right-click as an optional physical hand-in interaction.

### What was implemented

* Added a dedicated full-inventory contract slot submission path.
* Updated GUI Submit buttons to check all player inventory stacks.
* Updated `/cozycontracts submit <slot>` and `/cc submit <slot>` to use full-inventory submission.
* Counted exact-item requirements across multiple matching stacks.
* Allowed tag requirements to combine different matching items, such as mixed small flowers.
* Removed exactly the required total only after confirming enough matching items were available.
* Kept shift-right-click submission held-item-only.
* Reused the existing reward, completion, and player message logic.

### Why it mattered

Held-item submission was useful for proving the backend before the GUI existed. Once Submit buttons were added, closing the board and arranging the requested item in the main hand became tedious. Full-inventory GUI and command submission makes the board much easier to use, while shift-right-click remains available for players who prefer the direct hand-in interaction.

### Challenges / fixes

* Used a count-first approach so an unsuccessful submission never removes partial items.
* Removed items across stacks without exceeding the requested count.
* Supported mixed stacks for tag requirements without adding partial contract progress storage.
* Kept the held-stack and inventory submission paths separate so their intended behavior remains clear.

### Screenshots

Screenshots will be added after in-game testing.

### Next step

Improve Community Board visuals and feedback before beginning reward-shop work.

## Milestone 14 — Community Board Visual Polish

### Goal

Improve the Community Board's presentation and make its GUI easier to scan without changing any gameplay behavior.

### What was implemented

* Added horizontal facing so the board turns toward the player when placed.
* Replaced the full-cube model with a thin wooden notice board and supporting posts.
* Added simple pixel-art textures for the board front, sides, and posts.
* Updated blockstate rotations and collision shape to match the board's facing.
* Improved GUI title and row spacing.
* Made available and completed states easier to distinguish.
* Dimmed completed contract details and kept completed Submit buttons disabled.
* Clarified reward colors and the bottom inventory-submission instruction.
* Preserved the existing BlockEntity, submission paths, commands, and persistence behavior.

### Why it mattered

The Community Board is the central interaction point for the mod. Giving it a recognizable notice-board silhouette and improving the screen hierarchy makes the prototype feel more intentional and easier to use while staying close to Minecraft's visual language.

### Challenges / fixes

* Kept the model simple enough to rotate cleanly in all four horizontal directions.
* Matched the interaction shape to the thinner model instead of leaving full-cube collision.
* Focused only on presentation and usability so gameplay logic remained unchanged.

### Screenshots

![Community Board front view](screenshots/14-community-board-visual-polish/community-board-front-view.png)

![Community Board back and side view](screenshots/14-community-board-visual-polish/community-board-back-and-side-view.png)

![Polished Community Board GUI showing available requests](screenshots/14-community-board-visual-polish/polished-board-gui-available-requests.png)

![Polished Community Board GUI showing a completed request](screenshots/14-community-board-visual-polish/polished-board-gui-completed-request.png)

### Next step

Replace or refine the placeholder pixel art after visual testing, then begin the next gameplay milestone.

## Milestone 15 — Basic Favour Token Reward Shop

### Goal

Complete the first MVP gameplay loop by giving players a useful way to spend the Favour Tokens earned from community requests.

### What was implemented

* Added a Java-defined shop item model and registry.
* Added eight fixed vanilla rewards with individual Favour Token prices.
* Added server-side token counting and removal across multiple inventory stacks.
* Added server-authoritative purchase validation and reward delivery.
* Dropped purchased rewards near the player if their inventory was full.
* Added Requests and Shop sections to the Community Board GUI.
* Added Buy buttons for each available reward.
* Kept request submission, commands, shift-right-click hand-in, contract loading, and board persistence unchanged.

### Why it mattered

Players can now complete contracts, earn Favour Tokens, and spend those tokens on useful vanilla rewards. This closes the first complete MVP progression loop and gives contract completion an immediate purpose beyond accumulating currency.

### Challenges / fixes

* Kept purchases entirely server-side so the client cannot grant rewards or choose prices.
* Used atomic inventory payment so unaffordable purchases never remove partial token stacks.
* Kept shop stock Java-defined for this first version to avoid coupling it to contract JSON loading.
* Used a compact two-column layout so all rewards remain readable without item icons or inventory slots.

### Screenshots

Screenshots will be added after in-game testing.

### Next step

Evaluate the MVP economy and shop usability before considering JSON-driven stock, rotating rewards, or village-themed shops.

## Current Status

Cozy Contracts is an early prototype with a complete first gameplay loop: players can complete data-driven contracts, earn Favour Tokens, and spend them on vanilla rewards through the Community Board shop. Persistent board state, GUI and command submission, and shift-right-click hand-in remain available.

## Next Planned Work

1. Economy and shop usability balancing
2. Farmer's Delight/Create: Food compatibility contracts
3. Village Bond
4. Community Projects
