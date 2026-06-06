package de.fuballer.mcendgame.main.component.item.custom.armor.materials

import de.fuballer.mcendgame.main.util.minecraft.RegistryKeyUtil
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.equipment.ArmorMaterial
import net.minecraft.world.item.equipment.ArmorType
import net.minecraft.world.item.equipment.EquipmentAsset

object SuedeArmorMaterial : CustomArmorMaterial {
    override val baseDurability = 25
    override val registryKey: ResourceKey<EquipmentAsset> = RegistryKeyUtil.createEquipmentAssetKey("suede")

    override val instance = ArmorMaterial(
        baseDurability,
        mapOf(
            ArmorType.BOOTS to 2,
            ArmorType.LEGGINGS to 5,
            ArmorType.CHESTPLATE to 6,
            ArmorType.HELMET to 2,
        ),
        15,
        SoundEvents.ARMOR_EQUIP_GENERIC,
        1.0f,
        0.0f,
        ItemTags.REPAIRS_LEATHER_ARMOR,
        registryKey
    )
}