package de.fuballer.mcendgame.main.messaging.dungeon

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.world.WorldAttributeAction
import net.minecraft.server.level.ServerLevel

data class WorldAttributeChangedEvent(
    val world: ServerLevel,
    val attribute: CustomAttribute,
    val action: WorldAttributeAction,
)