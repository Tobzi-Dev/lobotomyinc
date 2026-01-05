package com.tobzi.lobotomyinc.mixin;

import com.tobzi.lobotomyinc.config.ModConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.NameTagItem;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NameTagItem.class)
public class NameTagItemMixin {

    @Inject(
            method = "useOnEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;decrement(I)V"
            ),
            cancellable = true
    )
    private void preventConsumption(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (ModConfig.FREE_NAMETAG) {

            if (entity instanceof VillagerEntity) {

                Text name = stack.getName();
                if (name != null && ModConfig.isLobotomizedName(name.getString())) {

                    cir.setReturnValue(ActionResult.SUCCESS);
                }
            }
        }
    }
}