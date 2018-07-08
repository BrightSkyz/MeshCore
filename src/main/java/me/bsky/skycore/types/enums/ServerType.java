package me.bsky.skycore.types.enums;

public enum ServerType {

    BUNGEECORD("Bungeecord", "bungeecord.jar"),
    SPIGOT("Spigot", "spigot.jar"),
    LOBBY("Spigot", "spigot.jar"),
    MINI("Spigot", "spigot.jar");

    private String cleanName;
    private String jarName;

    ServerType(String cleanName, String jarName) {
        this.cleanName = cleanName;
        this.jarName = jarName;
    }

    public String getCleanName() {
        return cleanName;
    }

    public String getJarName() {
        return jarName;
    }
}
