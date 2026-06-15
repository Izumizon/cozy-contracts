package com.lucasizumi.cozycontracts.network.packet;

import com.lucasizumi.cozycontracts.client.ClientPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public record OpenCommunityBoardScreenPacket(
        BlockPos boardPos,
        long day,
        List<ContractEntry> contracts) {

    public OpenCommunityBoardScreenPacket {
        contracts = List.copyOf(contracts);
    }

    public static void encode(
            OpenCommunityBoardScreenPacket packet,
            FriendlyByteBuf buffer) {
        buffer.writeBlockPos(packet.boardPos());
        buffer.writeVarLong(packet.day());
        buffer.writeCollection(packet.contracts(), ContractEntry::encode);
    }

    public static OpenCommunityBoardScreenPacket decode(FriendlyByteBuf buffer) {
        return new OpenCommunityBoardScreenPacket(
                buffer.readBlockPos(),
                buffer.readVarLong(),
                buffer.readList(ContractEntry::decode));
    }

    public static void handle(
            OpenCommunityBoardScreenPacket packet,
            Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientPacketHandler.openCommunityBoard(packet));
        context.setPacketHandled(true);
    }

    public record ContractEntry(
            String title,
            String requester,
            String requirementText,
            int rewardTokens,
            boolean completed) {

        private static void encode(FriendlyByteBuf buffer, ContractEntry entry) {
            buffer.writeUtf(entry.title());
            buffer.writeUtf(entry.requester());
            buffer.writeUtf(entry.requirementText());
            buffer.writeVarInt(entry.rewardTokens());
            buffer.writeBoolean(entry.completed());
        }

        private static ContractEntry decode(FriendlyByteBuf buffer) {
            return new ContractEntry(
                    buffer.readUtf(),
                    buffer.readUtf(),
                    buffer.readUtf(),
                    buffer.readVarInt(),
                    buffer.readBoolean());
        }
    }
}
