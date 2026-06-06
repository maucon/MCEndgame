package de.fuballer.mcendgame.client.component.item.custom.armor.transformer

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.world.entity.EquipmentSlot

abstract class EntityArmorTransformer {
    abstract fun transform(slot: EquipmentSlot, matrixStack: PoseStack)
}