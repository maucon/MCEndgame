package de.fuballer.mcendgame.main.component.item_filter

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.Slot
import net.minecraft.screen.slot.SlotActionType

class ItemFilterScreenHandler(
    syncId: Int,
    private val playerInventory: PlayerInventory,
    filterItems: Set<Item>,
    private val itemFilterService: ItemFilterService,
) : ScreenHandler(ScreenHandlerType.GENERIC_9X6, syncId) {
    private val filterInventory = SimpleInventory(9 * 6)

    init {
        filterInventory.onOpen(playerInventory.player)

        addFilterSlots()
        addFilterItems(filterItems)

        val topOffset = 18 + 6 * 18 + 13

        //addPlayerSlots(playerInventory, 8, topOffset)

        // Player inventory
        for (row in 0 until 3) {
            for (column in 0 until 9) {
                addSlot(
                    Slot(
                        playerInventory,
                        column + row * 9 + 9,
                        8 + column * 18,
                        topOffset + row * 18
                    )
                )
            }
        }

        // Hotbar
        for (column in 0 until 9) {
            addSlot(
                Slot(
                    playerInventory,
                    column,
                    8 + column * 18,
                    topOffset + 58
                )
            )
        }
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
            if (index >= filterInventory.size()) break
            setStackInSlot(index, 0, ItemStack(item))
        }
    }

    override fun onSlotClick(
        slotIndex: Int,
        button: Int,
        actionType: SlotActionType,
        player: PlayerEntity
    ) {
        offhandSwapLogic(slotIndex, button, actionType)

        if (slotIndex < 0 || slotIndex >= slots.size) return
        if (actionType != SlotActionType.PICKUP && actionType != SlotActionType.PICKUP_ALL && actionType != SlotActionType.QUICK_MOVE) return

        val slot = slots[slotIndex]
        val clickedStack = slot.stack
        if (clickedStack.isEmpty) return

        if (slot.inventory == playerInventory) {
            addItemToFilter(clickedStack.item)
            return
        }

        if (slot.inventory == filterInventory) {
            filterInventory.setStack(slot.index, ItemStack.EMPTY)
        }
    }

    private fun offhandSwapLogic(
        slotIndex: Int,
        button: Int,
        actionType: SlotActionType,
    ) {
        if (actionType != SlotActionType.SWAP || button != 40) return

        val offhandItem = playerInventory.getStack(button)
        val clickedItem = slots[slotIndex].stack

        if (offhandItem.isEmpty) {
            playerInventory.setStack(button, clickedItem)
            slots[slotIndex].stack = ItemStack.EMPTY
        } else if (clickedItem.isEmpty) {
            playerInventory.setStack(button, ItemStack.EMPTY)
            slots[slotIndex].stack = offhandItem
        } else {
            playerInventory.setStack(button, clickedItem)
            slots[slotIndex].stack = offhandItem
        }
    }

    private fun addItemToFilter(item: Item) {
        if (containsItem(item)) return
        addToFilterInventory(item)
    }

    private fun containsItem(item: Item): Boolean {
        for (index in 0 until filterInventory.size()) {
            val stack = filterInventory.getStack(index)
            if (stack.isOf(item)) return true
        }
        return false
    }

    private fun addToFilterInventory(item: Item) {
        for (index in 0 until filterInventory.size()) {
            if (!filterInventory.getStack(index).isEmpty) continue
            filterInventory.setStack(index, ItemStack(item))
            return
        }
    }

    override fun onClosed(player: PlayerEntity) {
        super.onClosed(player)
        itemFilterService.saveItemFilter(player, filterInventory)
    }

    override fun canUse(player: PlayerEntity) = filterInventory.canPlayerUse(player)

    // only gets called in onSlotClick which is overridden
    override fun quickMove(player: PlayerEntity, slot: Int): ItemStack = ItemStack.EMPTY
}