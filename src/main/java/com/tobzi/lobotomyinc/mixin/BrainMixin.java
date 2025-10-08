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

    /**
     * Injects into the Brain's tick method.
     * This code will decide on every tick whether the brain should be allowed to run or not.
     */
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void onTick(ServerWorld world, LivingEntity entity, CallbackInfo ci) {
        // We only apply this logic to Villager entities.
        if (entity instanceof VillagerEntity villager) {
            Text customName = villager.getCustomName();

            // Check if this villager is in our lobotomy list.
            if (customName != null && ModConfig.LOBOTOMY_NAMES.contains(customName.getString().toLowerCase())) {

                // If the villager does NOT need to restock its trades, we cancel
                // the entire brain tick. This stops all expensive AI tasks (like WorkstationCompetition)
                // before they can even start, completely eliminating the lag.
                if (!villager.shouldRestock()) {
                    ci.cancel();
                }
                // If villager.shouldRestock() is true, this code does nothing,
                // which allows the brain to "wake up" and tick normally to handle the restocking process.
            }
        }
    }
}