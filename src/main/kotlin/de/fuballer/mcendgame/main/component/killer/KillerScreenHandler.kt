package de.fuballer.mcendgame.main.component.killer

import de.fuballer.mcendgame.main.component.inventory.EmptySpriteSlot
import de.fuballer.mcendgame.main.component.killer.db.KillerEntity
import de.fuballer.mcendgame.main.component.killer.networking.KillerEntityPayload
import de.fuballer.mcendgame.main.component.screen.CustomScreenHandlerTypes
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.SlotActionType
import net.minecraft.util.Identifier

private val SLOT_SPRITES = listOf(
    PlayerScreenHandler.EMPTY_HELMET_SLOT_TEXTURE,
    PlayerScreenHandler.EMPTY_CHESTPLATE_SLOT_TEXTURE,
    PlayerScreenHandler.EMPTY_LEGGINGS_SLOT_TEXTURE,
    PlayerScreenHandler.EMPTY_BOOTS_SLOT_TEXTURE,
    Identifier.ofVanilla("container/slot/sword"),
    PlayerScreenHandler.EMPTY_OFFHAND_ARMOR_SLOT,
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
    playerInventory: PlayerInventory,
    payload: KillerEntityPayload,
) : ScreenHandler(CustomScreenHandlerTypes.KILLER, syncId) {
    var killerEntity: KillerEntity? = null
    private val killerInventory = SimpleInventory(6)

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
            killerInventory.setStack(slot, it.value)
        }
    }

    override fun onSlotClick(
        slotIndex: Int,
        button: Int,
        actionType: SlotActionType,
        player: PlayerEntity
    ) {
    }

    override fun canUse(player: PlayerEntity) = killerInventory.canPlayerUse(player)

    // only gets called in onSlotClick which is overridden
    override fun quickMove(player: PlayerEntity, slot: Int): ItemStack = ItemStack.EMPTY
}