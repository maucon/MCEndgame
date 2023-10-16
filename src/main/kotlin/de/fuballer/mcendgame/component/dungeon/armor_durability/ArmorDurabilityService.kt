package de.fuballer.mcendgame.component.dungeon.armor_durability

import de.fuballer.mcendgame.framework.stereotype.Service
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.event.player.PlayerItemDamageEvent
import java.lang.Integer.min

class ArmorDurabilityService : Service {
    fun onPlayerItemDamage(event: PlayerItemDamageEvent) {
        if (WorldUtil.isNotDungeonWorld(event.player.world)) return

        event.damage = min(event.damage, ArmorDurabilitySettings.MAX_ARMOR_DAMAGE)
    }
}
