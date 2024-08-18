package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.totem.totems.experience.ExperienceTotemType
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.getFirstAsDouble
import de.fuballer.mcendgame.util.extension.PlayerExtension.getHighestTotemTier
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerExpChangeEvent
import kotlin.random.Random

@Service
class IncreasedExperienceEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: PlayerExpChangeEvent) {
        val player = event.player
        if (!player.world.isDungeonWorld()) return

        val tier = player.getHighestTotemTier(ExperienceTotemType) ?: return

        val experienceIncrease = ExperienceTotemType.getAttributeRollsByTier(tier)
        val doubleAmount = event.amount * (1 + experienceIncrease.getFirstAsDouble())
        val modulo = doubleAmount % 1
        val finalAmount = doubleAmount.toInt() + if (Random.nextDouble() < modulo) 1 else 0

        event.amount = finalAmount
    }
}