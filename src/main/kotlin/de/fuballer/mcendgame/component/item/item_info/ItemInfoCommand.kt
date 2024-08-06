package de.fuballer.mcendgame.component.item.item_info

import de.fuballer.mcendgame.component.item.attribute.data.*
import de.fuballer.mcendgame.component.item.equipment.Equipment
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.command.CommandHandler
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.extract
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getCustomAttributes
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import java.text.DecimalFormat

private val DECIMAL_FORMAT = DecimalFormat("#.##")

@Service
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

        val book = getAttributesBook(item)
        player.openBook(book)
    }

    private fun getAttributesBook(item: ItemStack): ItemStack {
        val book = ItemStack(Material.WRITTEN_BOOK)
        val bookMeta = book.itemMeta as BookMeta

        bookMeta.author = ItemInfoSettings.BOOK_AUTHOR
        bookMeta.title = ItemInfoSettings.BOOK_TITLE

        getAttributePages(item)
            .forEach { bookMeta.addPage(it) }

        book.itemMeta = bookMeta
        return book
    }

    private fun getAttributePages(item: ItemStack): List<String> {
        val attributes = item.getCustomAttributes() ?: listOf()

        return attributes
            .map { mapAttributeToText(it) }
    }

    private fun mapAttributeToText(attribute: CustomAttribute): String {
        var attributeLore = attribute.getLore()
        if (attributeLore.firstOrNull() == ' ') {
            attributeLore = attributeLore.replaceFirstChar { "" }
        }

        val attributeRollsText = mapAttributeRollsToText(attribute.attributeRolls)

        return "${ItemInfoSettings.ATTRIBUTE_COLOR}$attributeLore\n\n" +
                "${ItemInfoSettings.VALUE_COLOR}${attributeRollsText}\n"
    }

    private fun mapAttributeRollsToText(attributeRolls: List<AttributeRoll<*>>) =
        attributeRolls.joinToString("\n\n") { it.extract(::mapDoubleRollToText, ::mapStringRollToText, ::mapIntRollToText) }

    private fun mapDoubleRollToText(roll: DoubleRoll) =
        String.format(
            "Min: %s\nMax: %s\nRoll: %s\n%%Roll: %s%%",
            DECIMAL_FORMAT.format(roll.bounds.min),
            DECIMAL_FORMAT.format(roll.bounds.max),
            DECIMAL_FORMAT.format(roll.getRoll()),
            DECIMAL_FORMAT.format(roll.percentRoll * 100)
        )

    private fun mapStringRollToText(roll: StringRoll) = "Options: ${roll.bounds.options}\nIndex: ${roll.indexRoll}\nValue: ${roll.getRoll()}"

    private fun mapIntRollToText(roll: IntRoll) =
        String.format(
            "Min: %s\nMax: %s\nRoll: %s\n%%Roll: %s%%",
            DECIMAL_FORMAT.format(roll.bounds.min),
            DECIMAL_FORMAT.format(roll.bounds.max),
            DECIMAL_FORMAT.format(roll.getRoll()),
            DECIMAL_FORMAT.format(roll.percentRoll * 100)
        )
}