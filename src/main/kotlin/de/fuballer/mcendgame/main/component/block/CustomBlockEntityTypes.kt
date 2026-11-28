package de.fuballer.mcendgame.main.component.block

import de.fuballer.mcendgame.main.component.block.blocks.demonic_crucible.DemonicCrucibleBlock
import de.fuballer.mcendgame.main.component.block.blocks.demonic_crucible.DemonicCrucibleBlockEntity
import de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.DungeonDeviceBlock
import de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.DungeonDeviceBlockEntity
import de.fuballer.mcendgame.main.component.block.blocks.totem_statue.TotemStatueBlock
import de.fuballer.mcendgame.main.component.block.blocks.totem_statue.TotemStatueBlockEntity
import de.fuballer.mcendgame.main.util.minecraft.RegistryUtil
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
object CustomBlockEntityTypes {
    val DUNGEON_DEVICE = RegistryUtil.registerBlockEntityType(::DungeonDeviceBlockEntity, CustomBlocks.DUNGEON_DEVICE, DungeonDeviceBlock.ID)
    val TOTEM_STATUE = RegistryUtil.registerBlockEntityType(::TotemStatueBlockEntity, CustomBlocks.TOTEM_STATUE, TotemStatueBlock.ID)
    val DEMONIC_CRUCIBLE = RegistryUtil.registerBlockEntityType(::DemonicCrucibleBlockEntity, CustomBlocks.DEMONIC_CRUCIBLE, DemonicCrucibleBlock.ID)
}