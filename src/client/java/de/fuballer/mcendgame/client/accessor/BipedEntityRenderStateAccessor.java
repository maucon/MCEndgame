package de.fuballer.mcendgame.client.accessor;

import de.fuballer.mcendgame.main.component.item.custom.armor.interfaces.HideBipedBoneArmor;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.math.Vec3d;

import java.util.Set;

public interface BipedEntityRenderStateAccessor {
    void mcendgame$setHiddenBones(Set<HideBipedBoneArmor.BipedBone> bones);

    Set<HideBipedBoneArmor.BipedBone> mcendgame$getHiddenBones();

    void mcendgame$setHiddenArmor(Set<EquipmentSlot> armorSlots);

    Set<EquipmentSlot> mcendgame$getHiddenArmor();

    void mcendgame$setVelocity(Vec3d velocity);

    Vec3d mcendgame$getVelocity();

    void mcendgame$setCapeVerticalLift(float verticalLift);

    float mcendgame$getCapeVerticalLift();

    void mcendgame$setCapeForwardDrag(float forwardDrag);

    float mcendgame$getCapeForwardDrag();

    void mcendgame$setCapeSidewaysSway(float sidewaysSway);

    float mcendgame$getCapeSidewaysSway();
}
