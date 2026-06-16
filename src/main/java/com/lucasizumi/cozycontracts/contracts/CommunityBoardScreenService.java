package com.lucasizumi.cozycontracts.contracts;

import com.lucasizumi.cozycontracts.network.ModNetworking;
import com.lucasizumi.cozycontracts.network.packet.OpenCommunityBoardScreenPacket;
import com.lucasizumi.cozycontracts.kitchen.KitchenBoardService;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Set;

public final class CommunityBoardScreenService {
    private CommunityBoardScreenService() {
    }

    public static void open(ServerPlayer player, Level level, BlockPos boardPos) {
        long day = level.getDayTime() / 24000L;
        Set<net.minecraft.resources.ResourceLocation> completedIds =
                CommunityBoardCompletions.getCompleted(level, boardPos, day);

        List<OpenCommunityBoardScreenPacket.ContractEntry> entries =
                CommunityBoardContractState.getActiveContracts(level, boardPos).stream()
                        .map(contract -> new OpenCommunityBoardScreenPacket.ContractEntry(
                                contract.getTitle(),
                                contract.getRequester(),
                                contract.getRequirement().getPreviewText(),
                                contract.getRewardTokens(),
                                completedIds.contains(contract.getId())))
                        .toList();

        List<OpenCommunityBoardScreenPacket.KitchenOrderEntry> kitchenEntries =
                KitchenBoardService.getKitchenOrdersForBoard((ServerLevel) level, boardPos).stream()
                        .map(order -> new OpenCommunityBoardScreenPacket.KitchenOrderEntry(
                                order.getTitle(),
                                order.getRequester(),
                                order.getType().name(),
                                order.getRequirementDisplay(),
                                order.getSupportDisplay()))
                        .toList();

        ModNetworking.sendToPlayer(
                player,
                new OpenCommunityBoardScreenPacket(boardPos, day, entries, kitchenEntries));
    }
}
