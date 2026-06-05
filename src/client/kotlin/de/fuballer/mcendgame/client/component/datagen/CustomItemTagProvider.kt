package de.fuballer.mcendgame.client.component.datagen

import de.fuballer.mcendgame.main.component.item.custom.armor.CustomArmorItems
import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItems
import de.fuballer.mcendgame.main.component.item.custom.tool.CustomToolItems
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemItems
import de.fuballer.mcendgame.main.component.tags.CustomTags
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider
import net.minecraft.core.HolderLookup
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import java.util.concurrent.CompletableFuture

class CustomItemTagProvider(
    packOutput: FabricPackOutput,
    registryLookup: CompletableFuture<HolderLookup.Provider>,
) : FabricTagsProvider.ItemTagsProvider(packOutput, registryLookup) {
    override fun addTags(wrapperLookup: HolderLookup.Provider) {
        valueLookupBuilder(ItemTags.SWORDS)
            .add(CustomToolItems.TWINFIRE)
            .add(CustomToolItems.BLOODHARVEST)
            .add(CustomToolItems.SERPENTS_FANG)
            .add(CustomToolItems.NIGHTREAVER)
            .add(CustomToolItems.RADIANT_DAWN)

        valueLookupBuilder(ItemTags.AXES)
            .add(CustomToolItems.FATESPLITTER)

        valueLookupBuilder(ItemTags.HEAD_ARMOR)
            .add(CustomArmorItems.DRUIDS_HELMET)
            .add(CustomArmorItems.ICEBORNE)
            .add(CustomArmorItems.SUEDE_HELMET)
            .add(CustomArmorItems.WITHER_ROSE_HELMET)
            .add(CustomArmorItems.EMBERCHANT)
            .add(CustomArmorItems.ABYSSAL_MASK)

        valueLookupBuilder(ItemTags.CHEST_ARMOR)
            .add(CustomArmorItems.BOUND_ABYSS)
            .add(CustomArmorItems.DRUIDS_CHESTPLATE)
            .add(CustomArmorItems.SUEDE_CHESTPLATE)
            .add(CustomArmorItems.VOIDWEAVER)
            .add(CustomArmorItems.WITHER_ROSE_CHESTPLATE)
            .add(CustomArmorItems.BROODMOTHER)

        valueLookupBuilder(ItemTags.LEG_ARMOR)
            .add(CustomArmorItems.LAMIAS_GIFT)
            .add(CustomArmorItems.DRUIDS_LEGGINGS)
            .add(CustomArmorItems.SUEDE_LEGGINGS)
            .add(CustomArmorItems.WITHER_ROSE_LEGGINGS)
            .add(CustomArmorItems.STONEWARD)
            .add(CustomArmorItems.GILDED_TEMPEST)
            .add(CustomArmorItems.WINDSTRIDER)

        valueLookupBuilder(ItemTags.FOOT_ARMOR)
            .add(CustomArmorItems.DRUIDS_BOOTS)
            .add(CustomArmorItems.GEISTERGALOSCHEN)
            .add(CustomArmorItems.MOONSHADOW)
            .add(CustomArmorItems.SUEDE_BOOTS)
            .add(CustomArmorItems.WITHER_ROSE_BOOTS)
            .add(CustomArmorItems.EMBERREIGN)

        valueLookupBuilder(CustomTags.BOW)
            .add(Items.BOW)
            .add(CustomToolItems.WINDSTRING)
            .add(CustomToolItems.HAILSTORM)
            .add(CustomToolItems.DUSK_PIERCER)

        valueLookupBuilder(ItemTags.BOW_ENCHANTABLE)
            .forceAddTag(CustomTags.BOW)

        valueLookupBuilder(CustomTags.SHIELD)
            .add(Items.SHIELD)
            .add(CustomToolItems.GRUDGEBEARER)

        valueLookupBuilder(CustomTags.MACE)
            .add(Items.MACE)
            .add(CustomToolItems.GRAVEBREAKER)

        valueLookupBuilder(ItemTags.MACE_ENCHANTABLE)
            .forceAddTag(CustomTags.MACE)

        valueLookupBuilder(ItemTags.WEAPON_ENCHANTABLE)
            .forceAddTag(CustomTags.MACE)

        valueLookupBuilder(ItemTags.DURABILITY_ENCHANTABLE)
            .forceAddTag(CustomTags.BOW)
            .forceAddTag(CustomTags.SHIELD)
            .forceAddTag(CustomTags.MACE)

        valueLookupBuilder(ItemTags.CAULDRON_CAN_REMOVE_DYE)
            .add(CustomArmorItems.SUEDE_HELMET)
            .add(CustomArmorItems.SUEDE_CHESTPLATE)
            .add(CustomArmorItems.SUEDE_LEGGINGS)
            .add(CustomArmorItems.SUEDE_BOOTS)

        valueLookupBuilder(CustomTags.DIAMOND_GEAR)
            .add(Items.DIAMOND_HELMET)
            .add(Items.DIAMOND_CHESTPLATE)
            .add(Items.DIAMOND_LEGGINGS)
            .add(Items.DIAMOND_BOOTS)
            .add(Items.DIAMOND_SWORD)
            .add(Items.DIAMOND_PICKAXE)
            .add(Items.DIAMOND_AXE)
            .add(Items.DIAMOND_SHOVEL)
            .add(Items.DIAMOND_HOE)

        valueLookupBuilder(CustomTags.NETHERITE_GEAR)
            .add(Items.NETHERITE_HELMET)
            .add(Items.NETHERITE_CHESTPLATE)
            .add(Items.NETHERITE_LEGGINGS)
            .add(Items.NETHERITE_BOOTS)
            .add(Items.NETHERITE_SWORD)
            .add(Items.NETHERITE_PICKAXE)
            .add(Items.NETHERITE_AXE)
            .add(Items.NETHERITE_SHOVEL)
            .add(Items.NETHERITE_HOE)


        valueLookupBuilder(CustomTags.REPAIRS_SPIDER_ARMOR)
            .add(Items.SPIDER_EYE)
            .add(Items.FERMENTED_SPIDER_EYE)

        valueLookupBuilder(CustomTags.DUNGEON_DROP_DISABLED)
            .add(Items.TRIDENT)
            .add(Items.MACE)

        valueLookupBuilder(CustomTags.DUNGEON_DISABLED)
            .forceAddTag(ItemTags.BOATS)
            .forceAddTag(ItemTags.EGGS)
            .add(Items.ENDER_PEARL)
            .add(Items.BUCKET)
            .add(Items.WATER_BUCKET)
            .add(Items.LAVA_BUCKET)
            .add(Items.POWDER_SNOW_BUCKET)
            .add(Items.COD_BUCKET)
            .add(Items.SALMON_BUCKET)
            .add(Items.TROPICAL_FISH_BUCKET)
            .add(Items.PUFFERFISH_BUCKET)
            .add(Items.TADPOLE_BUCKET)
            .add(Items.CHORUS_FRUIT)
            .add(Items.LEAD)
            .add(Items.FLINT_AND_STEEL)
            .add(Items.FIRE_CHARGE)
            .add(Items.FIREWORK_ROCKET)
            .add(Items.MINECART)
            .add(Items.HOPPER_MINECART)
            .add(Items.CHEST_MINECART)
            .add(Items.FURNACE_MINECART)
            .add(Items.TNT_MINECART)
            .add(Items.SNOWBALL)
            .add(Items.END_CRYSTAL)
            .add(Items.PAINTING)
            .add(Items.ITEM_FRAME)
            .add(Items.GLOW_ITEM_FRAME)

        valueLookupBuilder(CustomTags.CRYSTAL)
            .add(CrystalItems.REFORGE_CRYSTAL)
            .add(CrystalItems.CORRUPTION_CRYSTAL)
            .add(CrystalItems.CALIBRATION_CRYSTAL)
            .add(CrystalItems.PERMUTATION_CRYSTAL)
            .add(CrystalItems.SACRIFICIAL_CRYSTAL)

        valueLookupBuilder(CustomTags.TOTEM)
            .add(TotemItems.TOTEM_OF_BASTION)
            .add(TotemItems.TOTEM_OF_FORCE)
            .add(TotemItems.TOTEM_OF_FORTRESS)
            .add(TotemItems.TOTEM_OF_FRENZY)
            .add(TotemItems.TOTEM_OF_GRACE)
            .add(TotemItems.TOTEM_OF_IMPACT)
            .add(TotemItems.TOTEM_OF_SWIFTNESS)
            .add(TotemItems.TOTEM_OF_THICKNESS)
            .add(TotemItems.TOTEM_OF_VANGUARD)
            .add(TotemItems.TOTEM_OF_RENEWAL)
            .add(TotemItems.TOTEM_OF_TEMPEST)
            .add(TotemItems.TOTEM_OF_DEFIANCE)
            .add(TotemItems.TOTEM_OF_RECOVERY)
            .add(TotemItems.TOTEM_OF_RIME)
            .add(TotemItems.TOTEM_OF_ONSLAUGHT)
            .add(TotemItems.TOTEM_OF_RESILIENCE)
            .add(TotemItems.TOTEM_OF_FURY)
            .add(TotemItems.TOTEM_OF_VOLLEY)
            .add(TotemItems.TOTEM_OF_REACH)
            .add(TotemItems.TOTEM_OF_GIGANTISM)
            .add(TotemItems.TOTEM_OF_RESTORATION)
    }
}