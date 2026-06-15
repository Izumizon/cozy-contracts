package com.lucasizumi.cozycontracts.contracts;

import com.lucasizumi.cozycontracts.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public final class ContractSubmissionService {
    private ContractSubmissionService() {
    }

    public static boolean submitContractSlot(
            ServerPlayer player,
            Level level,
            BlockPos boardPos,
            int slotIndex) {
        List<Contract> activeContracts =
                CommunityBoardContractState.getActiveContracts(level, boardPos);

        if (slotIndex < 0 || slotIndex >= activeContracts.size()) {
            player.sendSystemMessage(Component.literal("Request slot must be 1, 2, or 3."));
            return false;
        }

        return submitContract(player, level, boardPos, activeContracts.get(slotIndex));
    }

    public static boolean submitFirstMatchingHeldItem(
            ServerPlayer player,
            Level level,
            BlockPos boardPos) {
        ItemStack heldStack = player.getMainHandItem();

        for (Contract contract : CommunityBoardContractState.getActiveContracts(level, boardPos)) {
            if (contract.getRequirement().matches(heldStack)) {
                return submitContract(player, level, boardPos, contract);
            }
        }

        player.sendSystemMessage(Component.literal("This item does not match any current request."));
        return false;
    }

    private static boolean submitContract(
            ServerPlayer player,
            Level level,
            BlockPos boardPos,
            Contract contract) {
        long day = level.getDayTime() / 24000L;
        ContractRequirement requirement = contract.getRequirement();
        ItemStack heldStack = player.getMainHandItem();

        if (CommunityBoardCompletions.isCompleted(level, boardPos, day, contract.getId())) {
            player.sendSystemMessage(Component.literal(
                    "This request has already been completed today. "
                            + "Come back tomorrow for new community requests."));
            return false;
        }

        if (!requirement.matches(heldStack) || heldStack.getCount() < requirement.getCount()) {
            player.sendSystemMessage(Component.literal(
                    contract.getTitle()
                            + " needs "
                            + requirement.getPreviewText()
                            + "."));
            return false;
        }

        heldStack.shrink(requirement.getCount());
        giveReward(player, contract.getRewardTokens());
        CommunityBoardCompletions.markCompleted(level, boardPos, day, contract.getId());
        player.sendSystemMessage(Component.literal(
                "Completed "
                        + contract.getTitle()
                        + "! "
                        + contract.getRequester()
                        + " thanks you."));
        player.sendSystemMessage(Component.literal(
                "You received "
                        + contract.getRewardTokens()
                        + " Favour Tokens."));
        return true;
    }

    private static void giveReward(ServerPlayer player, int rewardTokens) {
        ItemStack reward = new ItemStack(ModItems.FAVOUR_TOKEN.get(), rewardTokens);
        player.getInventory().add(reward);

        if (!reward.isEmpty()) {
            player.drop(reward, false);
        }
    }
}
