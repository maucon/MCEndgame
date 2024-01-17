package de.fuballer.mcendgame.component.dungeon.enemy.base_loot

import de.fuballer.mcendgame.domain.item.*
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ItemUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent

@Component
class EnemyBaseLootService : Listener {
    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        if (WorldUtil.isNotDungeonWorld(event.entity.world)) return

        event.entity.world.dropItemNaturally(event.entity.location, ItemUtil.createCustomItem(TitansEmbraceItemType))
        event.entity.world.dropItemNaturally(event.entity.location, ItemUtil.createCustomItem(ArcheryAnnexItemType))
        event.entity.world.dropItemNaturally(event.entity.location, ItemUtil.createCustomItem(ShrinkshadowItemType))
        event.entity.world.dropItemNaturally(event.entity.location, ItemUtil.createCustomItem(GeistergaloschenItemType))
        event.entity.world.dropItemNaturally(event.entity.location, ItemUtil.createCustomItem(TwinfireItemType))

        event.drops.clear()
    }
}
