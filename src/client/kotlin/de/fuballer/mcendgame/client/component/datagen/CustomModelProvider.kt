package de.fuballer.mcendgame.client.component.datagen

import de.fuballer.mcendgame.client.component.datagen.property.BowPullDurationProperty
import de.fuballer.mcendgame.main.component.block.CustomBlocks
import de.fuballer.mcendgame.main.component.item.custom.armor.CustomArmorItems
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItems
import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItems
import de.fuballer.mcendgame.main.component.item.custom.tool.CustomToolItems
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemItems
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.client.data.*
import net.minecraft.client.render.item.tint.DyeTintSource
import net.minecraft.item.Item

class CustomModelProvider(
    output: FabricDataOutput,
) : FabricModelProvider(output) {
    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        generator.registerCubeWithCustomTextures(CustomBlocks.DUNGEON_DEVICE, CustomBlocks.DUNGEON_DEVICE) { block, _ -> sideTopBottomTextureMap(block) }
        generator.registerTintableCross(CustomBlocks.DECAYING_COBWEB, BlockStateModelGenerator.CrossType.NOT_TINTED)
        generator.registerBuiltinWithParticle(CustomBlocks.TOTEM_STATUE, Blocks.STONE)
    }

    override fun generateItemModels(generator: ItemModelGenerator) {
        generator.register(CustomToolItems.TWINFIRE, Models.HANDHELD)
        generator.register(CustomToolItems.FATESPLITTER, Models.HANDHELD)
        generator.register(CustomToolItems.SERPENTS_FANG, Models.HANDHELD)
        generator.register(CustomToolItems.NIGHTREAVER, Models.HANDHELD)
        registerCustomBow(generator, CustomToolItems.WINDSTRING)
        registerCustomBow(generator, CustomToolItems.HAILSTORM)
        registerCustomBow(generator, CustomToolItems.DUSK_PIERCER)

        generator.register(CustomArmorItems.BOUND_ABYSS, Models.GENERATED)
        generator.register(CustomArmorItems.DRUIDS_BOOTS, Models.GENERATED)
        generator.register(CustomArmorItems.DRUIDS_CHESTPLATE, Models.GENERATED)
        generator.register(CustomArmorItems.DRUIDS_HELMET, Models.GENERATED)
        generator.register(CustomArmorItems.DRUIDS_LEGGINGS, Models.GENERATED)
        generator.register(CustomArmorItems.EMBERCHANT, Models.GENERATED)
        generator.register(CustomArmorItems.ICEBORNE, Models.GENERATED)
        generator.register(CustomArmorItems.LAMIAS_GIFT, Models.GENERATED)
        generateDyeable(generator, CustomArmorItems.SUEDE_BOOTS)
        generateDyeable(generator, CustomArmorItems.SUEDE_CHESTPLATE, hasOverlay = true)
        generateDyeable(generator, CustomArmorItems.SUEDE_HELMET)
        generateDyeable(generator, CustomArmorItems.SUEDE_LEGGINGS, hasOverlay = true)
        generator.register(CustomArmorItems.WITHER_ROSE_BOOTS, Models.GENERATED)
        generator.register(CustomArmorItems.WITHER_ROSE_CHESTPLATE, Models.GENERATED)
        generator.register(CustomArmorItems.WITHER_ROSE_HELMET, Models.GENERATED)
        generator.register(CustomArmorItems.WITHER_ROSE_LEGGINGS, Models.GENERATED)
        generator.register(CustomArmorItems.STONEWARD, Models.GENERATED)
        generator.register(CustomArmorItems.MOONSHADOW, Models.GENERATED)
        generator.register(CustomArmorItems.GEISTERGALOSCHEN, Models.GENERATED)
        generator.register(CustomArmorItems.VOIDWEAVER, Models.GENERATED)
        generator.register(CustomArmorItems.ABYSSAL_MASK, Models.GENERATED)
        generator.register(CustomArmorItems.GILDED_TEMPEST, Models.GENERATED)
        generator.register(CustomArmorItems.WINDSTRIDER, Models.GENERATED)
        generator.register(CustomArmorItems.BROODMOTHER, Models.GENERATED)
        generator.register(CustomArmorItems.EMBERREIGN, Models.GENERATED)

        generator.register(AspectItems.ASPECT_OF_ANCESTORS, Models.GENERATED)
        generator.register(AspectItems.ASPECT_OF_CURIO, Models.GENERATED)
        generator.register(AspectItems.ASPECT_OF_DOMINION, Models.GENERATED)
        generator.register(AspectItems.ASPECT_OF_DUALITY, Models.GENERATED)
        generator.register(AspectItems.ASPECT_OF_EMINENCE, Models.GENERATED)
        generator.register(AspectItems.ASPECT_OF_FORTITUDE, Models.GENERATED)
        generator.register(AspectItems.ASPECT_OF_FORTUNE, Models.GENERATED)
        generator.register(AspectItems.ASPECT_OF_GHOSTS, Models.GENERATED)
        generator.register(AspectItems.ASPECT_OF_GREED, Models.GENERATED)
        generator.register(AspectItems.ASPECT_OF_HORDES, Models.GENERATED)
        generator.register(AspectItems.ASPECT_OF_IMPATIENCE, Models.GENERATED)
        generator.register(AspectItems.ASPECT_OF_SAVAGERY, Models.GENERATED)
        generator.register(AspectItems.ASPECT_OF_TYRANNY, Models.GENERATED)
        generator.register(AspectItems.ASPECT_OF_ZEAL, Models.GENERATED)

        generator.register(CrystalItems.CALIBRATION_CRYSTAL, Models.GENERATED)
        generator.register(CrystalItems.SACRIFICIAL_CRYSTAL, Models.GENERATED)
        generator.register(CrystalItems.PERMUTATION_CRYSTAL, Models.GENERATED)
        generator.register(CrystalItems.REFORGE_CRYSTAL, Models.GENERATED)
        generator.register(CrystalItems.CORRUPTION_CRYSTAL, Models.GENERATED)

        generator.register(TotemItems.TOTEM_OF_BASTION, Models.GENERATED)
        generator.register(TotemItems.TOTEM_OF_FORCE, Models.GENERATED)
        generator.register(TotemItems.TOTEM_OF_FORTRESS, Models.GENERATED)
        generator.register(TotemItems.TOTEM_OF_FRENZY, Models.GENERATED)
        generator.register(TotemItems.TOTEM_OF_GRACE, Models.GENERATED)
        generator.register(TotemItems.TOTEM_OF_IMPACT, Models.GENERATED)
        generator.register(TotemItems.TOTEM_OF_SWIFTNESS, Models.GENERATED)
        generator.register(TotemItems.TOTEM_OF_THICKNESS, Models.GENERATED)
        generator.register(TotemItems.TOTEM_OF_VANGUARD, Models.GENERATED)
        generator.register(TotemItems.TOTEM_OF_RENEWAL, Models.GENERATED)
        generator.register(TotemItems.TOTEM_OF_TEMPEST, Models.GENERATED)
        generator.register(TotemItems.TOTEM_OF_DEFIANCE, Models.GENERATED)
        generator.register(TotemItems.TOTEM_OF_RECOVERY, Models.GENERATED)
        generator.register(TotemItems.TOTEM_OF_RIME, Models.GENERATED)
        generator.register(TotemItems.TOTEM_OF_ONSLAUGHT, Models.GENERATED)
        generator.register(TotemItems.TOTEM_OF_RESILIENCE, Models.GENERATED)
        generator.register(TotemItems.TOTEM_OF_FURY, Models.GENERATED)
        generator.register(TotemItems.TOTEM_OF_VOLLEY, Models.GENERATED)
        generator.register(TotemItems.TOTEM_OF_REACH, Models.GENERATED)
        generator.register(TotemItems.TOTEM_OF_GIGANTISM, Models.GENERATED)
        generator.register(TotemItems.TOTEM_OF_RESTORATION, Models.GENERATED)

        generator.register(CustomBlocks.TOTEM_STATUE.asItem(), Models.GENERATED)
        generator.register(CustomBlocks.DUNGEON_ENEMY_BLOCKER.asItem(), Models.GENERATED)
    }

    private fun sideTopBottomTextureMap(block: Block) = TextureMap()
        .put(TextureKey.PARTICLE, TextureMap.getSubId(block, "_top"))
        .put(TextureKey.UP, TextureMap.getSubId(block, "_top"))
        .put(TextureKey.DOWN, TextureMap.getSubId(block, "_bottom"))
        .put(TextureKey.NORTH, TextureMap.getSubId(block, "_side"))
        .put(TextureKey.EAST, TextureMap.getSubId(block, "_side"))
        .put(TextureKey.SOUTH, TextureMap.getSubId(block, "_side"))
        .put(TextureKey.WEST, TextureMap.getSubId(block, "_side"))

    fun generateDyeable(
        generator: ItemModelGenerator,
        item: Item,
        defaultColor: Int = -6265536,
        hasOverlay: Boolean = false,
    ) {
        val modelId = ModelIds.getItemModelId(item)
        val dyeableLayer = TextureMap.getId(item)

        if (!hasOverlay) {
            Models.GENERATED.upload(modelId, TextureMap.layer0(dyeableLayer), generator.modelCollector)
        } else {
            val overlayLayer = TextureMap.getSubId(item, "_overlay")
            Models.GENERATED_TWO_LAYERS.upload(modelId, TextureMap.layered(dyeableLayer, overlayLayer), generator.modelCollector)
        }

        generator.output.accept(item, ItemModels.tinted(modelId, DyeTintSource(defaultColor)))
    }

    fun registerCustomBow(
        generator: ItemModelGenerator,
        item: Item
    ) {
        val unbaked = ItemModels.basic(generator.registerSubModel(item, "", Models.BOW))
        val unbaked2 = ItemModels.basic(generator.registerSubModel(item, "_pulling_0", Models.BOW))
        val unbaked3 = ItemModels.basic(generator.registerSubModel(item, "_pulling_1", Models.BOW))
        val unbaked4 = ItemModels.basic(generator.registerSubModel(item, "_pulling_2", Models.BOW))

        generator.output.accept(
            item,
            ItemModels.condition(
                ItemModels.usingItemProperty(),
                ItemModels.rangeDispatch(
                    BowPullDurationProperty(),
                    1F,
                    unbaked2,
                    ItemModels.rangeDispatchEntry(unbaked3, 0.65f),
                    ItemModels.rangeDispatchEntry(unbaked4, 0.9f)
                ),
                unbaked
            )
        )
    }
}