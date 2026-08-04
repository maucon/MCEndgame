package de.fuballer.mcendgame.main.component.item.custom.crystal.item

import de.fuballer.mcendgame.main.component.corruption.CorruptionService
import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItem
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.ItemStack
import java.awt.Color

class CorruptionCrystalItem(
    settings: Properties,
) : CrystalItem(settings) {
    override val forgeColor = Color(157, 0, 0)

    override val description: MutableComponent = Component.translatable(DESCRIPTION_BASE_KEY + "corruption")

    override fun forge(stack: ItemStack) = CrystalForgeOutput(CorruptionService.corrupt(stack))
}