package com.tobzi.lobotomyinc.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tobzi.lobotomyinc.LobotomyInc;

import java.io.IOException;
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

    // --- Main Settings ---
    public static boolean LOBOTOMIZE_VILLAGERS_ENABLED = true;
    public static boolean FREE_NAMETAG = false;

    // --- Matching Rules ---
    public static List<String> EXACT_NAMES = new ArrayList<>();

    public static boolean MATCH_CONTAINS = false;
    public static List<String> CONTAINS_LIST = new ArrayList<>();

    public static boolean MATCH_PREFIX = false;
    public static List<String> PREFIX_LIST = new ArrayList<>();

    public static boolean MATCH_SUFFIX = false;
    public static List<String> SUFFIX_LIST = new ArrayList<>();

    public static boolean MATCH_SURROUND = false;
    public static List<String> SURROUND_LIST = new ArrayList<>();




    public static boolean isLobotomizedName(String name) {
        if (name == null) return false;

        String lowerName = name.toLowerCase();

        // 1. Exact Match
        if (EXACT_NAMES.contains(lowerName)) return true;

        // 2. Contains
        if (MATCH_CONTAINS) {
            for (String s : CONTAINS_LIST) {
                if (lowerName.contains(s)) return true;
            }
        }

        // 3. Prefix
        if (MATCH_PREFIX) {
            for (String s : PREFIX_LIST) {
                if (lowerName.startsWith(s)) return true;
            }
        }

        // 4. Suffix
        if (MATCH_SUFFIX) {
            for (String s : SUFFIX_LIST) {
                if (lowerName.endsWith(s)) return true;
            }
        }

        // 5. Surround
        if (MATCH_SURROUND) {
            for (String s : SURROUND_LIST) {
                if (lowerName.startsWith(s) && lowerName.endsWith(s)) return true;
            }
        }

        return false;
    }

    private static class ConfigData {
        boolean lobotomize_villagers_enabled = true;
        boolean free_nametag = false;
        List<String> exact_names = new ArrayList<>(Arrays.asList("Lobotomized"));

        AdvancedMatching advanced_matching = new AdvancedMatching();
    }

    private static class AdvancedMatching {
        boolean match_contains = false;
        List<String> contains_list = new ArrayList<>(Arrays.asList("@"));

        boolean match_prefix = false;
        List<String> prefix_list = new ArrayList<>(Arrays.asList("!"));

        boolean match_suffix = false;
        List<String> suffix_list = new ArrayList<>(Arrays.asList("#"));

        boolean match_surround = false;
        List<String> surround_list = new ArrayList<>(Arrays.asList("$"));
    }

    public static void load() {
        if (!Files.exists(CONFIG_FILE)) {
            loadFromData(new ConfigData());
            save();
            return;
        }

        try {
            String json = Files.readString(CONFIG_FILE, StandardCharsets.UTF_8);
            ConfigData configData = GSON.fromJson(json, ConfigData.class);


            if (configData.advanced_matching == null) {
                configData.advanced_matching = new AdvancedMatching();
            }

            loadFromData(configData);
            save();

        } catch (IOException e) {
            LobotomyInc.LOGGER.error("Failed to load JSON config for LobotomyInc", e);
        }
    }

    private static void loadFromData(ConfigData data) {
        LOBOTOMIZE_VILLAGERS_ENABLED = data.lobotomize_villagers_enabled;
        FREE_NAMETAG = data.free_nametag;

        EXACT_NAMES = sanitizeList(data.exact_names);

        MATCH_CONTAINS = data.advanced_matching.match_contains;
        CONTAINS_LIST = sanitizeList(data.advanced_matching.contains_list);

        MATCH_PREFIX = data.advanced_matching.match_prefix;
        PREFIX_LIST = sanitizeList(data.advanced_matching.prefix_list);

        MATCH_SUFFIX = data.advanced_matching.match_suffix;
        SUFFIX_LIST = sanitizeList(data.advanced_matching.suffix_list);

        MATCH_SURROUND = data.advanced_matching.match_surround;
        SURROUND_LIST = sanitizeList(data.advanced_matching.surround_list);
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_DIR);

            ConfigData data = new ConfigData();
            data.lobotomize_villagers_enabled = LOBOTOMIZE_VILLAGERS_ENABLED;
            data.free_nametag = FREE_NAMETAG;
            data.exact_names = EXACT_NAMES;

            data.advanced_matching.match_contains = MATCH_CONTAINS;
            data.advanced_matching.contains_list = CONTAINS_LIST;

            data.advanced_matching.match_prefix = MATCH_PREFIX;
            data.advanced_matching.prefix_list = PREFIX_LIST;

            data.advanced_matching.match_suffix = MATCH_SUFFIX;
            data.advanced_matching.suffix_list = SUFFIX_LIST;

            data.advanced_matching.match_surround = MATCH_SURROUND;
            data.advanced_matching.surround_list = SURROUND_LIST;

            String json = GSON.toJson(data);
            Files.writeString(CONFIG_FILE, json, StandardCharsets.UTF_8);

        } catch (IOException e) {
            LobotomyInc.LOGGER.error("Failed to save JSON config for LobotomyInc", e);
        }
    }

    private static List<String> sanitizeList(List<String> list) {
        if (list == null) return new ArrayList<>();
        return list.stream().map(String::toLowerCase).collect(Collectors.toList());
    }
}