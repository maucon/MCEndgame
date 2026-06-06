package de.fuballer.mcendgame.main.component.block.blocks.dungeon_device

import de.fuballer.mcendgame.main.component.block.CustomBlockEntityTypes
import de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.networking.DungeonDevicePayload
import de.fuballer.mcendgame.main.functional.inventory.ImplementedInventory
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.getDungeonLevel
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider
import net.minecraft.core.BlockPos
import net.minecraft.core.NonNullList
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.ContainerHelper
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

private val TITLE = Component.translatable("container.mcendgame.dungeon_device.title")

class DungeonDeviceBlockEntity(
    blockPos: BlockPos,
    blockState: BlockState,
) : BlockEntity(CustomBlockEntityTypes.DUNGEON_DEVICE, blockPos, blockState), ExtendedMenuProvider<DungeonDevicePayload>, ImplementedInventory {
    private val inventory = NonNullList.withSize(DungeonDeviceSettings.INVENTORY_SIZE, ItemStack.EMPTY)

    override fun getItems(): NonNullList<ItemStack> = inventory

    override fun createMenu(syncId: Int, playerInventory: Inventory, player: Player) = DungeonDeviceScreenHandler(syncId, playerInventory, this)

    override fun getDisplayName(): Component = TITLE

    override fun getScreenOpeningData(player: ServerPlayer): DungeonDevicePayload {
        val playerDungeonLevel = player.getDungeonLevel()
        return DungeonDevicePayload(worldPosition, level!!.dimension(), player.uuid, playerDungeonLevel)
    }

    override fun setChanged() = super.markDirty(level, worldPosition)

    override fun loadAdditional(view: ValueInput) {
        super.loadAdditional(view)
        ContainerHelper.loadAllItems(view, this.inventory)
    }

    override fun saveAdditional(view: ValueOutput) {
        super.saveAdditional(view)
        ContainerHelper.saveAllItems(view, this.inventory)
    }
}