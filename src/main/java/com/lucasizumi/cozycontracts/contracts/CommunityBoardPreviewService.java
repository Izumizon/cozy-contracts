package com.lucasizumi.cozycontracts.contracts;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Set;

public final class CommunityBoardPreviewService {
    private CommunityBoardPreviewService() {
    }

    public static void sendPreview(Player player, Level level, BlockPos boardPos) {
        long day = level.getDayTime() / 24000L;
        Set<net.minecraft.resources.ResourceLocation> completed =
                CommunityBoardCompletions.getCompleted(level, boardPos, day);

        player.sendSystemMessage(Component.literal("Community Requests - Day " + day));

        int number = 1;
        for (Contract contract : CommunityBoardContractState.getActiveContracts(level, boardPos)) {
            String completionPrefix = completed.contains(contract.getId()) ? "[Completed] " : "";
            player.sendSystemMessage(Component.literal(
                    number + ". " + completionPrefix + contract.getTitle()));
            player.sendSystemMessage(Component.literal(
                    "   "
                            + contract.getRequester()
                            + " needs: "
                            + contract.getRequirement().getPreviewText()));
            player.sendSystemMessage(Component.literal(
                    "   Reward: "
                            + contract.getRewardTokens()
                            + " Favour Tokens"));
            number++;
        }

        player.sendSystemMessage(Component.literal(
                "Hold the requested item and use /cc submit <1-3> to submit a request."));
    }
}
