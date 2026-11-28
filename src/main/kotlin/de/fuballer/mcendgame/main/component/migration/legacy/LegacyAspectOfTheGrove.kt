package de.fuballer.mcendgame.main.component.migration.legacy

import de.fuballer.mcendgame.main.component.migration.MigrationService
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class LegacyAspectOfTheGrove(
    settings: Properties,
) : Item(settings) {
    companion object {
        const val ID = "aspect_of_the_grove"
    }

    override fun inventoryTick(
        stack: ItemStack,
        level: ServerLevel,
        entity: Entity,
        slot: EquipmentSlot?,
    ) = MigrationService.convertLegacyItem(entity, stack, slot)
}