package de.fuballer.mcendgame.client.component.datagen

import de.fuballer.mcendgame.main.component.block.CustomBlockItemIds
import de.fuballer.mcendgame.main.component.tags.CustomTags
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider
import net.minecraft.core.HolderLookup
import net.minecraft.references.BlockIds
import net.minecraft.references.BlockItemIds
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Blocks
import java.util.concurrent.CompletableFuture

class CustomBlockTagProvider(
    packOutput: FabricPackOutput,
    registryLookup: CompletableFuture<HolderLookup.Provider>,
) : FabricTagsProvider.BlockTagsProvider(packOutput, registryLookup) {
    override fun addTags(wrapperLookup: HolderLookup.Provider) {
        builder(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(CustomBlockItemIds.DUNGEON_DEVICE)
            .add(CustomBlockItemIds.CRYSTAL_FORGE)
            .add(CustomBlockItemIds.TOTEM_STATUE)

        Blocks.OAK_SAPLING
        builder(BlockTags.SWORD_EFFICIENT)
            .add(CustomBlockItemIds.DECAYING_COBWEB)

        builder(CustomTags.DUNGEON_BREAKABLE)
            .add(CustomBlockItemIds.DECAYING_COBWEB.block)
            .add(BlockIds.FIRE)
            .add(BlockIds.SOUL_FIRE)
            .add(BlockItemIds.COBWEB)
            .add(BlockItemIds.SHORT_GRASS)
            .add(BlockItemIds.TALL_GRASS)
            .add(BlockItemIds.SHORT_DRY_GRASS)
            .add(BlockItemIds.TALL_DRY_GRASS)
            .add(BlockItemIds.FERN)
            .add(BlockItemIds.LARGE_FERN)
            .add(BlockItemIds.SMALL_DRIPLEAF)
            .add(BlockItemIds.BIG_DRIPLEAF)
            .add(BlockIds.BIG_DRIPLEAF_STEM)
            .add(BlockItemIds.WEEPING_VINES)
            .add(BlockIds.WEEPING_VINES_PLANT)
            .add(BlockItemIds.TWISTING_VINES)
            .add(BlockIds.TWISTING_VINES_PLANT)
            .add(BlockItemIds.SEAGRASS)
            .add(BlockIds.TALL_SEAGRASS)
            .add(BlockItemIds.KELP)
            .add(BlockIds.KELP_PLANT)
            .add(BlockItemIds.DEAD_TUBE_CORAL)
            .add(BlockItemIds.DEAD_BRAIN_CORAL)
            .add(BlockItemIds.DEAD_BUBBLE_CORAL)
            .add(BlockItemIds.DEAD_FIRE_CORAL)
            .add(BlockItemIds.DEAD_HORN_CORAL)
            .add(BlockItemIds.DEAD_TUBE_CORAL_FAN)
            .add(BlockItemIds.DEAD_BRAIN_CORAL_FAN)
            .add(BlockItemIds.DEAD_BUBBLE_CORAL_FAN)
            .add(BlockItemIds.DEAD_FIRE_CORAL_FAN)
            .add(BlockItemIds.DEAD_HORN_CORAL_FAN)
            .add(BlockIds.DEAD_TUBE_CORAL_WALL_FAN)
            .add(BlockIds.DEAD_BRAIN_CORAL_WALL_FAN)
            .add(BlockIds.DEAD_BUBBLE_CORAL_WALL_FAN)
            .add(BlockIds.DEAD_FIRE_CORAL_WALL_FAN)
            .add(BlockIds.DEAD_HORN_CORAL_WALL_FAN)
            .add(BlockItemIds.SEA_PICKLE)
            .add(BlockItemIds.SUGAR_CANE)
            .add(BlockItemIds.SWEET_BERRY_CROP)
            .add(BlockItemIds.BROWN_MUSHROOM)
            .add(BlockItemIds.RED_MUSHROOM)
            .add(BlockItemIds.NETHER_WART)
            .add(BlockItemIds.WARPED_FUNGUS)
            .add(BlockItemIds.CRIMSON_FUNGUS)
            .add(BlockItemIds.NETHER_SPROUTS)
            .add(BlockItemIds.WARPED_ROOTS)
            .add(BlockItemIds.CRIMSON_ROOTS)
            .add(BlockItemIds.HANGING_ROOTS)
            .add(BlockItemIds.LILY_PAD)
            .add(BlockIds.BAMBOO_SAPLING)
            .add(BlockItemIds.DEAD_BUSH)
            .add(BlockItemIds.REDSTONE_DUST)
            // region .add(BlockTags.SAPLINGS)
            // FIXME should be 'BlockTags.SAPLINGS', but the reference seems to be removed? We list the entries for now (https://minecraft.wiki/w/Block_tag_%28Java_Edition%29#saplings)
            .add(BlockItemIds.ACACIA_SAPLING)
            .add(BlockItemIds.AZALEA)
            .add(BlockItemIds.BIRCH_SAPLING)
            .add(BlockItemIds.CHERRY_SAPLING)
            .add(BlockItemIds.DARK_OAK_SAPLING)
            .add(BlockItemIds.FLOWERING_AZALEA)
            .add(BlockItemIds.JUNGLE_SAPLING)
            .add(BlockItemIds.MANGROVE_PROPAGULE)
            .add(BlockItemIds.OAK_SAPLING)
            .add(BlockItemIds.SPRUCE_SAPLING)
            // endregion
            .forceAddTag(BlockTags.FLOWERS)
            .forceAddTag(BlockTags.CORALS)
            .forceAddTag(BlockTags.WALL_CORALS)
            .forceAddTag(BlockTags.CAVE_VINES)

        builder(CustomTags.DUNGEON_INTERACTABLE)
            .add(CustomBlockItemIds.TOTEM_STATUE)
            .add(BlockItemIds.REDSTONE_DUST)
            .add(BlockItemIds.LEVER)
            .forceAddTag(BlockTags.BUTTONS)

        builder(CustomTags.PHASING_BLOCKING)
            .add(BlockItemIds.BARRIER)
            .add(BlockItemIds.BEDROCK)

        builder(CustomTags.NO_PHASING_SLOW_AND_FOG)
            .add(BlockItemIds.SCAFFOLDING)
    }
}