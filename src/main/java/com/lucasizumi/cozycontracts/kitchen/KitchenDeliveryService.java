package com.lucasizumi.cozycontracts.kitchen;

import com.lucasizumi.cozycontracts.block.entity.CommunityBoardBlockEntity;
import com.lucasizumi.cozycontracts.registry.ModBlocks;
import com.lucasizumi.cozycontracts.registry.ModItems;
import com.lucasizumi.cozycontracts.util.PlayerInventoryItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public final class KitchenDeliveryService {
    public static final double MAX_DISTANCE_SQUARED = 6.0D * 6.0D;

    private KitchenDeliveryService() {
    }

    public static boolean deliver(
            ServerPlayer player,
            ServerLevel level,
            BlockPos boardPos,
            ResourceLocation orderId) {
        if (!isValidBoard(level, boardPos)) {
            player.sendSystemMessage(Component.literal("That block is not a Community Board."));
            return false;
        }

        if (player.distanceToSqr(
                boardPos.getX() + 0.5D,
                boardPos.getY() + 0.5D,
                boardPos.getZ() + 0.5D) > MAX_DISTANCE_SQUARED) {
            player.sendSystemMessage(Component.literal(
                    "You are too far from the Community Board."));
            return false;
        }

        Optional<KitchenOrder> order = KitchenBoardService.getKitchenOrdersForBoard(level, boardPos)
                .stream()
                .filter(value -> value.getId().equals(orderId))
                .findFirst();
        if (order.isEmpty()) {
            player.sendSystemMessage(Component.literal(
                    "This kitchen order is not available here."));
            return false;
        }

        if (!(level.getBlockEntity(boardPos) instanceof CommunityBoardBlockEntity board)) {
            player.sendSystemMessage(Component.literal("That block is not a Community Board."));
            return false;
        }

        KitchenOrder kitchenOrder = order.get();
        long day = level.getDayTime() / 24000L;
        int deliveredCount = board.getKitchenDeliveryCount(day, kitchenOrder.getId());
        if (deliveredCount >= kitchenOrder.getDailyLimit()) {
            player.sendSystemMessage(Component.literal(
                    kitchenOrder.getTitle() + " has already been filled for today."));
            return false;
        }

        KitchenOrderRequirement requirement = kitchenOrder.getRequirement();
        if (!PlayerInventoryItems.removeMatching(
                player,
                requirement::matches,
                requirement.getCount())) {
            player.sendSystemMessage(Component.literal(
                    "You need "
                            + requirement.getCount()
                            + " "
                            + requirement.getDisplayName()
                            + " for "
                            + kitchenOrder.getTitle()
                            + "."));
            return false;
        }

        giveReward(player, kitchenOrder.getRewardTokens());
        board.incrementKitchenDeliveryCount(day, kitchenOrder.getId());
        player.sendSystemMessage(Component.literal(
                "Delivered "
                        + kitchenOrder.getTitle()
                        + ". You received "
                        + kitchenOrder.getRewardTokens()
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
