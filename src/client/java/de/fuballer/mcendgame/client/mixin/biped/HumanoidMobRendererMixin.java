package de.fuballer.mcendgame.client.mixin.biped;

import de.fuballer.mcendgame.client.accessor.BipedEntityRenderStateAccessor;
import de.fuballer.mcendgame.client.accessor.LivingEntityCapeDataAccessor;
import de.fuballer.mcendgame.main.component.item.custom.armor.interfaces.HideBipedBoneArmor;
import de.fuballer.mcendgame.main.component.item.custom.armor.interfaces.HideOtherArmorArmor;
import de.fuballer.mcendgame.main.util.extension.EntityExtension;
import net.minecraft.client.entity.ClientAvatarState;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(HumanoidMobRenderer.class)
public class HumanoidMobRendererMixin {
    @Inject(method = "extractHumanoidRenderState", at = @At("TAIL"))
    private static void updateBipedRenderState(
            LivingEntity entity,
            HumanoidRenderState state,
            float partialTicks,
            ItemModelResolver itemModelResolver,
            CallbackInfo ci
    ) {
        var accessor = (BipedEntityRenderStateAccessor) state;

        var armorItems = List.of(
                entity.getItemBySlot(EquipmentSlot.HEAD).getItem(),
                entity.getItemBySlot(EquipmentSlot.CHEST).getItem(),
                entity.getItemBySlot(EquipmentSlot.LEGS).getItem(),
                entity.getItemBySlot(EquipmentSlot.FEET).getItem()
        );

        var hiddenBones = armorItems.stream()
                .filter(item -> item instanceof HideBipedBoneArmor)
                .flatMap(item -> ((HideBipedBoneArmor) item).getHiddenBones().stream())
                .collect(Collectors.toSet());
        accessor.mcendgame$setHiddenBones(hiddenBones);

        var hiddenArmor = armorItems.stream()
                .filter(item -> item instanceof HideOtherArmorArmor)
                .flatMap(item -> ((HideOtherArmorArmor) item).getHiddenArmor().stream())
                .collect(Collectors.toSet());
        accessor.mcendgame$setHiddenArmor(hiddenArmor);

        accessor.mcendgame$setVelocity(entity.getDeltaMovement());

        if (EntityExtension.INSTANCE.needsCapeData(entity)) {
            updateCape(entity, state, partialTicks);
        }
    }

    @Unique
    private static void updateCape(LivingEntity entity, HumanoidRenderState state, float tickProgress) {
        var capeDataAccessor = (LivingEntityCapeDataAccessor) entity;
        ClientAvatarState capeState = capeDataAccessor.mcendgame$getCapeState();

        double d = capeState.getInterpolatedCloakX(tickProgress) - Mth.lerp(tickProgress, entity.xo, entity.getX());
        double e = capeState.getInterpolatedCloakY(tickProgress) - Mth.lerp(tickProgress, entity.yo, entity.getY());
        double f = capeState.getInterpolatedCloakZ(tickProgress) - Mth.lerp(tickProgress, entity.zo, entity.getZ());

        float g = Mth.rotLerp(tickProgress, entity.yBodyRotO, entity.yBodyRot);
        double h = Mth.sin((g * ((float) Math.PI / 180F)));
        double i = -Mth.cos((g * ((float) Math.PI / 180F)));

        var renderStateAccessor = (BipedEntityRenderStateAccessor) state;

        var verticalLift = (float) e * 10.0F;
        verticalLift = Mth.clamp(verticalLift, -6.0F, 32.0F);
        float j = capeState.getInterpolatedBob(tickProgress);
        float k = capeState.getInterpolatedWalkDistance(tickProgress);
        verticalLift += Mth.sin((k * 6.0F)) * 32.0F * j;
        renderStateAccessor.mcendgame$setCapeVerticalLift(verticalLift);

        var forwardDrag = (float) (d * h + f * i) * 100.0F;
        if (state instanceof AvatarRenderState playerState) forwardDrag *= 1.0F - playerState.fallFlyingScale();
        forwardDrag = Mth.clamp(forwardDrag, 0.0F, 150.0F);
        renderStateAccessor.mcendgame$setCapeForwardDrag(forwardDrag);

        var sidewaysSway = (float) (d * i - f * h) * 100.0F;
        sidewaysSway = Mth.clamp(sidewaysSway, -20.0F, 20.0F);
        renderStateAccessor.mcendgame$setCapeSidewaysSway(sidewaysSway);
    }
}
