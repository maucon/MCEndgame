package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.domain.technical.persistent_data.TypeKeys
import org.bukkit.GameMode
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile

object DungeonUtil {
    fun isEnemyOrEnemyProjectile(entity: Entity): Boolean {
        if (PersistentDataUtil.getBooleanValue(entity, TypeKeys.IS_ENEMY)) return true
        val proj = entity as? Projectile ?: return false

        val shooter = proj.shooter ?: return false
        val shooterEntity = shooter as? Entity ?: return false

        return PersistentDataUtil.getBooleanValue(shooterEntity, TypeKeys.IS_ENEMY)
    }

    fun isPlayerOrPlayerProjectile(entity: Entity): Boolean {
        if (entity is Player) return true
        val proj = entity as? Projectile ?: return false

        val shooter = proj.shooter ?: return false
        val shooterEntity = shooter as? Entity ?: return false

        return shooterEntity is Player
    }

    fun getNearbyPlayers(
        entity: LivingEntity,
        range: Double
    ) = getNearbyPlayers(entity, range, range, range)

    private fun getNearbyPlayers(entity: LivingEntity, x: Double, y: Double, z: Double): List<Player> {
        val entities = entity.world.getNearbyEntities(entity.location, x, y, z)

        val players = entities.filterIsInstance<Player>()
        return players.filter { it.gameMode == GameMode.ADVENTURE || it.gameMode == GameMode.SURVIVAL }
    }
}