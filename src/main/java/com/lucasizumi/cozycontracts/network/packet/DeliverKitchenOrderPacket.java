package com.lucasizumi.cozycontracts.network.packet;

import com.lucasizumi.cozycontracts.contracts.CommunityBoardScreenService;
import com.lucasizumi.cozycontracts.kitchen.KitchenDeliveryService;
import com.lucasizumi.cozycontracts.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record DeliverKitchenOrderPacket(
        BlockPos boardPos,
        ResourceLocation orderId) {

    public static void encode(
            DeliverKitchenOrderPacket packet,
            FriendlyByteBuf buffer) {
        buffer.writeBlockPos(packet.boardPos());
        buffer.writeResourceLocation(packet.orderId());
    }

    public static DeliverKitchenOrderPacket decode(FriendlyByteBuf buffer) {
        return new DeliverKitchenOrderPacket(
                buffer.readBlockPos(),
                buffer.readResourceLocation());
    }

    public static void handle(
            DeliverKitchenOrderPacket packet,
            Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> handleOnServer(packet, context.getSender()));
        context.setPacketHandled(true);
    }

    private static void handleOnServer(
            DeliverKitchenOrderPacket packet,
            ServerPlayer player) {
        if (player == null) {
            return;
        }

        ServerLevel level = player.serverLevel();
        KitchenDeliveryService.deliver(
                player,
                level,
                packet.boardPos(),
                packet.orderId());
        if (level.isLoaded(packet.boardPos())
                && level.getBlockState(packet.boardPos()).is(ModBlocks.COMMUNITY_BOARD.get())) {
            CommunityBoardScreenService.open(player, level, packet.boardPos());
        }
    }
}
