package de.fuballer.mcendgame.main.messaging.dungeon

import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Mob

data class DungeonBossKilledMessageCommand(
    val aspects: Map<AspectItem, Int>,
    val boss: Mob,
    val final: Boolean,
    var message: Component,
    var shouldSend: Boolean = true,
)