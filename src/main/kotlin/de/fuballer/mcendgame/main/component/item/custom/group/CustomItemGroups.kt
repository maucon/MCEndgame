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
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack

@Injectable
object CustomItemGroups {
    val CUSTOM_ARMOR_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), IdentifierUtil.default("armor"))
        .also {
            RegistryUtil.registerItemGroup(
                it, FabricItemGroup.builder()
                    .icon { ItemStack(CustomArmorItems.ICEBORNE) }
                    .title(Component.translatable("item_group.mcendgame.armor"))
            )
        }
    val CUSTOM_TOOLS_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), IdentifierUtil.default("tools"))
        .also {
            RegistryUtil.registerItemGroup(
                it, FabricItemGroup.builder()
                    .icon { ItemStack(CustomToolItems.BLOODHARVEST) }
                    .title(Component.translatable("item_group.mcendgame.tools"))
            )
        }
    val ASPECTS_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), IdentifierUtil.default("aspects"))
        .also {
            RegistryUtil.registerItemGroup(
                it, FabricItemGroup.builder()
                    .icon { ItemStack(AspectItems.ASPECT_OF_TYRANNY) }
                    .title(Component.translatable("item_group.mcendgame.aspects"))
            )
        }
    val CRYSTALS_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), IdentifierUtil.default("crystals"))
        .also {
            RegistryUtil.registerItemGroup(
                it, FabricItemGroup.builder()
                    .icon { ItemStack(CrystalItems.CALIBRATION_CRYSTAL) }
                    .title(Component.translatable("item_group.mcendgame.crystals"))
            )
        }
    val TOTEMS_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), IdentifierUtil.default("totems")).also {
        RegistryUtil.registerItemGroup(
            it, FabricItemGroup.builder()
                .icon { ItemStack(TotemItems.TOTEM_OF_BASTION) }
                .title(Component.translatable("item_group.mcendgame.totems"))
        )
    }
    val CUSTOM_BLOCKS_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), IdentifierUtil.default("blocks"))
        .also {
            RegistryUtil.registerItemGroup(
                it, FabricItemGroup.builder()
                    .icon { ItemStack(CustomBlocks.DUNGEON_DEVICE) }
                    .title(Component.translatable("item_group.mcendgame.blocks"))
            )
        }

    @Initializer
    fun init() {
        ItemGroupEvents.modifyEntriesEvent(CUSTOM_ARMOR_KEY).register { itemGroup ->
            itemGroup.accept(CustomArmorItems.ICEBORNE.defaultInstance)
            itemGroup.accept(CustomArmorItems.BOUND_ABYSS.defaultInstance)
            itemGroup.accept(CustomArmorItems.DRUIDS_HELMET.defaultInstance)
            itemGroup.accept(CustomArmorItems.DRUIDS_CHESTPLATE.defaultInstance)
            itemGroup.accept(CustomArmorItems.DRUIDS_LEGGINGS.defaultInstance)
            itemGroup.accept(CustomArmorItems.DRUIDS_BOOTS.defaultInstance)
            itemGroup.accept(CustomArmorItems.EMBERCHANT.defaultInstance)
            itemGroup.accept(CustomArmorItems.LAMIAS_GIFT.defaultInstance)
            itemGroup.accept(CustomArmorItems.WITHER_ROSE_HELMET.defaultInstance)
            itemGroup.accept(CustomArmorItems.WITHER_ROSE_CHESTPLATE.defaultInstance)
            itemGroup.accept(CustomArmorItems.WITHER_ROSE_LEGGINGS.defaultInstance)
            itemGroup.accept(CustomArmorItems.WITHER_ROSE_BOOTS.defaultInstance)
            itemGroup.accept(CustomArmorItems.SUEDE_HELMET.defaultInstance)
            itemGroup.accept(CustomArmorItems.SUEDE_CHESTPLATE.defaultInstance)
            itemGroup.accept(CustomArmorItems.SUEDE_LEGGINGS.defaultInstance)
            itemGroup.accept(CustomArmorItems.SUEDE_BOOTS.defaultInstance)
            itemGroup.accept(CustomArmorItems.STONEWARD.defaultInstance)
            itemGroup.accept(CustomArmorItems.MOONSHADOW.defaultInstance)
            itemGroup.accept(CustomArmorItems.GEISTERGALOSCHEN.defaultInstance)
            itemGroup.accept(CustomArmorItems.VOIDWEAVER.defaultInstance)
            itemGroup.accept(CustomArmorItems.ABYSSAL_MASK.defaultInstance)
            itemGroup.accept(CustomArmorItems.GILDED_TEMPEST.defaultInstance)
            itemGroup.accept(CustomArmorItems.WINDSTRIDER.defaultInstance)
            itemGroup.accept(CustomArmorItems.BROODMOTHER.defaultInstance)
            itemGroup.accept(CustomArmorItems.EMBERREIGN.defaultInstance)
        }
        ItemGroupEvents.modifyEntriesEvent(CUSTOM_TOOLS_KEY).register { itemGroup ->
            itemGroup.accept(CustomToolItems.BLOODHARVEST.defaultInstance)
            itemGroup.accept(CustomToolItems.TWINFIRE.defaultInstance)
            itemGroup.accept(CustomToolItems.FATESPLITTER.defaultInstance)
            itemGroup.accept(CustomToolItems.SERPENTS_FANG.defaultInstance)
            itemGroup.accept(CustomToolItems.NIGHTREAVER.defaultInstance)
            itemGroup.accept(CustomToolItems.GRAVEBREAKER.defaultInstance)
            itemGroup.accept(CustomToolItems.RADIANT_DAWN.defaultInstance)
            itemGroup.accept(CustomToolItems.WINDSTRING.defaultInstance)
            itemGroup.accept(CustomToolItems.HAILSTORM.defaultInstance)
            itemGroup.accept(CustomToolItems.DUSK_PIERCER.defaultInstance)
            itemGroup.accept(CustomMiscItems.VERDANT_ECHO.defaultInstance)
            itemGroup.accept(CustomMiscItems.MOLTEN_ROAR.defaultInstance)
            itemGroup.accept(CustomMiscItems.FRIGID_CRY.defaultInstance)
            itemGroup.accept(CustomToolItems.GRUDGEBEARER.defaultInstance)
        }
        ItemGroupEvents.modifyEntriesEvent(ASPECTS_KEY).register { itemGroup ->
            itemGroup.accept(AspectItems.ASPECT_OF_TYRANNY.defaultInstance)
            itemGroup.accept(AspectItems.ASPECT_OF_GREED.defaultInstance)
            itemGroup.accept(AspectItems.ASPECT_OF_DOMINION.defaultInstance)
            itemGroup.accept(AspectItems.ASPECT_OF_IMPATIENCE.defaultInstance)
            itemGroup.accept(AspectItems.ASPECT_OF_HORDES.defaultInstance)
            itemGroup.accept(AspectItems.ASPECT_OF_CURIO.defaultInstance)
            itemGroup.accept(AspectItems.ASPECT_OF_FORTUNE.defaultInstance)
            itemGroup.accept(AspectItems.ASPECT_OF_ZEAL.defaultInstance)
            itemGroup.accept(AspectItems.ASPECT_OF_GHOSTS.defaultInstance)
            itemGroup.accept(AspectItems.ASPECT_OF_FORTITUDE.defaultInstance)
            itemGroup.accept(AspectItems.ASPECT_OF_SAVAGERY.defaultInstance)
            itemGroup.accept(AspectItems.ASPECT_OF_EMINENCE.defaultInstance)
            itemGroup.accept(AspectItems.ASPECT_OF_ANCESTORS.defaultInstance)
            itemGroup.accept(AspectItems.ASPECT_OF_DUALITY.defaultInstance)
        }
        ItemGroupEvents.modifyEntriesEvent(CRYSTALS_KEY).register { itemGroup ->
            itemGroup.accept(CrystalItems.CALIBRATION_CRYSTAL.defaultInstance)
            itemGroup.accept(CrystalItems.SACRIFICIAL_CRYSTAL.defaultInstance)
            itemGroup.accept(CrystalItems.PERMUTATION_CRYSTAL.defaultInstance)
            itemGroup.accept(CrystalItems.REFORGE_CRYSTAL.defaultInstance)
            itemGroup.accept(CrystalItems.CORRUPTION_CRYSTAL.defaultInstance)
        }
        ItemGroupEvents.modifyEntriesEvent(TOTEMS_KEY).register { itemGroup ->
            itemGroup.accept(TotemItems.TOTEM_OF_BASTION.getMaxTierStack())
            itemGroup.accept(TotemItems.TOTEM_OF_FORCE.getMaxTierStack())
            itemGroup.accept(TotemItems.TOTEM_OF_FORTRESS.getMaxTierStack())
            itemGroup.accept(TotemItems.TOTEM_OF_FRENZY.getMaxTierStack())
            itemGroup.accept(TotemItems.TOTEM_OF_GRACE.getMaxTierStack())
            itemGroup.accept(TotemItems.TOTEM_OF_IMPACT.getMaxTierStack())
            itemGroup.accept(TotemItems.TOTEM_OF_SWIFTNESS.getMaxTierStack())
            itemGroup.accept(TotemItems.TOTEM_OF_THICKNESS.getMaxTierStack())
            itemGroup.accept(TotemItems.TOTEM_OF_VANGUARD.getMaxTierStack())
            itemGroup.accept(TotemItems.TOTEM_OF_RENEWAL.getMaxTierStack())
            itemGroup.accept(TotemItems.TOTEM_OF_TEMPEST.getMaxTierStack())
            itemGroup.accept(TotemItems.TOTEM_OF_DEFIANCE.getMaxTierStack())
            itemGroup.accept(TotemItems.TOTEM_OF_RECOVERY.getMaxTierStack())
            itemGroup.accept(TotemItems.TOTEM_OF_RIME.getMaxTierStack())
            itemGroup.accept(TotemItems.TOTEM_OF_ONSLAUGHT.getMaxTierStack())
            itemGroup.accept(TotemItems.TOTEM_OF_RESILIENCE.getMaxTierStack())
            itemGroup.accept(TotemItems.TOTEM_OF_FURY.getMaxTierStack())
            itemGroup.accept(TotemItems.TOTEM_OF_VOLLEY.getMaxTierStack())
            itemGroup.accept(TotemItems.TOTEM_OF_REACH.getMaxTierStack())
            itemGroup.accept(TotemItems.TOTEM_OF_GIGANTISM.getMaxTierStack())
            itemGroup.accept(TotemItems.TOTEM_OF_RESTORATION.getMaxTierStack())
        }
        ItemGroupEvents.modifyEntriesEvent(CUSTOM_BLOCKS_KEY).register { itemGroup ->
            itemGroup.accept(CustomBlocks.DUNGEON_DEVICE)
            itemGroup.accept(CustomBlocks.CRYSTAL_FORGE)
            itemGroup.accept(CustomBlocks.DECAYING_COBWEB)
            itemGroup.accept(CustomBlocks.TOTEM_STATUE)
        }
    }
}