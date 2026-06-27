package com.lucasizumi.cozycontracts.network.packet;

import com.lucasizumi.cozycontracts.client.ClientPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public record OpenCommunityBoardScreenPacket(
        BlockPos boardPos,
        long day,
        List<ContractEntry> contracts,
        List<KitchenOrderEntry> kitchenOrders,
        List<ProjectEntry> projects,
        ImprovementEntry improvements) {

    public OpenCommunityBoardScreenPacket {
        contracts = List.copyOf(contracts);
        kitchenOrders = List.copyOf(kitchenOrders);
        projects = List.copyOf(projects);
    }

    public static void encode(
            OpenCommunityBoardScreenPacket packet,
            FriendlyByteBuf buffer) {
        buffer.writeBlockPos(packet.boardPos());
        buffer.writeVarLong(packet.day());
        buffer.writeCollection(packet.contracts(), ContractEntry::encode);
        buffer.writeCollection(packet.kitchenOrders(), KitchenOrderEntry::encode);
        buffer.writeCollection(packet.projects(), ProjectEntry::encode);
        ImprovementEntry.encode(buffer, packet.improvements());
    }

    public static OpenCommunityBoardScreenPacket decode(FriendlyByteBuf buffer) {
        return new OpenCommunityBoardScreenPacket(
                buffer.readBlockPos(),
                buffer.readVarLong(),
                buffer.readList(ContractEntry::decode),
                buffer.readList(KitchenOrderEntry::decode),
                buffer.readList(ProjectEntry::decode),
                ImprovementEntry.decode(buffer));
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

    public record KitchenOrderEntry(
            ResourceLocation id,
            String title,
            String requester,
            String type,
            String requirementDisplay,
            String supportDisplay,
            int rewardTokens,
            int deliveredToday,
            int dailyLimit) {

        private static void encode(FriendlyByteBuf buffer, KitchenOrderEntry entry) {
            buffer.writeResourceLocation(entry.id());
            buffer.writeUtf(entry.title());
            buffer.writeUtf(entry.requester());
            buffer.writeUtf(entry.type());
            buffer.writeUtf(entry.requirementDisplay());
            buffer.writeUtf(entry.supportDisplay());
            buffer.writeVarInt(entry.rewardTokens());
            buffer.writeVarInt(entry.deliveredToday());
            buffer.writeVarInt(entry.dailyLimit());
        }

        private static KitchenOrderEntry decode(FriendlyByteBuf buffer) {
            return new KitchenOrderEntry(
                    buffer.readResourceLocation(),
                    buffer.readUtf(),
                    buffer.readUtf(),
                    buffer.readUtf(),
                    buffer.readUtf(),
                    buffer.readUtf(),
                    buffer.readVarInt(),
                    buffer.readVarInt(),
                    buffer.readVarInt());
        }
    }

    public record ProjectEntry(
            ResourceLocation id,
            String title,
            String improvementType,
            String description,
            BlockPos markerPos,
            boolean completed,
            boolean ready,
            List<String> missingRequirements) {

        public ProjectEntry {
            missingRequirements = List.copyOf(missingRequirements);
        }

        private static void encode(FriendlyByteBuf buffer, ProjectEntry entry) {
            buffer.writeResourceLocation(entry.id());
            buffer.writeUtf(entry.title());
            buffer.writeUtf(entry.improvementType());
            buffer.writeUtf(entry.description());
            buffer.writeBoolean(entry.markerPos() != null);
            if (entry.markerPos() != null) {
                buffer.writeBlockPos(entry.markerPos());
            }
            buffer.writeBoolean(entry.completed());
            buffer.writeBoolean(entry.ready());
            buffer.writeCollection(entry.missingRequirements(), FriendlyByteBuf::writeUtf);
        }

        private static ProjectEntry decode(FriendlyByteBuf buffer) {
            ResourceLocation id = buffer.readResourceLocation();
            String title = buffer.readUtf();
            String improvementType = buffer.readUtf();
            String description = buffer.readUtf();
            BlockPos markerPos = buffer.readBoolean() ? buffer.readBlockPos() : null;
            boolean completed = buffer.readBoolean();
            boolean ready = buffer.readBoolean();
            List<String> missingRequirements = buffer.readList(FriendlyByteBuf::readUtf);
            return new ProjectEntry(
                    id,
                    title,
                    improvementType,
                    description,
                    markerPos,
                    completed,
                    ready,
                    missingRequirements);
        }
    }

    public record ImprovementEntry(
            int farming,
            int builder,
            int decor,
            int unassignedMarkers,
            int activeMarkers,
            int completedSites) {

        private static void encode(FriendlyByteBuf buffer, ImprovementEntry entry) {
            buffer.writeVarInt(entry.farming());
            buffer.writeVarInt(entry.builder());
            buffer.writeVarInt(entry.decor());
            buffer.writeVarInt(entry.unassignedMarkers());
            buffer.writeVarInt(entry.activeMarkers());
            buffer.writeVarInt(entry.completedSites());
        }

        private static ImprovementEntry decode(FriendlyByteBuf buffer) {
            return new ImprovementEntry(
                    buffer.readVarInt(),
                    buffer.readVarInt(),
                    buffer.readVarInt(),
                    buffer.readVarInt(),
                    buffer.readVarInt(),
                    buffer.readVarInt());
        }
    }
}
