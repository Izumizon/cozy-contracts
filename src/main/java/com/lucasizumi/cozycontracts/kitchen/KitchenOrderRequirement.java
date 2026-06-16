package com.lucasizumi.cozycontracts.kitchen;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class KitchenOrderRequirement {
    private final Item item;
    private final int count;
    private final String displayName;

    public KitchenOrderRequirement(Item item, int count, String displayName) {
        this.item = item;
        this.count = count;
        this.displayName = displayName;
    }

    public boolean matches(ItemStack stack) {
        return stack.is(item);
    }

    public Item getItem() {
        return item;
    }

    public int getCount() {
        return count;
    }

    public String getDisplayName() {
        return displayName;
    }
}
