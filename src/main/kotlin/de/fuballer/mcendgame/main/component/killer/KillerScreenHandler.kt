package de.fuballer.mcendgame.main.component.killer

import de.fuballer.mcendgame.main.component.inventory.EmptySpriteSlot
import de.fuballer.mcendgame.main.component.killer.db.KillerEntity
import de.fuballer.mcendgame.main.component.killer.networking.KillerEntityPayload
import de.fuballer.mcendgame.main.component.screen.CustomScreenHandlerTypes
import net.minecraft.resources.Identifier
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerInput
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.item.ItemStack

private val SLOT_SPRITES = listOf(
    InventoryMenu.EMPTY_ARMOR_SLOT_HELMET,
    InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE,
    InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS,
    InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS,
    Identifier.withDefaultNamespace("container/slot/sword"),
    InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD,
)

private val EQUIPMENT_SLOTS = mapOf(
    EquipmentSlot.HEAD to 0,
    EquipmentSlot.CHEST to 1,
    EquipmentSlot.LEGS to 2,
    EquipmentSlot.FEET to 3,
    EquipmentSlot.MAINHAND to 4,
    EquipmentSlot.OFFHAND to 5,
)

class KillerScreenHandler(
    syncId: Int,
    payload: KillerEntityPayload,
) : AbstractContainerMenu(CustomScreenHandlerTypes.KILLER, syncId) {
    var killerEntity: KillerEntity
    private val killerInventory = SimpleContainer(6)

    init {
        for (armorSlot in 0..3) {
            addSlot(EmptySpriteSlot(killerInventory, armorSlot, 8, 18 + armorSlot * 18, SLOT_SPRITES[armorSlot]))
        }
        for (weaponSlot in 0..1) {
            addSlot(EmptySpriteSlot(killerInventory, weaponSlot + 4, 8, 94 + weaponSlot * 18, SLOT_SPRITES[4 + weaponSlot]))
        }

        killerEntity = payload.killerEntity

        payload.killerEntity.equipment.forEach {
            val slot = EQUIPMENT_SLOTS[it.key] ?: return@forEach
            killerInventory.setItem(slot, it.value)
        }
    }

    override fun clicked(
        slotIndex: Int,
        button: Int,
        containerInput: ContainerInput,
        player: Player
    ) {
    }

    override fun stillValid(player: Player) = killerInventory.stillValid(player)

    // only gets called in onSlotClick which is overridden
    override fun quickMoveStack(player: Player, slot: Int): ItemStack = ItemStack.EMPTY
}