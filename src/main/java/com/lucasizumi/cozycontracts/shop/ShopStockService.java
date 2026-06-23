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
        // Temporary alpha rule only. Future unlocks should come from deliberate settlement
        // development tracks and Community Projects, not nearby block-placement signals.
        return Set.copyOf(TEMPORARY_BOARD_CATEGORIES);
    }

    public static List<ShopItem> getShopItemsForBoard(Level level, BlockPos boardPos) {
        LinkedHashSet<ShopItem> visibleItems = new LinkedHashSet<>(
                ShopRegistry.getItemsForCategories(getShopCategoriesForBoard(level, boardPos)));

        // Keep all current supply bundles visible until deliberate settlement progression can
        // unlock categories without encouraging block spam around the Community Board.
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
