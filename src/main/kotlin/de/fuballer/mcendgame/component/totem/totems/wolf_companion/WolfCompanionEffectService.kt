package de.fuballer.mcendgame.component.totem.totems.wolf_companion

import de.fuballer.mcendgame.event.PlayerDungeonJoinEvent
import de.fuballer.mcendgame.event.PlayerDungeonLeaveEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.EntityExtension.isEnemy
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import de.fuballer.mcendgame.util.extension.PlayerExtension.getHighestTotemTier
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.DyeColor
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Wolf
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

@Component
class WolfCompanionEffectService : Listener {
    @EventHandler
    fun on(event: PlayerDungeonJoinEvent) {
        val player = event.player
        val tier = player.getHighestTotemTier(WolfCompanionTotemType) ?: return

        val (count, strength) = WolfCompanionTotemType.getValues(tier)
        val realStrength = strength - 1

        for (i in 1..count.toInt()) {
            val targetWorld = event.locationToTeleport.world!!
            val wolf = targetWorld.spawnEntity(event.locationToTeleport, EntityType.WOLF, false) as Wolf
            wolf.owner = player

            val potionEffect = PotionEffect(PotionEffectType.INCREASE_DAMAGE, Int.MAX_VALUE, realStrength.toInt(), false, false)
            wolf.addPotionEffect(potionEffect)

            wolf.isInvulnerable = true
            wolf.collarColor = DyeColor.entries.toTypedArray().random()
        }
    }

    @EventHandler
    fun on(event: PlayerDungeonLeaveEvent) {
        val player = event.player

        player.world.getEntitiesByClass(Wolf::class.java)
            .filter { it.owner == player }
            .forEach { it.remove() }
    }

    @EventHandler
    fun on(event: EntityTargetEvent) {
        val entity = event.entity

        if (!entity.world.isDungeonWorld()) return

        if (entity.isEnemy()) return
        if (entity !is Wolf) return

        if (event.target !is Player) return

        event.cancel()
    }
}