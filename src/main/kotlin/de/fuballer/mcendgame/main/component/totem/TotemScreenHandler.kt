package de.fuballer.mcendgame.main.component.totem

import de.fuballer.mcendgame.main.component.item.custom.totem.TotemType
import de.fuballer.mcendgame.main.component.screen.CustomScreenHandlerTypes
import de.fuballer.mcendgame.main.util.extension.EntityExtension.isInDungeonWorld
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ClickType
import net.minecraft.world.item.ItemStack

class TotemScreenHandler(
    syncId: Int,
    playerInventory: Inventory,
    totems: List<ItemStack> = listOf(),
    private val totemService: TotemService? = null,
) : AbstractContainerMenu(CustomScreenHandlerTypes.TOTEM, syncId) {
    private val totemInventory = SimpleContainer(8)

    init {
        addTotemSlots()
        fillTotemSlots(totems)

        addStandardInventorySlots(playerInventory, 8, 87)
    }

    private fun addTotemSlots() {
        for (i in 0..4) addSlot(TotemSlot(totemInventory, i, 26 + 27 * i, 56, TotemType.BASIC))
        addSlot(TotemSlot(totemInventory, 5, 48, 29, TotemType.EFFECT))
        addSlot(TotemSlot(totemInventory, 6, 112, 29, TotemType.EFFECT))
        addSlot(TotemSlot(totemInventory, 7, 80, 20, TotemType.ULTIMATE))
    }

    private fun fillTotemSlots(totems: List<ItemStack>) {
        for ((index, totem) in totems.withIndex()) {
            if (index >= totemInventory.containerSize) break
            setItem(index, 0, totem)
        }
    }

    override fun removed(player: Player) {
        super.removed(player)
        totemService?.savePlayerTotems(player, totemInventory)
    }

    override fun clicked(slotIndex: Int, button: Int, actionType: ClickType, player: Player) {
        if (player.isInDungeonWorld()) return
        super.clicked(slotIndex, button, actionType, player)
    }

    override fun quickMoveStack(player: Player, slotIndex: Int): ItemStack {
        val slot = slots[slotIndex]
        if (!slot.hasItem()) return ItemStack.EMPTY
        val stack = slot.item

        if (slotIndex < 8) {
            if (!moveItemStackTo(stack, 8, 44, true)) return ItemStack.EMPTY
            return stack
        } else {
            if (!moveItemStackTo(stack, 0, 8, false)) return ItemStack.EMPTY
            return stack
        }
    }

    override fun stillValid(player: Player) = totemInventory.stillValid(player)
}