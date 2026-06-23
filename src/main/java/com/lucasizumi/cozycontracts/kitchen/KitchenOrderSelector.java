package com.lucasizumi.cozycontracts.kitchen;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class KitchenOrderSelector {
    private static final int DAILY_MENU_COUNT = 3;
    private static final int STANDING_ORDER_COUNT = 3;
    private static final long SEED_SALT = 0x6B17C4A2D93E508FL;

    private KitchenOrderSelector() {
    }

    public static List<KitchenOrder> selectForBoard(Level level, BlockPos boardPos) {
        long day = level.getDayTime() / 24000L;
        long seed = SEED_SALT
                ^ boardPos.asLong()
                ^ (day * 341873128712L);
        RandomSource random = RandomSource.create(seed);
        List<KitchenOrder> selected = new ArrayList<>();

        selectWeighted(
                selected,
                ordersOfType(KitchenOrderType.DAILY_MENU),
                DAILY_MENU_COUNT,
                random);
        selectWeighted(
                selected,
                ordersOfType(KitchenOrderType.STANDING_ORDER),
                STANDING_ORDER_COUNT,
                random);

        List<KitchenOrder> feastPrepOrders = ordersOfType(KitchenOrderType.FEAST_PREP);
        if (!feastPrepOrders.isEmpty() && random.nextBoolean()) {
            selectWeighted(selected, feastPrepOrders, 1, random);
        }

        return List.copyOf(selected);
    }

    private static List<KitchenOrder> ordersOfType(KitchenOrderType type) {
        return KitchenOrderRegistry.getAllOrders().stream()
                .filter(order -> order.getType() == type)
                .sorted(Comparator.comparing(order -> order.getId().toString()))
                .toList();
    }

    private static void selectWeighted(
            List<KitchenOrder> selected,
            List<KitchenOrder> candidates,
            int targetCount,
            RandomSource random) {
        List<KitchenOrder> available = new ArrayList<>(candidates);
        int targetSize = selected.size() + Math.min(targetCount, available.size());

        while (selected.size() < targetSize && !available.isEmpty()) {
            int totalWeight = available.stream()
                    .mapToInt(order -> Math.max(0, order.getWeight()))
                    .sum();

            if (totalWeight == 0) {
                selected.add(available.remove(random.nextInt(available.size())));
                continue;
            }

            int roll = random.nextInt(totalWeight);
            for (int index = 0; index < available.size(); index++) {
                roll -= Math.max(0, available.get(index).getWeight());
                if (roll < 0) {
                    selected.add(available.remove(index));
                    break;
                }
            }
        }
    }
}
