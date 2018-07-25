package me.bsky.skycore.api.enums;

import org.bukkit.ChatColor;

public enum StaffRank {

    BRIGHT(ChatColor.translateAlternateColorCodes('&', "&b[Bright]"), "Bright", ChatColor.AQUA, 15),

    OWNER(ChatColor.translateAlternateColorCodes('&', "&c[Owner]"), "Owner", ChatColor.RED, 14),
    CO_OWNER(ChatColor.translateAlternateColorCodes('&', "&c[Co-Owner]"), "Co-Owner", ChatColor.RED, 13),
    MANAGER(ChatColor.translateAlternateColorCodes('&', "&c[Manager]"), "Manager", ChatColor.RED, 12),
    DEV(ChatColor.translateAlternateColorCodes('&', "&c[Dev]"), "Developer", ChatColor.RED, 11),
    ADMIN(ChatColor.translateAlternateColorCodes('&', "&c[Admin]"), "Admin", ChatColor.RED, 10),

    BUILDER(ChatColor.translateAlternateColorCodes('&', "&9[Builder]"), "Builder", ChatColor.BLUE, 9),

    MOD(ChatColor.translateAlternateColorCodes('&', "&3[Moderator]"), "Moderator", ChatColor.DARK_AQUA, 8),
    HELPER(ChatColor.translateAlternateColorCodes('&', "&3[Helper]"), "Helper", ChatColor.DARK_AQUA, 7);

    public String prefix;
    public String name;
    public ChatColor color;
    public Integer rankNumber;

    StaffRank(String prefix, String name, ChatColor color, Integer rankNumber) {
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
