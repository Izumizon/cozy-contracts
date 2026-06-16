package com.lucasizumi.cozycontracts.kitchen;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public final class KitchenOrderRequirement {
    public enum Type {
        EXACT_ITEM,
        ITEM_TAG
    }

    private final Type type;
    private final ResourceLocation id;
    private final int count;
    private final String displayName;

    private KitchenOrderRequirement(
            Type type,
            ResourceLocation id,
            int count,
            String displayName) {
        this.type = type;
        this.id = id;
        this.count = count;
        this.displayName = displayName;
    }

    public static KitchenOrderRequirement exactItem(
            ResourceLocation itemId,
            int count,
            String displayName) {
        return new KitchenOrderRequirement(Type.EXACT_ITEM, itemId, count, displayName);
    }

    public static KitchenOrderRequirement exactItem(
            Item item,
            int count,
            String displayName) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(item);
        return exactItem(itemId, count, displayName);
    }

    public static KitchenOrderRequirement itemTag(
            ResourceLocation tagId,
            int count,
            String displayName) {
        return new KitchenOrderRequirement(Type.ITEM_TAG, tagId, count, displayName);
    }

    public boolean matches(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        if (type == Type.EXACT_ITEM) {
            return id.equals(ForgeRegistries.ITEMS.getKey(stack.getItem()));
        }

        TagKey<Item> tag = TagKey.create(Registries.ITEM, id);
        return stack.is(tag);
    }

    public boolean isValid() {
        return type == Type.ITEM_TAG || ForgeRegistries.ITEMS.containsKey(id);
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
        return count + " " + displayName;
    }

    public String getDebugText() {
        return type + " " + id + " x" + count + " (" + displayName + ")";
    }
}
