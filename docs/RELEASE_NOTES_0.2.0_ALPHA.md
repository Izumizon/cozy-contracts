# Cozy Contracts 0.2.0-alpha

Cozy Contracts 0.2.0-alpha expands the Community Kitchen with optional Create: Food orders while keeping the mod fully standalone.

This milestone is content-only. It does not add a new screen, side UI, tracker, order items, pinned orders, multi-item orders, or new settlement systems.

## Added

- 10 optional Create: Food Community Kitchen orders
- 3 Daily Menu breakfast and dessert orders
- 5 Standing Order lunch, worker meal, and kitchen prep orders
- 2 low-limit Feast Prep dessert orders
- Cozy requester and support text for every new order
- required_mods: ["createfood"] gating on every new order

## Kitchen Order Highlights

- Waffle Breakfast Tray
- Pizza Lunchbox
- Donut Box
- Cheesecake Dessert
- Chocolate Treat Prep
- Sandwich Delivery
- Builder's Pizza Crate
- Worker's Breakfast Break
- Dessert Table Prep
- Village Feast Sweets

Prepared meals are weighted more heavily and reward more than the single low-reward chocolate prep order. Feast orders have higher rewards, low weights, and a daily limit of one.

## Optional Mod Support

Cozy Contracts still has no hard dependency on Create or Create: Food.

- Without Create: Food, all Create: Food Kitchen orders are skipped safely.
- With Create and Create: Food installed, the new Kitchen orders load through the existing JSON order system.
- Existing vanilla and Farmer's Delight Kitchen orders remain available.
- Existing Create: Food board contracts are unchanged.
- The enableCreateFood development toggle includes Create: Food's Farmer's Delight runtime dependency.

For development testing:

~~~powershell
.\gradlew.bat runClient -PenableCreateFood=true
~~~

## Validation Checklist

- Build Cozy Contracts without optional food mods.
- Build or launch with -PenableCreateFood=true.
- Confirm Create: Food Kitchen orders only load when createfood is installed.
- Confirm /cc debug kitchen lists the active Kitchen order pool correctly.
- Confirm the Kitchen tab remains scrollable and usable.
- Confirm vanilla and Farmer's Delight Kitchen content remains unchanged.

## Known Limitations

- Kitchen rewards and weights may change during alpha balance testing.
- Feast Prep remains an order type, not a separate advanced feast system.
- Resident Profiles, Taste Preferences, Prosperity, Storehouse, Village Bond, districts, and Village Network are not part of this milestone.
