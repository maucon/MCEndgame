package de.fuballer.mcendgame.component.item.item_info

import de.fuballer.mcendgame.component.item.attribute.AttributeUtil
import de.fuballer.mcendgame.component.item.attribute.data.CustomAttribute
import de.fuballer.mcendgame.component.item.attribute.data.RollType
import de.fuballer.mcendgame.component.item.attribute.data.SingleValueAttribute
import de.fuballer.mcendgame.component.item.equipment.Equipment
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.CommandHandler
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getCustomAttributes
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import java.text.DecimalFormat

private val DECIMAL_FORMAT = DecimalFormat("#.##")

@Component
class ItemInfoCommand : CommandHandler(ItemInfoSettings.COMMAND_NAME) {
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
            player.sendMessage(ItemInfoSettings.NO_ITEM)
            return
        }
        if (item.itemMeta == null) {
            player.sendMessage(ItemInfoSettings.INVALID_ITEM)
            return
        }

        val attributes = item.getCustomAttributes()
        if (!Equipment.existsByMaterial(itemType) || attributes.isNullOrEmpty()) {
            player.sendMessage(ItemInfoSettings.INVALID_ITEM)
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

        bookMeta.author = ItemInfoSettings.BOOK_AUTHOR
        bookMeta.title = ItemInfoSettings.BOOK_TITLE

        getAttributePages(item, itemType)
            .forEach { bookMeta.addPage(it) }

        book.itemMeta = bookMeta
        return book
    }

    private fun getAttributePages(item: ItemStack, itemType: Material): List<String> {
        val itemDisplayName = getItemTypeDisplayName(itemType)
        val header = "${ItemInfoSettings.ITEM_TYPE_COLOR}$itemDisplayName${ChatColor.RESET}"

        val attributes = item.getCustomAttributes() ?: listOf()

        return attributes
            .map { mapToAttributeText(it) }
            .map { "$header\n\n\n$it" }
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

    private fun mapToAttributeText(attribute: CustomAttribute): String {
        val attributeRollText = when (attribute.rollType) {
            RollType.STATIC -> ItemInfoSettings.NOT_ROLLED_TEXT
            RollType.SINGLE -> getAttributeRollText(attribute as SingleValueAttribute)
        }

        var attributeLore = AttributeUtil.getCorrectSignLore(attribute)
        if (attributeLore.firstOrNull() != null && attributeLore.first() == ' ') {
            attributeLore = attributeLore.replaceFirstChar { "" }
        }

        return "${ItemInfoSettings.ATTRIBUTE_COLOR}$attributeLore\n\n" +
                "${ItemInfoSettings.VALUE_COLOR}${attributeRollText}\n"
    }

    private fun getAttributeRollText(attribute: SingleValueAttribute): String {
        return String.format(
            "Min: %s\nMax: %s\nRoll: %s\n%%Roll: %s%%",
            DECIMAL_FORMAT.format(attribute.bounds.min),
            DECIMAL_FORMAT.format(attribute.bounds.max),
            DECIMAL_FORMAT.format(attribute.getAbsoluteRoll()),
            DECIMAL_FORMAT.format(attribute.percentRoll * 100)
        )
    }
}