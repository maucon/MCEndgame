package de.fuballer.mcendgame.main.util.minecraft

import de.fuballer.mcendgame.main.component.dungeon.generation.data.SpawnPosition
import de.fuballer.mcendgame.main.component.entity.EntityTypeStats
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.attributes.Attributes

object EntityUtil {
    fun spawnEntityWithStats(
        world: ServerLevel,
        type: EntityTypeStats,
        location: SpawnPosition,
    ): Mob {
        val entity = type.type.spawn(world, location.blockPos(), EntitySpawnReason.STRUCTURE)
            ?: throw Exception("Couldn't  spawn entity of type: ${type.type}, in world: $world")

        clearVehicleAndPassengers(entity)

        entity.snapTo(
            location.pos.x + 0.5,
            location.pos.y.toDouble(),
            location.pos.z + 0.5,
            location.rot.toFloat(),
            0F
        )
        setStats(entity, type)

        type.applyMisc(entity)

        return entity
    }

    private fun setStats(
        entity: Mob,
        type: EntityTypeStats,
    ) {
        val newMaxHealth = type.health
        entity.getAttribute(Attributes.MAX_HEALTH)?.baseValue = newMaxHealth
        entity.health = newMaxHealth.toFloat()

        entity.getAttribute(Attributes.ATTACK_DAMAGE)?.baseValue = type.attackDamage
        entity.getAttribute(Attributes.MOVEMENT_SPEED)?.baseValue = type.movementSpeed
        entity.getAttribute(Attributes.KNOCKBACK_RESISTANCE)?.baseValue = type.knockbackResistance
    }

    private fun clearVehicleAndPassengers(entity: Mob) {
        entity.vehicle?.let { vehicle ->
            entity.stopRiding()
            vehicle.discard()
        }
        entity.passengers.toList().forEach { passenger ->
            passenger.discard()
        }
    }
}