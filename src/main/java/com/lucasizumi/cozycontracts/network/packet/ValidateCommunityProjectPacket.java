package com.lucasizumi.cozycontracts.network.packet;

import com.lucasizumi.cozycontracts.contracts.CommunityBoardScreenService;
import com.lucasizumi.cozycontracts.projects.CommunityProjectService;
import com.lucasizumi.cozycontracts.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ValidateCommunityProjectPacket(
        BlockPos boardPos,
        ResourceLocation projectId) {
    private static final double MAX_DISTANCE_SQUARED = 6.0D * 6.0D;

    public static void encode(
            ValidateCommunityProjectPacket packet,
            FriendlyByteBuf buffer) {
        buffer.writeBlockPos(packet.boardPos());
        buffer.writeResourceLocation(packet.projectId());
    }

    public static ValidateCommunityProjectPacket decode(FriendlyByteBuf buffer) {
        return new ValidateCommunityProjectPacket(
                buffer.readBlockPos(),
                buffer.readResourceLocation());
    }

    public static void handle(
            ValidateCommunityProjectPacket packet,
            Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> handleOnServer(packet, context.getSender()));
        context.setPacketHandled(true);
    }

    private static void handleOnServer(
            ValidateCommunityProjectPacket packet,
            ServerPlayer player) {
        if (player == null || !isValidBoardInteraction(player, packet.boardPos())) {
            return;
        }

        ServerLevel level = player.serverLevel();
        CommunityProjectService.validateAndComplete(
                player,
                level,
                packet.boardPos(),
                packet.projectId());
        CommunityBoardScreenService.open(player, level, packet.boardPos());
    }

    private static boolean isValidBoardInteraction(ServerPlayer player, BlockPos boardPos) {
        ServerLevel level = player.serverLevel();
        double distanceSquared = player.distanceToSqr(
                boardPos.getX() + 0.5D,
                boardPos.getY() + 0.5D,
                boardPos.getZ() + 0.5D);

        if (distanceSquared > MAX_DISTANCE_SQUARED) {
            player.sendSystemMessage(Component.literal("You are too far from the Community Board."));
            return false;
        }

        if (!level.isLoaded(boardPos)
                || !level.getBlockState(boardPos).is(ModBlocks.COMMUNITY_BOARD.get())) {
            player.sendSystemMessage(Component.literal("That block is not a Community Board."));
            return false;
        }

        return true;
    }
}
