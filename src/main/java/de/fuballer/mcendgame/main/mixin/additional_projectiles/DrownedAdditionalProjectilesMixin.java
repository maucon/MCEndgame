package de.fuballer.mcendgame.main.mixin.additional_projectiles;

import com.llamalad7.mixinextras.sugar.Local;
import de.fuballer.mcendgame.main.component.custom_attribute.effects.projectile.AdditionalProjectilesUtil;
import kotlin.Unit;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.zombie.Drowned;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Drowned.class)
public class DrownedAdditionalProjectilesMixin {
    @Inject(
            method = "performRangedAttack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/projectile/Projectile;spawnProjectileUsingShoot(Lnet/minecraft/world/entity/projectile/Projectile;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;DDDFF)Lnet/minecraft/world/entity/projectile/Projectile;"
            ),
            cancellable = true
    )
    private void shootAdditionalArrows(
            LivingEntity target,
            float power,
            CallbackInfo ci,
            @Local(name = "tridentItemStack") ItemStack tridentItemStack,
            @Local(name = "xd") double xd,
            @Local(name = "yd") double yd,
            @Local(name = "zd") double zd,
            @Local(name = "distanceToTarget") double distanceToTarget,
            @Local(name = "serverLevel") ServerLevel serverLevel
    ) {
        var drowned = (Drowned) (Object) this;

        AdditionalProjectilesUtil.INSTANCE.shootProjectile(
                drowned,
                null,
                new Vec3(xd, yd + distanceToTarget * 0.2F, zd),
                (_) -> new ThrownTrident(serverLevel, drowned, tridentItemStack.copy()),
                (projectile, spreadVelocity, _) -> {
                    Projectile.spawnProjectileUsingShoot(
                            projectile,
                            serverLevel,
                            tridentItemStack,
                            spreadVelocity.x,
                            spreadVelocity.y,
                            spreadVelocity.z,
                            1.6F,
                            14 - serverLevel.getDifficulty().getId() * 4
                    );
                    return Unit.INSTANCE;
                }
        );

        drowned.playSound(SoundEvents.DROWNED_SHOOT, 1.0F, 1.0F / (drowned.getRandom().nextFloat() * 0.4F + 0.8F));

        ci.cancel();
    }
}