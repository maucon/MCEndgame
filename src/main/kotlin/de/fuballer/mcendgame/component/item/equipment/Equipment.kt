package de.fuballer.mcendgame.component.item.equipment

import de.fuballer.mcendgame.component.item.attribute.data.BaseAttribute
import de.fuballer.mcendgame.component.item.attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.component.item.equipment.armor.Boots
import de.fuballer.mcendgame.component.item.equipment.armor.Chestplate
import de.fuballer.mcendgame.component.item.equipment.armor.Helmet
import de.fuballer.mcendgame.component.item.equipment.armor.Leggings
import de.fuballer.mcendgame.component.item.equipment.tool.*
import de.fuballer.mcendgame.util.TextComponent
import de.fuballer.mcendgame.util.random.RandomOption
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.inventory.EquipmentSlot

interface Equipment {
    val material: Material
    val baseAttributes: List<BaseAttribute>
    val slot: EquipmentSlot

    /** whether to only grant extra attributes when the item is in the correct slot */
    val slotDependentAttributes: Boolean
    val rollableCustomAttributes: List<RandomOption<RollableCustomAttribute>>
    val rollableEnchants: List<RandomOption<ItemEnchantment>>

    companion object {
        private val SLOT_LORE_COLOR = NamedTextColor.GRAY
        val GENERIC_SLOT_LORE = TextComponent.create("When equipped:", SLOT_LORE_COLOR)

        fun getLoreForSlot(slot: EquipmentSlot): Component {
            val text = when (slot) {
                EquipmentSlot.HAND -> "When in Main Hand:"
                EquipmentSlot.OFF_HAND -> "When in Off Hand:"
                EquipmentSlot.FEET -> "When on Feet:"
                EquipmentSlot.LEGS -> "When on Legs:"
                EquipmentSlot.CHEST -> "When on Body:"
                EquipmentSlot.HEAD -> "When on Head:"
                EquipmentSlot.BODY -> return GENERIC_SLOT_LORE
            }

            return TextComponent.create(text, SLOT_LORE_COLOR)
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