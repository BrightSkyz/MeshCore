package me.bsky.skycore.types.enums;

public enum LogLevel {

    DEBUG("Debug"),
    INFO("Info"),
    WARN("Warn"),
    ERROR("Error"),
    SEVERE("Severe");

    private String name;

    LogLevel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
