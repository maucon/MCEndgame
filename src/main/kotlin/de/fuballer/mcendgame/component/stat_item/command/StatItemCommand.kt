package de.fuballer.mcendgame.component.stat_item.command

//@Component
//class StatItemCommand : CommandHandler {
//    override fun initialize(plugin: JavaPlugin) = plugin.getCommand(StatItemSettings.COMMAND_NAME)!!.setExecutor(this)
//
//    override fun onCommand(
//        sender: CommandSender,
//        command: Command,
//        label: String,
//        args: Array<out String>
//    ): Boolean {
//        val commandExecutor = sender as? Player ?: return false
//        openEquipmentStatBook(commandExecutor)
//        return true
//    }
//
//    private fun openEquipmentStatBook(player: Player) {
//        val item = player.inventory.itemInMainHand
//        val itemType = item.type
//        if (itemType == Material.AIR) {
//            player.sendMessage(StatItemSettings.STAT_ITEM_COMMAND_NO_ITEM)
//            return
//        }
//        if (!StatItemSettings.MATERIAL_TO_EQUIPMENT.containsKey(itemType)) {
//            player.sendMessage(StatItemSettings.STAT_ITEM_COMMAND_NO_ATTRIBUTES)
//            return
//        }
//
//        val book = getBook(item, itemType)
//        player.openBook(book)
//    }
//
//    private fun getBook(
//        item: ItemStack,
//        itemType: Material
//    ): ItemStack {
//        val book = ItemStack(Material.WRITTEN_BOOK)
//        val bookMeta = book.itemMeta as BookMeta
//
//        bookMeta.author = StatItemSettings.STAT_ITEM_BOOK_AUTHOR
//        bookMeta.title = StatItemSettings.STAT_ITEM_BOOK_TITLE
//
//        val text = getText(item, itemType)
//        bookMeta.addPage(text)
//
//        book.itemMeta = bookMeta
//
//        return book
//    }
//
//    private fun getText(
//        item: ItemStack,
//        itemType: Material
//    ): String {
//        val itemDisplayName = getItemTypeDisplayName(itemType)
//        var text = "${StatItemSettings.STAT_ITEM_COMMAND_ITEM_TYPE_COLOR}$itemDisplayName\n\n"
//
//        val maxAttributes = getMaxAttributes(itemType)
//
//        val meta = item.itemMeta
//        if (meta == null) {
//            text = text.plus(getStatText(maxAttributes, mutableMapOf()))
//            return text
//        }
//        val presentAttributes = meta.attributeModifiers
//        if (presentAttributes == null || presentAttributes.isEmpty) {
//            text = text.plus(getStatText(maxAttributes, mutableMapOf()))
//            return text
//        }
//
//        val presentAttributeValues = getPresentAttributeValues(item, maxAttributes, presentAttributes)
//
//        text = text.plus(getStatText(maxAttributes, presentAttributeValues))
//        return text
//    }
//
//    private fun getItemTypeDisplayName(itemType: Material): String {
//        var displayName = ""
//
//        val words = itemType.name.split("_")
//        for (word in words) {
//            val lowerCaseWord = word.lowercase()
//            val uppercaseWord = lowerCaseWord[0].uppercase() + lowerCaseWord.substring(1)
//            displayName = displayName.plus("$uppercaseWord ")
//        }
//
//        return displayName.trim()
//    }
//
//    private fun getPresentAttributeValues(
//        item: ItemStack,
//        maxAttributes: List<ItemAttribute>,
//        presentAttributes: Multimap<Attribute, AttributeModifier>
//    ): Map<Attribute, Double> {
//        val presentAttributeValues: MutableMap<Attribute, Double> = mutableMapOf()
//
//        val baseAttributes = StatItemSettings.MATERIAL_TO_EQUIPMENT[item.type]!!.baseAttributes
//
//        for ((attribute, _) in maxAttributes) {
//            presentAttributeValues[attribute] = getPresentAttributeValue(attribute, baseAttributes, presentAttributes)
//        }
//
//        return presentAttributeValues
//    }
//
//    private fun getPresentAttributeValue(
//        attribute: Attribute,
//        baseAttributes: List<ItemAttribute>,
//        presentAttributes: Multimap<Attribute, AttributeModifier>
//    ): Double {
//        val baseValue = baseAttributes
//            .filter { it.attribute == attribute }
//            .map { it.value }
//            .firstOrNull()
//            ?: Double.MAX_VALUE
//
//        var presentValue = 0.0
//
//        if (!presentAttributes.containsKey(attribute)) return presentValue
//
//        var sortedOut = false
//        for (am in presentAttributes[attribute]) {
//            val amValue = am.amount
//            if (sortedOut) {
//                presentValue = amValue
//                break
//            }
//            if (amValue != AttributeUtil.getActualAttributeValue(attribute, baseValue)) {
//                presentValue = amValue
//                break
//            }
//            sortedOut = true
//        }
//
//        return presentValue
//    }
//
//    private fun getStatText(
//        maxAttributes: List<ItemAttribute>,
//        presentAttributeValues: Map<Attribute, Double>
//    ): String {
//        var text = ""
//
//        for ((attribute, value) in maxAttributes) {
//            val attributeString = "${StatItemSettings.STAT_ITEM_COMMAND_ATTRIBUTE_COLOR}${AttributeUtil.getAttributeAsString(attribute)}:"
//            text = text.plus("$attributeString\n")
//
//            val presentValue = presentAttributeValues[attribute] ?: 0.0
//            text = text.plus(
//                String.format(
//                    "   %s%.2f / %s (%.2f%%)",
//                    StatItemSettings.STAT_ITEM_COMMAND_VALUE_COLOR,
//                    AttributeUtil.getDisplayedValue(attribute, presentValue),
//                    AttributeUtil.getDisplayedValue(attribute, value),
//                    presentValue / value * 100
//                )
//            )
//            text = text.plus("\n")
//        }
//
//        return text
//    }
//
//    private fun getMaxAttributes(type: Material): List<ItemAttribute> {
//        val equipment = StatItemSettings.MATERIAL_TO_EQUIPMENT[type] ?: return listOf()
//        return listOf()
////        return equipment.rollableAttributes.map { it.option } TODO
//    }
//}
