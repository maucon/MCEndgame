package de.fuballer.mcendgame.main.component.item.custom.armor.materials

import de.fuballer.mcendgame.main.util.minecraft.RegistryKeyUtil
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.equipment.ArmorMaterial
import net.minecraft.item.equipment.EquipmentAsset
import net.minecraft.item.equipment.EquipmentType
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.tag.ItemTags
import net.minecraft.sound.SoundEvents

object GildedTempestArmorMaterial : CustomArmorMaterial {
    override val baseDurability = 37
    override val registryKey: RegistryKey<EquipmentAsset> = RegistryKeyUtil.createEquipmentAssetKey("gilded_tempest")

    override val instance = ArmorMaterial(
        baseDurability,
        mapOf(
            EquipmentType.LEGGINGS to 6,
        ),
        15,
        SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
        1.0f,
        0.1f,
        ItemTags.REPAIRS_NETHERITE_ARMOR,
        registryKey
    )
}