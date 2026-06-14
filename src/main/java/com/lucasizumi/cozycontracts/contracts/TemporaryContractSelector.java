package com.lucasizumi.cozycontracts.contracts;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public final class TemporaryContractSelector {
    private static final int ACTIVE_CONTRACT_COUNT = 3;
    private static final long SEED_SALT = 0x43A7D19B5E2C6F81L;

    private TemporaryContractSelector() {
    }

    public static List<Contract> getActiveContracts(Level level, BlockPos boardPos) {
        // Temporary weighted generator. ContractRegistry will later be populated by
        // the final JSON-backed contract loading system.
        List<Contract> available = new ArrayList<>(ContractRegistry.getAllContracts());
        if (available.size() <= ACTIVE_CONTRACT_COUNT) {
            return List.copyOf(available);
        }

        long minecraftDay = level.getDayTime() / 24000L;
        long seed = SEED_SALT
                ^ boardPos.asLong()
                ^ (minecraftDay * 341873128712L);
        RandomSource random = RandomSource.create(seed);
        List<Contract> selected = new ArrayList<>(ACTIVE_CONTRACT_COUNT);

        while (selected.size() < ACTIVE_CONTRACT_COUNT && !available.isEmpty()) {
            int totalWeight = available.stream()
                    .mapToInt(contract -> Math.max(0, contract.getWeight()))
                    .sum();

            if (totalWeight == 0) {
                selected.add(available.remove(random.nextInt(available.size())));
                continue;
            }

            int roll = random.nextInt(totalWeight);
            for (int index = 0; index < available.size(); index++) {
                roll -= Math.max(0, available.get(index).getWeight());
                if (roll < 0) {
                    selected.add(available.remove(index));
                    break;
                }
            }
        }

        return List.copyOf(selected);
    }
}
