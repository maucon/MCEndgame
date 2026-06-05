package de.fuballer.mcendgame.main.component.item_filter

import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerInput
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class ItemFilterScreenHandler(
    syncId: Int,
    private val playerInventory: Inventory,
    filterItems: Set<Item>,
    private val itemFilterService: ItemFilterService,
) : AbstractContainerMenu(MenuType.GENERIC_9x6, syncId) {
    private val filterInventory = SimpleContainer(9 * 6)

    init {
        filterInventory.startOpen(playerInventory.player)

        addFilterSlots()
        addFilterItems(filterItems)

        val topOffset = 18 + 6 * 18 + 13
        addStandardInventorySlots(playerInventory, 8, topOffset)
    }

    private fun addFilterSlots() {
        for (row in 0 until 6) {
            for (column in 0..8) {
                addSlot(Slot(filterInventory, column + row * 9, 8 + column * 18, 18 + row * 18))
            }
        }
    }

    private fun addFilterItems(
        filterItems: Set<Item>
    ) {
        for ((index, item) in filterItems.withIndex()) {
            if (index >= filterInventory.containerSize) break
            setItem(index, 0, ItemStack(item))
        }
    }

    override fun clicked(
        slotIndex: Int,
        button: Int,
        containerInput: ContainerInput,
        player: Player
    ) {
        offhandSwapLogic(slotIndex, button, containerInput)

        if (slotIndex < 0 || slotIndex >= slots.size) return
        if (containerInput != ContainerInput.PICKUP && containerInput != ContainerInput.PICKUP_ALL && containerInput != ContainerInput.QUICK_MOVE) return

        val slot = slots[slotIndex]
        val clickedStack = slot.item
        if (clickedStack.isEmpty) return

        if (slot.container == playerInventory) {
            addItemToFilter(clickedStack.item)
            return
        }

        if (slot.container == filterInventory) {
            filterInventory.setItem(slot.containerSlot, ItemStack.EMPTY)
        }
    }

    private fun offhandSwapLogic(
        slotIndex: Int,
        button: Int,
        containerInput: ContainerInput,
    ) {
        if (containerInput != ContainerInput.SWAP || button != 40) return

        val offhandItem = playerInventory.getItem(button)
        val clickedItem = slots[slotIndex].item

        if (offhandItem.isEmpty) {
            playerInventory.setItem(button, clickedItem)
            slots[slotIndex].setByPlayer(ItemStack.EMPTY)
        } else if (clickedItem.isEmpty) {
            playerInventory.setItem(button, ItemStack.EMPTY)
            slots[slotIndex].setByPlayer(offhandItem)
        } else {
            playerInventory.setItem(button, clickedItem)
            slots[slotIndex].setByPlayer(offhandItem)
        }
    }

    private fun addItemToFilter(item: Item) {
        if (containsItem(item)) return
        addToFilterInventory(item)
    }

    private fun containsItem(item: Item): Boolean {
        for (index in 0 until filterInventory.containerSize) {
            val stack = filterInventory.getItem(index)
            if (stack.`is`(item)) return true
        }
        return false
    }

    private fun addToFilterInventory(item: Item) {
        for (index in 0 until filterInventory.containerSize) {
            if (!filterInventory.getItem(index).isEmpty) continue
            filterInventory.setItem(index, ItemStack(item))
            return
        }
    }

    override fun removed(player: Player) {
        super.removed(player)
        itemFilterService.saveItemFilter(player, filterInventory)
    }

    override fun stillValid(player: Player) = filterInventory.stillValid(player)

    // only gets called in onSlotClick which is overridden
    override fun quickMoveStack(player: Player, slot: Int): ItemStack = ItemStack.EMPTY
}