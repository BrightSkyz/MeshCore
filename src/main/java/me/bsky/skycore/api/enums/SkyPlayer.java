package me.bsky.skycore.api.enums;

import java.util.UUID;

public class SkyPlayer {

    private UUID uuid;

    SkyPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }
}
