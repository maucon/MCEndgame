package de.fuballer.mcendgame.component.dungeon.enemy.custom_entity

import org.bukkit.entity.EntityType

enum class CustomEntityType(
    val type: EntityType,
    val customName: String?,
    val canHaveWeapons: Boolean,
    val isRanged: Boolean,
    val canHaveArmor: Boolean,
    val dropBaseLoot: Boolean,
) {
    ZOMBIE(EntityType.ZOMBIE, null, true, false, true, true),
    HUSK(EntityType.HUSK, null, true, false, true, true),
    SKELETON(EntityType.SKELETON, null, true, true, true, true),
    STRAY(EntityType.STRAY, null, true, true, true, true),
    WITHER_SKELETON(EntityType.WITHER_SKELETON, null, true, false, true, true),
    CREEPER(EntityType.CREEPER, null, false, false, false, true),
    WITCH(EntityType.WITCH, null, false, false, false, true),

    NECROMANCER(EntityType.EVOKER, "Necromancer", false, false, true, false),

    DEMONIC_GOLEM(EntityType.RAVAGER, "Demonic Golem", false, false, false, false)
}