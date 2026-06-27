package com.lucasizumi.cozycontracts.projects;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public record CommunityProjectAssignment(
        ResourceLocation projectId,
        BlockPos markerPos) {
}
