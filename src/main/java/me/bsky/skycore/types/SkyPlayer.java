package me.bsky.skycore.types;

import me.bsky.skycore.types.enums.Rank;

import java.util.UUID;

public abstract class SkyPlayer {

    private UUID uuid;
    private Rank rank;
    private String ipAddress;
    
    protected SkyPlayer(UUID uuid, Rank rank, String ipAddress) {
        this.uuid = uuid;
        this.rank = rank;
        this.ipAddress = ipAddress;
    }

    public boolean hasRank(Rank rank) {
        return rank.rankNumber <= this.rank.rankNumber;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Rank getRank() {
        return rank;
    }

    public String getIpAddress() {
        return ipAddress;
    }
}
