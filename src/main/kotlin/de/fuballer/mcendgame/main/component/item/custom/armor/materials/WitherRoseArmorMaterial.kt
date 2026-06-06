package de.fuballer.mcendgame.main.component.item.custom.armor.materials

import de.fuballer.mcendgame.main.util.minecraft.RegistryKeyUtil
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.equipment.ArmorMaterial
import net.minecraft.world.item.equipment.ArmorType
import net.minecraft.world.item.equipment.EquipmentAsset

object WitherRoseArmorMaterial : CustomArmorMaterial {
    override val baseDurability = 37
    override val registryKey: ResourceKey<EquipmentAsset> = RegistryKeyUtil.createEquipmentAssetKey("wither_rose")

    override val instance = ArmorMaterial(
        baseDurability,
        mapOf(
            ArmorType.BOOTS to 3,
            ArmorType.LEGGINGS to 6,
            ArmorType.CHESTPLATE to 8,
            ArmorType.HELMET to 3,
        ),
        15,
        SoundEvents.ARMOR_EQUIP_GENERIC,
        3.0f,
        0.1f,
        ItemTags.REPAIRS_NETHERITE_ARMOR,
        registryKey
    )
}