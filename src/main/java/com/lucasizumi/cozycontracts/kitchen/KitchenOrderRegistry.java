package com.lucasizumi.cozycontracts.kitchen;

import com.lucasizumi.cozycontracts.CozyContracts;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class KitchenOrderRegistry {
    private static final Logger LOGGER = LogUtils.getLogger();

    /*
     * Java-defined kitchen orders are the prototype source for now. A future
     * datapack/JSON loader can replace or extend this snapshot without changing
     * callers that resolve orders through this registry.
     */
    private static final RegistrySnapshot SNAPSHOT = createSnapshot(List.of(
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
                    15)));

    private KitchenOrderRegistry() {
    }

    public static List<KitchenOrder> getAllOrders() {
        return SNAPSHOT.orders();
    }

    public static Optional<KitchenOrder> getById(ResourceLocation id) {
        return Optional.ofNullable(SNAPSHOT.ordersById().get(id));
    }

    public static Map<ResourceLocation, KitchenOrder> getAllById() {
        return SNAPSHOT.ordersById();
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
                KitchenOrderRequirement.exactItem(item, count, displayName),
                requirementDisplay,
                supportDisplay,
                rewardTokens,
                dailyLimit,
                tags,
                weight);
    }

    private static RegistrySnapshot createSnapshot(List<KitchenOrder> orders) {
        Map<ResourceLocation, KitchenOrder> ordersById = new LinkedHashMap<>();

        for (KitchenOrder order : orders) {
            if (!order.getRequirement().isValid()) {
                LOGGER.warn(
                        "Kitchen order {} has an invalid requirement {}; it will still be registered but cannot match items",
                        order.getId(),
                        order.getRequirement().getDebugText());
            }

            if (ordersById.putIfAbsent(order.getId(), order) != null) {
                LOGGER.warn(
                        "Ignoring duplicate kitchen order ID {}; keeping the first definition",
                        order.getId());
            }
        }

        return new RegistrySnapshot(
                List.copyOf(ordersById.values()),
                Map.copyOf(ordersById));
    }

    private record RegistrySnapshot(
            List<KitchenOrder> orders,
            Map<ResourceLocation, KitchenOrder> ordersById) {
    }
}
