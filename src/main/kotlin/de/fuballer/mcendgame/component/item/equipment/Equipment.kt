package de.fuballer.mcendgame.component.item.equipment

import de.fuballer.mcendgame.component.item.attribute.RollableAttribute
import de.fuballer.mcendgame.component.item.attribute.RolledAttribute
import de.fuballer.mcendgame.component.item.equipment.armor.Boots
import de.fuballer.mcendgame.component.item.equipment.armor.Chestplate
import de.fuballer.mcendgame.component.item.equipment.armor.Helmet
import de.fuballer.mcendgame.component.item.equipment.armor.Leggings
import de.fuballer.mcendgame.component.item.equipment.tool.*
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.EquipmentSlot

interface Equipment {
    val material: Material
    val baseAttributes: List<RolledAttribute>
    val slot: EquipmentSlot

    /** whether to only grant extra attributes when the item is in the correct slot */
    val slotDependentAttributes: Boolean
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

        private val materialToEquipment = mutableMapOf<Material, Equipment>()
            .apply {
                putAll(Boots.entries.associateBy { it.material })
                putAll(Chestplate.entries.associateBy { it.material })
                putAll(Helmet.entries.associateBy { it.material })
                putAll(Leggings.entries.associateBy { it.material })
                putAll(Axe.entries.associateBy { it.material })
                putAll(Hoe.entries.associateBy { it.material })
                putAll(Pickaxe.entries.associateBy { it.material })
                putAll(Shovel.entries.associateBy { it.material })
                putAll(Sword.entries.associateBy { it.material })
                putAll(Tool.entries.associateBy { it.material })
            }

        fun fromMaterial(material: Material) = materialToEquipment[material]

        fun existsByMaterial(material: Material) = materialToEquipment.containsKey(material)
    }
}