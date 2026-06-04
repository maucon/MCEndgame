package de.fuballer.mcendgame.main.functional.inventory

import net.minecraft.core.BlockPos
import net.minecraft.core.NonNullList
import net.minecraft.world.Container
import net.minecraft.world.ContainerHelper
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/**
 * A simple `Inventory` implementation with only default methods + an item list getter.
 */
interface ImplementedInventory : Container {
    /**
     * Retrieves the item list of this inventory.
     * Must return the same instance every time it's called.
     */
    fun getItems(): NonNullList<ItemStack>

    /**
     * Returns the inventory size.
     */
    override fun getContainerSize() = getItems().size

    /**
     * Checks if the inventory is empty.
     * @return true if this inventory has only empty stacks, false otherwise.
     */
    override fun isEmpty() = getItems().all { it.isEmpty }

    /**
     * Retrieves the item in the slot.
     */
    override fun getItem(slot: Int) = getItems()[slot]

    /**
     * Removes items from an inventory slot.
     * @param slot  The slot to remove from.
     * @param count How many items to remove. If there are fewer items in the slot than what are requested,
     * takes all items in that slot.
     */
    override fun removeItem(slot: Int, count: Int): ItemStack {
        val result = ContainerHelper.removeItem(getItems(), slot, count)
        if (!result.isEmpty) {
            setChanged()
        }
        return result
    }

    /**
     * Removes all items from an inventory slot.
     * @param slot The slot to remove from.
     */
    override fun removeItemNoUpdate(slot: Int): ItemStack = ContainerHelper.takeItem(getItems(), slot)

    /**
     * Replaces the current stack in an inventory slot with the provided stack.
     * @param slot  The inventory slot of which to replace the itemstack.
     * @param stack The replacing itemstack. If the stack is too big for
     * this inventory ([net.minecraft.world.entity.player.Inventory.getMaxStackSize]),
     * it gets resized to this inventory's maximum amount.
     */
    override fun setItem(slot: Int, stack: ItemStack) {
        getItems()[slot] = stack
        if (stack.count > stack.maxStackSize) {
            stack.count = stack.maxStackSize
        }
    }

    /**
     * Marks the state as dirty.
     * Must be called after changes in the inventory, so that the game can properly save
     * the inventory contents and notify neighboring blocks of inventory changes.
     */
    override fun setChanged() {
        // Override if you want behavior.
    }

    fun markDirty(world: Level?, pos: BlockPos) {
        world?.blockEntityChanged(pos)
    }

    /**
     * Clears the inventory.
     */
    override fun clearContent() {
        getItems().clear()
    }

    /**
     * @return true if the player can use the inventory, false otherwise.
     */
    override fun stillValid(player: Player) = true

    companion object {
        /**
         * Creates an inventory from the item list.
         */
        fun of(items: NonNullList<ItemStack>): ImplementedInventory {
            return object : ImplementedInventory {
                override fun getItems(): NonNullList<ItemStack> = items
            }
        }

        /**
         * Creates a new inventory with the specified size.
         */
        fun ofSize(size: Int): ImplementedInventory {
            return of(NonNullList.withSize(size, ItemStack.EMPTY))
        }
    }
}