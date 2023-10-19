package de.fuballer.mcendgame.domain.equipment

import de.fuballer.mcendgame.component.statitem.StatItemSettings
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.Material

interface Equipment {
    val material: Material
    val baseAttributes: List<ItemAttribute>
    val lore: String
    val rolledAttributes: List<RandomOption<ItemAttribute>>
    val enchantOptions: List<RandomOption<ItemEnchantment>>

    companion object {
        val HEAD_SLOT_LORE = "${StatItemSettings.SLOT_LORE_COLOR}When on Head:"
        val CHEST_SLOT_LORE = "${StatItemSettings.SLOT_LORE_COLOR}When on Body:"
        val LEGS_SLOT_LORE = "${StatItemSettings.SLOT_LORE_COLOR}When on Legs:"
        val FEET_SLOT_LORE = "${StatItemSettings.SLOT_LORE_COLOR}When on Feet:"
        val MAIN_HAND_SLOT_LORE = "${StatItemSettings.SLOT_LORE_COLOR}When in Main Hand:"
        val OFF_HAND_SLOT_LORE = "${StatItemSettings.SLOT_LORE_COLOR}When in Off Hand:"
    }
}
