package com.tobzi.lobotomyinc.mixin;

import com.tobzi.lobotomyinc.config.ModConfig;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (!ModConfig.LOBOTOMIZE_VILLAGERS_ENABLED) {
            return;
        }

        VillagerEntity self = (VillagerEntity) (Object) this;
        Text customName = self.getCustomName();

        if (customName != null && ModConfig.LOBOTOMY_NAMES.contains(customName.getString().toLowerCase())) {
            self.getNavigation().stop();
        }
    }
    @Inject(method = "sendAiDebugData", at = @At("HEAD"), cancellable = true)
    private void onSendAiDebugData(CallbackInfo ci) {
        if (!ModConfig.LOBOTOMIZE_VILLAGERS_ENABLED) {
            return;
        }

        VillagerEntity self = (VillagerEntity) (Object) this;
        Text customName = self.getCustomName();

        if (customName != null && ModConfig.LOBOTOMY_NAMES.contains(customName.getString().toLowerCase())) {
            ci.cancel();
        }
    }
}
