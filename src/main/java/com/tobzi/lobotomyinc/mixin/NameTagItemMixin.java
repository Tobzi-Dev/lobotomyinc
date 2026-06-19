package com.tobzi.lobotomyinc.mixin;

import com.tobzi.lobotomyinc.config.ModConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.NameTagItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NameTagItem.class)
public class NameTagItemMixin {

    @Inject(
            method = "interactLivingEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"
            ),
            cancellable = true
    )
    private void preventConsumption(ItemStack itemStack, Player player, LivingEntity target, InteractionHand type, CallbackInfoReturnable<InteractionResult> cir) {
        if (ModConfig.FREE_NAMETAG) {

            if (target instanceof Villager) {

                Component name = itemStack.getHoverName();
                if (ModConfig.isLobotomizedName(name.getString())) {

                    cir.setReturnValue(InteractionResult.SUCCESS);
                }
            }
        }
    }
}