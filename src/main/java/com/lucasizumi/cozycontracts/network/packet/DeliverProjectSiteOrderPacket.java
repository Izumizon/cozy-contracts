package com.lucasizumi.cozycontracts.network.packet;

import com.lucasizumi.cozycontracts.contracts.CommunityBoardScreenService;
import com.lucasizumi.cozycontracts.projects.ProjectSiteOrderDeliveryService;
import com.lucasizumi.cozycontracts.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record DeliverProjectSiteOrderPacket(
        BlockPos boardPos,
        ResourceLocation projectId,
        ResourceLocation orderId) {

    public static void encode(
            DeliverProjectSiteOrderPacket packet,
            FriendlyByteBuf buffer) {
        buffer.writeBlockPos(packet.boardPos());
        buffer.writeResourceLocation(packet.projectId());
        buffer.writeResourceLocation(packet.orderId());
    }

    public static DeliverProjectSiteOrderPacket decode(FriendlyByteBuf buffer) {
        return new DeliverProjectSiteOrderPacket(
                buffer.readBlockPos(),
                buffer.readResourceLocation(),
                buffer.readResourceLocation());
    }

    public static void handle(
            DeliverProjectSiteOrderPacket packet,
            Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> handleOnServer(packet, context.getSender()));
        context.setPacketHandled(true);
    }

    private static void handleOnServer(
            DeliverProjectSiteOrderPacket packet,
            ServerPlayer player) {
        if (player == null) {
            return;
        }

        ServerLevel level = player.serverLevel();
        ProjectSiteOrderDeliveryService.deliver(
                player,
                level,
                packet.boardPos(),
                packet.projectId(),
                packet.orderId());
        if (level.isLoaded(packet.boardPos())
                && level.getBlockState(packet.boardPos()).is(ModBlocks.COMMUNITY_BOARD.get())) {
            CommunityBoardScreenService.open(player, level, packet.boardPos());
        }
    }
}
