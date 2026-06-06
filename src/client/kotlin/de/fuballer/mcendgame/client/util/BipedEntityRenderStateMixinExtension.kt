package de.fuballer.mcendgame.client.util

import de.fuballer.mcendgame.client.accessor.BipedEntityRenderStateAccessor
import de.fuballer.mcendgame.main.component.item.custom.armor.interfaces.HideBipedBoneArmor
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import net.minecraft.world.entity.EquipmentSlot

object BipedEntityRenderStateMixinExtension {
    fun LivingEntityRenderState.setHiddenBones(hiddenBones: Set<HideBipedBoneArmor.BipedBone>) {
        val accessor = this as BipedEntityRenderStateAccessor
        accessor.`mcendgame$setHiddenBones`(hiddenBones)
    }

    fun LivingEntityRenderState.getHiddenBones(): Set<HideBipedBoneArmor.BipedBone> {
        val accessor = this as BipedEntityRenderStateAccessor
        return accessor.`mcendgame$getHiddenBones`()
    }

    fun LivingEntityRenderState.setHiddenArmor(hiddenArmor: Set<EquipmentSlot>) {
        val accessor = this as BipedEntityRenderStateAccessor
        accessor.`mcendgame$setHiddenArmor`(hiddenArmor)
    }

    fun LivingEntityRenderState.getHiddenArmor(): Set<EquipmentSlot> {
        val accessor = this as BipedEntityRenderStateAccessor
        return accessor.`mcendgame$getHiddenArmor`()
    }
}