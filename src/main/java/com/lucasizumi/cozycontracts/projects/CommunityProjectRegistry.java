package com.lucasizumi.cozycontracts.projects;

import com.lucasizumi.cozycontracts.CozyContracts;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class CommunityProjectRegistry {
    public static final ResourceLocation VILLAGE_FIELDS =
            ResourceLocation.parse(CozyContracts.MOD_ID + ":village_fields");
    public static final ResourceLocation GARDEN_CORNER =
            ResourceLocation.parse(CozyContracts.MOD_ID + ":garden_corner");
    public static final ResourceLocation BUILDERS_YARD =
            ResourceLocation.parse(CozyContracts.MOD_ID + ":builders_yard");

    private static final List<CommunityProject> STARTER_PROJECTS = List.of(
            new CommunityProject(
                    VILLAGE_FIELDS,
                    "Village Fields",
                    CommunityImprovementType.FARMING,
                    "Prepare a small field so the settlement has room to grow food.",
                    "Village Fields completed! The settlement now has its first Farming improvement. Build and decorate the field however you like."),
            new CommunityProject(
                    GARDEN_CORNER,
                    "Garden Corner",
                    CommunityImprovementType.DECOR,
                    "Create a small garden corner to make the settlement feel warmer and more lived-in.",
                    "Garden Corner completed! The settlement now has its first Decor improvement. Keep shaping the space however you like."),
            new CommunityProject(
                    BUILDERS_YARD,
                    "Builder's Yard",
                    CommunityImprovementType.BUILDER,
                    "Set up a small yard for construction supplies and future building plans.",
                    "Builder's Yard completed! The settlement now has its first Builder improvement. This space can grow with future projects."));
    private static final Map<ResourceLocation, CommunityProject> PROJECTS_BY_ID = buildProjectsById();

    private CommunityProjectRegistry() {
    }

    public static List<CommunityProject> getStarterProjects() {
        return STARTER_PROJECTS;
    }

    public static Optional<CommunityProject> getById(ResourceLocation id) {
        return Optional.ofNullable(PROJECTS_BY_ID.get(id));
    }

    private static Map<ResourceLocation, CommunityProject> buildProjectsById() {
        Map<ResourceLocation, CommunityProject> projects = new LinkedHashMap<>();
        for (CommunityProject project : STARTER_PROJECTS) {
            projects.put(project.getId(), project);
        }
        return Map.copyOf(projects);
    }
}
