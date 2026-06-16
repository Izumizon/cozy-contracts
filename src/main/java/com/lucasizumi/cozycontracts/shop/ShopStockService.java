package com.lucasizumi.cozycontracts.shop;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class ShopStockService {
    private static final Set<ShopCategory> TEMPORARY_BOARD_CATEGORIES = Set.of(
            ShopCategory.UNIVERSAL,
            ShopCategory.FARMING,
            ShopCategory.KITCHEN);

    private ShopStockService() {
    }

    public static Set<ShopCategory> getShopCategoriesForBoard(Level level, BlockPos boardPos) {
        // Temporary MVP rule only. Future settlement/district logic should replace this with
        // categories shaped by player-built districts, supplied items, and settlement progress.
        return Set.copyOf(TEMPORARY_BOARD_CATEGORIES);
    }

    public static List<ShopItem> getShopItemsForBoard(Level level, BlockPos boardPos) {
        LinkedHashSet<ShopItem> visibleItems = new LinkedHashSet<>(
                ShopRegistry.getItemsForCategories(getShopCategoriesForBoard(level, boardPos)));

        // MVP compatibility: keep the current reward shop fully visible until real district
        // filtering exists, even if some existing rewards belong to future-only categories.
        visibleItems.addAll(ShopRegistry.getAllItems());

        return List.copyOf(visibleItems);
    }

    public static boolean isShopItemAvailableForBoard(
            Level level,
            BlockPos boardPos,
            ResourceLocation shopItemId) {
        return getShopItemsForBoard(level, boardPos).stream()
                .anyMatch(item -> item.getId().equals(shopItemId));
    }
}
