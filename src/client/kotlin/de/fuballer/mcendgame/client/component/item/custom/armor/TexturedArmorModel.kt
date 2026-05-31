package de.fuballer.mcendgame.client.component.item.custom.armor

import net.minecraft.client.model.Model
import net.minecraft.util.Identifier

data class TexturedArmorModel<T : Model<*>>(
    val modelProvider: () -> T,
    val texture: Identifier? = null,
    val colorAbleTexture: Identifier? = null,
    val defaultColor: Int = -1,
    val translucentTexture: Identifier? = null,
    val emissiveTexture: Identifier? = null,
)