package com.lucasizumi.cozycontracts.kitchen;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import java.util.List;

public final class KitchenBoardService {
    private KitchenBoardService() {
    }

    public static List<KitchenOrder> getKitchenOrdersForBoard(
            ServerLevel level,
            BlockPos boardPos) {
        return KitchenOrderRegistry.getAllOrders();
    }
}
