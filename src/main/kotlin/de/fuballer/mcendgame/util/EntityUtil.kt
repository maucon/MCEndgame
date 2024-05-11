package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.util.extension.EntityExtension.setCustomEntityType
import de.fuballer.mcendgame.util.extension.EntityExtension.setHideEquipment
import de.fuballer.mcendgame.util.extension.EntityExtension.setIsEnemy
import de.fuballer.mcendgame.util.extension.EntityExtension.setMapTier
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.entity.*

object EntityUtil {
    fun spawnCustomEntity(entityType: CustomEntityType, loc: Location, mapTier: Int): Entity? {
        val world = loc.world ?: return null

        val entity = world.spawnEntity(loc, entityType.type, false)
        entity.customName = entityType.customName
        entity.isCustomNameVisible = false

        setCustomData(entity, entityType, mapTier)

        if (entity !is LivingEntity) return entity
        setAttributes(entity, entityType, mapTier)
        entity.removeWhenFarAway = false
        entity.isSilent = entityType.sounds != null

        setMiscellaneous(entity)

        return entity
    }

    fun setAttribute(
        entity: LivingEntity,
        attribute: Attribute,
        value: Double
    ) {
        val attributeInstance = entity.getAttribute(attribute) ?: return
        attributeInstance.baseValue = value
    }

    fun increaseBaseAttribute(
        entity: LivingEntity,
        attribute: Attribute,
        factor: Double
    ) {
        val attributeInstance = entity.getAttribute(attribute) ?: return
        attributeInstance.baseValue *= factor
    }

    fun getPlayerDamager(entity: LivingEntity): Player? {
        if (entity is Player) {
            return entity
        }
        if (entity is Projectile && (entity as Projectile).shooter is Player) {
            return (entity as Projectile).shooter as Player
        }
        return null
    }

    fun getLivingEntityDamager(entity: Entity): LivingEntity? {
        if (entity is LivingEntity) {
            return entity
        }
        if (entity is Projectile && entity.shooter is LivingEntity) {
            return entity.shooter as LivingEntity
        }
        return null
    }

    private fun setCustomData(entity: Entity, entityType: CustomEntityType, mapTier: Int) {
        entity.setMapTier(mapTier)
        entity.setHideEquipment(entityType.hideEquipment)
        entity.setCustomEntityType(entityType)
        entity.setIsEnemy()
    }

    private fun setAttributes(entity: LivingEntity, entityType: CustomEntityType, mapTier: Int) {
        val newHealth = entityType.baseHealth + mapTier * entityType.healthPerTier
        val newDamage = entityType.baseDamage + mapTier * entityType.damagePerTier
        val newSpeed = entityType.baseSpeed + mapTier * entityType.speedPerTier

        setAttribute(entity, Attribute.GENERIC_MAX_HEALTH, newHealth)
        entity.health = newHealth

        setAttribute(entity, Attribute.GENERIC_ATTACK_DAMAGE, newDamage)
        setAttribute(entity, Attribute.GENERIC_MOVEMENT_SPEED, newSpeed)
    }

    private fun setMiscellaneous(entity: LivingEntity) {
        if (entity is Raider) {
            entity.isPatrolLeader = false
        }
        if (entity is Ageable) {
            entity.setAdult()
        }
        if (entity is PiglinAbstract) {
            entity.isImmuneToZombification = true
        }
        if (entity is Slime) {
            entity.size = 2
        }
    }
}