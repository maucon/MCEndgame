package de.fuballer.mcendgame.component.dungeon.enemy.custom_entity

import org.bukkit.entity.EntityType

enum class CustomEntityType(
    val type: EntityType,
    val customName: String?,
    val canHaveWeapons: Boolean,
    val canHaveArmor: Boolean,
    val dropsBaseLoot: Boolean,
) {
    ZOMBIE(EntityType.ZOMBIE, null, true,true, true),
    HUSK(EntityType.HUSK, null, true,true,  true),
    SKELETON(EntityType.SKELETON, null, true,true,  true),
    STRAY(EntityType.STRAY, null, true, true, true),
    WITHER_SKELETON(EntityType.WITHER_SKELETON, null, true, true, true),
    CREEPER(EntityType.CREEPER, null, false,false,  true),
    WITCH(EntityType.WITCH, null, false, false, true),

    NECROMANCER(EntityType.EVOKER, "Necromancer", false, true, false),
}