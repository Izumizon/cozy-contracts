package com.lucasizumi.cozycontracts.shop;

import com.lucasizumi.cozycontracts.CozyContracts;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class ShopRegistry {
    private static final List<ShopItem> ITEMS = List.of(
            item("name_tag", new ItemStack(Items.NAME_TAG), 20, "Name Tag"),
            item("saddle", new ItemStack(Items.SADDLE), 25, "Saddle"),
            item("bell", new ItemStack(Items.BELL), 16, "Bell"),
            item("cherry_sapling", new ItemStack(Items.CHERRY_SAPLING), 12, "Cherry Sapling"),
            item("jungle_sapling", new ItemStack(Items.JUNGLE_SAPLING), 12, "Jungle Sapling"),
            item("lantern_bundle", new ItemStack(Items.LANTERN, 8), 4, "Lantern Bundle"),
            item("bone_meal_bundle", new ItemStack(Items.BONE_MEAL, 16), 3, "Bone Meal Bundle"),
            item("music_disc_cat", new ItemStack(Items.MUSIC_DISC_CAT), 32, "Music Disc"));

    private static final Map<ResourceLocation, ShopItem> ITEMS_BY_ID = createItemsById();

    private ShopRegistry() {
    }

    public static List<ShopItem> getAllItems() {
        return ITEMS;
    }

    public static Optional<ShopItem> getById(ResourceLocation id) {
        return Optional.ofNullable(ITEMS_BY_ID.get(id));
    }

    private static ShopItem item(
            String path,
            ItemStack rewardStack,
            int costTokens,
            String displayName) {
        return new ShopItem(
                ResourceLocation.parse(CozyContracts.MOD_ID + ":" + path),
                rewardStack,
                costTokens,
                displayName);
    }

    private static Map<ResourceLocation, ShopItem> createItemsById() {
        Map<ResourceLocation, ShopItem> itemsById = new LinkedHashMap<>();
        for (ShopItem item : ITEMS) {
            itemsById.putIfAbsent(item.getId(), item);
        }
        return Map.copyOf(itemsById);
    }
}
