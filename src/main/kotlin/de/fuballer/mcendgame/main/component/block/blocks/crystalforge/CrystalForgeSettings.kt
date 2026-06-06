package de.fuballer.mcendgame.main.component.block.blocks.crystalforge

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.util.CommonColors

object CrystalForgeSettings {
    const val CONTAINER_BASE_KEY = "container.mcendgame.crystal_forge."
    const val FORGE_ERROR_KEY = "${CONTAINER_BASE_KEY}forge_error."

    fun getForgeErrorText(id: String): MutableComponent = Component.translatable("$FORGE_ERROR_KEY$id").withColor(CommonColors.RED)
}