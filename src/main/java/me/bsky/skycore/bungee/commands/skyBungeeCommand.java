package me.bsky.skycore.bungee.commands;

import me.bsky.skycore.bungee.SkyBungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class skyBungeeCommand extends Command {

    private SkyBungee skyBungee;

    public skyBungeeCommand(SkyBungee skyBungee) {
        super("skybungee", null, "skyb");
        this.skyBungee = skyBungee;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer)sender;
            player.sendMessage(new ComponentBuilder("").append(getSkyBungee().getMessagePrefix()).append("SkyBungee is working!").color(ChatColor.GRAY).create());
        }
    }

    public SkyBungee getSkyBungee() {
        return skyBungee;
    }
}