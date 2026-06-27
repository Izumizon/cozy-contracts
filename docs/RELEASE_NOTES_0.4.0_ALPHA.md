# Cozy Contracts 0.4.0-alpha Release Notes

0.4.0-alpha adds Buildable Community Projects Foundation Lite. This is the first step toward settlement improvements that players build in the world instead of completing through abstract item donations.

## Highlights

- Added Project Marker block and recipe.
- Added a Projects tab to the Community Board.
- Added three starter buildable projects:
  - Village Fields
  - Garden Corner
  - Builder's Yard
- Added loose validation around Project Markers.
- Added settlement-level completed project persistence.
- Added visible Community Improvement counts for Farming, Builder, and Decor.
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

## Compatibility

Existing Requests, Shop, and Kitchen systems are unchanged. Optional Farmer's Delight, Create: Food, Create, and JEI support remain optional.

## Known Limitations

Community Projects are visible progress only in this alpha. Farming, Builder, and Decor improvement counts do not yet unlock shop stock, alter contract weighting, or provide Prosperity bonuses.
