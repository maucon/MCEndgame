package de.fuballer.mcendgame.main.component.item.custom.armor.materials

import de.fuballer.mcendgame.main.util.minecraft.RegistryKeyUtil
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.equipment.ArmorMaterial
import net.minecraft.item.equipment.EquipmentAsset
import net.minecraft.item.equipment.EquipmentType
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.tag.ItemTags
import net.minecraft.sound.SoundEvents

object SuedeArmorMaterial : CustomArmorMaterial {
    override val baseDurability = 25
    override val registryKey: RegistryKey<EquipmentAsset> = RegistryKeyUtil.createEquipmentAssetKey("suede")

    override val instance = ArmorMaterial(
        baseDurability,
        mapOf(
            EquipmentType.BOOTS to 2,
            EquipmentType.LEGGINGS to 5,
            EquipmentType.CHESTPLATE to 6,
            EquipmentType.HELMET to 2,
        ),
        15,
        SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
        1.0f,
        0.0f,
        ItemTags.REPAIRS_LEATHER_ARMOR,
        registryKey
    )
}