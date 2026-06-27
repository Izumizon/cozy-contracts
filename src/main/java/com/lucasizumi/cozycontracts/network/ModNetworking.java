package com.lucasizumi.cozycontracts.network;

import com.lucasizumi.cozycontracts.CozyContracts;
import com.lucasizumi.cozycontracts.network.packet.AssignCommunityProjectPacket;
import com.lucasizumi.cozycontracts.network.packet.DeliverKitchenOrderPacket;
import com.lucasizumi.cozycontracts.network.packet.DeliverProjectSiteOrderPacket;
import com.lucasizumi.cozycontracts.network.packet.OpenCommunityBoardScreenPacket;
import com.lucasizumi.cozycontracts.network.packet.PurchaseShopItemPacket;
import com.lucasizumi.cozycontracts.network.packet.SubmitCommunityBoardContractPacket;
import com.lucasizumi.cozycontracts.network.packet.ValidateCommunityProjectPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public final class ModNetworking {
    private static final String PROTOCOL_VERSION = "1";
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.parse(CozyContracts.MOD_ID + ":main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);

    private ModNetworking() {
    }

    public static void register() {
        CHANNEL.registerMessage(
                0,
                OpenCommunityBoardScreenPacket.class,
                OpenCommunityBoardScreenPacket::encode,
                OpenCommunityBoardScreenPacket::decode,
                OpenCommunityBoardScreenPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(
                1,
                SubmitCommunityBoardContractPacket.class,
                SubmitCommunityBoardContractPacket::encode,
                SubmitCommunityBoardContractPacket::decode,
                SubmitCommunityBoardContractPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(
                2,
                PurchaseShopItemPacket.class,
                PurchaseShopItemPacket::encode,
                PurchaseShopItemPacket::decode,
                PurchaseShopItemPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(
                3,
                DeliverKitchenOrderPacket.class,
                DeliverKitchenOrderPacket::encode,
                DeliverKitchenOrderPacket::decode,
                DeliverKitchenOrderPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(
                4,
                AssignCommunityProjectPacket.class,
                AssignCommunityProjectPacket::encode,
                AssignCommunityProjectPacket::decode,
                AssignCommunityProjectPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(
                5,
                ValidateCommunityProjectPacket.class,
                ValidateCommunityProjectPacket::encode,
                ValidateCommunityProjectPacket::decode,
                ValidateCommunityProjectPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(
                6,
                DeliverProjectSiteOrderPacket.class,
                DeliverProjectSiteOrderPacket::encode,
                DeliverProjectSiteOrderPacket::decode,
                DeliverProjectSiteOrderPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }

    public static void sendToPlayer(
            net.minecraft.server.level.ServerPlayer player,
            OpenCommunityBoardScreenPacket packet) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    public static void sendToServer(SubmitCommunityBoardContractPacket packet) {
        CHANNEL.sendToServer(packet);
    }

    public static void sendToServer(PurchaseShopItemPacket packet) {
        CHANNEL.sendToServer(packet);
    }

    public static void sendToServer(DeliverKitchenOrderPacket packet) {
        CHANNEL.sendToServer(packet);
    }

    public static void sendToServer(AssignCommunityProjectPacket packet) {
        CHANNEL.sendToServer(packet);
    }

    public static void sendToServer(ValidateCommunityProjectPacket packet) {
        CHANNEL.sendToServer(packet);
    }

    public static void sendToServer(DeliverProjectSiteOrderPacket packet) {
        CHANNEL.sendToServer(packet);
    }
}
