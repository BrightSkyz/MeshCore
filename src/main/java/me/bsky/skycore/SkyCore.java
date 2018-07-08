package me.bsky.skycore;

import me.bsky.skycore.application.SkyApplication;

import java.io.File;

public class SkyCore {

    public static SkyApplication skyApplication;

    public static void main(String[] args) {
        // Before we start, make sure the path won't give errors
        String path = new File(".").getAbsolutePath();
        if (path.contains("!") || path.contains("+")) {
            System.out.println("Error: You cannot use this application in a path that has a ! or a + character in the name.");
            return;
        }

        // Start SkyCore
        System.out.println("Loading SkyCore, please wait...");
        skyApplication = new SkyApplication();
        skyApplication.startApplication();
    }
}
