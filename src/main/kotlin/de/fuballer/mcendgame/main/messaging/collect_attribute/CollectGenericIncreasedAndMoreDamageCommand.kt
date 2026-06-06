package de.fuballer.mcendgame.main.messaging.collect_attribute

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttributeType
import de.maucon.mauconframework.command.CommandGateway
import net.minecraft.world.entity.LivingEntity

data class CollectGenericIncreasedAndMoreDamageCommand (
    val entity: LivingEntity,
    val attributes: Map<CustomAttributeType, List<CustomAttribute>> = entity.getAllCustomAttributes(),
    val increased: MutableList<Double> = mutableListOf(),
    val more: MutableList<Double> = mutableListOf(),
){
    init {
        val genericIncreasedCommand = CollectGenericIncreasedDamageCommand(entity, attributes)
        increased.addAll(CommandGateway.apply(genericIncreasedCommand).increased)

        val genericMoreCommand = CollectGenericMoreDamageCommand(entity, attributes)
        more.addAll(CommandGateway.apply(genericMoreCommand).more)
    }
}