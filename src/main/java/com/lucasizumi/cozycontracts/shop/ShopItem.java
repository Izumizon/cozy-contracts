package com.lucasizumi.cozycontracts.shop;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public final class ShopItem {
    private final ResourceLocation id;
    private final ItemStack rewardStack;
    private final int costTokens;
    private final String displayName;

    public ShopItem(
            ResourceLocation id,
            ItemStack rewardStack,
            int costTokens,
            String displayName) {
        this.id = id;
        this.rewardStack = rewardStack.copy();
        this.costTokens = costTokens;
        this.displayName = displayName;
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
}
