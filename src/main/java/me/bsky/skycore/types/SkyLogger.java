package me.bsky.skycore.types;

import me.bsky.skycore.types.enums.LogLevel;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SkyLogger {

    private PrintStream printStream;

    public SkyLogger(PrintStream printStream) {
        this.printStream = printStream;
    }

    public void log(LogLevel logLevel, String message) {
        String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").format(new Date());
        printStream.println("[" + date + " " + logLevel.getName().toUpperCase() + "] " + message);
    }

    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    public void warn(String message) {
        log(LogLevel.WARN, message);
    }

    public void error(String message) {
        log(LogLevel.ERROR, message);
    }

    public void severe(String message) {
        log(LogLevel.SEVERE, message);
    }
}
