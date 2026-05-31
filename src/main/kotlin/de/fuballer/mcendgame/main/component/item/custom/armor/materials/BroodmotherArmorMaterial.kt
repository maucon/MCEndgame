package de.fuballer.mcendgame.main.component.item.custom.armor.materials

import de.fuballer.mcendgame.main.component.tags.CustomTags
import de.fuballer.mcendgame.main.util.minecraft.RegistryKeyUtil
import net.minecraft.item.equipment.ArmorMaterial
import net.minecraft.item.equipment.EquipmentAsset
import net.minecraft.item.equipment.EquipmentType
import net.minecraft.registry.RegistryKey
import net.minecraft.sound.SoundEvents

object BroodmotherArmorMaterial : CustomArmorMaterial {
    override val baseDurability = 37
    override val registryKey: RegistryKey<EquipmentAsset> = RegistryKeyUtil.createEquipmentAssetKey("broodmother")

    override val instance = ArmorMaterial(
        baseDurability,
        mapOf(
            EquipmentType.CHESTPLATE to 8,
        ),
        15,
        SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
        1.0f,
        0.1f,
        CustomTags.REPAIRS_SPIDER_ARMOR,
        registryKey
    )
}