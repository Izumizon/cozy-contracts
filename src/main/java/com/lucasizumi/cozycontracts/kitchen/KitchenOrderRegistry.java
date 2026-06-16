package com.lucasizumi.cozycontracts.kitchen;

import com.lucasizumi.cozycontracts.CozyContracts;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

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
                    Items.BREAD,
                    4,
                    "Bread",
                    "Bring breakfast foods such as Bread or Eggs.",
                    "Supports the morning meal.",
                    2,
                    2,
                    Set.of("breakfast", "kitchen"),
                    20),
            order(
                    "warm_supper_pot",
                    "Warm Supper Pot",
                    "Community Kitchen",
                    KitchenOrderType.DAILY_MENU,
                    Items.MUSHROOM_STEW,
                    2,
                    "Mushroom Stew",
                    "Bring warm meals such as Mushroom Stew or Cooked Meat.",
                    "Supports the evening meal.",
                    3,
                    2,
                    Set.of("supper", "kitchen"),
                    20),
            order(
                    "miners_lunchbox",
                    "Miner's Lunchbox",
                    "Mining Crew",
                    KitchenOrderType.STANDING_ORDER,
                    Items.COOKED_BEEF,
                    3,
                    "Cooked Beef",
                    "Bring hearty packed meals for workers.",
                    "Future mining support.",
                    3,
                    3,
                    Set.of("lunch", "mining"),
                    15),
            order(
                    "farmhand_breakfast",
                    "Farmhand Breakfast",
                    "Farmers",
                    KitchenOrderType.STANDING_ORDER,
                    Items.EGG,
                    8,
                    "Eggs",
                    "Bring simple breakfast food for the fields.",
                    "Future farming support.",
                    2,
                    3,
                    Set.of("breakfast", "farming"),
                    15),
            order(
                    "school_snack_basket",
                    "School Snack Basket",
                    "Scholar Hall",
                    KitchenOrderType.STANDING_ORDER,
                    Items.COOKIE,
                    8,
                    "Cookies",
                    "Bring light snacks or baked goods.",
                    "Future scholar support.",
                    2,
                    3,
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
            Item item,
            int count,
            String displayName,
            String requirementDisplay,
            String supportDisplay,
            int rewardTokens,
            int dailyLimit,
            Set<String> tags,
            int weight) {
        return new KitchenOrder(
                ResourceLocation.parse(CozyContracts.MOD_ID + ":" + path),
                title,
                requester,
                type,
                new KitchenOrderRequirement(item, count, displayName),
                requirementDisplay,
                supportDisplay,
                rewardTokens,
                dailyLimit,
                tags,
                weight);
    }
}
