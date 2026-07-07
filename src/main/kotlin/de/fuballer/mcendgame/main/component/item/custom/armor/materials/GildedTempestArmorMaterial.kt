package de.fuballer.mcendgame.main.component.item.custom.armor.materials

import de.fuballer.mcendgame.main.util.minecraft.RegistryUtil
import net.minecraft.item.ArmorItem
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.sound.SoundEvents

object GildedTempestArmorMaterial : CustomArmorMaterial {
    override val baseDurability = 37

    override val instance = RegistryUtil.registerMaterial(
        "gilded_tempest",
        mapOf(
            ArmorItem.Type.HELMET to 3,
//            ArmorItem.Type.CHESTPLATE to 8,
//            ArmorItem.Type.LEGGINGS to 8,
//            ArmorItem.Type.BOOTS to 8,
        ),
        enchantability = 15,
        SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
        { Ingredient.ofItems(Items.NETHERITE_INGOT) },
        toughness = 1.0f,
        knockbackResistance = 0.1f,
        false
    )
}