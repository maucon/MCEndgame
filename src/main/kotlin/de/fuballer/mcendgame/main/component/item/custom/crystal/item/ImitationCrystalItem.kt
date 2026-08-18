package de.fuballer.mcendgame.main.component.item.custom.crystal.item

import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItem
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.ItemStack
import java.awt.Color

class ImitationCrystalItem(
    settings: Properties
) : CrystalItem(settings) {
    override val forgeColor = Color(95, 141, 50)

    override val description: MutableComponent = Component.translatable(DESCRIPTION_BASE_KEY + "imitation")

    override fun producesSecondaryOutput() = true

    override fun forge(stack: ItemStack): CrystalForgeOutput {
        return CrystalForgeOutput(stack, stack.copy())
    }
}