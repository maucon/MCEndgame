package de.fuballer.mcendgame.main.component.item.custom.armor.materials

import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.equipment.ArmorMaterial
import net.minecraft.world.item.equipment.EquipmentAsset

interface CustomArmorMaterial {
    val baseDurability: Int
    val registryKey: ResourceKey<EquipmentAsset>

    val instance: ArmorMaterial
}