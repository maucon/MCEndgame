package de.fuballer.mcendgame.component.item_generation.command

import de.fuballer.mcendgame.component.item_generation.ItemGenerationSettings
import de.fuballer.mcendgame.domain.attribute.RollableAttribute
import de.fuballer.mcendgame.domain.attribute.RolledAttribute
import de.fuballer.mcendgame.domain.persistent_data.DataTypeKeys
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.CommandHandler
import de.fuballer.mcendgame.util.PersistentDataUtil
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import org.bukkit.plugin.java.JavaPlugin

@Component
class StatItemCommand : CommandHandler {
    override fun initialize(plugin: JavaPlugin) = plugin.getCommand(ItemGenerationSettings.COMMAND_NAME)!!.setExecutor(this)

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        val commandExecutor = sender as? Player ?: return false
        openAttributesBook(commandExecutor)
        return true
    }

    private fun openAttributesBook(player: Player) {
        val item = player.inventory.itemInMainHand
        val itemType = item.type

        if (itemType == Material.AIR) {
            player.sendMessage(ItemGenerationSettings.COMMAND_NO_ITEM)
            return
        }

        if (!ItemGenerationSettings.MATERIAL_TO_EQUIPMENT.containsKey(itemType)) {
            player.sendMessage(ItemGenerationSettings.COMMAND_NO_ATTRIBUTES)
            return
        }

        val book = getAttributesBook(item, itemType)
        player.openBook(book)
    }

    private fun getAttributesBook(
        item: ItemStack,
        itemType: Material
    ): ItemStack {
        val book = ItemStack(Material.WRITTEN_BOOK)
        val bookMeta = book.itemMeta as BookMeta

        bookMeta.author = ItemGenerationSettings.STAT_ITEM_BOOK_AUTHOR
        bookMeta.title = ItemGenerationSettings.STAT_ITEM_BOOK_TITLE

        getAttributePages(item, itemType)
            .forEach { bookMeta.addPage(it) }

        book.itemMeta = bookMeta
        return book
    }

    private fun getAttributePages(
        item: ItemStack,
        itemType: Material
    ): List<String> {
        val itemDisplayName = getItemTypeDisplayName(itemType)
        val header = "${ItemGenerationSettings.COMMAND_ITEM_TYPE_COLOR}$itemDisplayName${ChatColor.RESET}"

        val attributeBounds = getAttributesBounds(itemType)
        val presentAttributes = getPresentAttributes(item)

        return attributeBounds.map {
            "$header\n\n${getAttributeText(it, presentAttributes)}"
        }
    }

    private fun getItemTypeDisplayName(itemType: Material): String {
        var displayName = ""

        val words = itemType.name.split("_")
        for (word in words) {
            val lowerCaseWord = word.lowercase()
            val uppercaseWord = lowerCaseWord[0].uppercase() + lowerCaseWord.substring(1)
            displayName = displayName.plus("$uppercaseWord ")
        }

        return displayName.trim()
    }

    private fun getAttributesBounds(type: Material): List<RollableAttribute> {
        val equipment = ItemGenerationSettings.MATERIAL_TO_EQUIPMENT[type] ?: return listOf()
        return equipment.rollableAttributes.map { it.option }
    }

    private fun getPresentAttributes(item: ItemStack): List<RolledAttribute> {
        val itemMeta = item.itemMeta ?: return listOf()

        return PersistentDataUtil.getValue(itemMeta, DataTypeKeys.ATTRIBUTES)
            ?: return listOf()
    }

    private fun getAttributeText(
        attributeBound: RollableAttribute,
        presentAttributes: List<RolledAttribute>
    ): String {
        val attribute = presentAttributes.firstOrNull { it.type == attributeBound.type }
        val attributeRollString = if (attribute == null) ItemGenerationSettings.COMMAND_ATTRIBUTE_NOT_PRESENT else getAttributeRollString(attributeBound, attribute.roll)

        return "${ItemGenerationSettings.COMMAND_ATTRIBUTE_COLOR}${attributeBound.type}:\n\n" +
                "${ItemGenerationSettings.COMMAND_VALUE_COLOR}${attributeRollString}\n"
    }

    private fun getAttributeRollString(attributeBounds: RollableAttribute, roll: Double) =
        String.format(
            "Min: %.2f\nMax: %.2f\nRoll: %.2f\nPercent Roll: %.1f%%",
            attributeBounds.min,
            attributeBounds.max,
            roll,
            (roll - attributeBounds.min) / (attributeBounds.max - attributeBounds.min) * 100
        )
}
