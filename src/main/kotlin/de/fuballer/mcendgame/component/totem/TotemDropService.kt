package de.fuballer.mcendgame.component.totem

import de.fuballer.mcendgame.component.totem.data.Totem
import de.fuballer.mcendgame.event.DungeonEntityDeathEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.EntityExtension.getMapTier
import de.fuballer.mcendgame.util.extension.EntityExtension.isEnemy
import de.fuballer.mcendgame.util.extension.EntityExtension.isMinion
import de.fuballer.mcendgame.util.random.RandomUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import kotlin.random.Random

@Component
class TotemDropService : Listener {
    @EventHandler
    fun on(event: DungeonEntityDeathEvent) {
        val entity = event.entity

        if (!entity.isEnemy()) return
        if (entity.isMinion()) return

        if (Random.nextDouble() > TotemSettings.TOTEM_DROP_CHANCE) return

        val mapTier = entity.getMapTier() ?: 1
        val type = RandomUtil.pick(TotemSettings.TOTEM_TYPES).option
        val tier = RandomUtil.pick(TotemSettings.TOTEM_TIERS, mapTier).option

        val totem = Totem(type, tier)
        val totemItem = totem.toItem()

        entity.world.dropItemNaturally(entity.location, totemItem)
    }
}