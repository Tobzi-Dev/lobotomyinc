package com.tobzi.lobotomyinc.mixin;

import com.tobzi.lobotomyinc.config.ModConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.npc.villager.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Brain.class)
public class BrainMixin {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void onTick(ServerLevel world, LivingEntity entity, CallbackInfo ci) {
        if (entity instanceof Villager villager) {
            Component customName = villager.getCustomName();

            if (customName != null && ModConfig.isLobotomizedName(customName.getString())) {

                if (!villager.shouldRestock(world)) {
                    ci.cancel();
                }
            }
        }
    }
}