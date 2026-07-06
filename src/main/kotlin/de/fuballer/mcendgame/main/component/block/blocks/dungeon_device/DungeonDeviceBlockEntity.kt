package de.fuballer.mcendgame.main.component.block.blocks.dungeon_device

import de.fuballer.mcendgame.main.component.block.CustomBlockEntityTypes
import de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.networking.DungeonDevicePayload
import de.fuballer.mcendgame.main.functional.inventory.ImplementedInventory
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.getDungeonLevel
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos

private val TITLE = Text.translatable("container.mcendgame.dungeon_device.title")

class DungeonDeviceBlockEntity(
    blockPos: BlockPos,
    blockState: BlockState,
) : BlockEntity(CustomBlockEntityTypes.DUNGEON_DEVICE, blockPos, blockState), ExtendedScreenHandlerFactory<DungeonDevicePayload>, ImplementedInventory {
    private val inventory = DefaultedList.ofSize(DungeonDeviceSettings.INVENTORY_SIZE, ItemStack.EMPTY)

    override fun getItems(): DefaultedList<ItemStack> = inventory

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity) = DungeonDeviceScreenHandler(syncId, playerInventory, this)

    override fun getDisplayName(): Text = TITLE

    override fun getScreenOpeningData(player: ServerPlayerEntity): DungeonDevicePayload {
        val playerDungeonLevel = player.getDungeonLevel()
        return DungeonDevicePayload(pos, world!!.registryKey, player.uuid, playerDungeonLevel)
    }

    override fun markDirty() = super<ImplementedInventory>.markDirty(world, pos)

    override fun readNbt(nbt: NbtCompound?, registryLookup: RegistryWrapper.WrapperLookup?) {
        super.readNbt(nbt, registryLookup)
        Inventories.readNbt(nbt, this.inventory, registryLookup)
    }

    override fun writeNbt(nbt: NbtCompound?, registryLookup: RegistryWrapper.WrapperLookup?) {
        super.writeNbt(nbt, registryLookup)
        Inventories.writeNbt(nbt, this.inventory, registryLookup)
    }
}