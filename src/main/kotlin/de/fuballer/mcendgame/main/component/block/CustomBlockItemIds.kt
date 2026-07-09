package de.fuballer.mcendgame.main.component.block

import de.fuballer.mcendgame.main.component.block.blocks.DecayingCobwebBlock
import de.fuballer.mcendgame.main.component.block.blocks.DungeonEnemyBlockerBlock
import de.fuballer.mcendgame.main.component.block.blocks.crystalforge.CrystalForgeBlock
import de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.DungeonDeviceBlock
import de.fuballer.mcendgame.main.component.block.blocks.totem_statue.TotemStatueBlock
import de.fuballer.mcendgame.main.util.minecraft.RegistryKeyUtil
import net.minecraft.references.BlockItemId

object CustomBlockItemIds {
    val DUNGEON_DEVICE = create(DungeonDeviceBlock.ID)
    val DECAYING_COBWEB = create(DecayingCobwebBlock.ID)
    val CRYSTAL_FORGE = create(CrystalForgeBlock.ID)
    val TOTEM_STATUE = create(TotemStatueBlock.ID)
    val DUNGEON_ENEMY_BLOCKER = create(DungeonEnemyBlockerBlock.ID)

    fun create(name: String): BlockItemId {
        val blockKey = RegistryKeyUtil.createBlockKey(name)
        val itemKey = RegistryKeyUtil.createItemKey(name)
        return BlockItemId(blockKey, itemKey)
    }
}