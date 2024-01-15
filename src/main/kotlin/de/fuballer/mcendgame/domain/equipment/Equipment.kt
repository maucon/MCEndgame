package de.fuballer.mcendgame.domain.equipment

import de.fuballer.mcendgame.component.stat_item.StatItemSettings
import de.fuballer.mcendgame.domain.attribute.RollableAttribute
import de.fuballer.mcendgame.domain.attribute.RolledAttribute
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.Material

interface Equipment {
    val material: Material
    val baseAttributes: List<RolledAttribute>
    val lore: String
    val rollableAttributes: List<RandomOption<RollableAttribute>>
    val rollableEnchants: List<RandomOption<ItemEnchantment>>

    companion object {
        val GENERIC_SLOT_LORE = "${StatItemSettings.SLOT_LORE_COLOR}When equipped:"
        val HEAD_SLOT_LORE = "${StatItemSettings.SLOT_LORE_COLOR}When on Head:"
        val CHEST_SLOT_LORE = "${StatItemSettings.SLOT_LORE_COLOR}When on Body:"
        val LEGS_SLOT_LORE = "${StatItemSettings.SLOT_LORE_COLOR}When on Legs:"
        val FEET_SLOT_LORE = "${StatItemSettings.SLOT_LORE_COLOR}When on Feet:"
        val MAIN_HAND_SLOT_LORE = "${StatItemSettings.SLOT_LORE_COLOR}When in Main Hand:"
        val OFF_HAND_SLOT_LORE = "${StatItemSettings.SLOT_LORE_COLOR}When in Off Hand:"
    }
}
