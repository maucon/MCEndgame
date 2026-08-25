package de.fuballer.mcendgame.client.component.datagen

import de.fuballer.mcendgame.client.component.datagen.property.BowPullDurationProperty
import de.fuballer.mcendgame.main.component.block.CustomBlocks
import de.fuballer.mcendgame.main.component.item.custom.armor.CustomArmorItems
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItems
import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItems
import de.fuballer.mcendgame.main.component.item.custom.tool.CustomToolItems
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemItems
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.minecraft.client.color.item.Dye
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.model.*
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

class CustomModelProvider(
    packOutput: FabricPackOutput,
) : FabricModelProvider(packOutput) {
    override fun generateBlockStateModels(generator: BlockModelGenerators) {
        generator.createCraftingTableLike(CustomBlocks.DUNGEON_DEVICE, CustomBlocks.DUNGEON_DEVICE) { block, _ -> sideTopBottomTextureMap(block) }
        generator.createCrossBlockWithDefaultItem(CustomBlocks.DECAYING_COBWEB, BlockModelGenerators.PlantType.NOT_TINTED)
        generator.createParticleOnlyBlock(CustomBlocks.TOTEM_STATUE, Blocks.STONE)
        generator.createAirLikeBlock(CustomBlocks.DUNGEON_ENEMY_BLOCKER, Items.BARRIER)
    }

    override fun generateItemModels(generator: ItemModelGenerators) {
        generator.generateFlatItem(CustomToolItems.TWINFIRE, ModelTemplates.FLAT_HANDHELD_ITEM)
        generator.generateFlatItem(CustomToolItems.FATESPLITTER, ModelTemplates.FLAT_HANDHELD_ITEM)
        generator.generateFlatItem(CustomToolItems.SERPENTS_FANG, ModelTemplates.FLAT_HANDHELD_ITEM)
        generator.generateFlatItem(CustomToolItems.NIGHTREAVER, ModelTemplates.FLAT_HANDHELD_ITEM)
        registerCustomBow(generator, CustomToolItems.WINDSTRING)
        registerCustomBow(generator, CustomToolItems.HAILSTORM)
        registerCustomBow(generator, CustomToolItems.DUSK_PIERCER)

        generator.generateFlatItem(CustomArmorItems.BOUND_ABYSS, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(CustomArmorItems.DRUIDS_BOOTS, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(CustomArmorItems.DRUIDS_CHESTPLATE, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(CustomArmorItems.DRUIDS_HELMET, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(CustomArmorItems.DRUIDS_LEGGINGS, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(CustomArmorItems.EMBERCHANT, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(CustomArmorItems.ICEBORNE, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(CustomArmorItems.LAMIAS_GIFT, ModelTemplates.FLAT_ITEM)
        generateDyeable(generator, CustomArmorItems.SUEDE_BOOTS)
        generateDyeable(generator, CustomArmorItems.SUEDE_CHESTPLATE, hasOverlay = true)
        generateDyeable(generator, CustomArmorItems.SUEDE_HELMET)
        generateDyeable(generator, CustomArmorItems.SUEDE_LEGGINGS, hasOverlay = true)
        generator.generateFlatItem(CustomArmorItems.WITHER_ROSE_BOOTS, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(CustomArmorItems.WITHER_ROSE_CHESTPLATE, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(CustomArmorItems.WITHER_ROSE_HELMET, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(CustomArmorItems.WITHER_ROSE_LEGGINGS, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(CustomArmorItems.STONEWARD, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(CustomArmorItems.MOONSHADOW, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(CustomArmorItems.GEISTERGALOSCHEN, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(CustomArmorItems.VOIDWEAVER, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(CustomArmorItems.ABYSSAL_MASK, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(CustomArmorItems.GILDED_TEMPEST, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(CustomArmorItems.WINDSTRIDER, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(CustomArmorItems.BROODMOTHER, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(CustomArmorItems.EMBERREIGN, ModelTemplates.FLAT_ITEM)

        generator.generateFlatItem(AspectItems.ASPECT_OF_ANCESTORS, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(AspectItems.ASPECT_OF_CURIO, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(AspectItems.ASPECT_OF_DOMINION, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(AspectItems.ASPECT_OF_DUALITY, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(AspectItems.ASPECT_OF_EMINENCE, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(AspectItems.ASPECT_OF_FORTITUDE, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(AspectItems.ASPECT_OF_FORTUNE, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(AspectItems.ASPECT_OF_GHOSTS, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(AspectItems.ASPECT_OF_GREED, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(AspectItems.ASPECT_OF_HORDES, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(AspectItems.ASPECT_OF_IMPATIENCE, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(AspectItems.ASPECT_OF_SAVAGERY, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(AspectItems.ASPECT_OF_THE_GROVE, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(AspectItems.ASPECT_OF_TYRANNY, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(AspectItems.ASPECT_OF_ZEAL, ModelTemplates.FLAT_ITEM)

        generator.generateFlatItem(CrystalItems.CALIBRATION_CRYSTAL, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(CrystalItems.SACRIFICIAL_CRYSTAL, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(CrystalItems.PERMUTATION_CRYSTAL, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(CrystalItems.REFORGE_CRYSTAL, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(CrystalItems.CORRUPTION_CRYSTAL, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(CrystalItems.IMITATION_CRYSTAL, ModelTemplates.FLAT_ITEM)

        generator.generateFlatItem(TotemItems.TOTEM_OF_BASTION, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(TotemItems.TOTEM_OF_FORCE, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(TotemItems.TOTEM_OF_FORTRESS, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(TotemItems.TOTEM_OF_FRENZY, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(TotemItems.TOTEM_OF_GRACE, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(TotemItems.TOTEM_OF_IMPACT, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(TotemItems.TOTEM_OF_SWIFTNESS, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(TotemItems.TOTEM_OF_THICKNESS, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(TotemItems.TOTEM_OF_VANGUARD, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(TotemItems.TOTEM_OF_RENEWAL, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(TotemItems.TOTEM_OF_TEMPEST, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(TotemItems.TOTEM_OF_DISPELLING, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(TotemItems.TOTEM_OF_DEFIANCE, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(TotemItems.TOTEM_OF_RECOVERY, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(TotemItems.TOTEM_OF_RIME, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(TotemItems.TOTEM_OF_ONSLAUGHT, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(TotemItems.TOTEM_OF_RESILIENCE, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(TotemItems.TOTEM_OF_FURY, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(TotemItems.TOTEM_OF_VOLLEY, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(TotemItems.TOTEM_OF_REACH, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(TotemItems.TOTEM_OF_GIGANTISM, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(TotemItems.TOTEM_OF_RESTORATION, ModelTemplates.FLAT_ITEM)

        generator.generateFlatItem(CustomBlocks.TOTEM_STATUE.asItem(), ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(CustomBlocks.DUNGEON_ENEMY_BLOCKER.asItem(), ModelTemplates.FLAT_ITEM)
    }

    private fun sideTopBottomTextureMap(block: Block) = TextureMapping()
        .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_top"))
        .put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"))
        .put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"))
        .put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_side"))
        .put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"))
        .put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_side"))
        .put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"))

    fun generateDyeable(
        generator: ItemModelGenerators,
        item: Item,
        defaultColor: Int = -6265536,
        hasOverlay: Boolean = false,
    ) {
        val modelId = ModelLocationUtils.getModelLocation(item)
        val dyeableLayer = TextureMapping.getItemTexture(item)

        if (!hasOverlay) {
            ModelTemplates.FLAT_ITEM.create(modelId, TextureMapping.layer0(dyeableLayer), generator.modelOutput)
        } else {
            val overlayLayer = TextureMapping.getItemTexture(item, "_overlay")
            ModelTemplates.TWO_LAYERED_ITEM.create(modelId, TextureMapping.layered(dyeableLayer, overlayLayer), generator.modelOutput)
        }

        generator.itemModelOutput.accept(item, ItemModelUtils.tintedModel(modelId, Dye(defaultColor)))
    }

    fun registerCustomBow(
        generator: ItemModelGenerators,
        item: Item
    ) {
        val unbaked = ItemModelUtils.plainModel(generator.createFlatItemModel(item, "", ModelTemplates.BOW))
        val unbaked2 = ItemModelUtils.plainModel(generator.createFlatItemModel(item, "_pulling_0", ModelTemplates.BOW))
        val unbaked3 = ItemModelUtils.plainModel(generator.createFlatItemModel(item, "_pulling_1", ModelTemplates.BOW))
        val unbaked4 = ItemModelUtils.plainModel(generator.createFlatItemModel(item, "_pulling_2", ModelTemplates.BOW))

        generator.itemModelOutput.accept(
            item,
            ItemModelUtils.conditional(
                ItemModelUtils.isUsingItem(),
                ItemModelUtils.rangeSelect(
                    BowPullDurationProperty(),
                    1F,
                    unbaked2,
                    ItemModelUtils.override(unbaked3, 0.65f),
                    ItemModelUtils.override(unbaked4, 0.9f)
                ),
                unbaked
            )
        )
    }
}