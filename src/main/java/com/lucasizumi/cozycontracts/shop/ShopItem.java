package com.lucasizumi.cozycontracts.shop;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public final class ShopItem {
    private final ResourceLocation id;
    private final ItemStack rewardStack;
    private final int costTokens;
    private final String displayName;
    private final Set<ShopCategory> categories;

    public ShopItem(
            ResourceLocation id,
            ItemStack rewardStack,
            int costTokens,
            String displayName,
            Set<ShopCategory> categories) {
        this.id = id;
        this.rewardStack = rewardStack.copy();
        this.costTokens = costTokens;
        this.displayName = displayName;
        this.categories = Collections.unmodifiableSet(EnumSet.copyOf(categories));
    }

    public ResourceLocation getId() {
        return id;
    }

    public ItemStack createRewardStack() {
        return rewardStack.copy();
    }

    public int getRewardCount() {
        return rewardStack.getCount();
    }

    public int getCostTokens() {
        return costTokens;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Set<ShopCategory> getCategories() {
        return categories;
    }
}
