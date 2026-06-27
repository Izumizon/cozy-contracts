package com.lucasizumi.cozycontracts.settlement;

import com.lucasizumi.cozycontracts.projects.CommunityImprovementType;
import com.lucasizumi.cozycontracts.projects.CommunityProjectAssignment;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class Settlement {
    private final String id;
    private final BlockPos center;
    private final ResourceLocation dimension;
    private final long createdDay;
    private final Set<ResourceLocation> completedProjectIds;
    private final Map<CommunityImprovementType, Integer> communityImprovements;
    private final Map<ResourceLocation, CommunityProjectAssignment> activeProjectAssignments;
    private final Map<ResourceLocation, BlockPos> completedProjectSites;

    public Settlement(
            String id,
            BlockPos center,
            ResourceLocation dimension,
            long createdDay) {
        this.id = id;
        this.center = center;
        this.dimension = dimension;
        this.createdDay = createdDay;
        this.completedProjectIds = new HashSet<>();
        this.communityImprovements = new EnumMap<>(CommunityImprovementType.class);
        this.activeProjectAssignments = new HashMap<>();
        this.completedProjectSites = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public BlockPos getCenter() {
        return center;
    }

    public ResourceLocation getDimension() {
        return dimension;
    }

    public long getCreatedDay() {
        return createdDay;
    }

    public Set<ResourceLocation> getCompletedProjectIds() {
        return Set.copyOf(completedProjectIds);
    }

    public boolean isProjectCompleted(ResourceLocation projectId) {
        return completedProjectIds.contains(projectId);
    }

    public void markProjectCompleted(ResourceLocation projectId, BlockPos markerPos) {
        completedProjectIds.add(projectId);
        activeProjectAssignments.remove(projectId);
        completedProjectSites.put(projectId, markerPos.immutable());
    }

    public Map<CommunityImprovementType, Integer> getCommunityImprovements() {
        return Map.copyOf(communityImprovements);
    }

    public int getCommunityImprovementCount(CommunityImprovementType type) {
        return communityImprovements.getOrDefault(type, 0);
    }

    public void addCommunityImprovement(CommunityImprovementType type) {
        communityImprovements.merge(type, 1, Integer::sum);
    }

    public Optional<CommunityProjectAssignment> getProjectAssignment(ResourceLocation projectId) {
        return Optional.ofNullable(activeProjectAssignments.get(projectId));
    }

    public Optional<BlockPos> getCompletedProjectSite(ResourceLocation projectId) {
        return Optional.ofNullable(completedProjectSites.get(projectId));
    }

    public Map<ResourceLocation, BlockPos> getCompletedProjectSites() {
        return Map.copyOf(completedProjectSites);
    }

    public Map<ResourceLocation, CommunityProjectAssignment> getActiveProjectAssignments() {
        return Map.copyOf(activeProjectAssignments);
    }

    public boolean hasMarkerAssigned(BlockPos markerPos) {
        return activeProjectAssignments.values().stream()
                .anyMatch(assignment -> assignment.markerPos().equals(markerPos));
    }

    public boolean hasMarkerReserved(BlockPos markerPos) {
        return hasMarkerAssigned(markerPos)
                || completedProjectSites.values().stream()
                .anyMatch(sitePos -> sitePos.equals(markerPos));
    }

    public void assignProject(ResourceLocation projectId, BlockPos markerPos) {
        activeProjectAssignments.put(
                projectId,
                new CommunityProjectAssignment(projectId, markerPos.immutable()));
    }

    public Optional<ResourceLocation> cancelProjectAssignmentAt(BlockPos markerPos) {
        Optional<ResourceLocation> projectId = activeProjectAssignments.entrySet().stream()
                .filter(entry -> entry.getValue().markerPos().equals(markerPos))
                .map(Map.Entry::getKey)
                .findFirst();
        projectId.ifPresent(activeProjectAssignments::remove);
        return projectId;
    }

    public Optional<ResourceLocation> removeCompletedProjectSiteAt(BlockPos markerPos) {
        Optional<ResourceLocation> projectId = completedProjectSites.entrySet().stream()
                .filter(entry -> entry.getValue().equals(markerPos))
                .map(Map.Entry::getKey)
                .findFirst();
        projectId.ifPresent(completedProjectSites::remove);
        return projectId;
    }

    public void restoreCompletedProject(ResourceLocation projectId) {
        completedProjectIds.add(projectId);
    }

    public void restoreCompletedProjectSite(ResourceLocation projectId, BlockPos markerPos) {
        completedProjectSites.put(projectId, markerPos.immutable());
    }

    public void restoreCommunityImprovement(CommunityImprovementType type, int count) {
        if (count > 0) {
            communityImprovements.put(type, count);
        }
    }

    public void restoreProjectAssignment(ResourceLocation projectId, BlockPos markerPos) {
        assignProject(projectId, markerPos);
    }
}
