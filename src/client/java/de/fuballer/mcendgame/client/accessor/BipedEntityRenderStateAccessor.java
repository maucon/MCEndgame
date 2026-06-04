package de.fuballer.mcendgame.client.accessor;

import de.fuballer.mcendgame.main.component.item.custom.armor.interfaces.HideBipedBoneArmor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public interface BipedEntityRenderStateAccessor {
    void mcendgame$setHiddenBones(Set<HideBipedBoneArmor.BipedBone> bones);

    Set<HideBipedBoneArmor.BipedBone> mcendgame$getHiddenBones();

    void mcendgame$setHiddenArmor(Set<EquipmentSlot> armorSlots);

    Set<EquipmentSlot> mcendgame$getHiddenArmor();

    void mcendgame$setVelocity(Vec3 velocity);

    Vec3 mcendgame$getVelocity();

    void mcendgame$setCapeVerticalLift(float verticalLift);

    float mcendgame$getCapeVerticalLift();

    void mcendgame$setCapeForwardDrag(float forwardDrag);

    float mcendgame$getCapeForwardDrag();

    void mcendgame$setCapeSidewaysSway(float sidewaysSway);

    float mcendgame$getCapeSidewaysSway();
}
