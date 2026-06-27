package com.lucasizumi.cozycontracts.projects;

import java.util.List;

public record ProjectValidationResult(
        boolean ready,
        List<String> missingRequirements) {
    public ProjectValidationResult {
        missingRequirements = List.copyOf(missingRequirements);
    }

    public static ProjectValidationResult success() {
        return new ProjectValidationResult(true, List.of());
    }

    public static ProjectValidationResult missing(List<String> requirements) {
        return new ProjectValidationResult(false, requirements);
    }
}
