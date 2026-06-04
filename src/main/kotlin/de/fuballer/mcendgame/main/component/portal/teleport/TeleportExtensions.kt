package de.fuballer.mcendgame.main.component.portal.teleport

import de.fuballer.mcendgame.main.configuration.RuntimeConfig
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.portal.TeleportTransition
import net.minecraft.world.phys.Vec3

object TeleportExtensions {
    fun Player.teleportTo(teleportLocation: TeleportLocation): Boolean {
        val worldKey = teleportLocation.world.dimension()
        val world = RuntimeConfig.SERVER.getLevel(worldKey) ?: return false

        val result = teleport(
            TeleportTransition(
                world,
                teleportLocation.coordinates,
                Vec3.ZERO,
                teleportLocation.yRot,
                teleportLocation.xRot
            ) { }
        )

        return result != null
    }
}