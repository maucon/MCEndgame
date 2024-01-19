package de.fuballer.mcendgame.component.item.item_info

import de.fuballer.mcendgame.domain.attribute.RollableAttribute
import de.fuballer.mcendgame.domain.attribute.RolledAttribute
import de.fuballer.mcendgame.domain.equipment.Equipment
import de.fuballer.mcendgame.domain.persistent_data.TypeKeys
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.CommandHandler
import de.fuballer.mcendgame.util.ItemUtil
import de.fuballer.mcendgame.util.PersistentDataUtil
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import org.bukkit.plugin.java.JavaPlugin
import java.text.DecimalFormat

private val DECIMAL_FORMAT = DecimalFormat("#.##")

@Component
class ItemInfoCommand : CommandHandler {
    override fun initialize(plugin: JavaPlugin) = plugin.getCommand(ItemInfoSettings.COMMAND_NAME)!!.setExecutor(this)

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

        val itemMeta = item.itemMeta
        if (itemMeta == null) {
            player.sendMessage(ItemInfoSettings.NO_ATTRIBUTES)
            return
        }

        val attributes = PersistentDataUtil.getValue(itemMeta, TypeKeys.ATTRIBUTES)
        if (!Equipment.existsByMaterial(itemType) || attributes.isNullOrEmpty()) {
            player.sendMessage(ItemInfoSettings.NO_ATTRIBUTES)
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

        val attributeBounds = ItemUtil.getEquipmentAttributes(item)
        val presentAttributes = getPresentAttributes(item)

        return attributeBounds
            .mapNotNull { getAttributeTextIfPresent(it, presentAttributes) }
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

    private fun getPresentAttributes(item: ItemStack): List<RolledAttribute> {
        val itemMeta = item.itemMeta ?: return listOf()

        return PersistentDataUtil.getValue(itemMeta, TypeKeys.ATTRIBUTES)
            ?: return listOf()
    }

    private fun getAttributeTextIfPresent(
        attributeBound: RollableAttribute,
        presentAttributes: List<RolledAttribute>
    ): String? {
        val attribute = presentAttributes.firstOrNull { it.type == attributeBound.type } ?: return null
        val attributeRollString = getAttributeRollString(attributeBound, attribute.roll)

        var attributeLore = ItemUtil.getCorrectSignLore(attribute)
        if (attributeLore.firstOrNull() != null && attributeLore.first() == ' ') {
            attributeLore = attributeLore.replaceFirstChar { "" }
        }

        return "${ItemInfoSettings.ATTRIBUTE_COLOR}$attributeLore\n\n" +
                "${ItemInfoSettings.VALUE_COLOR}${attributeRollString}\n"
    }

    private fun getAttributeRollString(attributeBounds: RollableAttribute, roll: Double): String {
        if (attributeBounds.min - attributeBounds.max == 0.0) {
            return ItemInfoSettings.NOT_ROLLED_TEXT
        }

        return String.format(
            "Min: %s\nMax: %s\nRoll: %s\n%%Roll: %s%%",
            DECIMAL_FORMAT.format(attributeBounds.min),
            DECIMAL_FORMAT.format(attributeBounds.max),
            DECIMAL_FORMAT.format(roll),
            DECIMAL_FORMAT.format((roll - attributeBounds.min) / (attributeBounds.max - attributeBounds.min) * 100)
        )
    }
}
