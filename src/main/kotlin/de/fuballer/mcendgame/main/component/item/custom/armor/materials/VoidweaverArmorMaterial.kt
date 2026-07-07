package de.fuballer.mcendgame.main.component.item.custom.armor.materials

import de.fuballer.mcendgame.main.util.minecraft.RegistryUtil
import net.minecraft.item.ArmorItem
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.sound.SoundEvents

object VoidweaverArmorMaterial : CustomArmorMaterial {
    override val baseDurability = 37

    override val instance = RegistryUtil.registerMaterial(
        "voidweaver",
        mapOf(
            ArmorItem.Type.CHESTPLATE to 8,
        ),
        enchantability = 15,
        SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
        { Ingredient.ofItems(Items.NETHERITE_INGOT) },
        toughness = 3.0f,
        knockbackResistance = 0.1f,
        false
    )
}