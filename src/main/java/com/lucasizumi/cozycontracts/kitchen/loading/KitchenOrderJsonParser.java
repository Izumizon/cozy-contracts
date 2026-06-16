package com.lucasizumi.cozycontracts.kitchen.loading;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lucasizumi.cozycontracts.kitchen.KitchenOrder;
import com.lucasizumi.cozycontracts.kitchen.KitchenOrderRequirement;
import com.lucasizumi.cozycontracts.kitchen.KitchenOrderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public final class KitchenOrderJsonParser {
    private KitchenOrderJsonParser() {
    }

    public static Optional<KitchenOrder> parse(ResourceLocation id, JsonElement json) {
        if (!json.isJsonObject()) {
            throw new IllegalArgumentException("Kitchen order root must be a JSON object");
        }

        JsonObject object = json.getAsJsonObject();
        if (!requiredModsLoaded(object)) {
            return Optional.empty();
        }

        KitchenOrderRequirement requirement =
                parseRequirement(requiredObject(object, "requirement"));

        return Optional.of(new KitchenOrder(
                id,
                requiredString(object, "title"),
                requiredString(object, "requester"),
                parseType(requiredString(object, "type")),
                requirement,
                requirement.getPreviewText(),
                optionalString(object, "support_display", ""),
                requiredPositiveInt(object, "reward_tokens"),
                requiredPositiveInt(object, "daily_limit"),
                Set.of(),
                optionalPositiveInt(object, "weight", 1)));
    }

    private static KitchenOrderRequirement parseRequirement(JsonObject object) {
        String type = requiredString(object, "type").toLowerCase(Locale.ROOT);
        ResourceLocation id = parseId(requiredString(object, "id"), "requirement id");
        int count = requiredPositiveInt(object, "count");
        String displayName = requiredString(object, "display");

        return switch (type) {
            case "item" -> {
                if (!ForgeRegistries.ITEMS.containsKey(id)) {
                    throw new IllegalArgumentException("Unknown kitchen item requirement: " + id);
                }
                yield KitchenOrderRequirement.exactItem(id, count, displayName);
            }
            case "tag" -> KitchenOrderRequirement.itemTag(id, count, displayName);
            default -> throw new IllegalArgumentException(
                    "Kitchen requirement type must be 'item' or 'tag'");
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
                throw new IllegalArgumentException("required_mods entries must be strings");
            }
            if (!ModList.get().isLoaded(modElement.getAsString())) {
                return false;
            }
        }
        return true;
    }

    private static KitchenOrderType parseType(String value) {
        try {
            return KitchenOrderType.valueOf(value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Invalid kitchen order type: " + value);
        }
    }

    private static ResourceLocation parseId(String value, String fieldName) {
        ResourceLocation id = ResourceLocation.tryParse(value);
        if (id == null) {
            throw new IllegalArgumentException("Invalid " + fieldName + ": " + value);
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

    private static String optionalString(
            JsonObject object,
            String fieldName,
            String fallback) {
        JsonElement element = object.get(fieldName);
        if (element == null) {
            return fallback;
        }
        if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isString()) {
            throw new IllegalArgumentException(
                    "Invalid string field: " + fieldName);
        }
        return element.getAsString().trim();
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

    private static int optionalPositiveInt(
            JsonObject object,
            String fieldName,
            int fallback) {
        if (!object.has(fieldName)) {
            return fallback;
        }
        return requiredPositiveInt(object, fieldName);
    }
}
