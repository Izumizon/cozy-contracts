package com.lucasizumi.cozycontracts.network.packet;

import com.lucasizumi.cozycontracts.contracts.CommunityBoardScreenService;
import com.lucasizumi.cozycontracts.contracts.ContractSubmissionService;
import com.lucasizumi.cozycontracts.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SubmitCommunityBoardContractPacket(BlockPos boardPos, int slotIndex) {
    private static final double MAX_DISTANCE_SQUARED = 6.0D * 6.0D;

    public static void encode(
            SubmitCommunityBoardContractPacket packet,
            FriendlyByteBuf buffer) {
        buffer.writeBlockPos(packet.boardPos());
        buffer.writeVarInt(packet.slotIndex());
    }

    public static SubmitCommunityBoardContractPacket decode(FriendlyByteBuf buffer) {
        return new SubmitCommunityBoardContractPacket(
                buffer.readBlockPos(),
                buffer.readVarInt());
    }

    public static void handle(
            SubmitCommunityBoardContractPacket packet,
            Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> handleOnServer(packet, context.getSender()));
        context.setPacketHandled(true);
    }

    private static void handleOnServer(
            SubmitCommunityBoardContractPacket packet,
            ServerPlayer player) {
        if (player == null) {
            return;
        }

        BlockPos boardPos = packet.boardPos();
        Level level = player.level();
        double distanceSquared = player.distanceToSqr(
                boardPos.getX() + 0.5D,
                boardPos.getY() + 0.5D,
                boardPos.getZ() + 0.5D);

        if (distanceSquared > MAX_DISTANCE_SQUARED) {
            player.sendSystemMessage(Component.literal(
                    "You are too far from the Community Board."));
            return;
        }

        if (!level.isLoaded(boardPos)
                || !level.getBlockState(boardPos).is(ModBlocks.COMMUNITY_BOARD.get())) {
            player.sendSystemMessage(Component.literal(
                    "That block is not a Community Board."));
            return;
        }

        ContractSubmissionService.submitContractSlotFromInventory(
                player,
                level,
                boardPos,
                packet.slotIndex());
        CommunityBoardScreenService.open(player, level, boardPos);
    }
}
