package de.fuballer.mcendgame.component.crafting

import de.fuballer.mcendgame.util.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

object CraftingItemSettings {
    val BASE_ITEM = Material.PAPER
    val CRAFTING_ITEM_USAGE_DISCLAIMER = arrayOf(
        TextComponent.empty(),
        TextComponent.create("Can be used in combination with", NamedTextColor.GRAY).decorate(TextDecoration.ITALIC),
        TextComponent.create("compatible items using an anvil.", NamedTextColor.GRAY).decorate(TextDecoration.ITALIC),
    )
}