package de.fuballer.mcendgame.client.component.item.custom.armor.transformer

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.world.entity.EquipmentSlot

class ScaleArmorTransformer(
    val scale: Float,
    val scaleOrigin: Float = 24f,
) : EntityArmorTransformer() {
    override fun transform(slot: EquipmentSlot, matrixStack: PoseStack) {
        matrixStack.scale(scale, scale, scale)
        val totalOffset = scaleOrigin * (scale - 1) / (16 * scale)
        matrixStack.translate(0f, -totalOffset, 0f)
    }
}