package com.lucasizumi.cozycontracts.contracts;

import com.lucasizumi.cozycontracts.network.ModNetworking;
import com.lucasizumi.cozycontracts.network.packet.OpenCommunityBoardScreenPacket;
import com.lucasizumi.cozycontracts.block.entity.CommunityBoardBlockEntity;
import com.lucasizumi.cozycontracts.kitchen.KitchenBoardService;
import com.lucasizumi.cozycontracts.projects.CommunityProjectService;
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

        ServerLevel serverLevel = (ServerLevel) level;
        CommunityBoardBlockEntity board =
                serverLevel.getBlockEntity(boardPos) instanceof CommunityBoardBlockEntity value
                        ? value
                        : null;

        List<OpenCommunityBoardScreenPacket.KitchenOrderEntry> kitchenEntries =
                KitchenBoardService.getKitchenOrdersForBoard(serverLevel, boardPos).stream()
                        .map(order -> new OpenCommunityBoardScreenPacket.KitchenOrderEntry(
                                order.getId(),
                                order.getTitle(),
                                order.getRequester(),
                                order.getType().name(),
                                order.getRequirementDisplay(),
                                order.getSupportDisplay(),
                                order.getRewardTokens(),
                                board == null
                                        ? 0
                                        : board.getKitchenDeliveryCount(day, order.getId()),
                                order.getDailyLimit()))
                        .toList();

        List<OpenCommunityBoardScreenPacket.ProjectEntry> projectEntries =
                CommunityProjectService.getProjectEntriesForBoard(serverLevel, boardPos);
        OpenCommunityBoardScreenPacket.ImprovementEntry improvementEntry =
                CommunityProjectService.getImprovementEntryForBoard(serverLevel, boardPos);

        ModNetworking.sendToPlayer(
                player,
                new OpenCommunityBoardScreenPacket(
                        boardPos,
                        day,
                        entries,
                        kitchenEntries,
                        projectEntries,
                        improvementEntry));
    }
}
