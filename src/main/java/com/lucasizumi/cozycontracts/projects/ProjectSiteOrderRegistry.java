package com.lucasizumi.cozycontracts.projects;

import com.lucasizumi.cozycontracts.CozyContracts;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class ProjectSiteOrderRegistry {
    private static final List<ProjectSiteOrder> ORDERS = List.of(
            standing("village_fields_wheat_delivery", CommunityProjectRegistry.VILLAGE_FIELDS, "Wheat Delivery", 2,
                    List.of(item(Items.WHEAT, 16, "Wheat"))),
            standing("village_fields_carrot_delivery", CommunityProjectRegistry.VILLAGE_FIELDS, "Carrot Delivery", 2,
                    List.of(item(Items.CARROT, 8, "Carrots"))),
            standing("village_fields_potato_delivery", CommunityProjectRegistry.VILLAGE_FIELDS, "Potato Delivery", 2,
                    List.of(item(Items.POTATO, 8, "Potatoes"))),
            standing("village_fields_beetroot_delivery", CommunityProjectRegistry.VILLAGE_FIELDS, "Beetroot Delivery", 2,
                    List.of(item(Items.BEETROOT, 8, "Beetroots"))),

            standing("garden_corner_flower_bundle", CommunityProjectRegistry.GARDEN_CORNER, "Flower Bundle", 2,
                    List.of(tag("minecraft:small_flowers", 8, "Small Flowers"))),
            standing("garden_corner_leafy_decoration", CommunityProjectRegistry.GARDEN_CORNER, "Leafy Decoration", 3,
                    List.of(item(Items.OAK_LEAVES, 32, "Oak Leaves"))),
            standing("garden_corner_candle_delivery", CommunityProjectRegistry.GARDEN_CORNER, "Candle Delivery", 3,
                    List.of(item(Items.CANDLE, 8, "Candles"))),
            standing("garden_corner_dye_basket", CommunityProjectRegistry.GARDEN_CORNER, "Dye Basket", 2,
                    List.of(item(Items.RED_DYE, 8, "Red Dye"))),

            standing("builders_yard_log_delivery", CommunityProjectRegistry.BUILDERS_YARD, "Log Delivery", 4,
                    List.of(tag("minecraft:logs", 32, "Logs"))),
            standing("builders_yard_stone_delivery", CommunityProjectRegistry.BUILDERS_YARD, "Stone Delivery", 2,
                    List.of(item(Items.STONE, 64, "Stone"))),
            standing("builders_yard_glass_delivery", CommunityProjectRegistry.BUILDERS_YARD, "Glass Delivery", 6,
                    List.of(item(Items.GLASS, 16, "Glass"))),
            standing("builders_yard_brick_delivery", CommunityProjectRegistry.BUILDERS_YARD, "Brick Delivery", 4,
                    List.of(item(Items.BRICK, 16, "Bricks"))),

            standing("pantry_bread_basket", CommunityProjectRegistry.COMMUNITY_PANTRY, "Bread Basket", 3,
                    List.of(item(Items.BREAD, 8, "Bread"))),
            standing("pantry_cookie_plate", CommunityProjectRegistry.COMMUNITY_PANTRY, "Cookie Plate", 5,
                    List.of(item(Items.COOKIE, 16, "Cookies"))),
            standing("pantry_baked_potato_basket", CommunityProjectRegistry.COMMUNITY_PANTRY, "Baked Potato Basket", 4,
                    List.of(item(Items.BAKED_POTATO, 16, "Baked Potatoes"))),
            standing("pantry_soup_delivery", CommunityProjectRegistry.COMMUNITY_PANTRY, "Soup Delivery", 5,
                    List.of(item(Items.MUSHROOM_STEW, 4, "Mushroom Stew"))),

            catering("pantry_farmhand_lunch", "Farmhand Lunch", 8, Set.of(),
                    List.of(
                            item(Items.BREAD, 4, "Bread"),
                            item(Items.BAKED_POTATO, 8, "Baked Potatoes"),
                            item(Items.CARROT, 8, "Carrots"))),
            catering("pantry_builders_supper", "Builder's Supper", 11, Set.of(),
                    List.of(
                            item(Items.BREAD, 6, "Bread"),
                            item(Items.COOKED_BEEF, 4, "Cooked Beef"),
                            item(Items.BAKED_POTATO, 8, "Baked Potatoes"))),
            catering("pantry_village_dessert_table", "Village Dessert Table", 14, Set.of(),
                    List.of(
                            item(Items.COOKIE, 16, "Cookies"),
                            item(Items.PUMPKIN_PIE, 2, "Pumpkin Pie"),
                            item(Items.CAKE, 1, "Cake"))),

            standing("pantry_fd_soup_kitchen_bowls", CommunityProjectRegistry.COMMUNITY_PANTRY, "Soup Kitchen Bowls", 7,
                    Set.of("farmersdelight"),
                    List.of(item("farmersdelight:vegetable_soup", 2, "Vegetable Soup"))),
            standing("pantry_fd_sandwich_tray", CommunityProjectRegistry.COMMUNITY_PANTRY, "Sandwich Tray", 8,
                    Set.of("farmersdelight"),
                    List.of(item("farmersdelight:egg_sandwich", 4, "Egg Sandwiches"))),
            catering("pantry_fd_workers_stew", "Workers' Stew Supper", 14, Set.of("farmersdelight"),
                    List.of(
                            item(Items.BREAD, 4, "Bread"),
                            item("farmersdelight:beef_stew", 2, "Beef Stew"),
                            item(Items.BAKED_POTATO, 8, "Baked Potatoes"))),
            catering("pantry_fd_picnic_catering", "Picnic Catering", 13, Set.of("farmersdelight"),
                    List.of(
                            item("farmersdelight:mixed_salad", 2, "Mixed Salad"),
                            item("farmersdelight:egg_sandwich", 3, "Egg Sandwiches"),
                            item(Items.COOKIE, 8, "Cookies"))),

            standing("pantry_create_pizza_slices", CommunityProjectRegistry.COMMUNITY_PANTRY, "Pizza Slice Crate", 9,
                    Set.of("createfood"),
                    List.of(item("createfood:cheese_pizza_slice", 8, "Cheese Pizza Slices"))),
            catering("pantry_create_dessert_table", "Cheesecake Dessert Table", 13, Set.of("createfood"),
                    List.of(
                            item("createfood:cheesecake_slice", 4, "Cheesecake Slices"),
                            item("createfood:chocolate_glazed_donut", 4, "Chocolate Donuts"),
                            item(Items.COOKIE, 8, "Cookies"))));

    private static final Map<ResourceLocation, ProjectSiteOrder> ORDERS_BY_ID = createOrdersById();

    private ProjectSiteOrderRegistry() {
    }

    public static List<ProjectSiteOrder> getAvailableOrdersForProject(ResourceLocation projectId) {
        return ORDERS.stream()
                .filter(order -> order.getProjectId().equals(projectId))
                .filter(ProjectSiteOrderRegistry::isAvailable)
                .sorted(Comparator.comparing(ProjectSiteOrder::isModded).reversed())
                .toList();
    }

    public static Optional<ProjectSiteOrder> getAvailableById(ResourceLocation id) {
        ProjectSiteOrder order = ORDERS_BY_ID.get(id);
        return order != null && isAvailable(order) ? Optional.of(order) : Optional.empty();
    }

    public static List<ProjectSiteOrder> getAllOrders() {
        return ORDERS;
    }

    private static boolean isAvailable(ProjectSiteOrder order) {
        if (order.getRequiredMods().stream().anyMatch(modId -> !ModList.get().isLoaded(modId))) {
            return false;
        }

        return order.getRequirements().stream().allMatch(ProjectSiteOrderRequirement::isValid);
    }

    private static Map<ResourceLocation, ProjectSiteOrder> createOrdersById() {
        Map<ResourceLocation, ProjectSiteOrder> orders = new LinkedHashMap<>();
        for (ProjectSiteOrder order : ORDERS) {
            orders.putIfAbsent(order.getId(), order);
        }
        return Map.copyOf(orders);
    }

    private static ProjectSiteOrder standing(
            String path,
            ResourceLocation projectId,
            String title,
            int rewardTokens,
            List<ProjectSiteOrderRequirement> requirements) {
        return standing(path, projectId, title, rewardTokens, Set.of(), requirements);
    }

    private static ProjectSiteOrder standing(
            String path,
            ResourceLocation projectId,
            String title,
            int rewardTokens,
            Set<String> requiredMods,
            List<ProjectSiteOrderRequirement> requirements) {
        return new ProjectSiteOrder(
                id(path),
                projectId,
                title,
                ProjectSiteOrderType.STANDING,
                requirements,
                rewardTokens,
                requiredMods);
    }

    private static ProjectSiteOrder catering(
            String path,
            String title,
            int rewardTokens,
            Set<String> requiredMods,
            List<ProjectSiteOrderRequirement> requirements) {
        return new ProjectSiteOrder(
                id(path),
                CommunityProjectRegistry.COMMUNITY_PANTRY,
                title,
                ProjectSiteOrderType.CATERING,
                requirements,
                rewardTokens,
                requiredMods);
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.parse(CozyContracts.MOD_ID + ":" + path);
    }

    private static ProjectSiteOrderRequirement item(
            net.minecraft.world.item.Item item,
            int count,
            String displayName) {
        return ProjectSiteOrderRequirement.exactItem(ForgeRegistries.ITEMS.getKey(item), count, displayName);
    }

    private static ProjectSiteOrderRequirement item(
            String itemId,
            int count,
            String displayName) {
        return ProjectSiteOrderRequirement.exactItem(ResourceLocation.parse(itemId), count, displayName);
    }

    private static ProjectSiteOrderRequirement tag(
            String tagId,
            int count,
            String displayName) {
        return ProjectSiteOrderRequirement.itemTag(ResourceLocation.parse(tagId), count, displayName);
    }
}
