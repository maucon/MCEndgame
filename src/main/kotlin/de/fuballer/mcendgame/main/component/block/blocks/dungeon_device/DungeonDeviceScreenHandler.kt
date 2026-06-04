package de.fuballer.mcendgame.main.component.block.blocks.dungeon_device

import de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.networking.DungeonDevicePayload
import de.fuballer.mcendgame.main.component.screen.CustomScreenHandlerTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

class DungeonDeviceScreenHandler(
    syncId: Int,
    playerInventory: Inventory,
    private val inventory: Container = SimpleContainer(DungeonDeviceSettings.INVENTORY_SIZE),
    val payload: DungeonDevicePayload = DungeonDevicePayload.EMPTY
) : AbstractContainerMenu(CustomScreenHandlerTypes.DUNGEON_DEVICE, syncId) {
    init {
        checkContainerSize(inventory, DungeonDeviceSettings.INVENTORY_SIZE)
        inventory.startOpen(playerInventory.player)

        // Our inventory
        for (row in 0..1) {
            for (col in 0..1) {
                this.addSlot(Slot(inventory, col + row * 2, 71 + col * 18, 26 + row * 18))
            }
        }

        // The player inventory
        for (row in 0..2) {
            for (col in 0..8) {
                this.addSlot(Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18))
            }
        }

        // The player hotbar
        for (row in 0..8) {
            this.addSlot(Slot(playerInventory, row, 8 + row * 18, 142))
        }
    }

    override fun quickMoveStack(player: Player, slotIndex: Int): ItemStack {
        val slot = slots[slotIndex]
        if (!slot.hasItem()) return ItemStack.EMPTY

        val originalStack = slot.item
        val newStack = originalStack.copy()
        if (slotIndex < inventory.containerSize) {
            val itemInserted = !this.moveItemStackTo(originalStack, inventory.containerSize, slots.size, true)
            if (itemInserted) return ItemStack.EMPTY
        } else if (!this.moveItemStackTo(originalStack, 0, inventory.containerSize, false)) {
            return ItemStack.EMPTY
        }

        if (originalStack.isEmpty) {
            slot.setByPlayer(ItemStack.EMPTY)
        } else {
            slot.setChanged()
        }

        return newStack
    }

    override fun stillValid(player: Player) = inventory.stillValid(player)
}