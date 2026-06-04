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
    val ICEBORNE = UniqueItemRegistry.registerArmorItem(::Iceborne, IceborneArmorMaterial, ArmorType.HELMET, "iceborne")
    val BOUND_ABYSS = UniqueItemRegistry.registerArmorItem(::BoundAbyss, BoundAbyssArmorMaterial, ArmorType.CHESTPLATE, "bound_abyss")
    val DRUIDS_HELMET = UniqueItemRegistry.registerArmorItem(::DruidsHelmet, DruidsArmorMaterial, ArmorType.HELMET, "druids_helmet")
    val DRUIDS_CHESTPLATE = UniqueItemRegistry.registerArmorItem(::DruidsChestplate, DruidsArmorMaterial, ArmorType.CHESTPLATE, "druids_chestplate")
    val DRUIDS_LEGGINGS = UniqueItemRegistry.registerArmorItem(::DruidsLeggings, DruidsArmorMaterial, ArmorType.LEGGINGS, "druids_leggings")
    val DRUIDS_BOOTS = UniqueItemRegistry.registerArmorItem(::DruidsBoots, DruidsArmorMaterial, ArmorType.BOOTS, "druids_boots")
    val EMBERCHANT = UniqueItemRegistry.registerArmorItem(::Emberchant, EmberchantArmorMaterial, ArmorType.HELMET, "emberchant")
    val LAMIAS_GIFT = UniqueItemRegistry.registerArmorItem(::LamiasGift, LamiasGiftArmorMaterial, ArmorType.LEGGINGS, "lamias_gift")
    val WITHER_ROSE_HELMET = UniqueItemRegistry.registerArmorItem(::WitherRoseHelmet, WitherRoseArmorMaterial, ArmorType.HELMET, "wither_rose_helmet")
    val WITHER_ROSE_CHESTPLATE = UniqueItemRegistry.registerArmorItem(::WitherRoseChestplate, WitherRoseArmorMaterial, ArmorType.CHESTPLATE, "wither_rose_chestplate")
    val WITHER_ROSE_LEGGINGS = UniqueItemRegistry.registerArmorItem(::WitherRoseLeggings, WitherRoseArmorMaterial, ArmorType.LEGGINGS, "wither_rose_leggings")
    val WITHER_ROSE_BOOTS = UniqueItemRegistry.registerArmorItem(::WitherRoseBoots, WitherRoseArmorMaterial, ArmorType.BOOTS, "wither_rose_boots")
    val SUEDE_HELMET = UniqueItemRegistry.registerArmorItem(::SuedeHelmet, SuedeArmorMaterial, ArmorType.HELMET, "suede_helmet")
    val SUEDE_CHESTPLATE = UniqueItemRegistry.registerArmorItem(::SuedeChestplate, SuedeArmorMaterial, ArmorType.CHESTPLATE, "suede_chestplate")
    val SUEDE_LEGGINGS = UniqueItemRegistry.registerArmorItem(::SuedeLeggings, SuedeArmorMaterial, ArmorType.LEGGINGS, "suede_leggings")
    val SUEDE_BOOTS = UniqueItemRegistry.registerArmorItem(::SuedeBoots, SuedeArmorMaterial, ArmorType.BOOTS, "suede_boots")
    val STONEWARD = UniqueItemRegistry.registerArmorItem(::Stoneward, StonewardArmorMaterial, ArmorType.LEGGINGS, "stoneward")
    val MOONSHADOW = UniqueItemRegistry.registerArmorItem(::Moonshadow, MoonshadowArmorMaterial, ArmorType.BOOTS, "moonshadow")
    val GEISTERGALOSCHEN = UniqueItemRegistry.registerArmorItem(::Geistergaloschen, GeistergaloschenArmorMaterial, ArmorType.BOOTS, "geistergaloschen")
    val VOIDWEAVER = UniqueItemRegistry.registerArmorItem(::Voidweaver, VoidweaverArmorMaterial, ArmorType.CHESTPLATE, "voidweaver")
    val ABYSSAL_MASK = UniqueItemRegistry.registerArmorItem(::AbyssalMask, AbyssalMaskArmorMaterial, ArmorType.HELMET, "abyssal_mask")
    val GILDED_TEMPEST = UniqueItemRegistry.registerArmorItem(::GildedTempest, GildedTempestArmorMaterial, ArmorType.LEGGINGS, "gilded_tempest")
    val WINDSTRIDER = UniqueItemRegistry.registerArmorItem(::Windstrider, WindstriderArmorMaterial, ArmorType.LEGGINGS, "windstrider")
    val BROODMOTHER = UniqueItemRegistry.registerArmorItem(::Broodmother, BroodmotherArmorMaterial, ArmorType.CHESTPLATE, "broodmother")
    val EMBERREIGN = UniqueItemRegistry.registerArmorItem(::Emberreign, EmberreignArmorMaterial, ArmorType.BOOTS, "emberreign")
}