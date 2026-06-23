# Kitchen Order JSON Format

Kitchen orders define the Community Kitchen's Daily Menu and Standing Orders.

Kitchen order files live at:

```text
data/<namespace>/kitchen_orders/<order_name>.json
```

The kitchen order ID comes from the file path. For example:

```text
data/cozy_contracts/kitchen_orders/morning_bread_basket.json
```

becomes:

```text
cozy_contracts:morning_bread_basket
```

## Full Example

```json
{
  "title": "Morning Bread Basket",
  "requester": "Community Kitchen",
  "type": "daily_menu",
  "requirement": {
    "type": "item",
    "id": "minecraft:bread",
    "count": 4,
    "display": "Bread"
  },
  "support_display": "Supports the morning meal.",
  "reward_tokens": 2,
  "daily_limit": 2,
  "weight": 20,
  "required_mods": []
}
```

## Fields

`title`
: Display name shown in the Kitchen tab.

`requester`
: Group or person requesting the food.

`type`
: Kitchen order type. Valid values are `daily_menu`, `standing_order`, and `feast_prep`.

`requirement`
: The item or tag requirement for delivery.

`requirement.type`
: `item` for an exact item requirement or `tag` for an item tag requirement.

`requirement.id`
: Item ID or item tag ID.

`requirement.count`
: Number of matching items required.

`requirement.display`
: Human-readable requirement name used in messages and debug output.

`support_display`
: Optional short text describing what the order supports.

`reward_tokens`
: Number of Favour Tokens granted per delivery.

`daily_limit`
: Number of times this order can be delivered per board per Minecraft day.

`weight`
: Optional selection weight for future Kitchen order selection.

`required_mods`
: Optional array of mod IDs. If any listed mod is missing, the order is skipped.

Kitchen delivery rewards and daily limits are balance values. They may change during alpha development as the Kitchen loop is tested.

## Item Requirement

```json
"requirement": {
  "type": "item",
  "id": "minecraft:cookie",
  "count": 8,
  "display": "Cookies"
}
```

Item requirements must reference a known item ID. Unknown item IDs are skipped with a warning.

## Tag Requirement

```json
"requirement": {
  "type": "tag",
  "id": "minecraft:fishes",
  "count": 4,
  "display": "Fish"
}
```

Tag requirements are supported by the data model and delivery matching. Tag IDs are not deeply validated during loading, so datapack authors should test them in-game.

## Required Mods

```json
"required_mods": ["farmersdelight"]
```

Orders with missing required mods are skipped safely. This supports the built-in Farmer's Delight and Create: Food Kitchen orders while keeping optional food mods optional.

Optional modded Kitchen orders should include `required_mods` so standalone Cozy Contracts worlds can still load safely. Farmer's Delight orders use `["farmersdelight"]`, and Create: Food orders use `["createfood"]`.

## Validation

Invalid Kitchen order JSON entries are skipped with warnings.

If no valid JSON Kitchen orders load, Cozy Contracts falls back to its built-in Java Kitchen order defaults so the Community Kitchen remains usable.

## Future Use

This format is intended to support food mod compatibility and datapacks. Resident Profiles, Taste Preferences, Prosperity, Storehouse support, and advanced Kitchen selection are planned future systems and are not part of this format yet.
