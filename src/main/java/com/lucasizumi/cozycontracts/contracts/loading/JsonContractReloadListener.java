package com.lucasizumi.cozycontracts.contracts.loading;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.lucasizumi.cozycontracts.contracts.Contract;
import com.lucasizumi.cozycontracts.contracts.ContractRegistry;
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

public final class JsonContractReloadListener extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().create();

    public JsonContractReloadListener() {
        super(GSON, "contracts");
    }

    public static void register(AddReloadListenerEvent event) {
        event.addListener(new JsonContractReloadListener());
    }

    @Override
    protected void apply(
            Map<ResourceLocation, JsonElement> jsonContracts,
            ResourceManager resourceManager,
            ProfilerFiller profiler) {
        List<Contract> loadedContracts = new ArrayList<>();

        for (Map.Entry<ResourceLocation, JsonElement> entry : jsonContracts.entrySet()) {
            try {
                ContractJsonParser.parse(entry.getKey(), entry.getValue())
                        .ifPresent(loadedContracts::add);
            } catch (RuntimeException exception) {
                LOGGER.warn(
                        "Skipping invalid Cozy Contracts contract {}: {}",
                        entry.getKey(),
                        exception.getMessage());
            }
        }

        ContractRegistry.replaceLoadedContracts(loadedContracts);
        LOGGER.info("Loaded {} JSON Cozy Contracts contracts", loadedContracts.size());
    }
}
