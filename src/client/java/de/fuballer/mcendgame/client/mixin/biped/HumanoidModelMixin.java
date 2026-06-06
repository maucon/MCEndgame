package de.fuballer.mcendgame.client.mixin.biped;

import de.fuballer.mcendgame.client.accessor.BipedEntityRenderStateAccessor;
import de.fuballer.mcendgame.main.component.item.custom.armor.interfaces.HideBipedBoneArmor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends HumanoidRenderState> {
    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("HEAD"))
    public void setAngles(T bipedEntityRenderState, CallbackInfo ci) {
        if (bipedEntityRenderState instanceof AvatarRenderState && ((AvatarRenderState) bipedEntityRenderState).isSpectator) return;

        var accessor = (BipedEntityRenderStateAccessor) bipedEntityRenderState;
        var model = (HumanoidModel<?>) (Object) this;

        var hiddenBones = accessor.mcendgame$getHiddenBones();

        model.head.visible = !hiddenBones.contains(HideBipedBoneArmor.BipedBone.HEAD);
        model.body.visible = !hiddenBones.contains(HideBipedBoneArmor.BipedBone.BODY);
        var armsVisible = !hiddenBones.contains(HideBipedBoneArmor.BipedBone.ARMS);
        model.leftArm.visible = armsVisible;
        model.rightArm.visible = armsVisible;
        var legsVisible = !hiddenBones.contains(HideBipedBoneArmor.BipedBone.LEGS);
        model.leftLeg.visible = legsVisible;
        model.rightLeg.visible = legsVisible;
    }
}
