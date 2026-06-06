package de.fuballer.mcendgame.main.component.item.custom

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.setCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.dungeon.loot.drop.ItemColor
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import kotlin.random.Random

interface UniqueAttributesItemInterface {
    fun getNameColor(): Int = ItemColor.UNIQUE.intColor

    fun getCustomAttributes(): List<RollableCustomAttribute>

    fun getAttributeModifierSlot(): EquipmentSlotGroup

    private fun getRolledAttributes(
        rolls: List<Double>,
    ): List<CustomAttribute> {
        val iterator = rolls.iterator()
        val lastRoll = rolls.lastOrNull()

        return getCustomAttributes().map { attribute ->
            val percentageRolls = mutableListOf<Double>()

            repeat(attribute.bounds.size) {
                if (iterator.hasNext()) {
                    percentageRolls.add(iterator.next())
                } else {
                    percentageRolls.add(lastRoll ?: Random.nextDouble())
                }
            }

            attribute.roll(percentageRolls, getAttributeModifierSlot())
        }
    }

    fun getRolledStack(
        item: Item,
        maxRoll: Boolean = false,
    ): ItemStack {
        return getRolledStack(item, if (maxRoll) listOf(1.0) else listOf())
    }

    fun getRolledStack(
        item: Item,
        rolls: List<Double>,
    ): ItemStack {
        val stack = ItemStack(item)

        val attributes = getRolledAttributes(rolls)
        stack.setCustomAttributes(attributes, getAttributeModifierSlot())

        return stack
    }
}