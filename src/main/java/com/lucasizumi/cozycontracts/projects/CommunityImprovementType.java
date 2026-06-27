package com.lucasizumi.cozycontracts.projects;

public enum CommunityImprovementType {
    FARMING("Farming"),
    BUILDER("Builder"),
    DECOR("Decor");

    private final String displayName;

    CommunityImprovementType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
