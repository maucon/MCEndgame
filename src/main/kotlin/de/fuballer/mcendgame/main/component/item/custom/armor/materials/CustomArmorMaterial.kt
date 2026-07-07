package de.fuballer.mcendgame.main.component.item.custom.armor.materials

import net.minecraft.item.ArmorMaterial
import net.minecraft.registry.entry.RegistryEntry

interface CustomArmorMaterial {
    val baseDurability: Int
//    val registryKey: RegistryKey<EquipmentAsset>

    val instance: RegistryEntry<ArmorMaterial>
}