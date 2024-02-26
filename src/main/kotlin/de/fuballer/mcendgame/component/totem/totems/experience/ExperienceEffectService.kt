package de.fuballer.mcendgame.component.totem.totems.experience

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.PlayerExtension.getHighestTotemTier
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerExpChangeEvent

@Component
class ExperienceEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: PlayerExpChangeEvent) {
        val player = event.player
        if (!player.world.isDungeonWorld()) return

        val tier = player.getHighestTotemTier(ExperienceTotemType) ?: return

        val (experienceIncrease) = ExperienceTotemType.getValues(tier)
        event.amount = (event.amount * (1 + experienceIncrease)).toInt()
    }
}