package com.lucasizumi.cozycontracts.kitchen.loading;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.lucasizumi.cozycontracts.kitchen.KitchenOrder;
import com.lucasizumi.cozycontracts.kitchen.KitchenOrderRegistry;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class JsonKitchenOrderReloadListener extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().create();

    public JsonKitchenOrderReloadListener() {
        super(GSON, "kitchen_orders");
    }

    public static void register(AddReloadListenerEvent event) {
        event.addListener(new JsonKitchenOrderReloadListener());
    }

    @Override
    protected void apply(
            Map<ResourceLocation, JsonElement> jsonOrders,
            ResourceManager resourceManager,
            ProfilerFiller profiler) {
        List<KitchenOrder> loadedOrders = new ArrayList<>();

        for (Map.Entry<ResourceLocation, JsonElement> entry : jsonOrders.entrySet()) {
            try {
                KitchenOrderJsonParser.parse(entry.getKey(), entry.getValue())
                        .ifPresent(loadedOrders::add);
            } catch (RuntimeException exception) {
                LOGGER.warn(
                        "Skipping invalid Cozy Contracts kitchen order {}: {}",
                        entry.getKey(),
                        exception.getMessage());
            }
        }

        KitchenOrderRegistry.replaceLoadedOrders(loadedOrders);
        LOGGER.info("Loaded {} JSON Cozy Contracts kitchen orders", loadedOrders.size());
    }
}
