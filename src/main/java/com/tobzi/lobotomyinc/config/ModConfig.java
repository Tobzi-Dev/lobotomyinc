package com.tobzi.lobotomyinc.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tobzi.lobotomyinc.LobotomyInc;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModConfig {

    private static final Path CONFIG_DIR = Paths.get("config");
    private static final Path CONFIG_FILE = CONFIG_DIR.resolve("lobotomyinc.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // These fields will hold the actual config values used by the mod.
    public static boolean LOBOTOMIZE_VILLAGERS_ENABLED = true;
    public static List<String> LOBOTOMY_NAMES = new ArrayList<>(Arrays.asList("борест", "testname"));

    /**
     * This is a simple data-holder class that matches the structure of our JSON file.
     */
    private static class ConfigData {
        boolean lobotomize_villagers_enabled;
        List<String> lobotomy_names;
    }

    public static void load() {
        if (!Files.exists(CONFIG_FILE)) {
            save();
            return;
        }

        try {
            // Read the entire file into a string.
            String json = Files.readString(CONFIG_FILE, StandardCharsets.UTF_8);

            // Use GSON to parse the JSON string into our ConfigData object.
            ConfigData configData = GSON.fromJson(json, ConfigData.class);

            // Update the mod's static fields from the loaded data.
            LOBOTOMIZE_VILLAGERS_ENABLED = configData.lobotomize_villagers_enabled;
            // Ensure the names are always lowercase for case-insensitive matching.
            LOBOTOMY_NAMES = configData.lobotomy_names.stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            LobotomyInc.LOGGER.error("Failed to load JSON config for LobotomyInc", e);
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_DIR);

            // Create a new data object with the default values.
            ConfigData configData = new ConfigData();
            configData.lobotomize_villagers_enabled = LOBOTOMIZE_VILLAGERS_ENABLED;
            configData.lobotomy_names = LOBOTOMY_NAMES;

            // Use GSON to convert our data object into a formatted JSON string.
            String json = GSON.toJson(configData);

            // Write the JSON string to the file, guaranteeing UTF-8 encoding.
            Files.writeString(CONFIG_FILE, json, StandardCharsets.UTF_8);

        } catch (IOException e) {
            LobotomyInc.LOGGER.error("Failed to save JSON config for LobotomyInc", e);
        }
    }
}