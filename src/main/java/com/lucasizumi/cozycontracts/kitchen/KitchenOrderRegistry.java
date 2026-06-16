package com.lucasizumi.cozycontracts.kitchen;

import com.lucasizumi.cozycontracts.CozyContracts;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public final class KitchenOrderRegistry {
    private static final List<KitchenOrder> ORDERS = List.of(
            order(
                    "morning_bread_basket",
                    "Morning Bread Basket",
                    "Community Kitchen",
                    KitchenOrderType.DAILY_MENU,
                    "Bring breakfast foods such as Bread or Eggs.",
                    "Supports the morning meal.",
                    Set.of("breakfast", "kitchen"),
                    20),
            order(
                    "warm_supper_pot",
                    "Warm Supper Pot",
                    "Community Kitchen",
                    KitchenOrderType.DAILY_MENU,
                    "Bring warm meals such as Mushroom Stew or Cooked Meat.",
                    "Supports the evening meal.",
                    Set.of("supper", "kitchen"),
                    20),
            order(
                    "miners_lunchbox",
                    "Miner's Lunchbox",
                    "Mining Crew",
                    KitchenOrderType.STANDING_ORDER,
                    "Bring hearty packed meals for workers.",
                    "Future mining support.",
                    Set.of("lunch", "mining"),
                    15),
            order(
                    "farmhand_breakfast",
                    "Farmhand Breakfast",
                    "Farmers",
                    KitchenOrderType.STANDING_ORDER,
                    "Bring simple breakfast food for the fields.",
                    "Future farming support.",
                    Set.of("breakfast", "farming"),
                    15),
            order(
                    "school_snack_basket",
                    "School Snack Basket",
                    "Scholar Hall",
                    KitchenOrderType.STANDING_ORDER,
                    "Bring light snacks or baked goods.",
                    "Future scholar support.",
                    Set.of("snack", "scholar"),
                    15));

    private KitchenOrderRegistry() {
    }

    public static List<KitchenOrder> getAllOrders() {
        return ORDERS;
    }

    public static Optional<KitchenOrder> getById(ResourceLocation id) {
        return ORDERS.stream()
                .filter(order -> order.getId().equals(id))
                .findFirst();
    }

    private static KitchenOrder order(
            String path,
            String title,
            String requester,
            KitchenOrderType type,
            String requirementDisplay,
            String supportDisplay,
            Set<String> tags,
            int weight) {
        return new KitchenOrder(
                ResourceLocation.parse(CozyContracts.MOD_ID + ":" + path),
                title,
                requester,
                type,
                requirementDisplay,
                supportDisplay,
                tags,
                weight);
    }
}
