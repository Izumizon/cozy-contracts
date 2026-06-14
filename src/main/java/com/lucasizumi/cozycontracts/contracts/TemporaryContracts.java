package com.lucasizumi.cozycontracts.contracts;

import net.minecraft.resources.ResourceLocation;

import java.util.List;

public final class TemporaryContracts {
    // Prototype hardcoded definitions. ContractRegistry is the public access layer
    // and will later be backed by datapack/JSON-loaded contracts.
    private static final List<Contract> CONTRACTS = List.of(
            contract("bakers_morning_rush", "Baker\u2019s Morning Rush", "Baker", "kitchen",
                    ContractDifficulty.EASY, 35,
                    exactItem("minecraft:bread", 8, "Bread"), 5),
            contract("sweet_treats", "Sweet Treats", "Baker", "kitchen",
                    ContractDifficulty.EASY, 25,
                    exactItem("minecraft:cookie", 12, "Cookies"), 6),
            contract("pumpkin_pie_order", "Pumpkin Pie Order", "Cook", "kitchen",
                    ContractDifficulty.MEDIUM, 18,
                    exactItem("minecraft:pumpkin_pie", 3, "Pumpkin Pies"), 8),
            contract("stock_the_pantry", "Stock the Pantry", "Farmer", "farmer",
                    ContractDifficulty.EASY, 25,
                    exactItem("minecraft:potato", 16, "Potatoes"), 5),
            contract("carrot_crate", "Carrot Crate", "Farmer", "farmer",
                    ContractDifficulty.EASY, 25,
                    exactItem("minecraft:carrot", 16, "Carrots"), 5),
            contract("flower_table", "Flower Table", "Decorator", "decorator",
                    ContractDifficulty.EASY, 25,
                    itemTag("minecraft:small_flowers", 8, "Small Flowers"), 4),
            contract("wool_delivery", "Wool Delivery", "Tailor", "decorator",
                    ContractDifficulty.EASY, 20,
                    itemTag("minecraft:wool", 16, "Wool"), 6),
            contract("cozy_carpets", "Cozy Carpets", "Decorator", "decorator",
                    ContractDifficulty.EASY, 18,
                    itemTag("minecraft:wool_carpets", 8, "Carpets"), 5),
            contract("roof_repairs", "Roof Repairs", "Mason", "builder",
                    ContractDifficulty.MEDIUM, 15,
                    itemTag("minecraft:logs", 32, "Logs"), 7),
            contract("street_lighting", "Street Lighting", "Mason", "builder",
                    ContractDifficulty.MEDIUM, 12,
                    exactItem("minecraft:torch", 16, "Torches"), 6),
            contract("seaside_supplies", "Seaside Supplies", "Fisher", "explorer",
                    ContractDifficulty.MEDIUM, 8,
                    exactItem("minecraft:kelp", 16, "Kelp"), 8),
            contract("bone_meal_help", "Bone Meal Help", "Farmer", "hunter",
                    ContractDifficulty.EASY, 10,
                    exactItem("minecraft:bone", 8, "Bones"), 5),
            contract("string_for_the_tailor", "String for the Tailor", "Tailor", "hunter",
                    ContractDifficulty.EASY, 10,
                    exactItem("minecraft:string", 12, "String"), 6),
            contract("glass_for_windows", "Glass for Windows", "Mason", "builder",
                    ContractDifficulty.MEDIUM, 10,
                    exactItem("minecraft:glass", 16, "Glass"), 8));

    private TemporaryContracts() {
    }

    public static List<Contract> getAllContracts() {
        return CONTRACTS;
    }

    private static Contract contract(
            String path,
            String title,
            String requester,
            String category,
            ContractDifficulty difficulty,
            int weight,
            ContractRequirement requirement,
            int rewardTokens) {
        return new Contract(
                ResourceLocation.parse("cozy_contracts:" + path),
                title,
                requester,
                category,
                difficulty,
                weight,
                requirement,
                rewardTokens);
    }

    private static ContractRequirement exactItem(String id, int count, String displayName) {
        return ContractRequirement.exactItem(ResourceLocation.parse(id), count, displayName);
    }

    private static ContractRequirement itemTag(String id, int count, String displayName) {
        return ContractRequirement.itemTag(ResourceLocation.parse(id), count, displayName);
    }
}
