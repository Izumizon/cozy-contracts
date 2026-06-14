package com.lucasizumi.cozycontracts.contracts;

import net.minecraft.resources.ResourceLocation;

public final class Contract {
    private final ResourceLocation id;
    private final String title;
    private final String requester;
    private final String category;
    private final ContractDifficulty difficulty;
    private final int weight;
    private final ContractRequirement requirement;
    private final int rewardTokens;

    public Contract(
            ResourceLocation id,
            String title,
            String requester,
            String category,
            ContractDifficulty difficulty,
            int weight,
            ContractRequirement requirement,
            int rewardTokens) {
        this.id = id;
        this.title = title;
        this.requester = requester;
        this.category = category;
        this.difficulty = difficulty;
        this.weight = weight;
        this.requirement = requirement;
        this.rewardTokens = rewardTokens;
    }

    public ResourceLocation getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getRequester() {
        return requester;
    }

    public String getCategory() {
        return category;
    }

    public ContractDifficulty getDifficulty() {
        return difficulty;
    }

    public int getWeight() {
        return weight;
    }

    public ContractRequirement getRequirement() {
        return requirement;
    }

    public int getRewardTokens() {
        return rewardTokens;
    }
}
