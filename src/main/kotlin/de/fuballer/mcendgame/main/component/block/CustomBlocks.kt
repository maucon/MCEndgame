package de.fuballer.mcendgame.main.component.block

import de.fuballer.mcendgame.main.component.block.blocks.DecayingCobwebBlock
import de.fuballer.mcendgame.main.component.block.blocks.DungeonEnemyBlockerBlock
import de.fuballer.mcendgame.main.component.block.blocks.crystalforge.CrystalForgeBlock
import de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.DungeonDeviceBlock
import de.fuballer.mcendgame.main.component.block.blocks.totem_statue.TotemStatueBlock
import de.fuballer.mcendgame.main.util.minecraft.RegistryUtil
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.material.PushReaction

@Injectable
object CustomBlocks {
    val DUNGEON_DEVICE = RegistryUtil.registerBlock(
        ::DungeonDeviceBlock,
        Properties.of()
            .explosionResistance(1200F)
            .destroyTime(10F)
            .requiresCorrectToolForDrops(),
        DungeonDeviceBlock.ID,
    )

    val DECAYING_COBWEB = RegistryUtil.registerBlock(
        ::DecayingCobwebBlock,
        Properties.of()
            .mapColor(MapColor.WOOL)
            .sound(SoundType.COBWEB)
            .forceSolidOn()
            .noCollision()
            .requiresCorrectToolForDrops()
            .noLootTable()
            .strength(4.0f)
            .pushReaction(PushReaction.DESTROY),
        DecayingCobwebBlock.ID,
    )

    val CRYSTAL_FORGE = RegistryUtil.registerBlock(
        ::CrystalForgeBlock,
        Properties.of()
            .explosionResistance(1200F)
            .destroyTime(10F)
            .requiresCorrectToolForDrops()
            .noOcclusion(),
        CrystalForgeBlock.ID,
    )

    val TOTEM_STATUE = RegistryUtil.registerBlock(
        ::TotemStatueBlock,
        Properties.of()
            .explosionResistance(1200F)
            .destroyTime(10F)
            .requiresCorrectToolForDrops()
            .noOcclusion(),
        TotemStatueBlock.ID,
    )

    val DUNGEON_ENEMY_BLOCKER = RegistryUtil.registerBlock(
        ::DungeonEnemyBlockerBlock,
        Properties.of()
            .strength(-1.0F, 3600000.8F)
            .mapColor(MapColor.NONE)
            .noLootTable()
            .noOcclusion()
            .isValidSpawn(Blocks::never)
            .noTerrainParticles()
            .pushReaction(PushReaction.BLOCK),
        DungeonEnemyBlockerBlock.ID
    )
}