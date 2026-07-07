package de.fuballer.mcendgame.main.util.extension

import net.minecraft.server.command.ServerCommandSource

private const val MODERATOR_PERMISSION_LEVEL = 2

object ServerCommandSourceExtension {
    fun ServerCommandSource.isModerator() = hasPermissionLevel(MODERATOR_PERMISSION_LEVEL)
}