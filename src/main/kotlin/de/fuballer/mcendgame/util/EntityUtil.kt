package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.domain.entity.CustomEntityType
import de.fuballer.mcendgame.technical.persistent_data.TypeKeys
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.entity.*

object EntityUtil {
    fun isCustomEntityType(entity: Entity, entityType: CustomEntityType): Boolean {
        val type = PersistentDataUtil.getValue(entity, TypeKeys.CUSTOM_ENTITY_TYPE) ?: return false
        return type == entityType
    }

    fun spawnCustomEntity(entityType: CustomEntityType, loc: Location, mapTier: Int): Entity? {
        val world = loc.world ?: return null

        val entity = world.spawnEntity(loc, entityType.type, false)
        entity.customName = entityType.customName
        entity.isCustomNameVisible = false

        setPersistentData(entity, entityType, mapTier)

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

    private fun setPersistentData(entity: Entity, entityType: CustomEntityType, mapTier: Int) {
        PersistentDataUtil.setValue(entity, TypeKeys.MAP_TIER, mapTier)
        PersistentDataUtil.setValue(entity, TypeKeys.HIDE_EQUIPMENT, entityType.hideEquipment)
        PersistentDataUtil.setValue(entity, TypeKeys.CUSTOM_ENTITY_TYPE, entityType)
        PersistentDataUtil.setValue(entity, TypeKeys.IS_ENEMY, true)
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