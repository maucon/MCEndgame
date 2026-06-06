package de.fuballer.mcendgame.main.util.extension

import net.minecraft.commands.CommandSourceStack
import net.minecraft.server.permissions.Permissions

object ServerCommandSourceExtension {
    fun CommandSourceStack.isModerator() = permissions().hasPermission(Permissions.COMMANDS_MODERATOR)
}