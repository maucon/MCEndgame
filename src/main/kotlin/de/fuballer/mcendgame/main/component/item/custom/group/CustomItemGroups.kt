package de.fuballer.mcendgame.main.component.item.custom.group

import de.fuballer.mcendgame.main.component.block.CustomBlocks
import de.fuballer.mcendgame.main.component.item.custom.armor.CustomArmorItems
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItems
import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItems
import de.fuballer.mcendgame.main.component.item.custom.misc.CustomMiscItems
import de.fuballer.mcendgame.main.component.item.custom.tool.CustomToolItems
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemItems
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import de.fuballer.mcendgame.main.util.minecraft.RegistryUtil
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKey
import net.minecraft.text.Text

@Injectable
object CustomItemGroups {
    val CUSTOM_ARMOR_KEY = RegistryKey.of(Registries.ITEM_GROUP.key, IdentifierUtil.default("armor"))
    val CUSTOM_ARMOR = RegistryUtil.registerItemGroup(
        CUSTOM_ARMOR_KEY, FabricItemGroup.builder()
            .icon { ItemStack(CustomArmorItems.ICEBORNE) }
            .displayName(Text.translatable("item_group.mcendgame.armor"))
    )

    val CUSTOM_TOOLS_KEY = RegistryKey.of(Registries.ITEM_GROUP.key, IdentifierUtil.default("tools"))
    val CUSTOM_TOOLS = RegistryUtil.registerItemGroup(
        CUSTOM_TOOLS_KEY, FabricItemGroup.builder()
            .icon { ItemStack(CustomToolItems.BLOODHARVEST) }
            .displayName(Text.translatable("item_group.mcendgame.tools"))
    )

    val ASPECTS_KEY = RegistryKey.of(Registries.ITEM_GROUP.key, IdentifierUtil.default("aspects"))
    val ASPECTS = RegistryUtil.registerItemGroup(
        ASPECTS_KEY, FabricItemGroup.builder()
            .icon { ItemStack(AspectItems.ASPECT_OF_TYRANNY) }
            .displayName(Text.translatable("item_group.mcendgame.aspects"))
    )

    val CRYSTALS_KEY = RegistryKey.of(Registries.ITEM_GROUP.key, IdentifierUtil.default("crystals"))
    val CRYSTALS = RegistryUtil.registerItemGroup(
        CRYSTALS_KEY, FabricItemGroup.builder()
            .icon { ItemStack(CrystalItems.CALIBRATION_CRYSTAL) }
            .displayName(Text.translatable("item_group.mcendgame.crystals"))
    )

    val TOTEMS_KEY = RegistryKey.of(Registries.ITEM_GROUP.key, IdentifierUtil.default("totems"))
    val TOTEMS = RegistryUtil.registerItemGroup(
        TOTEMS_KEY, FabricItemGroup.builder()
            .icon { ItemStack(TotemItems.TOTEM_OF_BASTION) }
            .displayName(Text.translatable("item_group.mcendgame.totems"))
    )

    val CUSTOM_BLOCKS_KEY = RegistryKey.of(Registries.ITEM_GROUP.key, IdentifierUtil.default("blocks"))
    val CUSTOM_BLOCKS = RegistryUtil.registerItemGroup(
        CUSTOM_BLOCKS_KEY, FabricItemGroup.builder()
            .icon { ItemStack(CustomBlocks.DUNGEON_DEVICE) }
            .displayName(Text.translatable("item_group.mcendgame.blocks"))
    )

    @Initializer
    fun init() {
        ItemGroupEvents.modifyEntriesEvent(CUSTOM_ARMOR_KEY).register { itemGroup ->
            itemGroup.add(CustomArmorItems.ICEBORNE.defaultStack)
            itemGroup.add(CustomArmorItems.BOUND_ABYSS.defaultStack)
            itemGroup.add(CustomArmorItems.DRUIDS_HELMET.defaultStack)
            itemGroup.add(CustomArmorItems.DRUIDS_CHESTPLATE.defaultStack)
            itemGroup.add(CustomArmorItems.DRUIDS_LEGGINGS.defaultStack)
            itemGroup.add(CustomArmorItems.DRUIDS_BOOTS.defaultStack)
            itemGroup.add(CustomArmorItems.EMBERCHANT.defaultStack)
            itemGroup.add(CustomArmorItems.LAMIAS_GIFT.defaultStack)
            itemGroup.add(CustomArmorItems.WITHER_ROSE_HELMET.defaultStack)
            itemGroup.add(CustomArmorItems.WITHER_ROSE_CHESTPLATE.defaultStack)
            itemGroup.add(CustomArmorItems.WITHER_ROSE_LEGGINGS.defaultStack)
            itemGroup.add(CustomArmorItems.WITHER_ROSE_BOOTS.defaultStack)
            itemGroup.add(CustomArmorItems.SUEDE_HELMET.defaultStack)
            itemGroup.add(CustomArmorItems.SUEDE_CHESTPLATE.defaultStack)
            itemGroup.add(CustomArmorItems.SUEDE_LEGGINGS.defaultStack)
            itemGroup.add(CustomArmorItems.SUEDE_BOOTS.defaultStack)
            itemGroup.add(CustomArmorItems.STONEWARD.defaultStack)
            itemGroup.add(CustomArmorItems.MOONSHADOW.defaultStack)
            itemGroup.add(CustomArmorItems.GEISTERGALOSCHEN.defaultStack)
            itemGroup.add(CustomArmorItems.VOIDWEAVER.defaultStack)
            itemGroup.add(CustomArmorItems.ABYSSAL_MASK.defaultStack)
            itemGroup.add(CustomArmorItems.GILDED_TEMPEST.defaultStack)
            itemGroup.add(CustomArmorItems.WINDSTRIDER.defaultStack)
            itemGroup.add(CustomArmorItems.BROODMOTHER.defaultStack)
            itemGroup.add(CustomArmorItems.EMBERREIGN.defaultStack)
        }
        ItemGroupEvents.modifyEntriesEvent(CUSTOM_TOOLS_KEY).register { itemGroup ->
            itemGroup.add(CustomToolItems.BLOODHARVEST.defaultStack)
            itemGroup.add(CustomToolItems.TWINFIRE.defaultStack)
            itemGroup.add(CustomToolItems.FATESPLITTER.defaultStack)
            itemGroup.add(CustomToolItems.SERPENTS_FANG.defaultStack)
            itemGroup.add(CustomToolItems.NIGHTREAVER.defaultStack)
            itemGroup.add(CustomToolItems.GRAVEBREAKER.defaultStack)
            itemGroup.add(CustomToolItems.RADIANT_DAWN.defaultStack)
            itemGroup.add(CustomToolItems.WINDSTRING.defaultStack)
            itemGroup.add(CustomToolItems.HAILSTORM.defaultStack)
            itemGroup.add(CustomToolItems.DUSK_PIERCER.defaultStack)
            itemGroup.add(CustomMiscItems.VERDANT_ECHO.defaultStack)
            itemGroup.add(CustomMiscItems.MOLTEN_ROAR.defaultStack)
            itemGroup.add(CustomMiscItems.FRIGID_CRY.defaultStack)
            itemGroup.add(CustomToolItems.GRUDGEBEARER.defaultStack)
        }
        ItemGroupEvents.modifyEntriesEvent(ASPECTS_KEY).register { itemGroup ->
            itemGroup.add(AspectItems.ASPECT_OF_TYRANNY.defaultStack)
            itemGroup.add(AspectItems.ASPECT_OF_GREED.defaultStack)
            itemGroup.add(AspectItems.ASPECT_OF_DOMINION.defaultStack)
            itemGroup.add(AspectItems.ASPECT_OF_IMPATIENCE.defaultStack)
            itemGroup.add(AspectItems.ASPECT_OF_HORDES.defaultStack)
            itemGroup.add(AspectItems.ASPECT_OF_CURIO.defaultStack)
            itemGroup.add(AspectItems.ASPECT_OF_FORTUNE.defaultStack)
            itemGroup.add(AspectItems.ASPECT_OF_ZEAL.defaultStack)
            itemGroup.add(AspectItems.ASPECT_OF_GHOSTS.defaultStack)
            itemGroup.add(AspectItems.ASPECT_OF_FORTITUDE.defaultStack)
            itemGroup.add(AspectItems.ASPECT_OF_SAVAGERY.defaultStack)
            itemGroup.add(AspectItems.ASPECT_OF_EMINENCE.defaultStack)
            itemGroup.add(AspectItems.ASPECT_OF_ANCESTORS.defaultStack)
            itemGroup.add(AspectItems.ASPECT_OF_DUALITY.defaultStack)
        }
        ItemGroupEvents.modifyEntriesEvent(CRYSTALS_KEY).register { itemGroup ->
            itemGroup.add(CrystalItems.CALIBRATION_CRYSTAL.defaultStack)
            itemGroup.add(CrystalItems.SACRIFICIAL_CRYSTAL.defaultStack)
            itemGroup.add(CrystalItems.PERMUTATION_CRYSTAL.defaultStack)
            itemGroup.add(CrystalItems.REFORGE_CRYSTAL.defaultStack)
            itemGroup.add(CrystalItems.CORRUPTION_CRYSTAL.defaultStack)
        }
        ItemGroupEvents.modifyEntriesEvent(TOTEMS_KEY).register { itemGroup ->
            itemGroup.add(TotemItems.TOTEM_OF_BASTION.getMaxTierStack())
            itemGroup.add(TotemItems.TOTEM_OF_FORCE.getMaxTierStack())
            itemGroup.add(TotemItems.TOTEM_OF_FORTRESS.getMaxTierStack())
            itemGroup.add(TotemItems.TOTEM_OF_FRENZY.getMaxTierStack())
            itemGroup.add(TotemItems.TOTEM_OF_GRACE.getMaxTierStack())
            itemGroup.add(TotemItems.TOTEM_OF_IMPACT.getMaxTierStack())
            itemGroup.add(TotemItems.TOTEM_OF_SWIFTNESS.getMaxTierStack())
            itemGroup.add(TotemItems.TOTEM_OF_THICKNESS.getMaxTierStack())
            itemGroup.add(TotemItems.TOTEM_OF_VANGUARD.getMaxTierStack())
            itemGroup.add(TotemItems.TOTEM_OF_RENEWAL.getMaxTierStack())
            itemGroup.add(TotemItems.TOTEM_OF_TEMPEST.getMaxTierStack())
            itemGroup.add(TotemItems.TOTEM_OF_DEFIANCE.getMaxTierStack())
            itemGroup.add(TotemItems.TOTEM_OF_RECOVERY.getMaxTierStack())
            itemGroup.add(TotemItems.TOTEM_OF_RIME.getMaxTierStack())
            itemGroup.add(TotemItems.TOTEM_OF_ONSLAUGHT.getMaxTierStack())
            itemGroup.add(TotemItems.TOTEM_OF_RESILIENCE.getMaxTierStack())
            itemGroup.add(TotemItems.TOTEM_OF_FURY.getMaxTierStack())
            itemGroup.add(TotemItems.TOTEM_OF_VOLLEY.getMaxTierStack())
            itemGroup.add(TotemItems.TOTEM_OF_REACH.getMaxTierStack())
            itemGroup.add(TotemItems.TOTEM_OF_GIGANTISM.getMaxTierStack())
            itemGroup.add(TotemItems.TOTEM_OF_RESTORATION.getMaxTierStack())
        }
        ItemGroupEvents.modifyEntriesEvent(CUSTOM_BLOCKS_KEY).register { itemGroup ->
            itemGroup.add(CustomBlocks.DUNGEON_DEVICE)
            itemGroup.add(CustomBlocks.CRYSTAL_FORGE)
            itemGroup.add(CustomBlocks.DECAYING_COBWEB)
            itemGroup.add(CustomBlocks.TOTEM_STATUE)
        }
    }
}