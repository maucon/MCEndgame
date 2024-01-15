package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.domain.entity.CustomEntityType
import de.fuballer.mcendgame.domain.persistent_data.DataTypeKeys
import org.bukkit.Location
import org.bukkit.entity.*

object EntityUtil {
    fun isCustomEntityType(entity: Entity, entityType: CustomEntityType): Boolean {
        val type = PersistentDataUtil.getValue(entity, DataTypeKeys.CUSTOM_ENTITY_TYPE) ?: return false
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

    private fun setPersistentData(entity: Entity, entityType: CustomEntityType, mapTier: Int) {
        PersistentDataUtil.setValue(entity, DataTypeKeys.MAP_TIER, mapTier)
        PersistentDataUtil.setValue(entity, DataTypeKeys.HIDE_EQUIPMENT, entityType.hideEquipment)
        PersistentDataUtil.setValue(entity, DataTypeKeys.CUSTOM_ENTITY_TYPE, entityType)
        PersistentDataUtil.setValue(entity, DataTypeKeys.IS_ENEMY, true)
    }

    private fun setAttributes(entity: LivingEntity, entityType: CustomEntityType, mapTier: Int) {
        val newHealth = entityType.baseHealth + mapTier * entityType.healthPerTier
        val newDamage = entityType.baseDamage + mapTier * entityType.damagePerTier
        val newSpeed = entityType.baseSpeed + mapTier * entityType.speedPerTier

        DungeonUtil.setBasicAttributes(entity, newHealth, newDamage, newSpeed)
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