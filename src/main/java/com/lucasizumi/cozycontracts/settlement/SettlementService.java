package com.lucasizumi.cozycontracts.settlement;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

public final class SettlementService {
    private static final String DATA_NAME = "cozy_contracts_settlements";

    private SettlementService() {
    }

    public static Settlement getOrCreateSettlementForBoard(
            ServerLevel level,
            BlockPos boardPos) {
        return getNearestSettlement(level, boardPos, 64)
                .orElseGet(() -> createSettlement(level, boardPos));
    }

    public static Optional<Settlement> getNearestSettlement(
            ServerLevel level,
            BlockPos boardPos,
            int radius) {
        double radiusSquared = radius * radius;
        return getData(level).getSettlements().stream()
                .filter(settlement -> distanceSquared(boardPos, settlement.getCenter())
                        <= radiusSquared)
                .min(Comparator.comparingDouble(
                        settlement -> distanceSquared(boardPos, settlement.getCenter())));
    }

    public static int getSettlementCount(ServerLevel level) {
        return getData(level).getSettlementCount();
    }

    public static void markSettlementsDirty(ServerLevel level) {
        getData(level).setDirty();
    }

    private static Settlement createSettlement(ServerLevel level, BlockPos boardPos) {
        Settlement settlement = new Settlement(
                UUID.randomUUID().toString(),
                boardPos.immutable(),
                level.dimension().location(),
                level.getDayTime() / 24000L);
        getData(level).addSettlement(settlement);
        return settlement;
    }

    private static SettlementSavedData getData(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                SettlementSavedData::load,
                SettlementSavedData::new,
                DATA_NAME);
    }

    private static double distanceSquared(BlockPos first, BlockPos second) {
        double x = first.getX() - second.getX();
        double y = first.getY() - second.getY();
        double z = first.getZ() - second.getZ();
        return x * x + y * y + z * z;
    }
}
