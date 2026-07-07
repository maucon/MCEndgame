package de.fuballer.mcendgame.main.component.item.custom.armor.materials

import de.fuballer.mcendgame.main.util.minecraft.RegistryUtil
import net.minecraft.item.ArmorItem
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.sound.SoundEvents

object DruidsArmorMaterial : CustomArmorMaterial {
    override val baseDurability = 37

    override val instance = RegistryUtil.registerMaterial(
        "druids",
        mapOf(
            ArmorItem.Type.HELMET to 3,
            ArmorItem.Type.CHESTPLATE to 8,
            ArmorItem.Type.LEGGINGS to 6,
            ArmorItem.Type.BOOTS to 3,
        ),
        enchantability = 15,
        SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
        { Ingredient.ofItems(Items.NETHERITE_INGOT) },
        3.0f,
        0.1f,
        false
    )
}