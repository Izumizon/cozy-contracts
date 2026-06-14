package com.lucasizumi.cozycontracts.contracts;

import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class ContractRegistry {
    /*
     * Currently backed by TemporaryContracts. Future datapack/JSON loading should
     * populate this central registry instead of changing its callers.
     */
    private static final Map<ResourceLocation, Contract> CONTRACTS_BY_ID = buildContractsById();
    private static final List<Contract> ALL_CONTRACTS =
            List.copyOf(CONTRACTS_BY_ID.values());

    private ContractRegistry() {
    }

    public static List<Contract> getAllContracts() {
        return ALL_CONTRACTS;
    }

    public static Optional<Contract> getById(ResourceLocation id) {
        return Optional.ofNullable(CONTRACTS_BY_ID.get(id));
    }

    public static Map<ResourceLocation, Contract> getAllById() {
        return CONTRACTS_BY_ID;
    }

    private static Map<ResourceLocation, Contract> buildContractsById() {
        Map<ResourceLocation, Contract> contracts = new LinkedHashMap<>();

        for (Contract contract : TemporaryContracts.getAllContracts()) {
            // Keep the first definition so an accidental duplicate cannot replace it.
            contracts.putIfAbsent(contract.getId(), contract);
        }

        return Map.copyOf(contracts);
    }
}
