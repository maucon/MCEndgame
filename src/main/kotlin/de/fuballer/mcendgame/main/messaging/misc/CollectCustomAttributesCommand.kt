package de.fuballer.mcendgame.main.messaging.misc

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import net.minecraft.world.entity.LivingEntity

data class CollectCustomAttributesCommand(
    val entity: LivingEntity,
    val customAttributes: MutableList<CustomAttribute>,
)