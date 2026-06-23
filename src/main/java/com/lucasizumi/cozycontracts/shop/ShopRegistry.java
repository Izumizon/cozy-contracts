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
            item("bell", new ItemStack(Items.BELL), 16, "Bell",
                    ShopCategory.UNIVERSAL, ShopCategory.MARKET),
            item("saddle", new ItemStack(Items.SADDLE), 25, "Saddle",
                    ShopCategory.UNIVERSAL, ShopCategory.FARMING, ShopCategory.MARKET),
            item("lantern_bundle", new ItemStack(Items.LANTERN, 8), 8, "Lantern Bundle",
                    ShopCategory.UNIVERSAL, ShopCategory.BUILDER, ShopCategory.DECORATOR),

            item("dirt_bundle", new ItemStack(Items.DIRT, 64), 3, "Dirt Bundle",
                    ShopCategory.FARMING, ShopCategory.BUILDER),
            item("bone_meal_bundle", new ItemStack(Items.BONE_MEAL, 32), 8, "Bone Meal Bundle",
                    ShopCategory.FARMING),
            item("oak_logs", new ItemStack(Items.OAK_LOG, 32), 8, "Oak Logs",
                    ShopCategory.FARMING, ShopCategory.BUILDER),
            item("spruce_logs", new ItemStack(Items.SPRUCE_LOG, 32), 8, "Spruce Logs",
                    ShopCategory.FARMING, ShopCategory.BUILDER),
            item("carrots", new ItemStack(Items.CARROT, 8), 4, "Carrots",
                    ShopCategory.FARMING, ShopCategory.KITCHEN),
            item("potatoes", new ItemStack(Items.POTATO, 8), 4, "Potatoes",
                    ShopCategory.FARMING, ShopCategory.KITCHEN),
            item("beetroot_seeds", new ItemStack(Items.BEETROOT_SEEDS, 8), 5, "Beetroot Seeds",
                    ShopCategory.FARMING),
            item("wheat", new ItemStack(Items.WHEAT, 16), 8, "Wheat",
                    ShopCategory.FARMING, ShopCategory.KITCHEN),
            item("hay_bale_bundle", new ItemStack(Items.HAY_BLOCK, 8), 10, "Hay Bale Bundle",
                    ShopCategory.FARMING),
            item("oak_saplings", new ItemStack(Items.OAK_SAPLING, 4), 10, "Oak Saplings",
                    ShopCategory.FARMING),
            item("cherry_saplings", new ItemStack(Items.CHERRY_SAPLING, 4), 12, "Cherry Saplings",
                    ShopCategory.FARMING),

            item("sugar", new ItemStack(Items.SUGAR, 16), 10, "Sugar",
                    ShopCategory.KITCHEN),
            item("eggs", new ItemStack(Items.EGG, 8), 8, "Eggs",
                    ShopCategory.KITCHEN, ShopCategory.FARMING),
            item("milk_bucket", new ItemStack(Items.MILK_BUCKET), 10, "Milk Bucket",
                    ShopCategory.KITCHEN, ShopCategory.FARMING),
            item("cocoa_beans", new ItemStack(Items.COCOA_BEANS, 8), 10, "Cocoa Beans",
                    ShopCategory.KITCHEN),
            item("charcoal", new ItemStack(Items.CHARCOAL, 16), 7, "Charcoal",
                    ShopCategory.KITCHEN, ShopCategory.BUILDER),
            item("bowls", new ItemStack(Items.BOWL, 16), 5, "Bowls",
                    ShopCategory.KITCHEN),
            item("glass_bottles", new ItemStack(Items.GLASS_BOTTLE, 16), 6, "Glass Bottles",
                    ShopCategory.KITCHEN),

            item("stone_bundle", new ItemStack(Items.STONE, 64), 4, "Stone Bundle",
                    ShopCategory.BUILDER),
            item("sand_bundle", new ItemStack(Items.SAND, 64), 4, "Sand Bundle",
                    ShopCategory.BUILDER),
            item("glass_bundle", new ItemStack(Items.GLASS, 32), 18, "Glass Bundle",
                    ShopCategory.BUILDER, ShopCategory.DECORATOR),
            item("clay_ball_bundle", new ItemStack(Items.CLAY_BALL, 32), 8, "Clay Ball Bundle",
                    ShopCategory.BUILDER, ShopCategory.DECORATOR),
            item("brick_bundle", new ItemStack(Items.BRICK, 32), 10, "Brick Bundle",
                    ShopCategory.BUILDER),
            item("stone_bricks", new ItemStack(Items.STONE_BRICKS, 32), 8, "Stone Bricks",
                    ShopCategory.BUILDER),
            item("scaffolding", new ItemStack(Items.SCAFFOLDING, 16), 10, "Scaffolding",
                    ShopCategory.BUILDER),

            item("candles", new ItemStack(Items.CANDLE, 16), 10, "Candles",
                    ShopCategory.DECORATOR),
            item("white_wool", new ItemStack(Items.WHITE_WOOL, 16), 8, "White Wool",
                    ShopCategory.DECORATOR),
            item("red_dye", new ItemStack(Items.RED_DYE, 8), 6, "Red Dye",
                    ShopCategory.DECORATOR),
            item("blue_dye", new ItemStack(Items.BLUE_DYE, 8), 8, "Blue Dye",
                    ShopCategory.DECORATOR),
            item("yellow_dye", new ItemStack(Items.YELLOW_DYE, 8), 6, "Yellow Dye",
                    ShopCategory.DECORATOR),
            item("poppies", new ItemStack(Items.POPPY, 8), 6, "Poppies",
                    ShopCategory.DECORATOR),
            item("dandelions", new ItemStack(Items.DANDELION, 8), 6, "Dandelions",
                    ShopCategory.DECORATOR),
            item("oak_leaves", new ItemStack(Items.OAK_LEAVES, 32), 7, "Oak Leaves",
                    ShopCategory.DECORATOR),
            item("terracotta", new ItemStack(Items.TERRACOTTA, 16), 8, "Terracotta",
                    ShopCategory.DECORATOR, ShopCategory.BUILDER),
            item("flower_pots", new ItemStack(Items.FLOWER_POT, 8), 8, "Flower Pots",
                    ShopCategory.DECORATOR));

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
