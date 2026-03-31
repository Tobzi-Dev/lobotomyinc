package com.tobzi.lobotomyinc.mixin;

import com.tobzi.lobotomyinc.config.ModConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.npc.villager.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
public abstract class VillagerEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (!ModConfig.LOBOTOMIZE_VILLAGERS_ENABLED) {
            return;
        }

        Villager self = (Villager) (Object) this;
        Component customName = self.getCustomName();

        if (customName != null && ModConfig.isLobotomizedName(customName.getString())) {
            self.getNavigation().stop();
        }
    }
}
