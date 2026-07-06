package de.fuballer.mcendgame.main.component.item.custom.armor.materials

import net.minecraft.item.ArmorMaterial
import net.minecraft.item.equipment.EquipmentAsset
import net.minecraft.registry.RegistryKey

interface CustomArmorMaterial {
    val baseDurability: Int
    val registryKey: RegistryKey<EquipmentAsset>

    val instance: ArmorMaterial
}