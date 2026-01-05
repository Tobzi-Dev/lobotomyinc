package com.tobzi.lobotomyinc.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.tobzi.lobotomyinc.LobotomyInc;
import com.tobzi.lobotomyinc.config.ModConfig;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class LobotomyCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(LobotomyInc.MOD_ID)
                .requires(source -> source.hasPermissionLevel(2))

                .then(literal("config")
                        .then(literal("reload")
                                .executes(context -> {
                                    ModConfig.load();
                                    context.getSource().sendFeedback(() -> Text.literal("LobotomyInc configuration reloaded."), true);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )

                .then(literal("free-nametag")
                        .then(argument("enabled", BoolArgumentType.bool())
                                .executes(context -> {
                                    boolean newValue = BoolArgumentType.getBool(context, "enabled");

                                    ModConfig.FREE_NAMETAG = newValue;
                                    ModConfig.save();

                                    context.getSource().sendFeedback(() -> Text.literal("Set 'free_nametag' to: " + newValue), true);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .then(literal("match-contains")
                        .then(argument("enabled", BoolArgumentType.bool())
                                .executes(context -> {
                                    boolean newValue = BoolArgumentType.getBool(context, "enabled");
                                    ModConfig.MATCH_CONTAINS = newValue;
                                    ModConfig.save();
                                    context.getSource().sendFeedback(() -> Text.literal("Set 'match_contains' to: " + newValue), true);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )

                .then(literal("match-prefix")
                        .then(argument("enabled", BoolArgumentType.bool())
                                .executes(context -> {
                                    boolean newValue = BoolArgumentType.getBool(context, "enabled");
                                    ModConfig.MATCH_PREFIX = newValue;
                                    ModConfig.save();
                                    context.getSource().sendFeedback(() -> Text.literal("Set 'match_prefix' to: " + newValue), true);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )

                .then(literal("match-suffix")
                        .then(argument("enabled", BoolArgumentType.bool())
                                .executes(context -> {
                                    boolean newValue = BoolArgumentType.getBool(context, "enabled");
                                    ModConfig.MATCH_SUFFIX = newValue;
                                    ModConfig.save();
                                    context.getSource().sendFeedback(() -> Text.literal("Set 'match_suffix' to: " + newValue), true);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )

                .then(literal("match-surround")
                        .then(argument("enabled", BoolArgumentType.bool())
                                .executes(context -> {
                                    boolean newValue = BoolArgumentType.getBool(context, "enabled");
                                    ModConfig.MATCH_SURROUND = newValue;
                                    ModConfig.save();
                                    context.getSource().sendFeedback(() -> Text.literal("Set 'match_surround' to: " + newValue), true);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
        );
    }
}