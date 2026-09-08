package de.fuballer.mcendgame.main.component.item.custom.crystal.item.synthesized

import de.fuballer.mcendgame.main.component.block.blocks.crystalforge.CrystalForgeSettings
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.updateCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.AttributeBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.AttributeType
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItemInterface
import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItem
import de.fuballer.mcendgame.main.component.item.equipment.Equipment
import de.fuballer.mcendgame.main.util.random.RandomOption
import de.fuballer.mcendgame.main.util.random.RandomUtil
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.ItemStack
import kotlin.math.abs

abstract class SynthesizedCrystal(
    settings: Properties
) : CrystalItem(settings) {
    abstract val forcedAttributes: Map<Equipment, EquipmentAttributes>

    override fun canForge(
        stack: ItemStack,
        secondaryOutputSlotFilled: Boolean,
    ): MutableComponent? {
        val cannotForgeReason = super.canForge(stack, secondaryOutputSlotFilled)
        if (cannotForgeReason != null) return cannotForgeReason

        if (stack.item is UniqueAttributesItemInterface) return CrystalForgeSettings.getForgeErrorText("cannot_forge_unique")

        val attributes = stack.getCustomAttributes()
        if (attributes.isEmpty()) return CrystalForgeSettings.getForgeErrorText("not_enough_attributes")

        return null
    }

    override fun forge(stack: ItemStack): CrystalForgeOutput {
        val newStack = stack.copy()

        val oldAttributes = stack.getCustomAttributes()
        if (oldAttributes.isEmpty()) return CrystalForgeOutput(newStack)

        val equipment = Equipment.fromItem(stack.item)

        var possibleAttributes = forcedAttributes[equipment]?.options ?: return CrystalForgeOutput(newStack)

        val possibleAttributeTypes = possibleAttributes.map { it.option.type }
        val allPossiblePresent = oldAttributes.map { it.type }.containsAll(possibleAttributeTypes)
        val removedAttribute = if (!allPossiblePresent) oldAttributes.random()
        else oldAttributes.filter { possibleAttributeTypes.contains(it.type) }.random()

        val newAttributes = oldAttributes.toMutableList()
        newAttributes.remove(removedAttribute)

        val presentAttributeTypes = newAttributes.map { it.type }
        possibleAttributes = possibleAttributes.filter { !presentAttributeTypes.contains(it.option.type) }
        val chosenAttribute = RandomUtil.pickOne(possibleAttributes).option

        val tier = removedAttribute.tier
        val attributeBounds = chosenAttribute.getBounds(tier)
        val rollableAttribute = RollableCustomAttribute(
            chosenAttribute.type,
            tier,
            attributeBounds,
        )

        val rolledAttribute = rollableAttribute.roll(removedAttribute.slot)
        newAttributes.add(rolledAttribute)

        newStack.updateCustomAttributes(newAttributes)
        return CrystalForgeOutput(newStack)
    }

    data class EquipmentAttributes(
        val options: List<RandomOption<EquipmentAttribute>>,
    ) {
        constructor(vararg options: RandomOption<EquipmentAttribute>) : this(options.toList())
    }

    data class EquipmentAttribute(
        val type: AttributeType,
        val tiers: Map<Int, List<AttributeBounds<*>>>,
    ) {
        constructor(
            type: AttributeType,
            vararg tiers: Pair<Int, AttributeBounds<*>>,
        ) : this(type, tiers.toMap().mapValues { listOf(it.value) })

        fun getBounds(tier: Int): List<AttributeBounds<*>> {
            tiers[tier]?.let { return it }
            return tiers.entries.minBy { abs(tier - it.key) * 2 + if (it.key < tier) 1 else 0 }.value
        }
    }
}