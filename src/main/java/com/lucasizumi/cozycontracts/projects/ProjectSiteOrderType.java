package com.lucasizumi.cozycontracts.projects;

public enum ProjectSiteOrderType {
    STANDING("Standing"),
    CATERING("Catering");

    private final String displayName;

    ProjectSiteOrderType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
