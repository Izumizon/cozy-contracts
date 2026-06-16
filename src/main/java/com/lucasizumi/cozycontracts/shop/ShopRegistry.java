package com.lucasizumi.cozycontracts.shop;

import com.lucasizumi.cozycontracts.CozyContracts;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class ShopRegistry {
    private static final List<ShopItem> ITEMS = List.of(
            item("name_tag", new ItemStack(Items.NAME_TAG), 20, "Name Tag",
                    ShopCategory.UNIVERSAL, ShopCategory.MARKET),
            item("saddle", new ItemStack(Items.SADDLE), 25, "Saddle",
                    ShopCategory.UNIVERSAL, ShopCategory.FARMING, ShopCategory.MARKET),
            item("bell", new ItemStack(Items.BELL), 16, "Bell",
                    ShopCategory.UNIVERSAL, ShopCategory.MARKET),
            item("cherry_sapling", new ItemStack(Items.CHERRY_SAPLING), 12, "Cherry Sapling",
                    ShopCategory.FARMING),
            item("jungle_sapling", new ItemStack(Items.JUNGLE_SAPLING), 12, "Jungle Sapling",
                    ShopCategory.FARMING),
            item("lantern_bundle", new ItemStack(Items.LANTERN, 8), 4, "Lantern Bundle",
                    ShopCategory.BUILDER, ShopCategory.DECORATOR),
            item("bone_meal_bundle", new ItemStack(Items.BONE_MEAL, 16), 3, "Bone Meal Bundle",
                    ShopCategory.FARMING),
            item("music_disc_cat", new ItemStack(Items.MUSIC_DISC_CAT), 32, "Music Disc",
                    ShopCategory.DECORATOR, ShopCategory.MARKET));

    private static final Map<ResourceLocation, ShopItem> ITEMS_BY_ID = createItemsById();

    private ShopRegistry() {
    }

    public static List<ShopItem> getAllItems() {
        return ITEMS;
    }

    public static List<ShopItem> getItemsForCategories(Set<ShopCategory> categories) {
        return ITEMS.stream()
                .filter(item -> item.getCategories().stream().anyMatch(categories::contains))
                .toList();
    }

    public static List<ShopItem> getUniversalItems() {
        return getItemsByCategory(ShopCategory.UNIVERSAL);
    }

    public static List<ShopItem> getItemsByCategory(ShopCategory category) {
        return ITEMS.stream()
                .filter(item -> item.getCategories().contains(category))
                .toList();
    }

    public static Optional<ShopItem> getById(ResourceLocation id) {
        return Optional.ofNullable(ITEMS_BY_ID.get(id));
    }

    private static ShopItem item(
            String path,
            ItemStack rewardStack,
            int costTokens,
            String displayName,
            ShopCategory... categories) {
        return new ShopItem(
                ResourceLocation.parse(CozyContracts.MOD_ID + ":" + path),
                rewardStack,
                costTokens,
                displayName,
                Set.of(categories));
    }

    private static Map<ResourceLocation, ShopItem> createItemsById() {
        Map<ResourceLocation, ShopItem> itemsById = new LinkedHashMap<>();
        for (ShopItem item : ITEMS) {
            itemsById.putIfAbsent(item.getId(), item);
        }
        return Map.copyOf(itemsById);
    }
}
