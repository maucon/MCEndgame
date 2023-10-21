package de.fuballer.mcendgame.component.dungeon.enemy.custom_entity

import de.fuballer.mcendgame.util.PersistentDataUtil
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType

enum class CustomEntityType(
    val type: EntityType,
    val customName: String?,
    val canHaveWeapons: Boolean,
    val isRanged: Boolean,
    val canHaveArmor: Boolean,
    val dropBaseLoot: Boolean,
    val hideEquipment: Boolean
) {
    ZOMBIE(EntityType.ZOMBIE, null, true, false, true, true, false),
    HUSK(EntityType.HUSK, null, true, false, true, true, false),
    SKELETON(EntityType.SKELETON, null, true, true, true, true, false),
    STRAY(EntityType.STRAY, null, true, true, true, true, false),
    WITHER_SKELETON(EntityType.WITHER_SKELETON, null, true, false, true, true, false),
    CREEPER(EntityType.CREEPER, null, false, false, false, true, false),
    WITCH(EntityType.WITCH, null, false, false, false, true, false),

    NECROMANCER(EntityType.EVOKER, "Necromancer", true, false, true, false, true),
    REAPER(EntityType.WITHER_SKELETON, "Reaper", true, false, true, false, true),

    RAVAGER(EntityType.RAVAGER, null, false, false, false, false, false),
    DEMONIC_GOLEM(EntityType.RAVAGER, "Demonic Golem", false, false, false, false, false),
    MINOTAUR(EntityType.IRON_GOLEM, "Minotaur", false, false, false, false, false),
    NAGA(EntityType.SKELETON, "Naga", true, true, true, false, true),

    STONE_PILLAR(EntityType.ZOMBIE, "Stone Pillar", false, false, false, false, true),

    POISON_SPIT(EntityType.LLAMA_SPIT, "Poison Spit", false, false, false, false, false);

    companion object{
        fun isType(entity: Entity, customEntityType: CustomEntityType): Boolean {
            val type = PersistentDataUtil.getValue(entity, DataTypeKeys.ENTITY_TYPE) ?: return false
            return type == customEntityType.toString()
        }
    }
}
