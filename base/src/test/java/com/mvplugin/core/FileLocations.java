package com.mvplugin.core;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FileLocations {

    public static final File SERVER_DIRECTORY = new File("bin/server");
    public static final File UPDATES_DIRECTORY = new File(SERVER_DIRECTORY, "updates");
    public static final File WORLDS_DIRECTORY = new File(SERVER_DIRECTORY, "worlds");
    public static final File PLUGIN_DIRECTORY = new File(SERVER_DIRECTORY, "plugins");
    public static final File MULTIVERSE_DIRECTORY = new File(PLUGIN_DIRECTORY, "Multiverse-Core");

    public static void setupDirectories() {
        MULTIVERSE_DIRECTORY.mkdirs();
        UPDATES_DIRECTORY.mkdirs();
        WORLDS_DIRECTORY.mkdirs();
    }

    public static void cleanupDirectories() throws IOException {
        FileUtils.deleteDirectory(SERVER_DIRECTORY);
    }
}
