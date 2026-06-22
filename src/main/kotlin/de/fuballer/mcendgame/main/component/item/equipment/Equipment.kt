package de.fuballer.mcendgame.main.component.item.equipment

import de.fuballer.mcendgame.main.component.item.equipment.armor.Boots
import de.fuballer.mcendgame.main.component.item.equipment.armor.Chestplate
import de.fuballer.mcendgame.main.component.item.equipment.armor.Helmet
import de.fuballer.mcendgame.main.component.item.equipment.armor.Leggings
import de.fuballer.mcendgame.main.component.item.equipment.data.TieredRollableCustomAttribute
import de.fuballer.mcendgame.main.component.item.equipment.enchantment.EquipmentEnchantment
import de.fuballer.mcendgame.main.component.item.equipment.tool.*
import de.fuballer.mcendgame.main.util.random.RandomOption
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.item.Item

interface Equipment {
    val item: Item
    val slot: EquipmentSlotGroup

    val rollableEnchants: List<RandomOption<EquipmentEnchantment>>
    val rollableCustomAttributes: List<RandomOption<TieredRollableCustomAttribute>>

    companion object {
        private val materialToEquipment = mutableMapOf<Item, Equipment>()
            .apply {
                putAll(Helmet.entries.associateBy { it.item })
                putAll(Chestplate.entries.associateBy { it.item })
                putAll(Leggings.entries.associateBy { it.item })
                putAll(Boots.entries.associateBy { it.item })
                putAll(Sword.entries.associateBy { it.item })
                putAll(Axe.entries.associateBy { it.item })
                putAll(Pickaxe.entries.associateBy { it.item })
                putAll(Shovel.entries.associateBy { it.item })
                putAll(Hoe.entries.associateBy { it.item })
                putAll(Bow.entries.associateBy { it.item })
                putAll(Horn.entries.associateBy { it.item })
                putAll(Shield.entries.associateBy { it.item })
                putAll(Mace.entries.associateBy { it.item })
                putAll(Miscellaneous.entries.associateBy { it.item })
            }

        fun fromItem(item: Item) = materialToEquipment[item]

        fun existsByMaterial(item: Item) = materialToEquipment.containsKey(item)
    }
}