package com.lucasizumi.cozycontracts.contracts;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public final class ContractRequirement {
    public enum Type {
        EXACT_ITEM,
        ITEM_TAG
    }

    private final Type type;
    private final ResourceLocation id;
    private final int count;
    private final String displayName;

    private ContractRequirement(Type type, ResourceLocation id, int count, String displayName) {
        this.type = type;
        this.id = id;
        this.count = count;
        this.displayName = displayName;
    }

    public static ContractRequirement exactItem(
            ResourceLocation itemId,
            int count,
            String displayName) {
        return new ContractRequirement(Type.EXACT_ITEM, itemId, count, displayName);
    }

    public static ContractRequirement itemTag(
            ResourceLocation tagId,
            int count,
            String displayName) {
        return new ContractRequirement(Type.ITEM_TAG, tagId, count, displayName);
    }

    public boolean matches(ItemStack stack) {
        if (type == Type.EXACT_ITEM) {
            return id.equals(ForgeRegistries.ITEMS.getKey(stack.getItem()));
        }

        TagKey<Item> tag = TagKey.create(Registries.ITEM, id);
        return stack.is(tag);
    }

    public Type getType() {
        return type;
    }

    public ResourceLocation getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPreviewText() {
        return "Bring " + count + " " + displayName;
    }
}
