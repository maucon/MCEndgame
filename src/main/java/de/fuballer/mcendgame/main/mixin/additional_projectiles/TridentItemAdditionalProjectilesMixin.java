package de.fuballer.mcendgame.main.mixin.additional_projectiles;

import com.llamalad7.mixinextras.sugar.Local;
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions;
import de.fuballer.mcendgame.main.component.custom_attribute.effects.projectile.AdditionalProjectilesUtil;
import kotlin.Unit;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TridentItem.class)
public class TridentItemAdditionalProjectilesMixin {
    @Inject(
            method = "releaseUsing",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/projectile/Projectile;spawnProjectileFromRotation(Lnet/minecraft/world/entity/projectile/Projectile$ProjectileFactory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;FFF)Lnet/minecraft/world/entity/projectile/Projectile;"
            ),
            cancellable = true
    )
    void throwAdditionalTridents(
            ItemStack itemStack,
            Level level,
            LivingEntity entity,
            int remainingTime,
            CallbackInfoReturnable<Boolean> cir,
            @Local(name = "player") Player player,
            @Local(name = "sound") Holder<SoundEvent> sound,
            @Local(name = "serverLevel") ServerLevel serverLevel,
            @Local(name = "thrownItemStack") ItemStack thrownItemStack
    ) {
        var additionalTridentCount = CustomAttributesExtensions.INSTANCE.getAdditionalProjectileCount(entity);
        if (additionalTridentCount <= 0) return;

        var xRot = player.getXRot();
        var yRot = player.getYRot();
        float xd = -Mth.sin(yRot * (float) (Math.PI / 180.0)) * Mth.cos(xRot * (float) (Math.PI / 180.0));
        float yd = -Mth.sin(xRot * (float) (Math.PI / 180.0));
        float zd = Mth.cos(yRot * (float) (Math.PI / 180.0)) * Mth.cos(xRot * (float) (Math.PI / 180.0));

        var noLoyaltyStack = thrownItemStack.copy();
        EnchantmentHelper.updateEnchantments(
                noLoyaltyStack, enchantments -> enchantments.removeIf(enchantment -> enchantment.is(Enchantments.LOYALTY))
        );

        AdditionalProjectilesUtil.INSTANCE.shootProjectile(
                entity,
                null,
                new Vec3(xd, yd, zd),
                (index) -> new ThrownTrident(serverLevel, player, index.isMain() ? thrownItemStack : noLoyaltyStack),
                (projectile, spreadVelocity, index) -> {
                    projectile.shoot(spreadVelocity.x, spreadVelocity.y, spreadVelocity.z, 2.5F, 1.0F);

                    serverLevel.addFreshEntity(projectile);

                    var sourceMovement = player.getKnownMovement();
                    projectile.setDeltaMovement(projectile.getDeltaMovement().add(sourceMovement.x, player.onGround() ? 0.0 : sourceMovement.y, sourceMovement.z));

                    if (projectile instanceof ThrownTrident trident) {
                        if (player.hasInfiniteMaterials() || !index.isMain()) {
                            trident.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        }
                    }

                    return Unit.INSTANCE;
                }
        );

        level.playSound(null, player, sound.value(), SoundSource.PLAYERS, 1.0F, 1.0F);

        cir.setReturnValue(true);
    }
}
