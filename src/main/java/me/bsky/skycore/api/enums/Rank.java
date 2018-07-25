package me.bsky.skycore.api.enums;

import org.bukkit.ChatColor;

public enum Rank {

    TWITCH(ChatColor.translateAlternateColorCodes('&', "&d[Twitch]"), "Twitch", ChatColor.LIGHT_PURPLE, 6),
    YOUTUBE(ChatColor.translateAlternateColorCodes('&', "&c[You&fTube&c]"), "YouTube", ChatColor.RED, 5),


    RANK4(ChatColor.translateAlternateColorCodes('&', "&f[Void]"), "RANK4", ChatColor.WHITE, 4),
    RANK3(ChatColor.translateAlternateColorCodes('&', "&7[Star]"), "RANK3", ChatColor.YELLOW, 3),
    RANK2(ChatColor.translateAlternateColorCodes('&', "&b[Sky]"), "RANK2", ChatColor.AQUA, 2),
    RANK1(ChatColor.translateAlternateColorCodes('&', "&2[Earth]"), "RANK1", ChatColor.DARK_GREEN, 1),

    MEMBER("", "Member", ChatColor.GRAY, 0);

    public String prefix;
    public String name;
    public ChatColor color;
    public Integer rankNumber;

    Rank(String prefix, String name, ChatColor color, Integer rankNumber) {
        this.prefix = prefix;
        this.name = name;
        this.color = color;
        this.rankNumber = rankNumber;
    }

    public static Rank fromString(String text) {
        for (Rank r : Rank.values()) {
            if (r.name.equalsIgnoreCase(text)) {
                return r;
            }
        }
        return null;
    }
}
