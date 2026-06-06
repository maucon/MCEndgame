package de.fuballer.mcendgame.main.component.item_filter

import de.fuballer.mcendgame.main.component.item_filter.db.ItemFilterEntity
import de.fuballer.mcendgame.main.component.item_filter.db.ItemFilterRepository
import de.fuballer.mcendgame.main.util.extension.WorldExtension.isDungeonWorld
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.network.chat.Component
import net.minecraft.world.Container
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import java.util.*

@Injectable
class ItemFilterService(
    private val itemFilterRepo: ItemFilterRepository,
) {
    @CommandHandler
    fun on(cmd: PlayerItemPickupCommand) {
        val player = cmd.player
        if (!player.level().isDungeonWorld()) return

        val uuid = player.uuid
        val filter = itemFilterRepo.findById(uuid)?.items ?: return
        if (filter.contains(cmd.item)) cmd.cancel()
    }

    fun openFilterInventory(player: Player) {
        val filter = getFilterOrCreate(player.uuid)

        val screenHandlerFactory = SimpleMenuProvider({ syncId, inventory, _ ->
            ItemFilterScreenHandler(syncId, inventory, filter, this)
        }, Component.translatable("container.mcendgame.filter.title"))

        player.openMenu(screenHandlerFactory)
    }

    private fun getFilterOrCreate(uuid: UUID): Set<Item> {
        val entity = itemFilterRepo.findById(uuid)
            ?: return setOf()

        return entity.items
    }

    fun saveItemFilter(player: Player, inventory: Container) {
        val newFilter = mutableSetOf<Item>()

        for (i in 0 until inventory.containerSize) {
            val stack = inventory.getItem(i)
            if (stack.isEmpty) continue

            newFilter.add(stack.item)
        }

        val entity = ItemFilterEntity(player.uuid, newFilter)
        itemFilterRepo.save(entity)
    }
}