package com.lucasizumi.cozycontracts.projects;

import net.minecraft.resources.ResourceLocation;

public final class CommunityProject {
    private final ResourceLocation id;
    private final String title;
    private final CommunityImprovementType improvementType;
    private final String description;
    private final String completionMessage;

    public CommunityProject(
            ResourceLocation id,
            String title,
            CommunityImprovementType improvementType,
            String description,
            String completionMessage) {
        this.id = id;
        this.title = title;
        this.improvementType = improvementType;
        this.description = description;
        this.completionMessage = completionMessage;
    }

    public ResourceLocation getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public CommunityImprovementType getImprovementType() {
        return improvementType;
    }

    public String getDescription() {
        return description;
    }

    public String getCompletionMessage() {
        return completionMessage;
    }
}
