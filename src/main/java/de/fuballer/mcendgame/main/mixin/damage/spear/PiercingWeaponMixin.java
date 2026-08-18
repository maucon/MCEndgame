package de.fuballer.mcendgame.main.mixin.damage.spear;

import de.fuballer.mcendgame.main.context.PierceContext;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.component.PiercingWeapon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PiercingWeapon.class)
public class PiercingWeaponMixin {
    @Inject(
            method = "attack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;stabAttack(Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/entity/Entity;FZZZ)Z")
    )
    public void setWeaponType(LivingEntity attacker, EquipmentSlot hand, CallbackInfo ci) {
        PierceContext.CURRENT.set(PierceContext.PierceType.PIERCE);
    }
}