package de.fuballer.mcendgame.main.component.item.custom.armor.materials

import de.fuballer.mcendgame.main.util.minecraft.RegistryKeyUtil
import net.minecraft.item.equipment.ArmorMaterial
import net.minecraft.item.equipment.EquipmentAsset
import net.minecraft.item.equipment.EquipmentType
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.tag.ItemTags
import net.minecraft.sound.SoundEvents

object WindstriderArmorMaterial : CustomArmorMaterial {
    override val baseDurability = 37
    override val registryKey: RegistryKey<EquipmentAsset> = RegistryKeyUtil.createEquipmentAssetKey("windstrider")

    override val instance = ArmorMaterial(
        baseDurability,
        mapOf(
            EquipmentType.LEGGINGS to 5,
        ),
        15,
        SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
        1.0f,
        0.0f,
        ItemTags.REPAIRS_LEATHER_ARMOR,
        registryKey
    )
}