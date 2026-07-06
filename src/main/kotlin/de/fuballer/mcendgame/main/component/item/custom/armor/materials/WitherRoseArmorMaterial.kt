package de.fuballer.mcendgame.main.component.item.custom.armor.materials

import de.fuballer.mcendgame.main.util.minecraft.RegistryKeyUtil
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.equipment.ArmorMaterial
import net.minecraft.item.equipment.EquipmentAsset
import net.minecraft.item.equipment.EquipmentType
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.tag.ItemTags
import net.minecraft.sound.SoundEvents

object WitherRoseArmorMaterial : CustomArmorMaterial {
    override val baseDurability = 37
    override val registryKey: RegistryKey<EquipmentAsset> = RegistryKeyUtil.createEquipmentAssetKey("wither_rose")

    override val instance = ArmorMaterial(
        baseDurability,
        mapOf(
            EquipmentType.BOOTS to 3,
            EquipmentType.LEGGINGS to 6,
            EquipmentType.CHESTPLATE to 8,
            EquipmentType.HELMET to 3,
        ),
        15,
        SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
        3.0f,
        0.1f,
        ItemTags.REPAIRS_NETHERITE_ARMOR,
        registryKey
    )
}