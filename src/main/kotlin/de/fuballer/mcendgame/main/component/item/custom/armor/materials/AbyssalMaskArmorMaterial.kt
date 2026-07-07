package de.fuballer.mcendgame.main.component.item.custom.armor.materials

import de.fuballer.mcendgame.main.util.minecraft.RegistryUtil
import net.minecraft.item.ArmorItem
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.sound.SoundEvents

object AbyssalMaskArmorMaterial : CustomArmorMaterial {
    override val baseDurability = 37

    override val instance = RegistryUtil.registerMaterial(
        "abyssal_mask",
        mapOf(
            ArmorItem.Type.HELMET to 3,
        ),
        enchantability = 15,
        SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
        { Ingredient.ofItems(Items.NETHERITE_INGOT) },
        toughness = 1.0f,
        knockbackResistance = 0.1f,
        false
    )
}