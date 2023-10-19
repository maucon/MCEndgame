package de.fuballer.mcendgame.component.dungeon.enemy.custom_entity

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

    RAVAGER(EntityType.RAVAGER, null, false, false, false, false, false),
    DEMONIC_GOLEM(EntityType.RAVAGER, "Demonic Golem", false, false, false, false, false),

    STONE_PILLAR(EntityType.ZOMBIE, "Stone Pillar", false, false, false, false, true),
}
