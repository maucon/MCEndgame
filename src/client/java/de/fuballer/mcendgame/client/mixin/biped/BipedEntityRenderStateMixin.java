package de.fuballer.mcendgame.client.mixin.biped;

import de.fuballer.mcendgame.client.accessor.BipedEntityRenderStateAccessor;
import de.fuballer.mcendgame.main.component.item.custom.armor.interfaces.HideBipedBoneArmor;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashSet;
import java.util.Set;

@Mixin(BipedEntityRenderState.class)
public class BipedEntityRenderStateMixin implements BipedEntityRenderStateAccessor {
    @Unique
    private Set<HideBipedBoneArmor.BipedBone> hiddenBones = new HashSet<>();
    @Unique
    private Set<EquipmentSlot> hiddenArmor = new HashSet<>();

    @Unique
    private Vec3d velocity = Vec3d.ZERO;

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
    public void mcendgame$setVelocity(Vec3d velocity) {
        this.velocity = velocity;
    }

    @Override
    public Vec3d mcendgame$getVelocity() {
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
