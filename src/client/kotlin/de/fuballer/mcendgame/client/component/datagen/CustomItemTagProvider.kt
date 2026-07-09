package de.fuballer.mcendgame.client.component.datagen

import de.fuballer.mcendgame.main.component.item.custom.armor.CustomArmorItemIds
import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItemIds
import de.fuballer.mcendgame.main.component.item.custom.tool.CustomToolItemIds
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemItemIds
import de.fuballer.mcendgame.main.component.tags.CustomTags
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider
import net.minecraft.core.HolderLookup
import net.minecraft.references.BlockItemIds
import net.minecraft.references.ItemIds
import net.minecraft.tags.ItemTags
import java.util.concurrent.CompletableFuture

class CustomItemTagProvider(
    packOutput: FabricPackOutput,
    registryLookup: CompletableFuture<HolderLookup.Provider>,
) : FabricTagsProvider.ItemTagsProvider(packOutput, registryLookup) {
    override fun addTags(wrapperLookup: HolderLookup.Provider) {
        builder(ItemTags.SWORDS)
            .add(CustomToolItemIds.TWINFIRE)
            .add(CustomToolItemIds.BLOODHARVEST)
            .add(CustomToolItemIds.SERPENTS_FANG)
            .add(CustomToolItemIds.NIGHTREAVER)
            .add(CustomToolItemIds.RADIANT_DAWN)

        builder(ItemTags.AXES)
            .add(CustomToolItemIds.FATESPLITTER)

        builder(ItemTags.HEAD_ARMOR)
            .add(CustomArmorItemIds.DRUIDS_HELMET)
            .add(CustomArmorItemIds.ICEBORNE)
            .add(CustomArmorItemIds.SUEDE_HELMET)
            .add(CustomArmorItemIds.WITHER_ROSE_HELMET)
            .add(CustomArmorItemIds.EMBERCHANT)
            .add(CustomArmorItemIds.ABYSSAL_MASK)

        builder(ItemTags.CHEST_ARMOR)
            .add(CustomArmorItemIds.BOUND_ABYSS)
            .add(CustomArmorItemIds.DRUIDS_CHESTPLATE)
            .add(CustomArmorItemIds.SUEDE_CHESTPLATE)
            .add(CustomArmorItemIds.VOIDWEAVER)
            .add(CustomArmorItemIds.WITHER_ROSE_CHESTPLATE)
            .add(CustomArmorItemIds.BROODMOTHER)

        builder(ItemTags.LEG_ARMOR)
            .add(CustomArmorItemIds.LAMIAS_GIFT)
            .add(CustomArmorItemIds.DRUIDS_LEGGINGS)
            .add(CustomArmorItemIds.SUEDE_LEGGINGS)
            .add(CustomArmorItemIds.WITHER_ROSE_LEGGINGS)
            .add(CustomArmorItemIds.STONEWARD)
            .add(CustomArmorItemIds.GILDED_TEMPEST)
            .add(CustomArmorItemIds.WINDSTRIDER)

        builder(ItemTags.FOOT_ARMOR)
            .add(CustomArmorItemIds.DRUIDS_BOOTS)
            .add(CustomArmorItemIds.GEISTERGALOSCHEN)
            .add(CustomArmorItemIds.MOONSHADOW)
            .add(CustomArmorItemIds.SUEDE_BOOTS)
            .add(CustomArmorItemIds.WITHER_ROSE_BOOTS)
            .add(CustomArmorItemIds.EMBERREIGN)

        builder(CustomTags.BOW)
            .add(ItemIds.BOW)
            .add(CustomToolItemIds.WINDSTRING)
            .add(CustomToolItemIds.HAILSTORM)
            .add(CustomToolItemIds.DUSK_PIERCER)

        builder(ItemTags.BOW_ENCHANTABLE)
            .forceAddTag(CustomTags.BOW)

        builder(CustomTags.SHIELD)
            .add(ItemIds.SHIELD)
            .add(CustomToolItemIds.GRUDGEBEARER)

        builder(CustomTags.MACE)
            .add(ItemIds.MACE)
            .add(CustomToolItemIds.GRAVEBREAKER)

        builder(ItemTags.MACE_ENCHANTABLE)
            .forceAddTag(CustomTags.MACE)

        builder(ItemTags.WEAPON_ENCHANTABLE)
            .forceAddTag(CustomTags.MACE)

        builder(ItemTags.DURABILITY_ENCHANTABLE)
            .forceAddTag(CustomTags.BOW)
            .forceAddTag(CustomTags.SHIELD)
            .forceAddTag(CustomTags.MACE)

        builder(ItemTags.CAULDRON_CAN_REMOVE_DYE)
            .add(CustomArmorItemIds.SUEDE_HELMET)
            .add(CustomArmorItemIds.SUEDE_CHESTPLATE)
            .add(CustomArmorItemIds.SUEDE_LEGGINGS)
            .add(CustomArmorItemIds.SUEDE_BOOTS)

        builder(CustomTags.DIAMOND_GEAR)
            .add(ItemIds.DIAMOND_HELMET)
            .add(ItemIds.DIAMOND_CHESTPLATE)
            .add(ItemIds.DIAMOND_LEGGINGS)
            .add(ItemIds.DIAMOND_BOOTS)
            .add(ItemIds.DIAMOND_SWORD)
            .add(ItemIds.DIAMOND_PICKAXE)
            .add(ItemIds.DIAMOND_AXE)
            .add(ItemIds.DIAMOND_SHOVEL)
            .add(ItemIds.DIAMOND_HOE)

        builder(CustomTags.NETHERITE_GEAR)
            .add(ItemIds.NETHERITE_HELMET)
            .add(ItemIds.NETHERITE_CHESTPLATE)
            .add(ItemIds.NETHERITE_LEGGINGS)
            .add(ItemIds.NETHERITE_BOOTS)
            .add(ItemIds.NETHERITE_SWORD)
            .add(ItemIds.NETHERITE_PICKAXE)
            .add(ItemIds.NETHERITE_AXE)
            .add(ItemIds.NETHERITE_SHOVEL)
            .add(ItemIds.NETHERITE_HOE)

        builder(CustomTags.REPAIRS_SPIDER_ARMOR)
            .add(ItemIds.SPIDER_EYE)
            .add(ItemIds.FERMENTED_SPIDER_EYE)

        builder(CustomTags.DUNGEON_DROP_DISABLED)
            .add(ItemIds.TRIDENT)
            .add(ItemIds.MACE)

        builder(CustomTags.DUNGEON_DISABLED)
            .forceAddTag(ItemTags.BOATS)
            .forceAddTag(ItemTags.EGGS)
            .add(ItemIds.ENDER_PEARL)
            .add(ItemIds.BUCKET)
            .add(ItemIds.WATER_BUCKET)
            .add(ItemIds.LAVA_BUCKET)
            .add(BlockItemIds.POWDER_SNOW.item)
            .add(ItemIds.COD_BUCKET)
            .add(ItemIds.SALMON_BUCKET)
            .add(ItemIds.TROPICAL_FISH_BUCKET)
            .add(ItemIds.PUFFERFISH_BUCKET)
            .add(ItemIds.TADPOLE_BUCKET)
            .add(ItemIds.CHORUS_FRUIT)
            .add(ItemIds.LEAD)
            .add(ItemIds.FLINT_AND_STEEL)
            .add(ItemIds.FIRE_CHARGE)
            .add(ItemIds.FIREWORK_ROCKET)
            .add(ItemIds.MINECART)
            .add(ItemIds.HOPPER_MINECART)
            .add(ItemIds.CHEST_MINECART)
            .add(ItemIds.FURNACE_MINECART)
            .add(ItemIds.TNT_MINECART)
            .add(ItemIds.SNOWBALL)
            .add(ItemIds.END_CRYSTAL)
            .add(ItemIds.PAINTING)
            .add(ItemIds.ITEM_FRAME)
            .add(ItemIds.GLOW_ITEM_FRAME)

        builder(CustomTags.CRYSTAL)
            .add(CrystalItemIds.REFORGE_CRYSTAL)
            .add(CrystalItemIds.CORRUPTION_CRYSTAL)
            .add(CrystalItemIds.CALIBRATION_CRYSTAL)
            .add(CrystalItemIds.PERMUTATION_CRYSTAL)
            .add(CrystalItemIds.SACRIFICIAL_CRYSTAL)

        builder(CustomTags.TOTEM)
            .add(TotemItemIds.TOTEM_OF_BASTION)
            .add(TotemItemIds.TOTEM_OF_FORCE)
            .add(TotemItemIds.TOTEM_OF_FORTRESS)
            .add(TotemItemIds.TOTEM_OF_FRENZY)
            .add(TotemItemIds.TOTEM_OF_GRACE)
            .add(TotemItemIds.TOTEM_OF_IMPACT)
            .add(TotemItemIds.TOTEM_OF_SWIFTNESS)
            .add(TotemItemIds.TOTEM_OF_THICKNESS)
            .add(TotemItemIds.TOTEM_OF_VANGUARD)
            .add(TotemItemIds.TOTEM_OF_RENEWAL)
            .add(TotemItemIds.TOTEM_OF_TEMPEST)
            .add(TotemItemIds.TOTEM_OF_DISPELLING)
            .add(TotemItemIds.TOTEM_OF_DEFIANCE)
            .add(TotemItemIds.TOTEM_OF_RECOVERY)
            .add(TotemItemIds.TOTEM_OF_RIME)
            .add(TotemItemIds.TOTEM_OF_ONSLAUGHT)
            .add(TotemItemIds.TOTEM_OF_RESILIENCE)
            .add(TotemItemIds.TOTEM_OF_FURY)
            .add(TotemItemIds.TOTEM_OF_VOLLEY)
            .add(TotemItemIds.TOTEM_OF_REACH)
            .add(TotemItemIds.TOTEM_OF_GIGANTISM)
            .add(TotemItemIds.TOTEM_OF_RESTORATION)
    }
}