package de.fuballer.mcendgame.domain.equipment

import de.fuballer.mcendgame.domain.attribute.RollableAttribute
import de.fuballer.mcendgame.domain.attribute.RolledAttribute
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.EquipmentSlot

interface Equipment {
    val material: Material
    val baseAttributes: List<RolledAttribute>
    val slot: EquipmentSlot
    val extraAttributesInSlot: Boolean
    val rollableAttributes: List<RandomOption<RollableAttribute>>
    val rollableEnchants: List<RandomOption<ItemEnchantment>>

    companion object {
        private val SLOT_LORE_COLOR = ChatColor.GRAY
        val GENERIC_SLOT_LORE = "${SLOT_LORE_COLOR}When equipped:"

        fun getLoreForSlot(slot: EquipmentSlot) =
            when (slot) {
                EquipmentSlot.HAND -> "${SLOT_LORE_COLOR}When in Main Hand:"
                EquipmentSlot.OFF_HAND -> "${SLOT_LORE_COLOR}When in Off Hand:"
                EquipmentSlot.FEET -> "${SLOT_LORE_COLOR}When on Feet:"
                EquipmentSlot.LEGS -> "${SLOT_LORE_COLOR}When on Legs:"
                EquipmentSlot.CHEST -> "${SLOT_LORE_COLOR}When on Body:"
                EquipmentSlot.HEAD -> "${SLOT_LORE_COLOR}When on Head:"
            }
    }
}
