package de.fuballer.mcendgame.main.component.migration

import com.mojang.logging.LogUtils
import de.fuballer.mcendgame.main.component.data_component_type.CustomDataComponentType
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItems
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemItem
import de.fuballer.mcendgame.main.component.migration.legacy.LegacyItems
import de.fuballer.mcendgame.main.messaging.misc.PlayerJoinEvent
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.contents.TranslatableContents
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

/**
 * Handles one-time data migrations for persisted game objects.

 */
@Injectable
object MigrationService {
    private val log = LogUtils.getLogger()

    /**
     * Migrates a legacy totem [ItemStack] that predates the [CustomDataComponentType.TOTEM_TIER]
     * data component by deriving its tier from the translatable lore line that was previously
     * used to display it, then writing that value into the new component.
     */
    fun migrateTotemTier(totem: ItemStack, player: Player) {
        if (totem.has(CustomDataComponentType.TOTEM_TIER)) return
        val lore = totem.get(DataComponents.LORE) ?: return

        val tier = lore.lines()
            .firstNotNullOfOrNull { line ->
                val content = line.contents
                if (content is TranslatableContents && content.key == TotemItem.TIER_KEY) {
                    content.args.getOrNull(0)?.toString()?.toIntOrNull()
                } else null
            }

        if (tier == null) {
            log.warn(
                "Could not migrate tier for totem ${totem.item} of player ${player.gameProfile.name}: no valid tier found in lore. " +
                        "This totem should be removed and replaced using /givetotem (Moderator)."
            )
            return
        }

        totem.set(CustomDataComponentType.TOTEM_TIER, tier)
        log.info("Migrated totem ${totem.item} to tier $tier of player ${player.gameProfile.name}")
    }

    /**
     * Returns the stack that replaces a removed item, or null if the stack does not need to be migrated.
     */
    private fun getLegacyReplacement(stack: ItemStack): ItemStack? =
        when (stack.item) {
            LegacyItems.ASPECT_OF_THE_GROVE -> AspectItems.VERDANT_SIGIL.defaultInstance.copyWithCount(stack.count)
            else -> null
        }

    @EventSubscriber(sync = true)
    fun on(event: PlayerJoinEvent) = migrateLegacyItems(event.player)

    /**
     * Replaces legacy items in the player's inventory, equipment and ender chest.
     */
    fun migrateLegacyItems(player: Player) {
        var changed = false
        val inventory = player.inventory

        inventory.nonEquipmentItems.forEachIndexed { slot, stack ->
            val replacement = getLegacyReplacement(stack) ?: return@forEachIndexed
            inventory.setItem(slot, replacement)
            logMigrated(player, stack)
            changed = true
        }

        EquipmentSlot.entries.forEach { slot ->
            val stack = player.getItemBySlot(slot)
            val replacement = getLegacyReplacement(stack) ?: return@forEach
            player.setItemSlot(slot, replacement)
            logMigrated(player, stack)
            changed = true
        }

        val enderChest = player.enderChestInventory
        enderChest.items.forEachIndexed { slot, stack ->
            val replacement = getLegacyReplacement(stack) ?: return@forEachIndexed
            enderChest.setItem(slot, replacement)
            logMigrated(player, stack)
            changed = true
        }

        // Inventory contents changed without a player interaction, so the client has to be told about it.
        if (changed) syncInventory(player)
    }


    /**
     * Replaces a single legacy stack in the slot it currently occupies.
     */
    fun convertLegacyItem(entity: Entity, stack: ItemStack, slot: EquipmentSlot?) {
        val player = entity as? Player ?: return
        val replacement = getLegacyReplacement(stack) ?: return
        val inventory = player.inventory
        val index = inventory.nonEquipmentItems.indexOfFirst { it === stack }

        if (index != -1) {
            inventory.setItem(index, replacement)
        } else if (slot != null && player.getItemBySlot(slot) === stack) {
            player.setItemSlot(slot, replacement)
        } else {
            return
        }

        syncInventory(player)
        logMigrated(player, stack)
    }

    /** Updates the client copy of the player's inventory, which is shown either by the open menu or by the inventory menu. */
    private fun syncInventory(player: Player) {
        player.inventoryMenu.broadcastChanges()
        val openMenu = player.containerMenu
        if (openMenu !== player.inventoryMenu) openMenu.broadcastChanges()
    }

    private fun logMigrated(player: Player, stack: ItemStack) {
        log.info("Migrated legacy item ${stack.item} x${stack.count} of player ${player.gameProfile.name} to its replacement")
    }
}