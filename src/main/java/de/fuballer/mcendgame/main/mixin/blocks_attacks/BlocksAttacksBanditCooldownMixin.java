package de.fuballer.mcendgame.main.mixin.blocks_attacks;

import com.llamalad7.mixinextras.sugar.Local;
import de.fuballer.mcendgame.main.component.entity.custom.entities.bandit.BanditEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlocksAttacks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlocksAttacks.class)
public class BlocksAttacksBanditCooldownMixin {
    @Inject(
            method = "disable",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;stopUsingItem()V"
            )
    )
    void a(
            ServerLevel level,
            LivingEntity user,
            float baseSeconds,
            ItemStack blockingWith,
            CallbackInfo ci,
            @Local(name = "cooldownTicks") int cooldownTicks
    ) {
        if (user instanceof BanditEntity banditEntity) {
            banditEntity.getCooldowns().addCooldown(blockingWith, cooldownTicks);
        }
    }
}
