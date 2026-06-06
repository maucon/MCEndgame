package de.fuballer.mcendgame.main.component.item.custom.armor.materials

import de.fuballer.mcendgame.main.util.minecraft.RegistryKeyUtil
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.equipment.ArmorMaterial
import net.minecraft.world.item.equipment.ArmorType
import net.minecraft.world.item.equipment.EquipmentAsset

object BoundAbyssArmorMaterial : CustomArmorMaterial {
    override val baseDurability = 37
    override val registryKey: ResourceKey<EquipmentAsset> = RegistryKeyUtil.createEquipmentAssetKey("bound_abyss")

    override val instance = ArmorMaterial(
        baseDurability,
        mapOf(
            ArmorType.CHESTPLATE to 8,
        ),
        15,
        SoundEvents.ARMOR_EQUIP_GENERIC,
        1.0f,
        0.1f,
        ItemTags.REPAIRS_GOLD_ARMOR,
        registryKey
    )
}