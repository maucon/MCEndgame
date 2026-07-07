package de.fuballer.mcendgame.main.component.item.custom.armor

import de.fuballer.mcendgame.main.component.item.custom.UniqueItemRegistry
import de.fuballer.mcendgame.main.component.item.custom.armor.item.abyssal_mask.AbyssalMask
import de.fuballer.mcendgame.main.component.item.custom.armor.item.bound_abyss.BoundAbyss
import de.fuballer.mcendgame.main.component.item.custom.armor.item.broodmother.Broodmother
import de.fuballer.mcendgame.main.component.item.custom.armor.item.druids.DruidsBoots
import de.fuballer.mcendgame.main.component.item.custom.armor.item.druids.DruidsChestplate
import de.fuballer.mcendgame.main.component.item.custom.armor.item.druids.DruidsHelmet
import de.fuballer.mcendgame.main.component.item.custom.armor.item.druids.DruidsLeggings
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
import net.minecraft.item.ArmorItem
import net.minecraft.item.Item

@Injectable
object CustomArmorItems {
    val ICEBORNE = UniqueItemRegistry.registerItem(
        Iceborne(
            IceborneArmorMaterial.instance,
            ArmorItem.Type.HELMET,
            Item.Settings().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(IceborneArmorMaterial.baseDurability))
        ),
        "iceborne"
    )
    val BOUND_ABYSS = UniqueItemRegistry.registerItem(
        BoundAbyss(
            BoundAbyssArmorMaterial.instance,
            ArmorItem.Type.CHESTPLATE,
            Item.Settings().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(BoundAbyssArmorMaterial.baseDurability))
        ),
        "bound_abyss"
    )
    val DRUIDS_HELMET = UniqueItemRegistry.registerItem(
        DruidsHelmet(
            DruidsArmorMaterial.instance,
            ArmorItem.Type.HELMET,
            Item.Settings().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(DruidsArmorMaterial.baseDurability))
        ),
        "druids_helmet"
    )
    val DRUIDS_CHESTPLATE = UniqueItemRegistry.registerItem(
        DruidsChestplate(
            DruidsArmorMaterial.instance,
            ArmorItem.Type.HELMET,
            Item.Settings().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(DruidsArmorMaterial.baseDurability))
        ),
        "druids_chestplate"
    )
    val DRUIDS_LEGGINGS = UniqueItemRegistry.registerItem(
        DruidsLeggings(
            DruidsArmorMaterial.instance,
            ArmorItem.Type.HELMET,
            Item.Settings().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(DruidsArmorMaterial.baseDurability))
        ),
        "druids_leggings"
    )
    val DRUIDS_BOOTS = UniqueItemRegistry.registerItem(
        DruidsBoots(
            DruidsArmorMaterial.instance,
            ArmorItem.Type.HELMET,
            Item.Settings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(DruidsArmorMaterial.baseDurability))
        ),
        "druids_boots"
    )
    val EMBERCHANT = UniqueItemRegistry.registerItem(
        DruidsBoots(
            EmberchantArmorMaterial.instance,
            ArmorItem.Type.HELMET,
            Item.Settings().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(EmberchantArmorMaterial.baseDurability))
        ),
        "emberchant"
    )
    val LAMIAS_GIFT = UniqueItemRegistry.registerItem(
        LamiasGift(
            LamiasGiftArmorMaterial.instance,
            ArmorItem.Type.LEGGINGS,
            Item.Settings().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(LamiasGiftArmorMaterial.baseDurability))
        ),
        "lamias_gift"
    )
    val WITHER_ROSE_HELMET = UniqueItemRegistry.registerItem(
        WitherRoseHelmet(
            WitherRoseArmorMaterial.instance,
            ArmorItem.Type.HELMET,
            Item.Settings().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(WitherRoseArmorMaterial.baseDurability))
        ),
        "wither_rose_helmet"
    )
    val WITHER_ROSE_CHESTPLATE = UniqueItemRegistry.registerItem(
        WitherRoseChestplate(
            WitherRoseArmorMaterial.instance,
            ArmorItem.Type.CHESTPLATE,
            Item.Settings().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(WitherRoseArmorMaterial.baseDurability))
        ),
        "wither_rose_chestplate"
    )
    val WITHER_ROSE_LEGGINGS = UniqueItemRegistry.registerItem(
        WitherRoseLeggings(
            WitherRoseArmorMaterial.instance,
            ArmorItem.Type.LEGGINGS,
            Item.Settings().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(WitherRoseArmorMaterial.baseDurability))
        ),
        "wither_rose_leggings"
    )
    val WITHER_ROSE_BOOTS = UniqueItemRegistry.registerItem(
        WitherRoseBoots(
            WitherRoseArmorMaterial.instance,
            ArmorItem.Type.BOOTS,
            Item.Settings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(WitherRoseArmorMaterial.baseDurability))
        ),
        "wither_rose_boots"
    )
    val SUEDE_HELMET = UniqueItemRegistry.registerItem(
        SuedeHelmet(
            SuedeArmorMaterial.instance,
            ArmorItem.Type.HELMET,
            Item.Settings().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(SuedeArmorMaterial.baseDurability))
        ),
        "suede_helmet"
    )
    val SUEDE_CHESTPLATE = UniqueItemRegistry.registerItem(
        SuedeChestplate(
            SuedeArmorMaterial.instance,
            ArmorItem.Type.CHESTPLATE,
            Item.Settings().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(SuedeArmorMaterial.baseDurability))
        ),
        "suede_chestplate"
    )
    val SUEDE_LEGGINGS = UniqueItemRegistry.registerItem(
        SuedeLeggings(
            SuedeArmorMaterial.instance,
            ArmorItem.Type.LEGGINGS,
            Item.Settings().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(SuedeArmorMaterial.baseDurability))
        ),
        "suede_leggings"
    )
    val SUEDE_BOOTS = UniqueItemRegistry.registerItem(
        SuedeBoots(
            SuedeArmorMaterial.instance,
            ArmorItem.Type.BOOTS,
            Item.Settings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(SuedeArmorMaterial.baseDurability))
        ),
        "suede_boots"
    )
    val STONEWARD = UniqueItemRegistry.registerItem(
        Stoneward(
            StonewardArmorMaterial.instance,
            ArmorItem.Type.LEGGINGS,
            Item.Settings().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(StonewardArmorMaterial.baseDurability))
        ),
        "stoneward"
    )
    val MOONSHADOW = UniqueItemRegistry.registerItem(
        Moonshadow(
            MoonshadowArmorMaterial.instance,
            ArmorItem.Type.BOOTS,
            Item.Settings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(MoonshadowArmorMaterial.baseDurability))
        ),
        "moonshadow"
    )
    val GEISTERGALOSCHEN = UniqueItemRegistry.registerItem(
        Geistergaloschen(
            GeistergaloschenArmorMaterial.instance,
            ArmorItem.Type.BOOTS,
            Item.Settings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(GeistergaloschenArmorMaterial.baseDurability))
        ),
        "geistergaloschen"
    )
    val VOIDWEAVER = UniqueItemRegistry.registerItem(
        Voidweaver(
            VoidweaverArmorMaterial.instance,
            ArmorItem.Type.CHESTPLATE,
            Item.Settings().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(VoidweaverArmorMaterial.baseDurability))
        ),
        "voidweaver"
    )
    val ABYSSAL_MASK = UniqueItemRegistry.registerItem(
        AbyssalMask(
            AbyssalMaskArmorMaterial.instance,
            ArmorItem.Type.HELMET,
            Item.Settings().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(AbyssalMaskArmorMaterial.baseDurability))
        ),
        "abyssal_mask"
    )
    val GILDED_TEMPEST = UniqueItemRegistry.registerItem(
        GildedTempest(
            GildedTempestArmorMaterial.instance,
            ArmorItem.Type.LEGGINGS,
            Item.Settings().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(GildedTempestArmorMaterial.baseDurability))
        ),
        "gilded_tempest"
    )
    val WINDSTRIDER = UniqueItemRegistry.registerItem(
        Windstrider(
            WindstriderArmorMaterial.instance,
            ArmorItem.Type.LEGGINGS,
            Item.Settings().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(WindstriderArmorMaterial.baseDurability))
        ),
        "windstrider"
    )
    val BROODMOTHER = UniqueItemRegistry.registerItem(
        Broodmother(
            BroodmotherArmorMaterial.instance,
            ArmorItem.Type.CHESTPLATE,
            Item.Settings().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(BroodmotherArmorMaterial.baseDurability))
        ),
        "broodmother"
    )
    val EMBERREIGN = UniqueItemRegistry.registerItem(
        Emberreign(
            EmberreignArmorMaterial.instance,
            ArmorItem.Type.BOOTS,
            Item.Settings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(EmberreignArmorMaterial.baseDurability))
        ),
        "emberreign"
    )
}