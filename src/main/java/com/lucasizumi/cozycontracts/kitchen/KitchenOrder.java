package com.lucasizumi.cozycontracts.kitchen;

import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.Set;

public final class KitchenOrder {
    private final ResourceLocation id;
    private final String title;
    private final String requester;
    private final KitchenOrderType type;
    private final KitchenOrderRequirement requirement;
    private final String requirementDisplay;
    private final String supportDisplay;
    private final int rewardTokens;
    private final int dailyLimit;
    private final Set<String> tags;
    private final int weight;

    public KitchenOrder(
            ResourceLocation id,
            String title,
            String requester,
            KitchenOrderType type,
            KitchenOrderRequirement requirement,
            String requirementDisplay,
            String supportDisplay,
            int rewardTokens,
            int dailyLimit,
            Set<String> tags,
            int weight) {
        this.id = id;
        this.title = title;
        this.requester = requester;
        this.type = type;
        this.requirement = requirement;
        this.requirementDisplay = requirementDisplay;
        this.supportDisplay = supportDisplay;
        this.rewardTokens = rewardTokens;
        this.dailyLimit = dailyLimit;
        this.tags = Collections.unmodifiableSet(Set.copyOf(tags));
        this.weight = weight;
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

    public KitchenOrderType getType() {
        return type;
    }

    public KitchenOrderRequirement getRequirement() {
        return requirement;
    }

    public String getRequirementDisplay() {
        return requirementDisplay;
    }

    public String getSupportDisplay() {
        return supportDisplay;
    }

    public int getRewardTokens() {
        return rewardTokens;
    }

    public int getDailyLimit() {
        return dailyLimit;
    }

    public Set<String> getTags() {
        return tags;
    }

    public int getWeight() {
        return weight;
    }
}
