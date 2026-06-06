package de.fuballer.mcendgame.main.messaging.collect_attribute

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttributeType
import net.minecraft.world.entity.LivingEntity

data class CollectGenericIncreasedDamageCommand(
    val entity: LivingEntity,
    val attributes: Map<CustomAttributeType, List<CustomAttribute>> = entity.getAllCustomAttributes(),
    val increased: MutableList<Double> = mutableListOf(),
)