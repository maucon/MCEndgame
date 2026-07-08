package de.fuballer.mcendgame.main.mixin.additional_arrows;

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions;
import de.fuballer.mcendgame.main.component.custom_attribute.effects.AdditionalArrowsSettings;
import de.fuballer.mcendgame.main.component.tags.CustomTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeletonEntity.class)
public abstract class AbstractSkeletonAdditionalArrowsMixin {
    @Shadow
    protected abstract PersistentProjectileEntity createArrowProjectile(ItemStack arrow, float damageModifier, @Nullable ItemStack shotFrom);

    @Inject(method = "shootAt", at = @At("HEAD"), cancellable = true)
    private void shootAdditionalArrows(
            LivingEntity target,
            float pullProgress,
            CallbackInfo ci
    ) {
        AbstractSkeletonEntity skeleton = (AbstractSkeletonEntity) (Object) this;
        if (!(skeleton.getEntityWorld() instanceof ServerWorld serverWorld)) return;

        var additionalArrowCount = CustomAttributesExtensions.INSTANCE.getAdditionalArrowCount(skeleton);
        if (additionalArrowCount <= 0) return;

        var hand = skeleton.getMainHandStack().isIn(CustomTags.INSTANCE.getBOW()) ? Hand.MAIN_HAND : Hand.OFF_HAND;
        var bowStack = skeleton.getStackInHand(hand);
        var projectileStack = skeleton.getProjectileType(bowStack);

        var totalArrowCount = additionalArrowCount + 1;

        var distanceX = target.getX() - skeleton.getX();
        var distanceZ = target.getZ() - skeleton.getZ();
        var horizontalDistance = Math.sqrt(distanceX * distanceX + distanceZ * distanceZ);

        var spread = AdditionalArrowsSettings.SPREAD_PER_ARROW * additionalArrowCount;
        var spreadRotation = -spread;

        for (var count = 0; count < totalArrowCount; count++) {
            var arrow = createArrowProjectile(projectileStack, pullProgress, bowStack);

            var directionXZ = new Vec3d(distanceX, 0, distanceZ).rotateY((float) Math.toRadians(spreadRotation));
            var distanceY = target.getBodyY(0.3333333333333333) - arrow.getY();

            arrow.setVelocity(
                    directionXZ.x,
                    distanceY + horizontalDistance * 0.2F,
                    directionXZ.z,
                    1.6F,
                    14 - serverWorld.getDifficulty().getId() * 4
            );

            serverWorld.spawnEntity(arrow);

            spreadRotation += 2 * AdditionalArrowsSettings.SPREAD_PER_ARROW;
        }

        skeleton.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (skeleton.getRandom().nextFloat() * 0.4F + 0.8F));

        ci.cancel();
    }
}
