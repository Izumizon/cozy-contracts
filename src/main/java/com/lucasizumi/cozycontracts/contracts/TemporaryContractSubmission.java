package com.lucasizumi.cozycontracts.contracts;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public final class TemporaryContractSubmission {
    private TemporaryContractSubmission() {
    }

    public static void trySubmitHeldItem(Player player, Level level, BlockPos boardPos) {
        if (player instanceof ServerPlayer serverPlayer) {
            ContractSubmissionService.submitFirstMatchingHeldItem(serverPlayer, level, boardPos);
        }
    }
}
