package me.bsky.skycore.types;

import me.bsky.skycore.bungeecord.SkyBungee;
import me.bsky.skycore.spigot.SkySpigot;
import me.bsky.skycore.types.enums.ProgramMode;
import me.bsky.skycore.types.enums.Rank;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public abstract class SkyCommand extends Command {

    private ProgramMode programMode;
    private Object main;
    private String command;
    private Rank requiredRank;
    private String aliases[];

    public SkyCommand(ProgramMode programMode, Object main, String command, Rank requiredRank, String ...aliases) {
        super(command, null, aliases);
        this.programMode = programMode;
        this.main = main;
        this.command = command;
        this.requiredRank = requiredRank;
        this.aliases = aliases;
        if (programMode == ProgramMode.BUNGEECORD) {
            getSkyBungee().getProxy().getPluginManager().registerCommand(getSkyBungee(), this);
        }
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (getSkyBungee().getSkyPlayerMap().get(player.getUniqueId()).hasRank(requiredRank)) {
                executeCommand(player, args);
            }
        } else {
            sender.sendMessage(new TextComponent("You need to be a player."));
        }
    }

    public abstract void executeCommand(CommandSender sender, String args[]);

    public SkyBungee getSkyBungee() {
        if (programMode == ProgramMode.BUNGEECORD) {
            return (SkyBungee) main;
        }
        return null;
    }

    public SkySpigot getSkySpigot() {
        if (programMode == ProgramMode.SPIGOT) {
            return (SkySpigot) main;
        }
        return null;
    }

    public ProgramMode getProgramMode() {
        return programMode;
    }

    public String getCommand() {
        return command;
    }

    public String[] getAliases() {
        return aliases;
    }
}
