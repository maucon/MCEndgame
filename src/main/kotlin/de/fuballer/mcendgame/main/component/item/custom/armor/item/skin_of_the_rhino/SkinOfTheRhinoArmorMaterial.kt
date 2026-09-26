package de.fuballer.mcendgame.main.component.item.custom.armor.item.skin_of_the_rhino

import de.fuballer.mcendgame.main.component.item.custom.armor.materials.CustomArmorMaterial
import de.fuballer.mcendgame.main.util.minecraft.RegistryKeyUtil
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.equipment.ArmorMaterial
import net.minecraft.world.item.equipment.ArmorType
import net.minecraft.world.item.equipment.EquipmentAsset

object SkinOfTheRhinoArmorMaterial : CustomArmorMaterial {
    override val baseDurability = 37
    override val registryKey: ResourceKey<EquipmentAsset> = RegistryKeyUtil.createEquipmentAssetKey("skin_of_the_rhino")

    override val instance = ArmorMaterial(
        baseDurability,
        mapOf(
            ArmorType.CHESTPLATE to 8,
        ),
        15,
        SoundEvents.ARMOR_EQUIP_GENERIC,
        3.0f,
        0.1f,
        ItemTags.REPAIRS_NETHERITE_ARMOR,
        registryKey,
    )
}