package de.fuballer.mcendgame.main.component.item.custom.creative_mode_tab

import de.fuballer.mcendgame.main.component.block.CustomBlocks
import de.fuballer.mcendgame.main.component.item.custom.armor.CustomArmorItems
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItems
import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItems
import de.fuballer.mcendgame.main.component.item.custom.misc.CustomMiscItems
import de.fuballer.mcendgame.main.component.item.custom.tool.CustomToolItems
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemItems
import de.fuballer.mcendgame.main.util.minecraft.RegistryUtil
import de.maucon.mauconframework.di.annotation.Injectable
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

@Injectable
object CustomCreativeModeTab {
    init {
        RegistryUtil.registerCreativeModeTab(
            "armor",
            FabricCreativeModeTab.builder()
                .icon { ItemStack(CustomArmorItems.ICEBORNE) }
                .title(Component.translatable("item_group.mcendgame.armor"))
                .displayItems { _, output ->
                    output.accept(CustomArmorItems.ICEBORNE.defaultInstance)
                    output.accept(CustomArmorItems.BOUND_ABYSS.defaultInstance)
                    output.accept(CustomArmorItems.DRUIDS_HELMET.defaultInstance)
                    output.accept(CustomArmorItems.DRUIDS_CHESTPLATE.defaultInstance)
                    output.accept(CustomArmorItems.DRUIDS_LEGGINGS.defaultInstance)
                    output.accept(CustomArmorItems.DRUIDS_BOOTS.defaultInstance)
                    output.accept(CustomArmorItems.EMBERCHANT.defaultInstance)
                    output.accept(CustomArmorItems.LAMIAS_GIFT.defaultInstance)
                    output.accept(CustomArmorItems.WITHER_ROSE_HELMET.defaultInstance)
                    output.accept(CustomArmorItems.WITHER_ROSE_CHESTPLATE.defaultInstance)
                    output.accept(CustomArmorItems.WITHER_ROSE_LEGGINGS.defaultInstance)
                    output.accept(CustomArmorItems.WITHER_ROSE_BOOTS.defaultInstance)
                    output.accept(CustomArmorItems.SUEDE_HELMET.defaultInstance)
                    output.accept(CustomArmorItems.SUEDE_CHESTPLATE.defaultInstance)
                    output.accept(CustomArmorItems.SUEDE_LEGGINGS.defaultInstance)
                    output.accept(CustomArmorItems.SUEDE_BOOTS.defaultInstance)
                    output.accept(CustomArmorItems.STONEWARD.defaultInstance)
                    output.accept(CustomArmorItems.MOONSHADOW.defaultInstance)
                    output.accept(CustomArmorItems.GEISTERGALOSCHEN.defaultInstance)
                    output.accept(CustomArmorItems.VOIDWEAVER.defaultInstance)
                    output.accept(CustomArmorItems.ABYSSAL_MASK.defaultInstance)
                    output.accept(CustomArmorItems.GILDED_TEMPEST.defaultInstance)
                    output.accept(CustomArmorItems.WINDSTRIDER.defaultInstance)
                    output.accept(CustomArmorItems.BROODMOTHER.defaultInstance)
                    output.accept(CustomArmorItems.EMBERREIGN.defaultInstance)
                }
        )
        RegistryUtil.registerCreativeModeTab(
            "tools",
            FabricCreativeModeTab.builder()
                .icon { ItemStack(CustomToolItems.BLOODHARVEST) }
                .title(Component.translatable("item_group.mcendgame.tools"))
                .displayItems { _, output ->
                    output.accept(CustomToolItems.BLOODHARVEST.defaultInstance)
                    output.accept(CustomToolItems.TWINFIRE.defaultInstance)
                    output.accept(CustomToolItems.FATESPLITTER.defaultInstance)
                    output.accept(CustomToolItems.SERPENTS_FANG.defaultInstance)
                    output.accept(CustomToolItems.NIGHTREAVER.defaultInstance)
                    output.accept(CustomToolItems.GRAVEBREAKER.defaultInstance)
                    output.accept(CustomToolItems.RADIANT_DAWN.defaultInstance)
                    output.accept(CustomToolItems.WINDSTRING.defaultInstance)
                    output.accept(CustomToolItems.HAILSTORM.defaultInstance)
                    output.accept(CustomToolItems.DUSK_PIERCER.defaultInstance)
                    output.accept(CustomMiscItems.VERDANT_ECHO.defaultInstance)
                    output.accept(CustomMiscItems.MOLTEN_ROAR.defaultInstance)
                    output.accept(CustomMiscItems.FRIGID_CRY.defaultInstance)
                    output.accept(CustomToolItems.GRUDGEBEARER.defaultInstance)
                }
        )
        RegistryUtil.registerCreativeModeTab(
            "aspects",
            FabricCreativeModeTab.builder()
                .icon { ItemStack(AspectItems.ASPECT_OF_TYRANNY) }
                .title(Component.translatable("item_group.mcendgame.aspects"))
                .displayItems { _, output ->
                    output.accept(AspectItems.ASPECT_OF_TYRANNY.defaultInstance)
                    output.accept(AspectItems.ASPECT_OF_GREED.defaultInstance)
                    output.accept(AspectItems.ASPECT_OF_DOMINION.defaultInstance)
                    output.accept(AspectItems.ASPECT_OF_IMPATIENCE.defaultInstance)
                    output.accept(AspectItems.ASPECT_OF_HORDES.defaultInstance)
                    output.accept(AspectItems.ASPECT_OF_CURIO.defaultInstance)
                    output.accept(AspectItems.ASPECT_OF_FORTUNE.defaultInstance)
                    output.accept(AspectItems.ASPECT_OF_ZEAL.defaultInstance)
                    output.accept(AspectItems.ASPECT_OF_GHOSTS.defaultInstance)
                    output.accept(AspectItems.ASPECT_OF_FORTITUDE.defaultInstance)
                    output.accept(AspectItems.ASPECT_OF_SAVAGERY.defaultInstance)
                    output.accept(AspectItems.ASPECT_OF_EMINENCE.defaultInstance)
                    output.accept(AspectItems.ASPECT_OF_ANCESTORS.defaultInstance)
                    output.accept(AspectItems.ASPECT_OF_DUALITY.defaultInstance)
                }
        )
        RegistryUtil.registerCreativeModeTab(
            "crystals",
            FabricCreativeModeTab.builder()
                .icon { ItemStack(CrystalItems.CALIBRATION_CRYSTAL) }
                .title(Component.translatable("item_group.mcendgame.crystals"))
                .displayItems { _, output ->
                    output.accept(CrystalItems.CALIBRATION_CRYSTAL.defaultInstance)
                    output.accept(CrystalItems.SACRIFICIAL_CRYSTAL.defaultInstance)
                    output.accept(CrystalItems.PERMUTATION_CRYSTAL.defaultInstance)
                    output.accept(CrystalItems.REFORGE_CRYSTAL.defaultInstance)
                    output.accept(CrystalItems.CORRUPTION_CRYSTAL.defaultInstance)
                }
        )
        RegistryUtil.registerCreativeModeTab(
            "totems",
            FabricCreativeModeTab.builder()
                .icon { ItemStack(TotemItems.TOTEM_OF_BASTION) }
                .title(Component.translatable("item_group.mcendgame.totems"))
                .displayItems { _, output ->
                    output.accept(TotemItems.TOTEM_OF_BASTION.getMaxTierStack())
                    output.accept(TotemItems.TOTEM_OF_FORCE.getMaxTierStack())
                    output.accept(TotemItems.TOTEM_OF_FORTRESS.getMaxTierStack())
                    output.accept(TotemItems.TOTEM_OF_FRENZY.getMaxTierStack())
                    output.accept(TotemItems.TOTEM_OF_GRACE.getMaxTierStack())
                    output.accept(TotemItems.TOTEM_OF_IMPACT.getMaxTierStack())
                    output.accept(TotemItems.TOTEM_OF_SWIFTNESS.getMaxTierStack())
                    output.accept(TotemItems.TOTEM_OF_THICKNESS.getMaxTierStack())
                    output.accept(TotemItems.TOTEM_OF_VANGUARD.getMaxTierStack())
                    output.accept(TotemItems.TOTEM_OF_RENEWAL.getMaxTierStack())
                    output.accept(TotemItems.TOTEM_OF_TEMPEST.getMaxTierStack())
                    output.accept(TotemItems.TOTEM_OF_DEFIANCE.getMaxTierStack())
                    output.accept(TotemItems.TOTEM_OF_RECOVERY.getMaxTierStack())
                    output.accept(TotemItems.TOTEM_OF_RIME.getMaxTierStack())
                    output.accept(TotemItems.TOTEM_OF_ONSLAUGHT.getMaxTierStack())
                    output.accept(TotemItems.TOTEM_OF_RESILIENCE.getMaxTierStack())
                    output.accept(TotemItems.TOTEM_OF_FURY.getMaxTierStack())
                    output.accept(TotemItems.TOTEM_OF_VOLLEY.getMaxTierStack())
                    output.accept(TotemItems.TOTEM_OF_REACH.getMaxTierStack())
                    output.accept(TotemItems.TOTEM_OF_GIGANTISM.getMaxTierStack())
                    output.accept(TotemItems.TOTEM_OF_RESTORATION.getMaxTierStack())
                }
        )
        RegistryUtil.registerCreativeModeTab(
            "blocks",
            FabricCreativeModeTab.builder()
                .icon { ItemStack(CustomBlocks.DUNGEON_DEVICE) }
                .title(Component.translatable("item_group.mcendgame.blocks"))
                .displayItems { _, output ->
                    output.accept(CustomBlocks.DUNGEON_DEVICE)
                    output.accept(CustomBlocks.CRYSTAL_FORGE)
                    output.accept(CustomBlocks.DECAYING_COBWEB)
                    output.accept(CustomBlocks.TOTEM_STATUE)
                }
        )
    }
}