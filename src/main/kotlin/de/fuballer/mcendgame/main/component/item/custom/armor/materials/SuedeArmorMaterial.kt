package de.fuballer.mcendgame.main.component.item.custom.armor.materials

import de.fuballer.mcendgame.main.util.minecraft.RegistryUtil
import net.minecraft.item.ArmorItem
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.sound.SoundEvents

object SuedeArmorMaterial : CustomArmorMaterial {
    override val baseDurability = 25

    override val instance = RegistryUtil.registerMaterial(
        "suede",
        mapOf(
            ArmorItem.Type.HELMET to 2,
            ArmorItem.Type.CHESTPLATE to 6,
            ArmorItem.Type.LEGGINGS to 5,
            ArmorItem.Type.BOOTS to 2,
        ),
        enchantability = 15,
        SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
        { Ingredient.ofItems(Items.LEATHER) },
        toughness = 1.0f,
        knockbackResistance = 0.1f,
        false
    )
}