package de.fuballer.mcendgame.main.component.item.custom.crystal.item

import de.fuballer.mcendgame.main.component.block.blocks.crystalforge.CrystalForgeSettings
import de.fuballer.mcendgame.main.component.dungeon.enemy.equipment.EquipmentGenerationSettings
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItemInterface
import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItem
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import java.awt.Color

class ReforgeCrystalItem(
    settings: Properties,
) : CrystalItem(settings) {
    override val forgeColor = Color(232, 40, 160)

    override val description: MutableComponent = Component.translatable(DESCRIPTION_BASE_KEY + "reforge")

    override fun canForge(stack: ItemStack): MutableComponent? {
        val cannotForgeReason = super.canForge(stack)
        if (cannotForgeReason != null) return cannotForgeReason

        if (stack.item !is UniqueAttributesItemInterface) return CrystalForgeSettings.getForgeErrorText("can_only_forge_unique")

        return null
    }

    override fun forge(stack: ItemStack): ItemStack {
        var uniqueItem: Item
        do {
            uniqueItem = EquipmentGenerationSettings.getRandomUniqueEquipment(tagsExactMatch = false)!!.item
        } while (uniqueItem == stack.item)

        val uniqueInterface = uniqueItem as UniqueAttributesItemInterface
        return uniqueInterface.getRolledStack(uniqueItem)
    }
}