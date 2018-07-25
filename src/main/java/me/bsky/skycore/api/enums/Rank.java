package me.bsky.skycore.api.enums;

import org.bukkit.ChatColor;

public enum Rank {

    BRIGHT(ChatColor.translateAlternateColorCodes('&', "&b[Bright]"), "Bright", ChatColor.AQUA, 13),

    OWNER(ChatColor.translateAlternateColorCodes('&', "&c[Owner]"), "Owner", ChatColor.RED, 13),
    CO_OWNER(ChatColor.translateAlternateColorCodes('&', "&c[Co-Owner]"), "Co-Owner", ChatColor.RED, 12),
    DEV(ChatColor.translateAlternateColorCodes('&', "&c[Dev]"), "Developer", ChatColor.RED, 11),
    ADMIN(ChatColor.translateAlternateColorCodes('&', "&c[Admin]"), "Admin", ChatColor.RED, 10),

    BUILDER(ChatColor.translateAlternateColorCodes('&', "&9[Builder]"), "Builder", ChatColor.BLUE, 9),

    MOD(ChatColor.translateAlternateColorCodes('&', "&3[Moderator]"), "Moderator", ChatColor.DARK_AQUA, 8),
    HELPER(ChatColor.translateAlternateColorCodes('&', "&3[Helper]"), "Helper", ChatColor.DARK_AQUA, 7),

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
