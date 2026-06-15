package com.lucasizumi.cozycontracts.settlement;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.List;

public class SettlementSavedData extends SavedData {
    private static final String SETTLEMENTS_TAG = "Settlements";
    private static final String ID_TAG = "Id";
    private static final String CENTER_X_TAG = "CenterX";
    private static final String CENTER_Y_TAG = "CenterY";
    private static final String CENTER_Z_TAG = "CenterZ";
    private static final String DIMENSION_TAG = "Dimension";
    private static final String CREATED_DAY_TAG = "CreatedDay";

    private final List<Settlement> settlements = new ArrayList<>();

    public static SettlementSavedData load(CompoundTag tag) {
        SettlementSavedData data = new SettlementSavedData();
        ListTag savedSettlements = tag.getList(SETTLEMENTS_TAG, Tag.TAG_COMPOUND);

        for (int index = 0; index < savedSettlements.size(); index++) {
            CompoundTag savedSettlement = savedSettlements.getCompound(index);
            Settlement settlement = loadSettlement(savedSettlement);
            if (settlement != null) {
                data.settlements.add(settlement);
            }
        }

        return data;
    }

    public List<Settlement> getSettlements() {
        return List.copyOf(settlements);
    }

    public void addSettlement(Settlement settlement) {
        settlements.add(settlement);
        setDirty();
    }

    public int getSettlementCount() {
        return settlements.size();
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag savedSettlements = new ListTag();
        for (Settlement settlement : settlements) {
            CompoundTag savedSettlement = new CompoundTag();
            savedSettlement.putString(ID_TAG, settlement.getId());
            savedSettlement.putInt(CENTER_X_TAG, settlement.getCenter().getX());
            savedSettlement.putInt(CENTER_Y_TAG, settlement.getCenter().getY());
            savedSettlement.putInt(CENTER_Z_TAG, settlement.getCenter().getZ());
            savedSettlement.putString(DIMENSION_TAG, settlement.getDimension().toString());
            savedSettlement.putLong(CREATED_DAY_TAG, settlement.getCreatedDay());
            savedSettlements.add(savedSettlement);
        }

        tag.put(SETTLEMENTS_TAG, savedSettlements);
        return tag;
    }

    private static Settlement loadSettlement(CompoundTag tag) {
        if (!tag.contains(ID_TAG, Tag.TAG_STRING)
                || !tag.contains(CENTER_X_TAG, Tag.TAG_INT)
                || !tag.contains(CENTER_Y_TAG, Tag.TAG_INT)
                || !tag.contains(CENTER_Z_TAG, Tag.TAG_INT)
                || !tag.contains(DIMENSION_TAG, Tag.TAG_STRING)
                || !tag.contains(CREATED_DAY_TAG, Tag.TAG_LONG)) {
            return null;
        }

        ResourceLocation dimension = ResourceLocation.tryParse(tag.getString(DIMENSION_TAG));
        if (dimension == null) {
            return null;
        }

        return new Settlement(
                tag.getString(ID_TAG),
                new BlockPos(
                        tag.getInt(CENTER_X_TAG),
                        tag.getInt(CENTER_Y_TAG),
                        tag.getInt(CENTER_Z_TAG)),
                dimension,
                tag.getLong(CREATED_DAY_TAG));
    }
}
