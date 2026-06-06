package de.fuballer.mcendgame.main.mixin.additional_arrows;

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions;
import de.fuballer.mcendgame.main.component.custom_attribute.effects.AdditionalArrowsSettings;
import de.fuballer.mcendgame.main.component.tags.CustomTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeleton.class)
public abstract class AbstractSkeletonAdditionalArrowsMixin {
    @Shadow
    protected abstract AbstractArrow getArrow(ItemStack arrow, float damageModifier, @Nullable ItemStack shotFrom);

    @Inject(method = "performRangedAttack", at = @At("HEAD"), cancellable = true)
    private void shootAdditionalArrows(
            LivingEntity target,
            float pullProgress,
            CallbackInfo ci
    ) {
        AbstractSkeleton skeleton = (AbstractSkeleton) (Object) this;
        if (!(skeleton.level() instanceof ServerLevel serverWorld)) return;

        var additionalArrowCount = CustomAttributesExtensions.INSTANCE.getAdditionalArrowCount(skeleton);
        if (additionalArrowCount <= 0) return;

        var hand = skeleton.getMainHandItem().is(CustomTags.INSTANCE.getBOW()) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        var bowStack = skeleton.getItemInHand(hand);
        var projectileStack = skeleton.getProjectile(bowStack);

        var totalArrowCount = additionalArrowCount + 1;

        var distanceX = target.getX() - skeleton.getX();
        var distanceZ = target.getZ() - skeleton.getZ();
        var horizontalDistance = Math.sqrt(distanceX * distanceX + distanceZ * distanceZ);

        var spread = AdditionalArrowsSettings.SPREAD_PER_ARROW * additionalArrowCount;
        var spreadRotation = -spread;

        for (var count = 0; count < totalArrowCount; count++) {
            var arrow = getArrow(projectileStack, pullProgress, bowStack);

            var directionXZ = new Vec3(distanceX, 0, distanceZ).yRot((float) Math.toRadians(spreadRotation));
            var distanceY = target.getY(0.3333333333333333) - arrow.getY();

            arrow.shoot(
                    directionXZ.x,
                    distanceY + horizontalDistance * 0.2F,
                    directionXZ.z,
                    1.6F,
                    14 - serverWorld.getDifficulty().getId() * 4
            );

            serverWorld.addFreshEntity(arrow);

            spreadRotation += 2 * AdditionalArrowsSettings.SPREAD_PER_ARROW;
        }

        skeleton.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (skeleton.getRandom().nextFloat() * 0.4F + 0.8F));

        ci.cancel();
    }
}
