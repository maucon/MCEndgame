package de.fuballer.mcendgame.main.component.block.blocks.crystalforge

import de.fuballer.mcendgame.main.component.block.blocks.crystalforge.slot.CrystalSlot
import de.fuballer.mcendgame.main.component.block.blocks.crystalforge.slot.ForgeableEquipmentSlot
import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItem
import de.fuballer.mcendgame.main.component.screen.CustomScreenHandlerTypes
import de.fuballer.mcendgame.main.messaging.crystals.CrystalForgeUsedEvent
import de.maucon.mauconframework.event.EventGateway
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

class CrystalForgeScreenHandler(
    syncId: Int,
    private val playerInventory: Inventory,
) : AbstractContainerMenu(CustomScreenHandlerTypes.CRYSTAL_FORGE, syncId) {
    private val inputInventory = SimpleContainer(2)
    private val outputInventory = SimpleContainer(1)

    init {
        addSlot(ForgeableEquipmentSlot(inputInventory, 0, 44, 20))
        addSlot(CrystalSlot(inputInventory, 1, 116, 20))

        //secondary output
        addSlot(object : Slot(outputInventory, 0, 80, 20) {
            override fun mayPlace(itemStack: ItemStack) = false

            override fun isHighlightable() = isSecondaryOutputSlotFilled()
        })

        // player inventory
        for (row in 0..2) {
            for (col in 0..8) {
                addSlot(Slot(playerInventory, 9 + row * 9 + col, 8 + col * 18, 84 + row * 18))
            }
        }

        // player hotbar
        for (hotbarSlot in 0..8) {
            addSlot(Slot(playerInventory, hotbarSlot, 8 + hotbarSlot * 18, 142))
        }
    }

    override fun quickMoveStack(player: Player, slotIndex: Int): ItemStack {
        val slot = slots[slotIndex]
        if (!slot.hasItem()) return ItemStack.EMPTY

        if (slotIndex >= inputInventory.containerSize + outputInventory.containerSize) return quickMoveToInputInventory(slot.item)
        return quickMoveToPlayerInventory(slot.item)
    }

    private fun quickMoveToInputInventory(
        itemStack: ItemStack,
    ) = if (moveItemStackTo(itemStack, 0, inputInventory.containerSize, false)) itemStack else ItemStack.EMPTY

    private fun quickMoveToPlayerInventory(
        itemStack: ItemStack,
    ) = if (moveItemStackTo(itemStack, inputInventory.containerSize + outputInventory.containerSize, slots.size, true)) itemStack else ItemStack.EMPTY

    override fun stillValid(player: Player) = true

    override fun removed(player: Player) {
        super.removed(player)
        clearContainer(player, inputInventory)
    }

    fun forge() {
        val toForgeStack = inputInventory.getItem(0)
        val crystalStack = inputInventory.getItem(1)
        val crystalItem = crystalStack.item as? CrystalItem ?: return

        if (crystalItem.canForge(toForgeStack, isSecondaryOutputSlotFilled()) != null) return

        val event = CrystalForgeUsedEvent(playerInventory.player, crystalStack.item)
        EventGateway.publish(event)

        val forgeOutput = crystalItem.forge(toForgeStack)
        crystalStack.shrink(1)
        inputInventory.setItem(0, forgeOutput.main)
        if (forgeOutput.secondary != null) outputInventory.setItem(0, forgeOutput.secondary)

        broadcastChanges()
    }

    private fun isSecondaryOutputSlotFilled() = !outputInventory.getItem(0).isEmpty
}