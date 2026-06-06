package de.fuballer.mcendgame.client.component.item.custom.armor

import net.minecraft.client.model.Model
import net.minecraft.client.renderer.entity.state.HumanoidRenderState
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack

data class TexturedArmorModel<T : Model<*>>(
    val modelProvider: () -> T,
    val texture: Identifier? = null,
    val colorAbleTexture: Identifier? = null,
    val defaultColor: Int = -1,
    val translucentTexture: Identifier? = null,
    val emissiveTexture: Identifier? = null,
    val specialTextures: List<SpecialRenderTypeArmorTexture> = listOf(),
) {
    data class SpecialRenderTypeArmorTexture(
        val texture: Identifier,
        val renderType: (Identifier) -> RenderType,
        val colorData: (HumanoidRenderState, ItemStack) -> Int, // used to smuggle data into shader for now
    )
}