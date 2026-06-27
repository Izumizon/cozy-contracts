# Cozy Contracts 0.4.0-alpha Release Notes

0.4.0-alpha adds Buildable Community Projects Foundation Lite and Project Site Orders Lite. This is the first step toward settlement improvements that players build in the world instead of completing through abstract item donations.

## Highlights

- Added Project Marker block and recipe.
- Added a Projects tab to the Community Board.
- Added four starter buildable projects:
  - Village Fields
  - Garden Corner
  - Builder's Yard
  - Community Pantry
- Added loose validation around Project Markers.
- Added settlement-level completed project persistence.
- Added visible Community Improvement counts for Farming, Builder, Decor, and Kitchen.
- Added repeatable Standing Site Orders for completed Project Sites.
- Added Community Pantry Catering Orders Lite with multiple requirements.
- Added debug output for markers, assignments, validation, completed projects, and improvement counts.

## How Projects Work

1. Place a Project Marker near the settlement.
2. Open the Community Board.
3. Open the Projects tab.
4. Assign a starter project to the nearest unassigned Project Marker.
5. Build around the marker.
6. Validate the project from the Projects tab.
7. If the loose requirements are met, the project is registered as a Community Improvement for the settlement.

Validation is meant to guide, not judge. Missing requirements are shown as helpful messages such as "Village Fields still needs planted crops nearby." The system does not rate beauty, require exact shapes, or enforce strict blueprints.

## Starter Projects

### Village Fields

Adds one Farming improvement.

Loosely checks for:

- farmland nearby
- planted crops nearby
- water near the field

### Garden Corner

Adds one Decor improvement.

Loosely checks for:

- flowers nearby
- natural decoration such as leaves, grass, or dirt
- a cozy light source

### Builder's Yard

Adds one Builder improvement.

Loosely checks for:

- a work block such as a crafting table or stonecutter
- a storage block such as a chest or barrel
- building materials such as logs, stone, bricks, or glass
- a light source

### Community Pantry

Adds one Kitchen improvement.

Loosely checks for:

- a cooking block such as a smoker, furnace, or campfire
- a storage block such as a chest or barrel
- a preparation block such as a crafting table
- a light source

## Project Site Orders Lite

Completed Project Sites can now expose optional focused local orders in the Projects tab.

- Village Fields offers farming deliveries.
- Garden Corner offers garden and decoration deliveries.
- Builder's Yard offers building material deliveries.
- Community Pantry offers cooking deliveries and Catering Orders.

Standing Site Orders are repeatable and pay modest Favour Token rewards. Catering Orders require multiple foods or dishes and pay better because they require more preparation.

Optional Farmer's Delight and Create: Food pantry orders can appear when those mods are installed. Vanilla site orders remain the fallback/basic content, and Cozy Contracts still does not require those optional mods.

## What This Does Not Add Yet

- abstract material-donation projects
- token costs for projects
- auto-building
- generated structures
- exact blueprints
- Project Marker GUI
- houses or house validation
- residents
- Prosperity
- Storehouse
- Village Bond
- full districts
- Village Network
- project chains or repeatable projects
- JSON project loading
- shop unlocks
- Prosperity or Storehouse support from site orders

## Compatibility

Existing Requests, Shop, and Kitchen systems are unchanged. Optional Farmer's Delight, Create: Food, Create, and JEI support remain optional.

## Known Limitations

Community Projects are still intentionally light in this alpha. Farming, Builder, Decor, and Kitchen improvement counts do not yet unlock shop stock, alter main board contract weighting, or provide Prosperity bonuses. Project Site Orders Lite gives completed sites repeatable local work, not full project upgrades or settlement development tracks.
