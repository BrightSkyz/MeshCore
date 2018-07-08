package me.bsky.skycore.types.enums;

public enum ProgramMode {

    APPLICATION(0, "Application"),
    BUNGEECORD(1, "Bungeecord"),
    SPIGOT(2, "Spigot");

    private Integer typeId;
    private String cleanName;

    ProgramMode(Integer typeId, String cleanName) {
        this.typeId = typeId;
        this.cleanName = cleanName;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public String getCleanName() {
        return cleanName;
    }
}
