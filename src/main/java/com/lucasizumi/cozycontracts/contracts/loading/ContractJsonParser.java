package com.lucasizumi.cozycontracts.contracts.loading;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lucasizumi.cozycontracts.contracts.Contract;
import com.lucasizumi.cozycontracts.contracts.ContractDifficulty;
import com.lucasizumi.cozycontracts.contracts.ContractRequirement;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;
import java.util.Optional;

public final class ContractJsonParser {
    private ContractJsonParser() {
    }

    public static Optional<Contract> parse(ResourceLocation id, JsonElement json) {
        if (!json.isJsonObject()) {
            throw new IllegalArgumentException("Contract root must be a JSON object");
        }

        JsonObject object = json.getAsJsonObject();
        if (!requiredModsLoaded(object)) {
            return Optional.empty();
        }

        return Optional.of(new Contract(
                id,
                requiredString(object, "title"),
                requiredString(object, "requester"),
                requiredString(object, "category"),
                parseDifficulty(requiredString(object, "difficulty")),
                requiredPositiveInt(object, "weight"),
                parseRequirement(requiredObject(object, "requirement")),
                requiredPositiveInt(object, "reward_tokens")));
    }

    private static ContractRequirement parseRequirement(JsonObject object) {
        String type = requiredString(object, "type").toLowerCase(Locale.ROOT);
        ResourceLocation id = parseId(requiredString(object, "id"));
        int count = requiredPositiveInt(object, "count");
        String displayName = requiredString(object, "display_name");

        return switch (type) {
            case "item" -> {
                if (!ForgeRegistries.ITEMS.containsKey(id)) {
                    throw new IllegalArgumentException("Unknown item requirement: " + id);
                }
                yield ContractRequirement.exactItem(id, count, displayName);
            }
            case "tag" -> ContractRequirement.itemTag(id, count, displayName);
            default -> throw new IllegalArgumentException(
                    "Requirement type must be 'item' or 'tag'");
        };
    }

    private static boolean requiredModsLoaded(JsonObject object) {
        if (!object.has("required_mods")) {
            return true;
        }

        JsonElement element = object.get("required_mods");
        if (!element.isJsonArray()) {
            throw new IllegalArgumentException("required_mods must be an array");
        }

        JsonArray requiredMods = element.getAsJsonArray();
        for (JsonElement modElement : requiredMods) {
            if (!modElement.isJsonPrimitive()
                    || !modElement.getAsJsonPrimitive().isString()) {
                throw new IllegalArgumentException(
                        "required_mods entries must be strings");
            }
            if (!ModList.get().isLoaded(modElement.getAsString())) {
                return false;
            }
        }
        return true;
    }

    private static ContractDifficulty parseDifficulty(String value) {
        try {
            return ContractDifficulty.valueOf(value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Invalid difficulty: " + value);
        }
    }

    private static ResourceLocation parseId(String value) {
        ResourceLocation id = ResourceLocation.tryParse(value);
        if (id == null) {
            throw new IllegalArgumentException("Invalid requirement id: " + value);
        }
        return id;
    }

    private static JsonObject requiredObject(JsonObject object, String fieldName) {
        JsonElement element = object.get(fieldName);
        if (element == null || !element.isJsonObject()) {
            throw new IllegalArgumentException(
                    "Missing or invalid object field: " + fieldName);
        }
        return element.getAsJsonObject();
    }

    private static String requiredString(JsonObject object, String fieldName) {
        JsonElement element = object.get(fieldName);
        if (element == null
                || !element.isJsonPrimitive()
                || !element.getAsJsonPrimitive().isString()) {
            throw new IllegalArgumentException(
                    "Missing or invalid string field: " + fieldName);
        }

        String value = element.getAsString().trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Field cannot be empty: " + fieldName);
        }
        return value;
    }

    private static int requiredPositiveInt(JsonObject object, String fieldName) {
        JsonElement element = object.get(fieldName);
        if (element == null
                || !element.isJsonPrimitive()
                || !element.getAsJsonPrimitive().isNumber()) {
            throw new IllegalArgumentException(
                    "Missing or invalid integer field: " + fieldName);
        }

        int value;
        try {
            value = element.getAsInt();
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Invalid integer field: " + fieldName);
        }
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be greater than zero");
        }
        return value;
    }
}
