package com.lucasizumi.cozycontracts.settlement;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public final class Settlement {
    private final String id;
    private final BlockPos center;
    private final ResourceLocation dimension;
    private final long createdDay;

    public Settlement(
            String id,
            BlockPos center,
            ResourceLocation dimension,
            long createdDay) {
        this.id = id;
        this.center = center;
        this.dimension = dimension;
        this.createdDay = createdDay;
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
}
