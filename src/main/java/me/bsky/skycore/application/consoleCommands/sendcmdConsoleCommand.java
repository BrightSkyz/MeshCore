package me.bsky.skycore.application.consoleCommands;

import me.bsky.skycore.application.SkyConsole;
import me.bsky.skycore.application.SkyConsoleCommand;

public class sendcmdConsoleCommand extends SkyConsoleCommand {

    public sendcmdConsoleCommand(SkyConsole skyConsole) {
        super("sendcmd", skyConsole);
    }

    @Override
    public void execute(String[] args) {
        if (args.length > 1) {
            if (getSkyConsole().getSkyApplication().getServerManagerModule().getServer(args[0]) != null) {
                // Send command to the server
                StringBuilder fullCommand = new StringBuilder();
                for (Integer i = 1; i < args.length; i++) {
                    fullCommand.append(" " + args[i]);
                }
                getSkyConsole().getSkyApplication().getServerManagerModule().getServer(args[0]).sendCommand(fullCommand.toString());
            } else {
                getSkyLogger().warn("The server doesn't exist.");
            }
        } else {
            getSkyLogger().warn("Incorrect usage: sendcmd <server name> <command>");
        }
    }
}
