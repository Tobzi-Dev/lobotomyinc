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
import java.util.List;
import java.util.stream.Collectors;

public class ModConfig {

    private static final Path CONFIG_DIR = Paths.get("config");
    private static final Path CONFIG_FILE = CONFIG_DIR.resolve("lobotomyinc.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();


    public static boolean LOBOTOMIZE_VILLAGERS_ENABLED = true;
    public static List<String> LOBOTOMY_NAMES = new ArrayList<>();

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
            String json = Files.readString(CONFIG_FILE, StandardCharsets.UTF_8);

            ConfigData configData = GSON.fromJson(json, ConfigData.class);

            LOBOTOMIZE_VILLAGERS_ENABLED = configData.lobotomize_villagers_enabled;
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

            ConfigData configData = new ConfigData();
            configData.lobotomize_villagers_enabled = LOBOTOMIZE_VILLAGERS_ENABLED;
            configData.lobotomy_names = LOBOTOMY_NAMES;

            String json = GSON.toJson(configData);

            Files.writeString(CONFIG_FILE, json, StandardCharsets.UTF_8);

        } catch (IOException e) {
            LobotomyInc.LOGGER.error("Failed to save JSON config for LobotomyInc", e);
        }
    }
}