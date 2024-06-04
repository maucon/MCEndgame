package de.fuballer.mcendgame.component.dungeon.modifier

enum class ModifierType {
    /** the drop rates of an entity with this modifier when slain are modified  */
    LOOT_DROP,

    /** the drop rates of the entities slain by an entity with this modifier are modified  */
    MAGIC_FIND
}