package de.fuballer.mcendgame.main.util.minecraft

import de.fuballer.mcendgame.main.component.dungeon.generation.data.SpawnPosition
import de.fuballer.mcendgame.main.component.entity.EntityTypeStats
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.MobEntity
import net.minecraft.server.world.ServerWorld

object EntityUtil {
    fun spawnEntityWithStats(
        world: ServerWorld,
        type: EntityTypeStats,
        location: SpawnPosition,
    ): MobEntity {
        val entity = type.type.spawn(world, location.blockPos(), SpawnReason.STRUCTURE)
            ?: throw Exception("Couldn't  spawn entity of type: ${type.type}, in world: $world")

        clearVehicleAndPassengers(entity)

        entity.refreshPositionAndAngles(
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
        entity: MobEntity,
        type: EntityTypeStats,
    ) {
        val newMaxHealth = type.health
        entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)?.baseValue = newMaxHealth
        entity.health = newMaxHealth.toFloat()

        entity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE)?.baseValue = type.attackDamage
        entity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)?.baseValue = type.movementSpeed
        entity.getAttributeInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = type.knockbackResistance
    }

    private fun clearVehicleAndPassengers(entity: MobEntity) {
        entity.vehicle?.let { vehicle ->
            entity.stopRiding()
            vehicle.discard()
        }
        entity.passengerList.toList().forEach { passenger ->
            passenger.discard()
        }
    }
}