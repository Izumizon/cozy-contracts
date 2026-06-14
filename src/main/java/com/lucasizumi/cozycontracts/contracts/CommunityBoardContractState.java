package com.lucasizumi.cozycontracts.contracts;

import com.lucasizumi.cozycontracts.block.entity.CommunityBoardBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public final class CommunityBoardContractState {
    private static final int ACTIVE_CONTRACT_COUNT = 3;

    private CommunityBoardContractState() {
    }

    public static List<Contract> getActiveContracts(Level level, BlockPos boardPos) {
        BlockEntity blockEntity = level.getBlockEntity(boardPos);
        if (!(blockEntity instanceof CommunityBoardBlockEntity board)) {
            return TemporaryContractSelector.getActiveContracts(level, boardPos);
        }

        long day = level.getDayTime() / 24000L;
        int expectedCount = Math.min(
                ACTIVE_CONTRACT_COUNT,
                ContractRegistry.getAllContracts().size());

        if (board.hasActiveContractsForDay(day)) {
            List<Contract> storedContracts =
                    resolveContracts(board.getActiveContractIds(day));
            if (storedContracts.size() == expectedCount) {
                return storedContracts;
            }
        }

        // Definitions are still temporary and hardcoded; only their selected IDs
        // are persisted on the board until JSON contracts replace this prototype.
        List<Contract> generated =
                TemporaryContractSelector.getActiveContracts(level, boardPos);
        board.setActiveContractIds(
                day,
                generated.stream().map(Contract::getId).toList());
        return generated;
    }

    private static List<Contract> resolveContracts(List<ResourceLocation> contractIds) {
        List<Contract> resolved = new ArrayList<>();

        for (ResourceLocation contractId : new LinkedHashSet<>(contractIds)) {
            ContractRegistry.getById(contractId).ifPresent(resolved::add);
        }

        return List.copyOf(resolved);
    }
}
