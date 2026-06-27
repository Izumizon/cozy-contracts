package com.lucasizumi.cozycontracts.projects;

import com.lucasizumi.cozycontracts.network.packet.OpenCommunityBoardScreenPacket;
import com.lucasizumi.cozycontracts.registry.ModBlocks;
import com.lucasizumi.cozycontracts.settlement.Settlement;
import com.lucasizumi.cozycontracts.settlement.SettlementService;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class CommunityProjectService {
    private static final int MARKER_SEARCH_RADIUS = 64;
    private static final int MARKER_SEARCH_Y_RADIUS = 24;

    private CommunityProjectService() {
    }

    public static List<OpenCommunityBoardScreenPacket.ProjectEntry> getProjectEntriesForBoard(
            ServerLevel level,
            BlockPos boardPos) {
        Settlement settlement = SettlementService.getOrCreateSettlementForBoard(level, boardPos);
        return CommunityProjectRegistry.getStarterProjects().stream()
                .map(project -> createProjectEntry(level, settlement, project))
                .toList();
    }

    public static OpenCommunityBoardScreenPacket.ImprovementEntry getImprovementEntryForBoard(
            ServerLevel level,
            BlockPos boardPos) {
        Settlement settlement = SettlementService.getOrCreateSettlementForBoard(level, boardPos);
        MarkerCounts markerCounts = countProjectMarkers(level, settlement);
        return new OpenCommunityBoardScreenPacket.ImprovementEntry(
                settlement.getCommunityImprovementCount(CommunityImprovementType.FARMING),
                settlement.getCommunityImprovementCount(CommunityImprovementType.BUILDER),
                settlement.getCommunityImprovementCount(CommunityImprovementType.DECOR),
                markerCounts.unassigned(),
                markerCounts.active(),
                markerCounts.completedSites());
    }

    public static boolean assignNearestMarker(
            ServerPlayer player,
            ServerLevel level,
            BlockPos boardPos,
            ResourceLocation projectId) {
        CommunityProject project = CommunityProjectRegistry.getById(projectId).orElse(null);
        if (project == null) {
            player.sendSystemMessage(Component.literal("That Community Project is not available."));
            return false;
        }

        Settlement settlement = SettlementService.getOrCreateSettlementForBoard(level, boardPos);
        if (settlement.isProjectCompleted(projectId)) {
            player.sendSystemMessage(Component.literal(
                    project.getTitle() + " has already been completed for this settlement."));
            return false;
        }

        Optional<CommunityProjectAssignment> existingAssignment =
                settlement.getProjectAssignment(projectId);
        if (existingAssignment.isPresent()) {
            player.sendSystemMessage(Component.literal(
                    project.getTitle()
                            + " is already assigned to the Project Marker at "
                            + existingAssignment.get().markerPos().toShortString()
                            + "."));
            return false;
        }

        Optional<BlockPos> markerPos = findNearestUnassignedMarker(level, settlement);
        if (markerPos.isEmpty()) {
            player.sendSystemMessage(Component.literal(
                    "Place an unassigned Project Marker near this settlement, then try assigning "
                            + project.getTitle()
                            + " again."));
            return false;
        }

        settlement.assignProject(projectId, markerPos.get());
        SettlementService.markSettlementsDirty(level);
        player.sendSystemMessage(Component.literal(
                project.getTitle()
                        + " assigned to the Project Marker at "
                        + markerPos.get().toShortString()
                        + ". Build around the marker, then validate it from the Projects tab."));
        return true;
    }

    public static boolean validateAndComplete(
            ServerPlayer player,
            ServerLevel level,
            BlockPos boardPos,
            ResourceLocation projectId) {
        CommunityProject project = CommunityProjectRegistry.getById(projectId).orElse(null);
        if (project == null) {
            player.sendSystemMessage(Component.literal("That Community Project is not available."));
            return false;
        }

        Settlement settlement = SettlementService.getOrCreateSettlementForBoard(level, boardPos);
        if (settlement.isProjectCompleted(projectId)) {
            player.sendSystemMessage(Component.literal(
                    project.getTitle() + " has already been completed for this settlement."));
            return false;
        }

        CommunityProjectAssignment assignment =
                settlement.getProjectAssignment(projectId).orElse(null);
        if (assignment == null) {
            player.sendSystemMessage(Component.literal(
                    project.getTitle() + " needs a Project Marker assignment before it can be validated."));
            return false;
        }

        ProjectValidationResult validation =
                validateAssignment(level, assignment, project);
        if (!validation.ready()) {
            player.sendSystemMessage(Component.literal(project.getTitle() + " still needs:"));
            for (String missingRequirement : validation.missingRequirements()) {
                player.sendSystemMessage(Component.literal("- " + missingRequirement));
            }
            return false;
        }

        settlement.markProjectCompleted(projectId, assignment.markerPos());
        settlement.addCommunityImprovement(project.getImprovementType());
        SettlementService.markSettlementsDirty(level);
        player.sendSystemMessage(Component.literal(project.getCompletionMessage()));
        return true;
    }

    public static void handleMarkerBroken(
            ServerPlayer player,
            ServerLevel level,
            BlockPos markerPos) {
        Optional<Settlement> settlement = SettlementService.getNearestSettlement(
                level,
                markerPos,
                MARKER_SEARCH_RADIUS);
        if (settlement.isEmpty()) {
            return;
        }

        Optional<ResourceLocation> activeProjectId =
                settlement.get().cancelProjectAssignmentAt(markerPos);
        if (activeProjectId.isPresent()) {
            String title = CommunityProjectRegistry.getById(activeProjectId.get())
                    .map(CommunityProject::getTitle)
                    .orElse(activeProjectId.get().toString());
            SettlementService.markSettlementsDirty(level);
            player.sendSystemMessage(Component.literal(
                    "Project Marker removed. " + title + " has been unassigned and can be assigned again."));
            return;
        }

        Optional<ResourceLocation> completedProjectId =
                settlement.get().removeCompletedProjectSiteAt(markerPos);
        if (completedProjectId.isPresent()) {
            String title = CommunityProjectRegistry.getById(completedProjectId.get())
                    .map(CommunityProject::getTitle)
                    .orElse(completedProjectId.get().toString());
            SettlementService.markSettlementsDirty(level);
            player.sendSystemMessage(Component.literal(
                    "Project Site marker removed. "
                            + title
                            + " remains registered with the settlement."));
        }
    }

    public static String getMarkerStatusMessage(ServerLevel level, BlockPos markerPos) {
        Optional<Settlement> settlement = SettlementService.getNearestSettlement(
                level,
                markerPos,
                MARKER_SEARCH_RADIUS);
        if (settlement.isEmpty()) {
            return "Project Marker placed. Use the Community Board Projects tab to assign or validate a project.";
        }

        Optional<Map.Entry<ResourceLocation, CommunityProjectAssignment>> activeAssignment =
                settlement.get().getActiveProjectAssignments().entrySet().stream()
                        .filter(entry -> entry.getValue().markerPos().equals(markerPos))
                        .findFirst();
        if (activeAssignment.isPresent()) {
            String title = CommunityProjectRegistry.getById(activeAssignment.get().getKey())
                    .map(CommunityProject::getTitle)
                    .orElse(activeAssignment.get().getKey().toString());
            return "Active Project Marker for " + title + ". Use the Community Board Projects tab to validate it.";
        }

        Optional<Map.Entry<ResourceLocation, BlockPos>> completedSite =
                settlement.get().getCompletedProjectSites().entrySet().stream()
                        .filter(entry -> entry.getValue().equals(markerPos))
                        .findFirst();
        if (completedSite.isPresent()) {
            return "This project has been registered with the settlement. This marker can stay as a project site or be removed safely.";
        }

        return "Unassigned Project Marker. Use the Community Board Projects tab to assign a Community Project.";
    }

    public static ProjectValidationResult validateAssignment(
            ServerLevel level,
            CommunityProjectAssignment assignment,
            CommunityProject project) {
        if (!level.isLoaded(assignment.markerPos())
                || !level.getBlockState(assignment.markerPos()).is(ModBlocks.PROJECT_MARKER.get())) {
            return ProjectValidationResult.missing(List.of(
                    project.getTitle() + " still needs its Project Marker to be present."));
        }

        return CommunityProjectValidator.validate(level, assignment.markerPos(), project);
    }

    private static OpenCommunityBoardScreenPacket.ProjectEntry createProjectEntry(
            ServerLevel level,
            Settlement settlement,
            CommunityProject project) {
        boolean completed = settlement.isProjectCompleted(project.getId());
        CommunityProjectAssignment assignment =
                settlement.getProjectAssignment(project.getId()).orElse(null);
        BlockPos completedSite =
                settlement.getCompletedProjectSite(project.getId()).orElse(null);
        List<String> missingRequirements = List.of();
        boolean ready = false;

        if (!completed && assignment != null) {
            ProjectValidationResult result = validateAssignment(level, assignment, project);
            missingRequirements = result.missingRequirements();
            ready = result.ready();
        }

        return new OpenCommunityBoardScreenPacket.ProjectEntry(
                project.getId(),
                project.getTitle(),
                project.getImprovementType().getDisplayName(),
                project.getDescription(),
                completed
                        ? completedSite
                        : assignment == null ? null : assignment.markerPos(),
                completed,
                ready,
                missingRequirements);
    }

    private static Optional<BlockPos> findNearestUnassignedMarker(
            ServerLevel level,
            Settlement settlement) {
        BlockPos center = settlement.getCenter();
        int radius = MARKER_SEARCH_RADIUS;
        int yRadius = MARKER_SEARCH_Y_RADIUS;
        double bestDistance = Double.MAX_VALUE;
        BlockPos bestPos = null;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -yRadius; y <= yRadius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos = center.offset(x, y, z);
                    double distance = center.distSqr(pos);
                    if (distance > radius * radius
                            || distance >= bestDistance
                            || !level.getBlockState(pos).is(ModBlocks.PROJECT_MARKER.get())
                            || settlement.hasMarkerReserved(pos)) {
                        continue;
                    }

                    bestDistance = distance;
                    bestPos = pos.immutable();
                }
            }
        }

        return Optional.ofNullable(bestPos);
    }

    private static MarkerCounts countProjectMarkers(
            ServerLevel level,
            Settlement settlement) {
        int unassigned = 0;
        int active = 0;
        int completedSites = 0;

        for (BlockPos markerPos : findProjectMarkers(level, settlement.getCenter())) {
            if (settlement.getCompletedProjectSites().containsValue(markerPos)) {
                completedSites++;
            } else if (settlement.hasMarkerAssigned(markerPos)) {
                active++;
            } else {
                unassigned++;
            }
        }

        return new MarkerCounts(unassigned, active, completedSites);
    }

    private static List<BlockPos> findProjectMarkers(ServerLevel level, BlockPos center) {
        List<BlockPos> markers = new ArrayList<>();
        int radius = MARKER_SEARCH_RADIUS;
        int yRadius = MARKER_SEARCH_Y_RADIUS;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -yRadius; y <= yRadius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos = center.offset(x, y, z);
                    if (center.distSqr(pos) <= radius * radius
                            && level.getBlockState(pos).is(ModBlocks.PROJECT_MARKER.get())) {
                        markers.add(pos.immutable());
                    }
                }
            }
        }

        return markers;
    }

    public static String formatImprovements(Map<CommunityImprovementType, Integer> improvements) {
        return "Farming "
                + improvements.getOrDefault(CommunityImprovementType.FARMING, 0)
                + ", Builder "
                + improvements.getOrDefault(CommunityImprovementType.BUILDER, 0)
                + ", Decor "
                + improvements.getOrDefault(CommunityImprovementType.DECOR, 0);
    }

    private record MarkerCounts(
            int unassigned,
            int active,
            int completedSites) {
    }
}
