package de.fuballer.mcendgame.main.util.extension

import de.fuballer.mcendgame.main.configuration.RuntimeConfig
import net.minecraft.entity.damage.DamageType
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys

object DamageTypeExtension {
    fun DamageType.isOf(type: RegistryKey<DamageType>): Boolean {
        val registry = RuntimeConfig.SERVER.registryManager.getOptional(RegistryKeys.DAMAGE_TYPE).orElseThrow()
        val key1 = registry.getKey(this).orElse(null)

        return key1 == type
    }
}