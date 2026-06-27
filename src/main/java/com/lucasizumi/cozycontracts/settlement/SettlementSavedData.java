package com.lucasizumi.cozycontracts.settlement;

import com.lucasizumi.cozycontracts.projects.CommunityImprovementType;
import com.lucasizumi.cozycontracts.projects.CommunityProjectAssignment;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SettlementSavedData extends SavedData {
    private static final String SETTLEMENTS_TAG = "Settlements";
    private static final String ID_TAG = "Id";
    private static final String CENTER_X_TAG = "CenterX";
    private static final String CENTER_Y_TAG = "CenterY";
    private static final String CENTER_Z_TAG = "CenterZ";
    private static final String DIMENSION_TAG = "Dimension";
    private static final String CREATED_DAY_TAG = "CreatedDay";
    private static final String COMPLETED_PROJECTS_TAG = "CompletedProjects";
    private static final String COMPLETED_PROJECT_SITES_TAG = "CompletedProjectSites";
    private static final String COMMUNITY_IMPROVEMENTS_TAG = "CommunityImprovements";
    private static final String ACTIVE_PROJECTS_TAG = "ActiveProjects";
    private static final String PROJECT_ID_TAG = "ProjectId";
    private static final String MARKER_X_TAG = "MarkerX";
    private static final String MARKER_Y_TAG = "MarkerY";
    private static final String MARKER_Z_TAG = "MarkerZ";

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
            savedSettlement.put(
                    COMPLETED_PROJECTS_TAG,
                    saveResourceLocations(settlement.getCompletedProjectIds()));
            savedSettlement.put(
                    COMPLETED_PROJECT_SITES_TAG,
                    saveProjectSites(settlement.getCompletedProjectSites()));
            savedSettlement.put(
                    COMMUNITY_IMPROVEMENTS_TAG,
                    saveCommunityImprovements(settlement.getCommunityImprovements()));
            savedSettlement.put(
                    ACTIVE_PROJECTS_TAG,
                    saveActiveProjectAssignments(settlement.getActiveProjectAssignments()));
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

        Settlement settlement = new Settlement(
                tag.getString(ID_TAG),
                new BlockPos(
                        tag.getInt(CENTER_X_TAG),
                        tag.getInt(CENTER_Y_TAG),
                        tag.getInt(CENTER_Z_TAG)),
                dimension,
                tag.getLong(CREATED_DAY_TAG));
        for (ResourceLocation projectId : loadResourceLocations(tag, COMPLETED_PROJECTS_TAG)) {
            settlement.restoreCompletedProject(projectId);
        }
        restoreProjectSites(settlement, tag.getList(COMPLETED_PROJECT_SITES_TAG, Tag.TAG_COMPOUND));

        CompoundTag improvements = tag.getCompound(COMMUNITY_IMPROVEMENTS_TAG);
        for (String key : improvements.getAllKeys()) {
            try {
                settlement.restoreCommunityImprovement(
                        CommunityImprovementType.valueOf(key),
                        improvements.getInt(key));
            } catch (IllegalArgumentException ignored) {
                // Unknown future improvement types are skipped safely.
            }
        }

        ListTag assignments = tag.getList(ACTIVE_PROJECTS_TAG, Tag.TAG_COMPOUND);
        for (int index = 0; index < assignments.size(); index++) {
            CompoundTag savedAssignment = assignments.getCompound(index);
            ResourceLocation projectId = ResourceLocation.tryParse(
                    savedAssignment.getString(PROJECT_ID_TAG));
            if (projectId != null) {
                settlement.restoreProjectAssignment(
                        projectId,
                        new BlockPos(
                                savedAssignment.getInt(MARKER_X_TAG),
                                savedAssignment.getInt(MARKER_Y_TAG),
                                savedAssignment.getInt(MARKER_Z_TAG)));
            }
        }

        return settlement;
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

    private static CompoundTag saveCommunityImprovements(
            Map<CommunityImprovementType, Integer> improvements) {
        CompoundTag savedImprovements = new CompoundTag();
        for (Map.Entry<CommunityImprovementType, Integer> entry : improvements.entrySet()) {
            savedImprovements.putInt(entry.getKey().name(), entry.getValue());
        }
        return savedImprovements;
    }

    private static ListTag saveActiveProjectAssignments(
            Map<ResourceLocation, CommunityProjectAssignment> assignments) {
        ListTag savedAssignments = new ListTag();
        for (CommunityProjectAssignment assignment : assignments.values()) {
            CompoundTag savedAssignment = new CompoundTag();
            savedAssignment.putString(PROJECT_ID_TAG, assignment.projectId().toString());
            savedAssignment.putInt(MARKER_X_TAG, assignment.markerPos().getX());
            savedAssignment.putInt(MARKER_Y_TAG, assignment.markerPos().getY());
            savedAssignment.putInt(MARKER_Z_TAG, assignment.markerPos().getZ());
            savedAssignments.add(savedAssignment);
        }
        return savedAssignments;
    }

    private static ListTag saveProjectSites(Map<ResourceLocation, BlockPos> sites) {
        ListTag savedSites = new ListTag();
        for (Map.Entry<ResourceLocation, BlockPos> entry : sites.entrySet()) {
            CompoundTag savedSite = new CompoundTag();
            savedSite.putString(PROJECT_ID_TAG, entry.getKey().toString());
            savedSite.putInt(MARKER_X_TAG, entry.getValue().getX());
            savedSite.putInt(MARKER_Y_TAG, entry.getValue().getY());
            savedSite.putInt(MARKER_Z_TAG, entry.getValue().getZ());
            savedSites.add(savedSite);
        }
        return savedSites;
    }

    private static void restoreProjectSites(Settlement settlement, ListTag savedSites) {
        for (int index = 0; index < savedSites.size(); index++) {
            CompoundTag savedSite = savedSites.getCompound(index);
            ResourceLocation projectId = ResourceLocation.tryParse(savedSite.getString(PROJECT_ID_TAG));
            if (projectId != null) {
                settlement.restoreCompletedProjectSite(
                        projectId,
                        new BlockPos(
                                savedSite.getInt(MARKER_X_TAG),
                                savedSite.getInt(MARKER_Y_TAG),
                                savedSite.getInt(MARKER_Z_TAG)));
            }
        }
    }
}
