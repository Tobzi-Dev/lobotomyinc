package com.tobzi.lobotomyinc.mixin;

import com.tobzi.lobotomyinc.config.ModConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Brain.class)
public class BrainMixin {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void onTick(ServerWorld world, LivingEntity entity, CallbackInfo ci) {
        if (entity instanceof VillagerEntity villager) {
            Text customName = villager.getCustomName();

            if (customName != null && ModConfig.LOBOTOMY_NAMES.contains(customName.getString().toLowerCase())) {

                if (!villager.shouldRestock(world)) {
                    ci.cancel();
                }
            }
        }
    }
}