package de.fuballer.mcendgame.main.component.dungeon.enemy.equipment.data

import de.fuballer.mcendgame.main.component.item.equipment.Equipment
import net.minecraft.world.entity.EquipmentSlot

data class TaggedEquipment(
    val equipment: Equipment,
    val slots: Set<EquipmentSlot>,
    val tags: Set<EquipmentTag> = setOf(),
) {
    constructor(
        equipment: Equipment,
        slot: EquipmentSlot,
        tags: Set<EquipmentTag> = setOf(),
    ) : this(equipment, setOf(slot), tags)

    companion object {
        fun forBothHands(
            equipment: Equipment,
            tags: Set<EquipmentTag> = setOf(),
        ) = TaggedEquipment(equipment, setOf(EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND), tags)

        fun forRangedWeapon(
            equipment: Equipment,
        ) = TaggedEquipment(equipment, EquipmentSlot.MAINHAND, setOf(EquipmentTag.RANGED))
    }
}