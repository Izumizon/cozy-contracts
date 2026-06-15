package com.lucasizumi.cozycontracts.contracts;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class ContractRegistry {
    private static final Logger LOGGER = LogUtils.getLogger();

    /*
     * TemporaryContracts is the fallback source. Datapack/JSON reloads replace this
     * immutable snapshot without requiring callers to know the backing source.
     */
    private static volatile RegistrySnapshot snapshot =
            createSnapshot(TemporaryContracts.getAllContracts(), false);

    private ContractRegistry() {
    }

    public static List<Contract> getAllContracts() {
        return snapshot.contracts();
    }

    public static Optional<Contract> getById(ResourceLocation id) {
        return Optional.ofNullable(snapshot.contractsById().get(id));
    }

    public static Map<ResourceLocation, Contract> getAllById() {
        return snapshot.contractsById();
    }

    public static boolean isUsingJsonContracts() {
        return snapshot.jsonLoaded();
    }

    public static void replaceLoadedContracts(Collection<Contract> contracts) {
        if (contracts.isEmpty()) {
            snapshot = createSnapshot(TemporaryContracts.getAllContracts(), false);
            LOGGER.warn(
                    "No JSON contracts loaded; using {} temporary fallback contracts",
                    snapshot.contracts().size());
            return;
        }

        snapshot = createSnapshot(contracts, true);
        LOGGER.info(
                "Contract registry now uses {} JSON-loaded contracts",
                snapshot.contracts().size());
    }

    private static RegistrySnapshot createSnapshot(
            Collection<Contract> contracts,
            boolean jsonLoaded) {
        Map<ResourceLocation, Contract> contractsById = new LinkedHashMap<>();

        for (Contract contract : contracts) {
            if (contractsById.putIfAbsent(contract.getId(), contract) != null) {
                LOGGER.warn(
                        "Ignoring duplicate contract ID {}; keeping the first definition",
                        contract.getId());
            }
        }

        return new RegistrySnapshot(
                List.copyOf(contractsById.values()),
                Map.copyOf(contractsById),
                jsonLoaded);
    }

    private record RegistrySnapshot(
            List<Contract> contracts,
            Map<ResourceLocation, Contract> contractsById,
            boolean jsonLoaded) {
    }
}
