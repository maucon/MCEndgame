package de.fuballer.mcendgame.client.component.item.custom.armor.transformer

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.world.entity.EquipmentSlot

private const val SCALE = 1.25f

class PiglinArmorTransformer : EntityArmorTransformer() {
    override fun transform(slot: EquipmentSlot, matrixStack: PoseStack) {
        if (slot != EquipmentSlot.HEAD) return
        matrixStack.scale(SCALE, SCALE, SCALE)
        matrixStack.translate(0f, 0.0625f, 0f)
    }
}