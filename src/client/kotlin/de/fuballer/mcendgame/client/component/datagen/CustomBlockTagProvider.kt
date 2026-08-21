package de.fuballer.mcendgame.client.component.datagen

import de.fuballer.mcendgame.main.component.block.CustomBlocks
import de.fuballer.mcendgame.main.component.tags.CustomTags
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Blocks
import java.util.concurrent.CompletableFuture

// https://modrinth.com/mod/pneumono_gravestones
private val GRAVESTONES_GRAVESTONE_TECHNICAL = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("gravestones", "gravestone_technical"))

class CustomBlockTagProvider(
    packOutput: FabricPackOutput,
    registryLookup: CompletableFuture<HolderLookup.Provider>,
) : FabricTagsProvider.BlockTagsProvider(packOutput, registryLookup) {
    override fun addTags(wrapperLookup: HolderLookup.Provider) {
        valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(CustomBlocks.DUNGEON_DEVICE)
            .add(CustomBlocks.CRYSTAL_FORGE)
            .add(CustomBlocks.TOTEM_STATUE)

        valueLookupBuilder(BlockTags.SWORD_EFFICIENT)
            .add(CustomBlocks.DECAYING_COBWEB)

        valueLookupBuilder(CustomTags.DUNGEON_BREAKABLE)
            .forceAddTag(BlockTags.FLOWERS)
            .forceAddTag(BlockTags.CORALS)
            .forceAddTag(BlockTags.WALL_CORALS)
            .forceAddTag(BlockTags.SAPLINGS)
            .forceAddTag(BlockTags.CAVE_VINES)
            .add(Blocks.FIRE)
            .add(Blocks.SOUL_FIRE)
            .add(Blocks.COBWEB)
            .add(CustomBlocks.DECAYING_COBWEB)
            .add(Blocks.SHORT_GRASS)
            .add(Blocks.TALL_GRASS)
            .add(Blocks.SHORT_DRY_GRASS)
            .add(Blocks.TALL_DRY_GRASS)
            .add(Blocks.FERN)
            .add(Blocks.LARGE_FERN)
            .add(Blocks.SMALL_DRIPLEAF)
            .add(Blocks.BIG_DRIPLEAF)
            .add(Blocks.BIG_DRIPLEAF_STEM)
            .add(Blocks.WEEPING_VINES)
            .add(Blocks.WEEPING_VINES_PLANT)
            .add(Blocks.TWISTING_VINES)
            .add(Blocks.TWISTING_VINES_PLANT)
            .add(Blocks.SEAGRASS)
            .add(Blocks.TALL_SEAGRASS)
            .add(Blocks.KELP)
            .add(Blocks.KELP_PLANT)
            .add(Blocks.DEAD_TUBE_CORAL)
            .add(Blocks.DEAD_BRAIN_CORAL)
            .add(Blocks.DEAD_BUBBLE_CORAL)
            .add(Blocks.DEAD_FIRE_CORAL)
            .add(Blocks.DEAD_HORN_CORAL)
            .add(Blocks.DEAD_TUBE_CORAL_FAN)
            .add(Blocks.DEAD_BRAIN_CORAL_FAN)
            .add(Blocks.DEAD_BUBBLE_CORAL_FAN)
            .add(Blocks.DEAD_FIRE_CORAL_FAN)
            .add(Blocks.DEAD_HORN_CORAL_FAN)
            .add(Blocks.DEAD_TUBE_CORAL_WALL_FAN)
            .add(Blocks.DEAD_BRAIN_CORAL_WALL_FAN)
            .add(Blocks.DEAD_BUBBLE_CORAL_WALL_FAN)
            .add(Blocks.DEAD_FIRE_CORAL_WALL_FAN)
            .add(Blocks.DEAD_HORN_CORAL_WALL_FAN)
            .add(Blocks.SEA_PICKLE)
            .add(Blocks.SUGAR_CANE)
            .add(Blocks.SWEET_BERRY_BUSH)
            .add(Blocks.BROWN_MUSHROOM)
            .add(Blocks.RED_MUSHROOM)
            .add(Blocks.NETHER_WART)
            .add(Blocks.WARPED_FUNGUS)
            .add(Blocks.CRIMSON_FUNGUS)
            .add(Blocks.NETHER_SPROUTS)
            .add(Blocks.WARPED_ROOTS)
            .add(Blocks.CRIMSON_ROOTS)
            .add(Blocks.HANGING_ROOTS)
            .add(Blocks.LILY_PAD)
            .add(Blocks.BAMBOO_SAPLING)
            .add(Blocks.DEAD_BUSH)
            .add(Blocks.REDSTONE_WIRE)
            .add(Blocks.BUSH)
            .add(Blocks.FIREFLY_BUSH)
        
        builder(CustomTags.DUNGEON_BREAKABLE)
            .addOptional(GRAVESTONES_GRAVESTONE_TECHNICAL)

        valueLookupBuilder(CustomTags.DUNGEON_INTERACTABLE)
            .add(CustomBlocks.TOTEM_STATUE)
            .forceAddTag(BlockTags.BUTTONS)
            .add(Blocks.REDSTONE_WIRE)
            .add(Blocks.LEVER)

        builder(CustomTags.DUNGEON_INTERACTABLE)
            .addOptional(GRAVESTONES_GRAVESTONE_TECHNICAL)

        valueLookupBuilder(CustomTags.PHASING_BLOCKING)
            .add(Blocks.BARRIER)
            .add(Blocks.BEDROCK)

        valueLookupBuilder(CustomTags.NO_PHASING_SLOW_AND_FOG)
            .add(Blocks.SCAFFOLDING)
    }
}