package de.fuballer.mcendgame.main.messaging.misc

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import net.minecraft.network.chat.Component

data class GetCustomAttributesTextsCommand(
    val attributes: List<CustomAttribute>,
    val detailed: Boolean = false,
    var texts: List<Component> = listOf(),
){
    constructor(
        attribute: CustomAttribute,
        detailed: Boolean = false,
    ) : this(listOf(attribute), detailed)
}