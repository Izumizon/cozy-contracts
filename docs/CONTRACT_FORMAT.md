# Contract JSON Format

Cozy Contracts loads contract definitions from Minecraft datapacks and built-in data resources.

## File Location

Place each contract in:

```text
data/<namespace>/contracts/<contract_name>.json
```

For example:

```text
data/cozy_contracts/contracts/bakers_morning_rush.json
```

The contract ID comes from the namespace and file path. The example above becomes `cozy_contracts:bakers_morning_rush`. Do not add an `id` field to the JSON.

## Item Requirement Example

```json
{
  "title": "Baker's Morning Rush",
  "requester": "Baker",
  "category": "kitchen",
  "difficulty": "EASY",
  "weight": 35,
  "reward_tokens": 5,
  "requirement": {
    "type": "item",
    "id": "minecraft:bread",
    "count": 8,
    "display_name": "Bread"
  },
  "required_mods": []
}
```

## Tag Requirement Example

```json
{
  "title": "Roof Repairs",
  "requester": "Mason",
  "category": "builder",
  "difficulty": "MEDIUM",
  "weight": 15,
  "reward_tokens": 7,
  "requirement": {
    "type": "tag",
    "id": "minecraft:logs",
    "count": 32,
    "display_name": "Logs"
  }
}
```

## Fields

| Field | Description |
| --- | --- |
| `title` | Player-facing contract title. |
| `requester` | Village role or requester shown to the player. |
| `category` | Group such as `kitchen`, `farmer`, `decorator`, or `builder`. |
| `difficulty` | Balancing tier: `EASY`, `MEDIUM`, or `HARD`. Parsing is case-insensitive. |
| `weight` | Positive weighted-selection value. Higher values make selection more likely. |
| `reward_tokens` | Positive number of Favour Tokens awarded on completion. |
| `requirement` | Object describing one item or tag requirement. |
| `requirement.type` | `item` for one exact item or `tag` for any item in a tag. |
| `requirement.id` | Resource location of the item or item tag. |
| `requirement.count` | Positive number of matching items required. |
| `requirement.display_name` | Readable name used in board and submission messages. |
| `required_mods` | Optional list of mod IDs that must be loaded. Defaults to an empty list. |

## Requirement Types

- `item`: matches one exact registered item, such as `minecraft:bread`.
- `tag`: matches any item in an item tag, such as `minecraft:logs`.

## Completion Methods

Contracts can be completed through the Community Board GUI, commands, or shift-right-click hand-in.

- GUI submission searches the full player inventory for matching items.
- `/cozycontracts submit <slot>` and `/cc submit <slot>` search the full player inventory.
- Shift-right-click hand-in remains held-stack based and only checks the player's main-hand stack.

The JSON format is the same for all completion methods.

## Loading Rules

- Contracts with missing required mods are skipped safely.
- Invalid contracts are skipped and logged with a warning instead of crashing reload.
- If no valid JSON contracts load, Cozy Contracts uses its temporary Java fallback pool.
- Datapacks can add or override available contract resources through Minecraft's normal resource-pack priority.

This format is intended to support future compatibility contracts for Farmer's Delight, Create: Food, and other cooking or farming mods.
