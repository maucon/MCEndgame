package de.fuballer.mcendgame.component.dungeon.modifier

/**
 * Enumeration representing different types of modifiers that can be applied to entities.
 */
enum class ModifierType {
    /**
     * Modifier affecting the drop rates of items when the entity with this modifier is slain.
     */
    LOOT_DROP,

    /**
     * Modifier affecting the drop rates of items when entities are slain by the entity with this modifier.
     */
    MAGIC_FIND
}