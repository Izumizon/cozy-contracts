# Cozy Contracts 0.3.0-alpha

Cozy Contracts 0.3.0-alpha reworks the Favour Token shop into a practical settlement supply market. The shop now focuses on useful bundles that help players build, cook, farm, and decorate without becoming a creative-mode catalogue or selling major progression skips.

## Shop Supply Rework

- Replaced the small generic reward list with 39 Java-defined supply entries.
- Added universal convenience stock such as Name Tags, Bells, Saddles, and Lantern bundles.
- Added farming supplies including dirt, bone meal, logs, crops, seeds, hay bales, and saplings.
- Added Kitchen supplies including sugar, eggs, milk, cocoa beans, wheat, charcoal, bowls, and bottles.
- Added builder supplies including stone, sand, glass, clay, bricks, stone bricks, and scaffolding.
- Added decorator supplies including candles, wool, dyes, flowers, leaves, terracotta, and flower pots.
- Kept category metadata on every shop item.

## Shop Interface

- Kept the existing Community Board Shop tab.
- Added mouse-wheel scrolling for the larger stock list.
- Added All, Village, Farming, Kitchen, Builder, and Decor filter tabs using existing shop category metadata.
- Changing filters resets the Shop list to the top.
- Kept the two-column Community Board style.
- Kept bundle counts visible in item names.
- Added the helper line: `Spend Favour Tokens on settlement supplies.`

## Purchase Safety

- Purchases remain server-authoritative.
- Token payment remains count-first and atomic.
- Invalid or unavailable packet item IDs remain rejected.
- Purchased items still enter the player inventory when possible.
- Leftover rewards still drop near the player when the inventory is full.
- Direct contract and Kitchen inputs are priced to avoid positive token loops.

## Design Boundaries

This update does not add full districts, settlement development tracks, Community Projects, Prosperity, Storehouse, Village Bond, or nearby block detection.

Shop categories are not controlled by placing composters, smokers, flowers, or other signal blocks around the Community Board. Future stock unlocks may use deliberate settlement development tracks and Community Projects instead.

## Testing Checklist

- Open the Community Board Shop tab and scroll through the full stock list.
- Confirm each category tab shows the expected supply group and All shows all 39 entries.
- Confirm changing categories resets scrolling to the top.
- Confirm Buy buttons remain aligned with visible rows.
- Purchase a normal supply bundle.
- Confirm the exact Favour Token cost is removed.
- Fill the inventory and confirm leftover rewards drop near the player.
- Confirm invalid or unavailable shop item IDs are rejected server-side.
- Confirm Requests and Kitchen tabs still work.
- Confirm daily Kitchen selection and deliveries still work.

## Known Limitations

- All current alpha supply bundles remain visible while category-based settlement unlocks are still future work.
- Stock remains Java-defined; shop JSON loading is not included.
- Prices are alpha balance values and may be tuned after playtesting.
