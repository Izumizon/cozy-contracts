package com.lucasizumi.cozycontracts.projects;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public final class CommunityProjectValidator {
    public static final int PROJECT_RADIUS = 8;

    private CommunityProjectValidator() {
    }

    public static ProjectValidationResult validate(
            Level level,
            BlockPos markerPos,
            CommunityProject project) {
        ScanResult scan = scan(level, markerPos);
        List<String> missing = new ArrayList<>();

        if (project.getId().equals(CommunityProjectRegistry.VILLAGE_FIELDS)) {
            if (!scan.hasFarmland) {
                missing.add("Village Fields still needs farmland nearby.");
            }
            if (!scan.hasPlantedCrops) {
                missing.add("Village Fields still needs planted crops nearby.");
            }
            if (!scan.hasWater) {
                missing.add("Village Fields still needs water near the field.");
            }
        } else if (project.getId().equals(CommunityProjectRegistry.GARDEN_CORNER)) {
            if (!scan.hasFlowers) {
                missing.add("Garden Corner still needs flowers nearby.");
            }
            if (!scan.hasNaturalDecoration) {
                missing.add("Garden Corner still needs natural decoration such as leaves, grass, or dirt.");
            }
            if (!scan.hasLightSource) {
                missing.add("Garden Corner still needs a cozy light source such as a candle, lantern, or torch.");
            }
        } else if (project.getId().equals(CommunityProjectRegistry.BUILDERS_YARD)) {
            if (!scan.hasWorkBlock) {
                missing.add("Builder's Yard still needs a work block such as a crafting table or stonecutter.");
            }
            if (!scan.hasStorageBlock) {
                missing.add("Builder's Yard still needs storage such as a chest or barrel.");
            }
            if (!scan.hasBuildingMaterials) {
                missing.add("Builder's Yard still needs building materials such as logs, stone, bricks, or glass.");
            }
            if (!scan.hasLightSource) {
                missing.add("Builder's Yard still needs a light source nearby.");
            }
        }

        return missing.isEmpty()
                ? ProjectValidationResult.success()
                : ProjectValidationResult.missing(missing);
    }

    private static ScanResult scan(Level level, BlockPos markerPos) {
        ScanResult result = new ScanResult();
        BlockPos.MutableBlockPos current = new BlockPos.MutableBlockPos();

        for (int x = -PROJECT_RADIUS; x <= PROJECT_RADIUS; x++) {
            for (int y = -3; y <= 3; y++) {
                for (int z = -PROJECT_RADIUS; z <= PROJECT_RADIUS; z++) {
                    current.set(markerPos.getX() + x, markerPos.getY() + y, markerPos.getZ() + z);
                    BlockState state = level.getBlockState(current);
                    Block block = state.getBlock();

                    result.hasFarmland |= block == Blocks.FARMLAND;
                    result.hasPlantedCrops |= block instanceof CropBlock;
                    result.hasWater |= level.getFluidState(current).is(FluidTags.WATER);
                    result.hasFlowers |= state.is(BlockTags.FLOWERS);
                    result.hasNaturalDecoration |= state.is(BlockTags.LEAVES)
                            || state.is(BlockTags.DIRT)
                            || block == Blocks.GRASS_BLOCK
                            || block == Blocks.GRASS
                            || block == Blocks.FERN
                            || block == Blocks.LARGE_FERN;
                    result.hasLightSource |= state.getLightEmission(level, current) > 0;
                    result.hasWorkBlock |= block == Blocks.CRAFTING_TABLE
                            || block == Blocks.STONECUTTER;
                    result.hasStorageBlock |= block == Blocks.CHEST
                            || block == Blocks.TRAPPED_CHEST
                            || block == Blocks.BARREL;
                    result.hasBuildingMaterials |= state.is(BlockTags.LOGS)
                            || block == Blocks.STONE
                            || block == Blocks.COBBLESTONE
                            || block == Blocks.BRICKS
                            || block == Blocks.STONE_BRICKS
                            || block == Blocks.GLASS
                            || block == Blocks.SAND
                            || block == Blocks.CLAY;
                }
            }
        }

        return result;
    }

    private static final class ScanResult {
        private boolean hasFarmland;
        private boolean hasPlantedCrops;
        private boolean hasWater;
        private boolean hasFlowers;
        private boolean hasNaturalDecoration;
        private boolean hasLightSource;
        private boolean hasWorkBlock;
        private boolean hasStorageBlock;
        private boolean hasBuildingMaterials;
    }
}
