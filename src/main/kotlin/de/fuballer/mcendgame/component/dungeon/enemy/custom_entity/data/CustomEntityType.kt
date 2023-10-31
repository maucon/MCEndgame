package de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.data

import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.CustomEntitySettings
import de.fuballer.mcendgame.util.DungeonUtil
import de.fuballer.mcendgame.util.PersistentDataUtil
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Raider

enum class CustomEntityType(
    val type: EntityType,
    val customName: String?,
    val data: CustomEntityData
) {
    ZOMBIE(EntityType.ZOMBIE, null, CustomEntitySettings.ZOMBIE_DATA),
    HUSK(EntityType.HUSK, null, CustomEntitySettings.HUSK_DATA),
    SKELETON(EntityType.SKELETON, null, CustomEntitySettings.SKELETON_DATA),
    STRAY(EntityType.STRAY, null, CustomEntitySettings.STRAY_DATA),
    WITHER_SKELETON(EntityType.WITHER_SKELETON, null, CustomEntitySettings.WITHER_SKELETON_DATA),
    CREEPER(EntityType.CREEPER, null, CustomEntitySettings.CREEPER_DATA),
    WITCH(EntityType.WITCH, null, CustomEntitySettings.WITCH_DATA),

    NECROMANCER(EntityType.EVOKER, "Necromancer", CustomEntitySettings.NECROMANCER_DATA),
    REAPER(EntityType.WITHER_SKELETON, "Reaper", CustomEntitySettings.REAPER_DATA),
    CHUPACABRA(EntityType.WOLF, "Chupacabra", CustomEntitySettings.CHUPACABRA_DATA),
    STALKER(EntityType.ZOMBIE, "Stalker", CustomEntitySettings.STALKER_DATA),
    DEMONIC_GOLEM(EntityType.RAVAGER, "Demonic Golem", CustomEntitySettings.DEMONIC_GOLEM_DATA),
    MINOTAUR(EntityType.IRON_GOLEM, "Minotaur", CustomEntitySettings.MINOTAUR_DATA),
    NAGA(EntityType.SKELETON, "Naga", CustomEntitySettings.NAGA_DATA),
    CYCLOPS(EntityType.ZOMBIE, "Cyclops", CustomEntitySettings.CYCLOPS_DATA),
    HARPY(EntityType.SKELETON, "Harpy", CustomEntitySettings.HARPY_DATA),
    CERBERUS(EntityType.RAVAGER, "Cerberus", CustomEntitySettings.CERBERUS_DATA),
    SUCCUBUS(EntityType.ZOMBIE, "Succubus", CustomEntitySettings.SUCCUBUS_DATA),
    INCUBUS(EntityType.ZOMBIE, "Incubus", CustomEntitySettings.INCUBUS_DATA),

    STONE_PILLAR(EntityType.ZOMBIE, "Stone Pillar", CustomEntitySettings.STONE_PILLAR_DATA),

    POISON_SPIT(EntityType.LLAMA_SPIT, "Poison Spit", CustomEntitySettings.POISON_SPIT_DATA);

    companion object {
        fun isType(entity: Entity, customEntityType: CustomEntityType): Boolean {
            val type = PersistentDataUtil.getValue(entity, DataTypeKeys.ENTITY_TYPE) ?: return false
            return type == customEntityType.toString()
        }

        fun spawnCustomEntity(customEntityType: CustomEntityType, loc: Location, mapTier: Int): Entity? {
            val world = loc.world ?: return null

            val entity = world.spawnEntity(loc, customEntityType.type)
            entity.customName = customEntityType.customName
            entity.isCustomNameVisible = false

            setPersistentData(entity, customEntityType, mapTier)

            if (entity !is LivingEntity) return entity
            setAttributes(entity, customEntityType.data, mapTier)
            entity.removeWhenFarAway = false
            entity.isSilent = customEntityType.data.isSilent

            if (entity is Raider) {
                entity.isPatrolLeader = false
            }

            return entity
        }

        private fun setPersistentData(entity: Entity, type: CustomEntityType, mapTier: Int) {
            PersistentDataUtil.setValue(entity, DataTypeKeys.DROP_BASE_LOOT, type.data.dropBaseLoot)
            PersistentDataUtil.setValue(entity, DataTypeKeys.MAP_TIER, mapTier)
            PersistentDataUtil.setValue(entity, DataTypeKeys.HIDE_EQUIPMENT, type.data.hideEquipment)
            PersistentDataUtil.setValue(entity, DataTypeKeys.ENTITY_TYPE, type.toString())
            PersistentDataUtil.setValue(entity, DataTypeKeys.IS_ENEMY, true)
        }

        private fun setAttributes(entity: LivingEntity, data: CustomEntityData, mapTier: Int) {
            val newHealth = data.maxLifeBase + mapTier * data.maxLifePerTier
            val newDamage = data.damageBase + mapTier * data.damagePerTier
            val newSpeed = data.speedBase + mapTier * data.speedPerTier

            DungeonUtil.setBasicAttributes(entity, newHealth, newDamage, newSpeed)
        }
    }
}
