package de.fuballer.mcendgame.main.component.item.custom.aspect

import de.fuballer.mcendgame.main.component.dungeon.loot.drop.ItemColor
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemLore

abstract class AspectItem(
    settings: Properties,
) : Item(settings) {
    companion object {
        const val TRANSLATABLE_BASE_KEY = "item.mcendgame.aspect."
        const val TRANSLATABLE_DESCRIPTION_KEY = TRANSLATABLE_BASE_KEY + "description."
    }

    abstract val tier: Int
    abstract val limit: Int

    abstract val description: List<MutableComponent>

    abstract val disabledAspects: List<AspectItem>

    override fun getDefaultInstance(): ItemStack {
        val stack = super.defaultInstance

        val list = mutableListOf<Component>()
        description.forEach {
            list.add(it.withStyle { style -> style.withItalic(false).withColor(ChatFormatting.GRAY) })
        }
        list.add(Component.translatable(TRANSLATABLE_BASE_KEY + "limit", limit).withStyle { style -> style.withItalic(false).withColor(ChatFormatting.DARK_GRAY) })

        stack.set(DataComponents.LORE, ItemLore(list))

        return stack
    }

    override fun getName(stack: ItemStack): MutableComponent = super.getName(stack).copy().withColor(ItemColor.ASPECT.intColor)
}