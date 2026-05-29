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
import net.minecraft.component.ComponentType
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import java.util.*
import kotlin.math.max

@Injectable
object CustomAttributesExtensions {
    private val COMPONENT_TYPE: ComponentType<List<CustomAttribute>> =
        RegistryUtil.registerDataComponentType(
            ComponentType.builder<List<CustomAttribute>>()
                .codec(CustomAttribute.CODEC.listOf())
                .build(),
            "custom_attributes"
        )

    //TODO #86 change how attributes slots are handled
    fun ItemStack.setCustomAttributes(
        customAttributes: List<CustomAttribute>,
        slot: AttributeModifierSlot,
    ) {
        set(COMPONENT_TYPE, customAttributes)

        val attributeModifierComponent = getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT)

        val attributeComponentBuilder = AttributeModifiersComponent.builder()
        addNonModAttributes(attributeModifierComponent, attributeComponentBuilder)
        addVanillaTypeAttributes(customAttributes, attributeComponentBuilder, slot)

        set(DataComponentTypes.ATTRIBUTE_MODIFIERS, attributeComponentBuilder.build())
    }

    /**
     * Automatically uses the slot of given attributes or defaults to [AttributeModifierSlot.ANY] if empty
     */
    fun ItemStack.updateCustomAttributes(
        customAttributes: List<CustomAttribute>,
    ) {
        val slot = if (customAttributes.isEmpty()) AttributeModifierSlot.ANY else customAttributes[0].slot
        return setCustomAttributes(customAttributes, slot)
    }

    fun ItemStack.getCustomAttributes(): List<CustomAttribute> {
        return get(COMPONENT_TYPE)
            ?: return emptyList()
    }

    fun ItemStack.rerollCustomAttributeIds() {
        val attributes = getCustomAttributes()
        if (attributes.isEmpty()) return

        updateCustomAttributes(attributes.map { it.copy(id = UUID.randomUUID()) })
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

        val feetItem = this.getEquippedStack(EquipmentSlot.FEET)
        val feetAttributes = feetItem.getCustomAttributes().filter { AttributeModifierSlot.FEET.isOrIsChildOf(it.slot) }
        customAttributes.addAll(feetAttributes)
        val legsItem = this.getEquippedStack(EquipmentSlot.LEGS)
        val legsAttributes = legsItem.getCustomAttributes().filter { AttributeModifierSlot.LEGS.isOrIsChildOf(it.slot) }
        customAttributes.addAll(legsAttributes)
        val chestItem = this.getEquippedStack(EquipmentSlot.CHEST)
        val chestAttributes = chestItem.getCustomAttributes().filter { AttributeModifierSlot.CHEST.isOrIsChildOf(it.slot) }
        customAttributes.addAll(chestAttributes)
        val headItem = this.getEquippedStack(EquipmentSlot.HEAD)
        val headAttributes = headItem.getCustomAttributes().filter { AttributeModifierSlot.HEAD.isOrIsChildOf(it.slot) }
        customAttributes.addAll(headAttributes)

        val mainHandItem = this.getEquippedStack(EquipmentSlot.MAINHAND)
        val mainHandAttributes = mainHandItem.getCustomAttributes().filter { AttributeModifierSlot.MAINHAND.isOrIsChildOf(it.slot) }
        customAttributes.addAll(mainHandAttributes)
        val offHandItem = this.getEquippedStack(EquipmentSlot.OFFHAND)
        val offHandAttributes = offHandItem.getCustomAttributes().filter { AttributeModifierSlot.OFFHAND.isOrIsChildOf(it.slot) }
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

    private fun addNonModAttributes(attributeModifierComponent: AttributeModifiersComponent, builder: AttributeModifiersComponent.Builder) {
        for (modifier in attributeModifierComponent.modifiers) {
            if (modifier.modifier.id.namespace == MCEndgame.MOD_ID) continue
            builder.add(modifier.attribute, modifier.modifier, modifier.slot)
        }
    }

    private fun addVanillaTypeAttributes(
        customAttributes: List<CustomAttribute>,
        builder: AttributeModifiersComponent.Builder,
        slot: AttributeModifierSlot
    ) {
        customAttributes
            .filter { it.type is VanillaAttributeType }
            .forEach {
                val vanillaAttributeType = it.type as VanillaAttributeType
                val attribute = vanillaAttributeType.attribute
                val modifier = EntityAttributeModifier(IdentifierUtil.defaultCustomAttribute(it), it.rolls[0].asDoubleRoll().getValue(), vanillaAttributeType.scaleType)
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
        val attributeInstance = getAttributeInstance(type.attribute) ?: return
        val modifier = EntityAttributeModifier(IdentifierUtil.defaultCustomAttribute(customAttribute), customAttribute.rolls[0].asDoubleRoll().getValue(), type.scaleType)
        attributeInstance.addPersistentModifier(modifier)
    }

    fun LivingEntity.addCustomAttributes(customAttributes: List<CustomAttribute>) {
        customAttributes.forEach { addCustomAttribute(it) }
    }

    fun LivingEntity.getCustomAttributes(): List<CustomAttribute> {
        val accessor = this as LivingEntityCustomAttributesAccessor
        return accessor.`mcendgame$getCustomAttributes`()
    }

    fun LivingEntity.getCustomAttributesFromWorld(): List<CustomAttribute> = (entityWorld as? ServerWorld)?.getCustomTypeAttributes(this) ?: listOf()
}