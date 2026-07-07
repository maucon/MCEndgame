package de.fuballer.mcendgame.main.component.item.custom.armor.materials

import de.fuballer.mcendgame.main.util.minecraft.RegistryUtil
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.sound.SoundEvents

object BoundAbyssArmorMaterial : CustomArmorMaterial {
    override val baseDurability = 37

    override val instance = RegistryUtil.registerMaterial(
        "bound_abyss",
        mapOf(
//            ArmorItem.Type.HELMET to 8,
            ArmorItem.Type.CHESTPLATE to 8,
//            ArmorItem.Type.LEGGINGS to 8,
//            ArmorItem.Type.BOOTS to 8,
        ),
        enchantability = 15,
        SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
        { Ingredient.ofItems(Items.GOLD_INGOT) },
        1.0f,
        0.1f,
        false
    )
}