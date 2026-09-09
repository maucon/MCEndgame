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
import de.fuballer.mcendgame.main.component.item.equipment.armor.Boots
import de.fuballer.mcendgame.main.component.item.equipment.armor.Chestplate
import de.fuballer.mcendgame.main.component.item.equipment.armor.Helmet
import de.fuballer.mcendgame.main.component.item.equipment.armor.Leggings
import de.fuballer.mcendgame.main.component.item.equipment.data.TieredRollableCustomAttribute
import de.fuballer.mcendgame.main.component.item.equipment.tool.*
import de.fuballer.mcendgame.main.util.random.RandomOption
import de.fuballer.mcendgame.main.util.random.RandomUtil
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.ItemStack
import kotlin.math.abs

abstract class SynthesizedCrystal(
    settings: Properties
) : CrystalItem(settings) {
    abstract val forcedAttributes: Map<Equipment, EquipmentAttributes>

    companion object {
        const val ATTRIBUTE_DEFAULT_WEIGHT = 100
        const val ATTRIBUTE_DEFAULT_FACTOR = 0.8

        val MELEE_WEAPONS = listOf<List<Equipment>>(
            Sword.entries,
            Axe.entries,
            Pickaxe.entries,
            Shovel.entries,
            Hoe.entries,
            Spear.entries,
            Mace.entries,
            listOf(Miscellaneous.TRIDENT),
        )
        val RANGED_WEAPONS = listOf<List<Equipment>>(
            Bow.entries,
            listOf(Miscellaneous.CROSSBOW),
        )
        val WEAPONS = MELEE_WEAPONS.toMutableList().apply { addAll(RANGED_WEAPONS) }
        val MELEE_WEAPONS_WITH_SHIELDS = MELEE_WEAPONS.toMutableList().apply { add(Shield.entries) }
        val WEAPONS_WITH_SHIELD = WEAPONS.toMutableList().apply { add(Shield.entries) }
        val HELMETS_AND_BOOTS = listOf<List<Equipment>>(
            Helmet.entries,
            Boots.entries,
        )
        val ARMOR = listOf<List<Equipment>>(
            Helmet.entries,
            Chestplate.entries,
            Leggings.entries,
            Boots.entries,
        )
        val CHESTPLATES_WITH_ELYTRA = Chestplate.entries.toMutableList<Equipment>().apply { add(Miscellaneous.ELYTRA) }
    }

    override fun canForge(
        stack: ItemStack,
        secondaryOutputSlotFilled: Boolean,
    ): MutableComponent? {
        val cannotForgeReason = super.canForge(stack, secondaryOutputSlotFilled)
        if (cannotForgeReason != null) return cannotForgeReason

        if (stack.item is UniqueAttributesItemInterface) return CrystalForgeSettings.getForgeErrorText("cannot_forge_unique")

        val equipment = Equipment.fromItem(stack.item)
        val possibleAttributes = forcedAttributes[equipment]?.options
        if (possibleAttributes?.isNotEmpty() != true) return CrystalForgeSettings.getForgeErrorText("no_synthesized_attribute")

        val presentAttributes = stack.getCustomAttributes()
        if (presentAttributes.isEmpty()) return CrystalForgeSettings.getForgeErrorText("not_enough_attributes")

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

        companion object {
            fun fromBoundsLists(
                type: AttributeType,
                vararg tiers: Pair<Int, List<AttributeBounds<*>>>,
            ) = EquipmentAttribute(type, tiers.toMap())
        }

        fun getBounds(tier: Int): List<AttributeBounds<*>> {
            tiers[tier]?.let { return it }
            return tiers.entries.minBy { abs(tier - it.key) * 2 + if (it.key < tier) 1 else 0 }.value
        }
    }

    fun MutableMap<Equipment, EquipmentAttributes>.putEquipmentAttributes(
        equipment: Iterable<Equipment>,
        vararg options: RandomOption<EquipmentAttribute>
    ): MutableMap<Equipment, EquipmentAttributes> {
        putAll(equipment.associateWith { EquipmentAttributes(*options) })
        return this
    }

    fun MutableMap<Equipment, EquipmentAttributes>.putEquipmentAttributes(
        equipment: Iterable<Iterable<Equipment>>,
        vararg options: RandomOption<EquipmentAttribute>
    ): MutableMap<Equipment, EquipmentAttributes> {
        equipment.forEach { putEquipmentAttributes(it, *options) }
        return this
    }

    data class CopyExistingData(
        val type: AttributeType,
        val weight: Int = ATTRIBUTE_DEFAULT_WEIGHT,
        val factor: Double = ATTRIBUTE_DEFAULT_FACTOR,
        val takeFrom: Equipment? = null,
    )

    fun MutableMap<Equipment, EquipmentAttributes>.fromExisting(
        equipment: Iterable<Equipment>,
        vararg toCopy: CopyExistingData,
    ): MutableMap<Equipment, EquipmentAttributes> {
        putAll(equipment.associateWith { equip ->
            EquipmentAttributes(
                toCopy.map { copy ->
                    RandomOption(
                        weight = copy.weight,
                        EquipmentAttribute(
                            copy.type,
                            getEquipmentAttributeBounds(
                                copy.takeFrom?.rollableCustomAttributes ?: equip.rollableCustomAttributes,
                                copy.type,
                                copy.factor,
                            )
                        )
                    )
                }
            )
        })
        return this
    }

    @JvmName("fromExistingGrouped")
    fun MutableMap<Equipment, EquipmentAttributes>.fromExisting(
        equipment: Iterable<Iterable<Equipment>>,
        vararg toCopy: CopyExistingData,
    ): MutableMap<Equipment, EquipmentAttributes> {
        equipment.forEach { fromExisting(it, *toCopy) }
        return this
    }

    fun getEquipmentAttributeBounds(
        attributes: List<RandomOption<TieredRollableCustomAttribute>>,
        type: AttributeType,
        factor: Double,
    ): Map<Int, List<AttributeBounds<*>>> = attributes
        .first { it.option.type == type }.option.tiers
        .map { it.option }
        .associate { data -> data.tier to data.bounds.map { it.withFactor(factor) } }
}