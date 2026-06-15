package com.lucasizumi.cozycontracts.network.packet;

import com.lucasizumi.cozycontracts.registry.ModBlocks;
import com.lucasizumi.cozycontracts.shop.ShopPurchaseService;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record PurchaseShopItemPacket(
        BlockPos boardPos,
        ResourceLocation shopItemId) {
    private static final double MAX_DISTANCE_SQUARED = 6.0D * 6.0D;

    public static void encode(
            PurchaseShopItemPacket packet,
            FriendlyByteBuf buffer) {
        buffer.writeBlockPos(packet.boardPos());
        buffer.writeResourceLocation(packet.shopItemId());
    }

    public static PurchaseShopItemPacket decode(FriendlyByteBuf buffer) {
        return new PurchaseShopItemPacket(
                buffer.readBlockPos(),
                buffer.readResourceLocation());
    }

    public static void handle(
            PurchaseShopItemPacket packet,
            Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> handleOnServer(packet, context.getSender()));
        context.setPacketHandled(true);
    }

    private static void handleOnServer(
            PurchaseShopItemPacket packet,
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

        ShopPurchaseService.purchase(player, packet.shopItemId());
    }
}
