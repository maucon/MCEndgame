package de.fuballer.mcendgame.main.mixin.additional_arrows;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions;
import de.fuballer.mcendgame.main.component.custom_attribute.effects.AdditionalArrowsSettings;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ProjectileWeaponItem.class)
public class ProjectileWeaponItemAdditionalArrowsMixin {
    @ModifyVariable(
            method = "draw",
            at = @At(value = "STORE"),
            ordinal = 0
    )
    private static int modifyProjectileCount(
            int original,
            ItemStack stack,
            ItemStack projectileStack,
            LivingEntity shooter
    ) {
        var additional = CustomAttributesExtensions.INSTANCE.getAdditionalArrowCount(shooter);
        return original + additional;
    }

    @ModifyExpressionValue(
            method = "shoot",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;processProjectileSpread(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/Entity;F)F"
            )
    )
    private float modifySpread(
            float original,
            ServerLevel world,
            LivingEntity shooter
    ) {
        var additional = CustomAttributesExtensions.INSTANCE.getAdditionalArrowCount(shooter);
        return original + AdditionalArrowsSettings.SPREAD_PER_ARROW * additional;
    }
}
