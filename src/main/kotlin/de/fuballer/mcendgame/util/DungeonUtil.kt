package de.fuballer.mcendgame.util

import org.bukkit.GameMode
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

object DungeonUtil {
    fun isPlayerOrCompanion(entity: Entity) = entity.type == EntityType.PLAYER || entity.type == EntityType.WOLF

    fun getNearbyPlayers(entity: LivingEntity, range: Double) = getNearbyPlayers(entity, range, range, range)

    private fun getNearbyPlayers(entity: LivingEntity, x: Double, y: Double, z: Double): List<Player> {
        val entities = entity.world.getNearbyEntities(entity.location, x, y, z)

        val players = entities.filterIsInstance<Player>()
        return players.filter { it.gameMode == GameMode.ADVENTURE || it.gameMode == GameMode.SURVIVAL }
    }
}