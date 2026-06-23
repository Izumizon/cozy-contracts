package com.lucasizumi.cozycontracts.kitchen;

import com.lucasizumi.cozycontracts.block.entity.CommunityBoardBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public final class KitchenBoardService {
    private KitchenBoardService() {
    }

    public static List<KitchenOrder> getKitchenOrdersForBoard(
            ServerLevel level,
            BlockPos boardPos) {
        if (!(level.getBlockEntity(boardPos) instanceof CommunityBoardBlockEntity board)) {
            return KitchenOrderSelector.selectForBoard(level, boardPos);
        }

        long day = level.getDayTime() / 24000L;
        if (board.hasActiveKitchenOrdersForDay(day)) {
            List<ResourceLocation> storedIds = board.getActiveKitchenOrderIds(day);
            List<KitchenOrder> storedOrders = resolveOrders(storedIds);
            if (storedOrders.size() == storedIds.size()) {
                return storedOrders;
            }
        }

        List<KitchenOrder> selected = KitchenOrderSelector.selectForBoard(level, boardPos);
        board.setActiveKitchenOrderIds(
                day,
                selected.stream().map(KitchenOrder::getId).toList());
        return selected;
    }

    private static List<KitchenOrder> resolveOrders(List<ResourceLocation> orderIds) {
        List<KitchenOrder> resolved = new ArrayList<>();

        for (ResourceLocation orderId : new LinkedHashSet<>(orderIds)) {
            KitchenOrderRegistry.getById(orderId).ifPresent(resolved::add);
        }

        return List.copyOf(resolved);
    }
}
