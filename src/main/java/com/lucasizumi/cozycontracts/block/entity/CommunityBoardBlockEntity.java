package com.lucasizumi.cozycontracts.block.entity;

import com.lucasizumi.cozycontracts.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommunityBoardBlockEntity extends BlockEntity {
    private static final String COMPLETION_DAY_TAG = "CompletionDay";
    private static final String COMPLETED_CONTRACTS_TAG = "CompletedContracts";
    private static final String ACTIVE_CONTRACTS_DAY_TAG = "ActiveContractsDay";
    private static final String ACTIVE_CONTRACTS_TAG = "ActiveContracts";
    private static final String KITCHEN_DELIVERY_DAY_TAG = "KitchenDeliveryDay";
    private static final String KITCHEN_DELIVERIES_TAG = "KitchenDeliveries";

    private long completionDay = Long.MIN_VALUE;
    private final Set<ResourceLocation> completedContractIds = new HashSet<>();
    private long activeContractsDay = Long.MIN_VALUE;
    private final List<ResourceLocation> activeContractIds = new ArrayList<>();
    private long kitchenDeliveryDay = Long.MIN_VALUE;
    private final Map<ResourceLocation, Integer> kitchenDeliveryCounts = new HashMap<>();

    public CommunityBoardBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COMMUNITY_BOARD.get(), pos, state);
    }

    public boolean isCompleted(long day, ResourceLocation contractId) {
        ensureCompletionDay(day);
        return completedContractIds.contains(contractId);
    }

    public void markCompleted(long day, ResourceLocation contractId) {
        ensureCompletionDay(day);
        if (completedContractIds.add(contractId)) {
            setChanged();
        }
    }

    public Set<ResourceLocation> getCompleted(long day) {
        ensureCompletionDay(day);
        return Set.copyOf(completedContractIds);
    }

    public List<ResourceLocation> getActiveContractIds(long day) {
        ensureActiveContractsDay(day);
        return List.copyOf(activeContractIds);
    }

    public void setActiveContractIds(long day, List<ResourceLocation> ids) {
        ensureActiveContractsDay(day);
        activeContractIds.clear();
        activeContractIds.addAll(ids);
        setChanged();
    }

    public boolean hasActiveContractsForDay(long day) {
        ensureActiveContractsDay(day);
        return !activeContractIds.isEmpty();
    }

    public long getStoredActiveContractsDay() {
        return activeContractsDay;
    }

    public List<ResourceLocation> getStoredActiveContractIds() {
        return List.copyOf(activeContractIds);
    }

    public long getStoredCompletionDay() {
        return completionDay;
    }

    public Set<ResourceLocation> getStoredCompletedContractIds() {
        return Set.copyOf(completedContractIds);
    }

    public int getKitchenDeliveryCount(long day, ResourceLocation orderId) {
        ensureKitchenDeliveryDay(day);
        return kitchenDeliveryCounts.getOrDefault(orderId, 0);
    }

    public void incrementKitchenDeliveryCount(long day, ResourceLocation orderId) {
        ensureKitchenDeliveryDay(day);
        kitchenDeliveryCounts.merge(orderId, 1, Integer::sum);
        setChanged();
    }

    public Map<ResourceLocation, Integer> getKitchenDeliveryCounts(long day) {
        ensureKitchenDeliveryDay(day);
        return Map.copyOf(kitchenDeliveryCounts);
    }

    private void ensureCompletionDay(long day) {
        if (completionDay == day) {
            return;
        }

        completionDay = day;
        completedContractIds.clear();
        setChanged();
    }

    private void ensureActiveContractsDay(long day) {
        if (activeContractsDay == day) {
            return;
        }

        activeContractsDay = day;
        activeContractIds.clear();
        setChanged();
    }

    private void ensureKitchenDeliveryDay(long day) {
        if (kitchenDeliveryDay == day) {
            return;
        }

        kitchenDeliveryDay = day;
        kitchenDeliveryCounts.clear();
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putLong(COMPLETION_DAY_TAG, completionDay);
        tag.put(COMPLETED_CONTRACTS_TAG, saveResourceLocations(completedContractIds));
        tag.putLong(ACTIVE_CONTRACTS_DAY_TAG, activeContractsDay);
        tag.put(ACTIVE_CONTRACTS_TAG, saveResourceLocations(activeContractIds));
        tag.putLong(KITCHEN_DELIVERY_DAY_TAG, kitchenDeliveryDay);
        tag.put(KITCHEN_DELIVERIES_TAG, saveKitchenDeliveries(kitchenDeliveryCounts));
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        completionDay = tag.getLong(COMPLETION_DAY_TAG);
        completedContractIds.clear();
        completedContractIds.addAll(loadResourceLocations(tag, COMPLETED_CONTRACTS_TAG));

        activeContractsDay = tag.getLong(ACTIVE_CONTRACTS_DAY_TAG);
        activeContractIds.clear();
        activeContractIds.addAll(loadResourceLocations(tag, ACTIVE_CONTRACTS_TAG));

        kitchenDeliveryDay = tag.getLong(KITCHEN_DELIVERY_DAY_TAG);
        kitchenDeliveryCounts.clear();
        kitchenDeliveryCounts.putAll(loadKitchenDeliveries(tag, KITCHEN_DELIVERIES_TAG));
    }

    private static ListTag saveResourceLocations(Iterable<ResourceLocation> ids) {
        ListTag savedIds = new ListTag();
        for (ResourceLocation id : ids) {
            savedIds.add(StringTag.valueOf(id.toString()));
        }
        return savedIds;
    }

    private static List<ResourceLocation> loadResourceLocations(CompoundTag tag, String key) {
        List<ResourceLocation> ids = new ArrayList<>();
        ListTag savedIds = tag.getList(key, Tag.TAG_STRING);

        for (int index = 0; index < savedIds.size(); index++) {
            ResourceLocation id = ResourceLocation.tryParse(savedIds.getString(index));
            if (id != null) {
                ids.add(id);
            }
        }

        return ids;
    }

    private static CompoundTag saveKitchenDeliveries(Map<ResourceLocation, Integer> deliveries) {
        CompoundTag savedDeliveries = new CompoundTag();
        for (Map.Entry<ResourceLocation, Integer> entry : deliveries.entrySet()) {
            savedDeliveries.putInt(entry.getKey().toString(), entry.getValue());
        }
        return savedDeliveries;
    }

    private static Map<ResourceLocation, Integer> loadKitchenDeliveries(CompoundTag tag, String key) {
        Map<ResourceLocation, Integer> deliveries = new HashMap<>();
        CompoundTag savedDeliveries = tag.getCompound(key);

        for (String savedKey : savedDeliveries.getAllKeys()) {
            ResourceLocation id = ResourceLocation.tryParse(savedKey);
            if (id != null) {
                deliveries.put(id, savedDeliveries.getInt(savedKey));
            }
        }

        return deliveries;
    }
}
