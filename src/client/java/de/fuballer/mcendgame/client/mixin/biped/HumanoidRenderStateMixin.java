package de.fuballer.mcendgame.client.mixin.biped;

import de.fuballer.mcendgame.client.accessor.BipedEntityRenderStateAccessor;
import de.fuballer.mcendgame.main.component.item.custom.armor.interfaces.HideBipedBoneArmor;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashSet;
import java.util.Set;

@Mixin(HumanoidRenderState.class)
public class HumanoidRenderStateMixin implements BipedEntityRenderStateAccessor {
    @Unique
    private Set<HideBipedBoneArmor.BipedBone> hiddenBones = new HashSet<>();
    @Unique
    private Set<EquipmentSlot> hiddenArmor = new HashSet<>();

    @Unique
    private Vec3 velocity = Vec3.ZERO;

    @Unique
    private float capeVerticalLift = 0f;
    @Unique
    private float capeForwardDrag = 0f;
    @Unique
    private float capeSidewaysSway = 0f;

    @Override
    public void mcendgame$setHiddenBones(Set<HideBipedBoneArmor.BipedBone> bones) {
        hiddenBones = bones;
    }

    @Override
    public Set<HideBipedBoneArmor.BipedBone> mcendgame$getHiddenBones() {
        return hiddenBones;
    }

    @Override
    public void mcendgame$setHiddenArmor(Set<EquipmentSlot> armorSlots) {
        hiddenArmor = armorSlots;
    }

    @Override
    public Set<EquipmentSlot> mcendgame$getHiddenArmor() {
        return hiddenArmor;
    }

    @Override
    public void mcendgame$setVelocity(Vec3 velocity) {
        this.velocity = velocity;
    }

    @Override
    public Vec3 mcendgame$getVelocity() {
        return velocity;
    }

    @Override
    public void mcendgame$setCapeVerticalLift(float verticalLift) {
        capeVerticalLift = verticalLift;
    }

    @Override
    public float mcendgame$getCapeVerticalLift() {
        return capeVerticalLift;
    }

    @Override
    public void mcendgame$setCapeForwardDrag(float forwardDrag) {
        capeForwardDrag = forwardDrag;
    }

    @Override
    public float mcendgame$getCapeForwardDrag() {
        return capeForwardDrag;
    }

    @Override
    public void mcendgame$setCapeSidewaysSway(float sidewaysSway) {
        capeSidewaysSway = sidewaysSway;
    }

    @Override
    public float mcendgame$getCapeSidewaysSway() {
        return capeSidewaysSway;
    }
}
