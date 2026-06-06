package de.fuballer.mcendgame.main.component.portal

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component

object PortalSettings {
    val TELEPORTATION_FAILED_MESSAGE: Component =
        Component.translatable("error.mcendgame.teleport.failed").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.BOLD)

    const val DEFAULT_HITBOX_HEIGHT = 2.0f
    const val DEFAULT_HITBOX_WIDTH = 0.75f
}