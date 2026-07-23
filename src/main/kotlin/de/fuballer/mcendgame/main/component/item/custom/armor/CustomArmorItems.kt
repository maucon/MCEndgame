package de.fuballer.mcendgame.main.component.item.custom.armor

import de.fuballer.mcendgame.main.component.item.custom.UniqueItemRegistry
import de.fuballer.mcendgame.main.component.item.custom.armor.item.abyssal_mask.AbyssalMask
import de.fuballer.mcendgame.main.component.item.custom.armor.item.bound_abyss.BoundAbyss
import de.fuballer.mcendgame.main.component.item.custom.armor.item.broodmother.Broodmother
import de.fuballer.mcendgame.main.component.item.custom.armor.item.druids.DruidsBoots
import de.fuballer.mcendgame.main.component.item.custom.armor.item.druids.DruidsChestplate
import de.fuballer.mcendgame.main.component.item.custom.armor.item.druids.DruidsHelmet
import de.fuballer.mcendgame.main.component.item.custom.armor.item.druids.DruidsLeggings
import de.fuballer.mcendgame.main.component.item.custom.armor.item.emberchant.Emberchant
import de.fuballer.mcendgame.main.component.item.custom.armor.item.emberreign.Emberreign
import de.fuballer.mcendgame.main.component.item.custom.armor.item.geistergaloschen.Geistergaloschen
import de.fuballer.mcendgame.main.component.item.custom.armor.item.gilded_tempest.GildedTempest
import de.fuballer.mcendgame.main.component.item.custom.armor.item.iceborne.Iceborne
import de.fuballer.mcendgame.main.component.item.custom.armor.item.lamias_gift.LamiasGift
import de.fuballer.mcendgame.main.component.item.custom.armor.item.moonshadow.Moonshadow
import de.fuballer.mcendgame.main.component.item.custom.armor.item.stoneward.Stoneward
import de.fuballer.mcendgame.main.component.item.custom.armor.item.suede.SuedeBoots
import de.fuballer.mcendgame.main.component.item.custom.armor.item.suede.SuedeChestplate
import de.fuballer.mcendgame.main.component.item.custom.armor.item.suede.SuedeHelmet
import de.fuballer.mcendgame.main.component.item.custom.armor.item.suede.SuedeLeggings
import de.fuballer.mcendgame.main.component.item.custom.armor.item.voidweaver.Voidweaver
import de.fuballer.mcendgame.main.component.item.custom.armor.item.windstrider.Windstrider
import de.fuballer.mcendgame.main.component.item.custom.armor.item.wither_rose.WitherRoseBoots
import de.fuballer.mcendgame.main.component.item.custom.armor.item.wither_rose.WitherRoseChestplate
import de.fuballer.mcendgame.main.component.item.custom.armor.item.wither_rose.WitherRoseHelmet
import de.fuballer.mcendgame.main.component.item.custom.armor.item.wither_rose.WitherRoseLeggings
import de.fuballer.mcendgame.main.component.item.custom.armor.materials.*
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.world.item.equipment.ArmorType

@Injectable
object CustomArmorItems {
    val ICEBORNE = UniqueItemRegistry.registerArmorItem(::Iceborne, IceborneArmorMaterial, ArmorType.HELMET, CustomArmorItemIds.ICEBORNE)
    val BOUND_ABYSS = UniqueItemRegistry.registerArmorItem(::BoundAbyss, BoundAbyssArmorMaterial, ArmorType.CHESTPLATE, CustomArmorItemIds.BOUND_ABYSS)
    val DRUIDS_HELMET = UniqueItemRegistry.registerArmorItem(::DruidsHelmet, DruidsArmorMaterial, ArmorType.HELMET, CustomArmorItemIds.DRUIDS_HELMET)
    val DRUIDS_CHESTPLATE = UniqueItemRegistry.registerArmorItem(::DruidsChestplate, DruidsArmorMaterial, ArmorType.CHESTPLATE, CustomArmorItemIds.DRUIDS_CHESTPLATE)
    val DRUIDS_LEGGINGS = UniqueItemRegistry.registerArmorItem(::DruidsLeggings, DruidsArmorMaterial, ArmorType.LEGGINGS, CustomArmorItemIds.DRUIDS_LEGGINGS)
    val DRUIDS_BOOTS = UniqueItemRegistry.registerArmorItem(::DruidsBoots, DruidsArmorMaterial, ArmorType.BOOTS, CustomArmorItemIds.DRUIDS_BOOTS)
    val EMBERCHANT = UniqueItemRegistry.registerArmorItem(::Emberchant, EmberchantArmorMaterial, ArmorType.HELMET, CustomArmorItemIds.EMBERCHANT)
    val LAMIAS_GIFT = UniqueItemRegistry.registerArmorItem(::LamiasGift, LamiasGiftArmorMaterial, ArmorType.LEGGINGS, CustomArmorItemIds.LAMIAS_GIFT)
    val WITHER_ROSE_HELMET = UniqueItemRegistry.registerArmorItem(::WitherRoseHelmet, WitherRoseArmorMaterial, ArmorType.HELMET, CustomArmorItemIds.WITHER_ROSE_HELMET)
    val WITHER_ROSE_CHESTPLATE = UniqueItemRegistry.registerArmorItem(::WitherRoseChestplate, WitherRoseArmorMaterial, ArmorType.CHESTPLATE, CustomArmorItemIds.WITHER_ROSE_CHESTPLATE)
    val WITHER_ROSE_LEGGINGS = UniqueItemRegistry.registerArmorItem(::WitherRoseLeggings, WitherRoseArmorMaterial, ArmorType.LEGGINGS, CustomArmorItemIds.WITHER_ROSE_LEGGINGS)
    val WITHER_ROSE_BOOTS = UniqueItemRegistry.registerArmorItem(::WitherRoseBoots, WitherRoseArmorMaterial, ArmorType.BOOTS, CustomArmorItemIds.WITHER_ROSE_BOOTS)
    val SUEDE_HELMET = UniqueItemRegistry.registerArmorItem(::SuedeHelmet, SuedeArmorMaterial, ArmorType.HELMET, CustomArmorItemIds.SUEDE_HELMET)
    val SUEDE_CHESTPLATE = UniqueItemRegistry.registerArmorItem(::SuedeChestplate, SuedeArmorMaterial, ArmorType.CHESTPLATE, CustomArmorItemIds.SUEDE_CHESTPLATE)
    val SUEDE_LEGGINGS = UniqueItemRegistry.registerArmorItem(::SuedeLeggings, SuedeArmorMaterial, ArmorType.LEGGINGS, CustomArmorItemIds.SUEDE_LEGGINGS)
    val SUEDE_BOOTS = UniqueItemRegistry.registerArmorItem(::SuedeBoots, SuedeArmorMaterial, ArmorType.BOOTS, CustomArmorItemIds.SUEDE_BOOTS)
    val STONEWARD = UniqueItemRegistry.registerArmorItem(::Stoneward, StonewardArmorMaterial, ArmorType.LEGGINGS, CustomArmorItemIds.STONEWARD)
    val MOONSHADOW = UniqueItemRegistry.registerArmorItem(::Moonshadow, MoonshadowArmorMaterial, ArmorType.BOOTS, CustomArmorItemIds.MOONSHADOW)
    val GEISTERGALOSCHEN = UniqueItemRegistry.registerArmorItem(::Geistergaloschen, GeistergaloschenArmorMaterial, ArmorType.BOOTS, CustomArmorItemIds.GEISTERGALOSCHEN)
    val VOIDWEAVER = UniqueItemRegistry.registerArmorItem(::Voidweaver, VoidweaverArmorMaterial, ArmorType.CHESTPLATE, CustomArmorItemIds.VOIDWEAVER)
    val ABYSSAL_MASK = UniqueItemRegistry.registerArmorItem(::AbyssalMask, AbyssalMaskArmorMaterial, ArmorType.HELMET, CustomArmorItemIds.ABYSSAL_MASK)
    val GILDED_TEMPEST = UniqueItemRegistry.registerArmorItem(::GildedTempest, GildedTempestArmorMaterial, ArmorType.LEGGINGS, CustomArmorItemIds.GILDED_TEMPEST)
    val WINDSTRIDER = UniqueItemRegistry.registerArmorItem(::Windstrider, WindstriderArmorMaterial, ArmorType.LEGGINGS, CustomArmorItemIds.WINDSTRIDER)
    val BROODMOTHER = UniqueItemRegistry.registerArmorItem(::Broodmother, BroodmotherArmorMaterial, ArmorType.CHESTPLATE, CustomArmorItemIds.BROODMOTHER)
    val EMBERREIGN = UniqueItemRegistry.registerArmorItem(::Emberreign, EmberreignArmorMaterial, ArmorType.BOOTS, CustomArmorItemIds.EMBERREIGN)
}