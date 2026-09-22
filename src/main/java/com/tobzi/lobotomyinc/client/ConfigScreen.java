package com.tobzi.lobotomyinc.client;

import com.tobzi.lobotomyinc.config.ModConfig;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.ListOption;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ConfigScreen {

    private static final List<String> DEF_EXACT = List.of("lobotomized");
    private static final List<String> DEF_CONTAINS = List.of("@");
    private static final List<String> DEF_PREFIX = List.of("!");
    private static final List<String> DEF_SUFFIX = List.of("#");
    private static final List<String> DEF_SURROUND = List.of("$");

    private static final String CASE_NOTE = " Names are case-insensitive";

    public static Screen create(Screen parent) {
        ConfigCategory general = ConfigCategory.createBuilder()
                .name(Component.literal("General"))
                .option(toggle("Lobotomize villagers",
                        "Turn off/on the lobotomy",
                        true,
                        () -> ModConfig.LOBOTOMIZE_VILLAGERS_ENABLED,
                        v -> ModConfig.LOBOTOMIZE_VILLAGERS_ENABLED = v))
                .option(toggle("Free nametag",
                        "Lobotomising villager does not consume the nametag",
                        false,
                        () -> ModConfig.FREE_NAMETAG,
                        v -> ModConfig.FREE_NAMETAG = v))
                .group(stringList("Exact names",
                        "Name matches exactly one of the names in list" + CASE_NOTE,
                        DEF_EXACT,
                        () -> ModConfig.EXACT_NAMES,
                        v -> ModConfig.EXACT_NAMES = v))
                .build();


        ConfigCategory advanced = ConfigCategory.createBuilder()
                .name(Component.literal("Advanced matching"))
                .option(toggle("Match contains",
                        "Name contains any symbol(s) from the list",
                        false,
                        () -> ModConfig.MATCH_CONTAINS,
                        v -> ModConfig.MATCH_CONTAINS = v))
                .option(toggle("Match prefix",
                        "Name starts with symbol(s) from the list",
                        false,
                        () -> ModConfig.MATCH_PREFIX,
                        v -> ModConfig.MATCH_PREFIX = v))
                .option(toggle("Match suffix",
                        "Name ends with symbol(s) from the list",
                        false,
                        () -> ModConfig.MATCH_SUFFIX,
                        v -> ModConfig.MATCH_SUFFIX = v))
                .option(toggle("Match surround",
                        "Name starts and ends with the same symbol(s) from the list",
                        false,
                        () -> ModConfig.MATCH_SURROUND,
                        v -> ModConfig.MATCH_SURROUND = v))
                .group(stringList("Contains list",
                        "Used when 'Match contains' is on." + CASE_NOTE,
                        DEF_CONTAINS,
                        () -> ModConfig.CONTAINS_LIST,
                        v -> ModConfig.CONTAINS_LIST = v))
                .group(stringList("Prefix list",
                        "Used when 'Match prefix' is on." + CASE_NOTE,
                        DEF_PREFIX,
                        () -> ModConfig.PREFIX_LIST,
                        v -> ModConfig.PREFIX_LIST = v))
                .group(stringList("Suffix list",
                        "Used when 'Match suffix' is on." + CASE_NOTE,
                        DEF_SUFFIX,
                        () -> ModConfig.SUFFIX_LIST,
                        v -> ModConfig.SUFFIX_LIST = v))
                .group(stringList("Surround list",
                        "Used when 'Match surround' is on." + CASE_NOTE,
                        DEF_SURROUND,
                        () -> ModConfig.SURROUND_LIST,
                        v -> ModConfig.SURROUND_LIST = v))
                .build();

        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("LobotomyInc"))
                .category(general)
                .category(advanced)
                .save(ModConfig::save)
                .build()
                .generateScreen(parent);
    }



    private static Option<Boolean> toggle(String name, String desc, boolean def,
                                          Supplier<Boolean> getter, Consumer<Boolean> setter) {
        return Option.<Boolean>createBuilder()
                .name(Component.literal(name))
                .description(OptionDescription.of(Component.literal(desc)))
                .binding(def, getter, setter)
                .controller(TickBoxControllerBuilder::create)
                .build();
    }

    private static ListOption<String> stringList(String name, String desc, List<String> def,
                                                 Supplier<List<String>> getter, Consumer<List<String>> setter) {
        return ListOption.<String>createBuilder()
                .name(Component.literal(name))
                .description(OptionDescription.of(Component.literal(desc)))
                .binding(new ArrayList<>(def),
                        () -> new ArrayList<>(getter.get()),
                        v -> setter.accept(clean(v)))
                .controller(StringControllerBuilder::create)
                .initial("")
                .build();
    }

    private static List<String> clean(List<String> in) {
        List<String> out = new ArrayList<>();
        for (String s : in) {
            if (s == null || s.isEmpty()) continue;
            String lower = s.toLowerCase();
            if (!out.contains(lower)) out.add(lower);
        }
        return out;
    }
}