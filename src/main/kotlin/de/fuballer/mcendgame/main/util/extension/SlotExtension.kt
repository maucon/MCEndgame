package de.fuballer.mcendgame.main.util.extension

import net.minecraft.world.entity.EquipmentSlotGroup

object SlotExtension {
    fun EquipmentSlotGroup.isOrIsChildOf(other: EquipmentSlotGroup): Boolean {
        if (this == other) return true
        if (other == EquipmentSlotGroup.ANY) return true

        if (other == EquipmentSlotGroup.HAND &&
            (this == EquipmentSlotGroup.MAINHAND
                    || this == EquipmentSlotGroup.OFFHAND)
        ) return true

        if (other == EquipmentSlotGroup.ARMOR &&
            (this == EquipmentSlotGroup.HEAD
                    || this == EquipmentSlotGroup.CHEST
                    || this == EquipmentSlotGroup.LEGS
                    || this == EquipmentSlotGroup.FEET)
        ) return true

        return false
    }
}