package com.lucasizumi.cozycontracts.projects;

import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Set;

public final class ProjectSiteOrder {
    private final ResourceLocation id;
    private final ResourceLocation projectId;
    private final String title;
    private final ProjectSiteOrderType type;
    private final List<ProjectSiteOrderRequirement> requirements;
    private final int rewardTokens;
    private final Set<String> requiredMods;

    public ProjectSiteOrder(
            ResourceLocation id,
            ResourceLocation projectId,
            String title,
            ProjectSiteOrderType type,
            List<ProjectSiteOrderRequirement> requirements,
            int rewardTokens,
            Set<String> requiredMods) {
        this.id = id;
        this.projectId = projectId;
        this.title = title;
        this.type = type;
        this.requirements = List.copyOf(requirements);
        this.rewardTokens = rewardTokens;
        this.requiredMods = Set.copyOf(requiredMods);
    }

    public ResourceLocation getId() {
        return id;
    }

    public ResourceLocation getProjectId() {
        return projectId;
    }

    public String getTitle() {
        return title;
    }

    public ProjectSiteOrderType getType() {
        return type;
    }

    public List<ProjectSiteOrderRequirement> getRequirements() {
        return requirements;
    }

    public int getRewardTokens() {
        return rewardTokens;
    }

    public Set<String> getRequiredMods() {
        return requiredMods;
    }

    public boolean isModded() {
        return !requiredMods.isEmpty();
    }

    public String getRequirementPreview() {
        return requirements.stream()
                .map(ProjectSiteOrderRequirement::getPreviewText)
                .reduce((first, second) -> first + ", " + second)
                .orElse("No requirements");
    }
}
