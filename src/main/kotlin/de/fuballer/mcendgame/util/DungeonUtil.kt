package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.data.DataTypeKeys
import org.bukkit.GameMode
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile

object DungeonUtil {
    fun isEnemyOrProjectile(entity: Entity): Boolean {
        if (PersistentDataUtil.getValue(entity, DataTypeKeys.IS_ENEMY) == true) return true
        val proj = entity as? Projectile ?: return false

        val shooter = proj.shooter ?: return false
        val shooterEntity = shooter as? Entity ?: return false

        return PersistentDataUtil.getValue(shooterEntity, DataTypeKeys.IS_ENEMY) == true
    }

    fun getNearbyPlayers(entity: LivingEntity, range: Double) = getNearbyPlayers(entity, range, range, range)

    private fun getNearbyPlayers(entity: LivingEntity, x: Double, y: Double, z: Double): List<Player> {
        val entities = entity.world.getNearbyEntities(entity.location, x, y, z)

        val players = entities.filterIsInstance<Player>()
        return players.filter { it.gameMode == GameMode.ADVENTURE || it.gameMode == GameMode.SURVIVAL }
    }
}