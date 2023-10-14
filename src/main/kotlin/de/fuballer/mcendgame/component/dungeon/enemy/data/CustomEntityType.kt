package de.fuballer.mcendgame.component.dungeon.enemy.data

import org.bukkit.entity.EntityType

enum class CustomEntityType(
    val type: EntityType,
    val customName: String?,
    val canHaveEquip: Boolean
) {
    ZOMBIE(EntityType.ZOMBIE, null, true),
    HUSK(EntityType.HUSK, null, true),
    SKELETON(EntityType.SKELETON, null, true),
    STRAY(EntityType.STRAY, null, true),
    WITHER_SKELETON(EntityType.WITHER_SKELETON, null, true),
    CREEPER(EntityType.CREEPER, null, false),
    WITCH(EntityType.WITCH, null, false),

    NECROMANCER(EntityType.EVOKER, "Necromancer", true),
}