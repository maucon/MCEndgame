package de.fuballer.mcendgame.main.util.minecraft

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.addCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
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
        reason: EntitySpawnReason = EntitySpawnReason.STRUCTURE,
    ): Mob {
        val entity = type.type.spawn(world, location.blockPos(), reason)
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

        val spellDamage = type.spellDamage
        if (spellDamage > 0) entity.addCustomAttribute(CustomAttribute(CustomAttributeTypes.SPELL_DAMAGE, roll = DoubleRoll(DoubleBounds(spellDamage))))
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