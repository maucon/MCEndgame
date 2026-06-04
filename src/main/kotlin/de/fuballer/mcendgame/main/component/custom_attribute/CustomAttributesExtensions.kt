package de.fuballer.mcendgame.main.component.custom_attribute

import de.fuballer.mcendgame.main.MCEndgame
import de.fuballer.mcendgame.main.accessor.LivingEntityCustomAttributesAccessor
import de.fuballer.mcendgame.main.component.custom_attribute.data.*
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes.ADDITIONAL_ARROWS
import de.fuballer.mcendgame.main.messaging.collect_attribute.CollectHealFactorCommand
import de.fuballer.mcendgame.main.messaging.misc.CollectCustomAttributesCommand
import de.fuballer.mcendgame.main.util.extension.SlotExtension.isOrIsChildOf
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getCustomTypeAttributes
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import de.fuballer.mcendgame.main.util.minecraft.RegistryUtil
import de.maucon.mauconframework.command.CommandGateway
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemAttributeModifiers
import kotlin.math.max

@Injectable
object CustomAttributesExtensions {
    private val COMPONENT_TYPE: DataComponentType<List<CustomAttribute>> =
        RegistryUtil.registerDataComponentType(
            DataComponentType.builder<List<CustomAttribute>>()
                .persistent(CustomAttribute.CODEC.listOf())
                .build(),
            "custom_attributes"
        )

    //TODO #86 change how attributes slots are handled
    fun ItemStack.setCustomAttributes(
        customAttributes: List<CustomAttribute>,
        slot: EquipmentSlotGroup,
    ) {
        set(COMPONENT_TYPE, customAttributes)

        val attributeModifierComponent = getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY)

        val attributeComponentBuilder = ItemAttributeModifiers.builder()
        addNonModAttributes(attributeModifierComponent, attributeComponentBuilder)
        addVanillaTypeAttributes(customAttributes, attributeComponentBuilder, slot)

        set(DataComponents.ATTRIBUTE_MODIFIERS, attributeComponentBuilder.build())
    }

    /**
     * Automatically uses the slot of given attributes or defaults to [EquipmentSlotGroup.ANY] if empty
     */
    fun ItemStack.updateCustomAttributes(
        customAttributes: List<CustomAttribute>,
    ) {
        val slot = if (customAttributes.isEmpty()) EquipmentSlotGroup.ANY else customAttributes[0].slot
        return setCustomAttributes(customAttributes, slot)
    }

    fun ItemStack.getCustomAttributes(): List<CustomAttribute> {
        return get(COMPONENT_TYPE)
            ?: return emptyList()
    }

    fun LivingEntity.getAllCustomAttributes(): Map<CustomAttributeType, List<CustomAttribute>> {
        if (isSpectator) return mapOf()

        val customAttributes = mutableListOf<CustomAttribute>()
        customAttributes.addAll(getCustomAttributes())
        customAttributes.addAll(getCustomAttributesFromWorld())
        customAttributes.addAll(getCustomAttributesOfItems())

        val command = CollectCustomAttributesCommand(this, customAttributes)
        val cmd = CommandGateway.apply(command)

        return cmd.customAttributes
            .filter { it.type is CustomAttributeType }
            .groupBy { it.type as CustomAttributeType }
    }

    private fun LivingEntity.getCustomAttributesOfItems(): List<CustomAttribute> {
        val customAttributes = mutableListOf<CustomAttribute>()

        val feetItem = this.getItemBySlot(EquipmentSlot.FEET)
        val feetAttributes = feetItem.getCustomAttributes().filter { EquipmentSlotGroup.FEET.isOrIsChildOf(it.slot) }
        customAttributes.addAll(feetAttributes)
        val legsItem = this.getItemBySlot(EquipmentSlot.LEGS)
        val legsAttributes = legsItem.getCustomAttributes().filter { EquipmentSlotGroup.LEGS.isOrIsChildOf(it.slot) }
        customAttributes.addAll(legsAttributes)
        val chestItem = this.getItemBySlot(EquipmentSlot.CHEST)
        val chestAttributes = chestItem.getCustomAttributes().filter { EquipmentSlotGroup.CHEST.isOrIsChildOf(it.slot) }
        customAttributes.addAll(chestAttributes)
        val headItem = this.getItemBySlot(EquipmentSlot.HEAD)
        val headAttributes = headItem.getCustomAttributes().filter { EquipmentSlotGroup.HEAD.isOrIsChildOf(it.slot) }
        customAttributes.addAll(headAttributes)

        val mainHandItem = this.getItemBySlot(EquipmentSlot.MAINHAND)
        val mainHandAttributes = mainHandItem.getCustomAttributes().filter { EquipmentSlotGroup.MAINHAND.isOrIsChildOf(it.slot) }
        customAttributes.addAll(mainHandAttributes)
        val offHandItem = this.getItemBySlot(EquipmentSlot.OFFHAND)
        val offHandAttributes = offHandItem.getCustomAttributes().filter { EquipmentSlotGroup.OFFHAND.isOrIsChildOf(it.slot) }
        customAttributes.addAll(offHandAttributes)

        return customAttributes.filter { it.type is CustomAttributeType }
    }

    fun LivingEntity.isGhostly() = getAllCustomAttributes().contains(CustomAttributeTypes.GHOSTLY_APPEARANCE)
    fun LivingEntity.hasEntityPhasing() = getAllCustomAttributes().contains(CustomAttributeTypes.ENTITY_PHASING)
    fun LivingEntity.hasBlockPhasing() = getAllCustomAttributes().contains(CustomAttributeTypes.BLOCK_PHASING)

    fun AttributeRoll<*>.asDoubleRoll() = this as DoubleRoll
    fun AttributeRoll<*>.asStringRoll() = this as StringRoll
    fun AttributeRoll<*>.asIntRoll() = this as IntRoll
    fun AttributeBounds<*>.asDoubleBounds() = this as DoubleBounds
    fun AttributeBounds<*>.asStringBounds() = this as StringBounds
    fun AttributeBounds<*>.asIntBounds() = this as IntBounds

    private fun addNonModAttributes(attributeModifierComponent: ItemAttributeModifiers, builder: ItemAttributeModifiers.Builder) {
        for (modifier in attributeModifierComponent.modifiers) {
            if (modifier.modifier.id.namespace == MCEndgame.MOD_ID) continue
            builder.add(modifier.attribute, modifier.modifier, modifier.slot)
        }
    }

    private fun addVanillaTypeAttributes(
        customAttributes: List<CustomAttribute>,
        builder: ItemAttributeModifiers.Builder,
        slot: EquipmentSlotGroup
    ) {
        customAttributes
            .filter { it.type is VanillaAttributeType }
            .forEach {
                val vanillaAttributeType = it.type as VanillaAttributeType
                val attribute = vanillaAttributeType.attribute
                val modifier = AttributeModifier(IdentifierUtil.defaultCustomAttribute(it), it.rolls[0].asDoubleRoll().getValue(), vanillaAttributeType.scaleType)
                builder.add(attribute, modifier, slot)
            }
    }

    fun LivingEntity.getHealingFactor() = CommandGateway.apply(CollectHealFactorCommand(this)).getFactor()

    fun LivingEntity.getAdditionalArrowCount(): Int {
        val attributes = getAllCustomAttributes()[ADDITIONAL_ARROWS]
        if (attributes.isNullOrEmpty()) return 0
        val arrowCount = attributes.sumOf { it.rolls[0].asIntRoll().getValue() }
        return max(0, arrowCount)
    }

    fun LivingEntity.addCustomAttribute(customAttribute: CustomAttribute) {
        val accessor = this as LivingEntityCustomAttributesAccessor
        accessor.`mcendgame$addCustomAttribute`(customAttribute)

        val type = customAttribute.type
        if (type !is VanillaAttributeType) return
        val attributeInstance = getAttribute(type.attribute) ?: return
        val modifier = AttributeModifier(IdentifierUtil.defaultCustomAttribute(customAttribute), customAttribute.rolls[0].asDoubleRoll().getValue(), type.scaleType)
        attributeInstance.addPermanentModifier(modifier)
    }

    fun LivingEntity.addCustomAttributes(customAttributes: List<CustomAttribute>) {
        customAttributes.forEach { addCustomAttribute(it) }
    }

    fun LivingEntity.getCustomAttributes(): List<CustomAttribute> {
        val accessor = this as LivingEntityCustomAttributesAccessor
        return accessor.`mcendgame$getCustomAttributes`()
    }

    fun LivingEntity.getCustomAttributesFromWorld(): List<CustomAttribute> = (level() as? ServerLevel)?.getCustomTypeAttributes(this) ?: listOf()
}