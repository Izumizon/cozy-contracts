package com.lucasizumi.cozycontracts.projects;

import com.lucasizumi.cozycontracts.registry.ModBlocks;
import com.lucasizumi.cozycontracts.registry.ModItems;
import com.lucasizumi.cozycontracts.settlement.Settlement;
import com.lucasizumi.cozycontracts.settlement.SettlementService;
import com.lucasizumi.cozycontracts.util.PlayerInventoryItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public final class ProjectSiteOrderDeliveryService {
    private static final double MAX_DISTANCE_SQUARED = 6.0D * 6.0D;

    private ProjectSiteOrderDeliveryService() {
    }

    public static boolean deliver(
            ServerPlayer player,
            ServerLevel level,
            BlockPos boardPos,
            ResourceLocation projectId,
            ResourceLocation orderId) {
        if (!isValidBoard(level, boardPos)) {
            player.sendSystemMessage(Component.literal("That block is not a Community Board."));
            return false;
        }

        if (player.distanceToSqr(
                boardPos.getX() + 0.5D,
                boardPos.getY() + 0.5D,
                boardPos.getZ() + 0.5D) > MAX_DISTANCE_SQUARED) {
            player.sendSystemMessage(Component.literal("You are too far from the Community Board."));
            return false;
        }

        Settlement settlement = SettlementService.getOrCreateSettlementForBoard(level, boardPos);
        if (!settlement.isProjectCompleted(projectId)
                || settlement.getCompletedProjectSite(projectId).isEmpty()) {
            player.sendSystemMessage(Component.literal(
                    "That completed Project Site is not available for local orders."));
            return false;
        }

        Optional<ProjectSiteOrder> order = ProjectSiteOrderRegistry.getAvailableById(orderId)
                .filter(value -> value.getProjectId().equals(projectId));
        if (order.isEmpty()) {
            player.sendSystemMessage(Component.literal(
                    "That Project Site order is not available here."));
            return false;
        }

        ProjectSiteOrder siteOrder = order.get();
        for (ProjectSiteOrderRequirement requirement : siteOrder.getRequirements()) {
            if (PlayerInventoryItems.countMatching(player, requirement::matches) < requirement.getCount()) {
                player.sendSystemMessage(Component.literal(
                        siteOrder.getTitle()
                                + " needs "
                                + requirement.getCount()
                                + " "
                                + requirement.getDisplayName()
                                + "."));
                return false;
            }
        }

        for (ProjectSiteOrderRequirement requirement : siteOrder.getRequirements()) {
            PlayerInventoryItems.removeMatching(player, requirement::matches, requirement.getCount());
        }

        giveReward(player, siteOrder.getRewardTokens());
        player.sendSystemMessage(Component.literal(
                "Delivered "
                        + siteOrder.getTitle()
                        + ". You received "
                        + siteOrder.getRewardTokens()
                        + " Favour Tokens."));
        return true;
    }

    private static boolean isValidBoard(ServerLevel level, BlockPos boardPos) {
        return level.isLoaded(boardPos)
                && level.getBlockState(boardPos).is(ModBlocks.COMMUNITY_BOARD.get());
    }

    private static void giveReward(ServerPlayer player, int rewardTokens) {
        ItemStack reward = new ItemStack(ModItems.FAVOUR_TOKEN.get(), rewardTokens);
        player.getInventory().add(reward);

        if (!reward.isEmpty()) {
            player.drop(reward, false);
        }
    }
}
