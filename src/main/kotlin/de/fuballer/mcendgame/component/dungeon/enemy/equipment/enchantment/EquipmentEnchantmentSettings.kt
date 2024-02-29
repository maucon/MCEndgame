package de.fuballer.mcendgame.component.dungeon.enemy.equipment.enchantment

object EquipmentEnchantmentSettings {
    private const val ENCHANT_TRIES_PER_TIER = 0.5

    fun calculateEnchantTries(mapTier: Int) = 1 + (mapTier * ENCHANT_TRIES_PER_TIER).toInt()
}