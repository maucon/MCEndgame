package de.fuballer.mcendgame.main.messaging.collect_attribute

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttributeType
import de.maucon.mauconframework.command.CommandGateway
import net.minecraft.world.entity.LivingEntity

data class CollectElementalDamageCommand(
    override val entity: LivingEntity,
    override val attributes: Map<CustomAttributeType, List<CustomAttribute>> = entity.getAllCustomAttributes(),
    override val flat: MutableList<Double> = mutableListOf(),
    override val increased: MutableList<Double> = mutableListOf(),
    override val more: MutableList<Double> = mutableListOf(),
) : CollectAttributeStatCommand {
    init {
        val genericDamageCommand = CollectGenericIncreasedAndMoreDamageCommand(entity, attributes)
        val cmd = CommandGateway.apply(genericDamageCommand)
        increased.addAll(cmd.increased)
        more.addAll(cmd.more)
    }
}