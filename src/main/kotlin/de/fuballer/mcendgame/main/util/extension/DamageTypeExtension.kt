package de.fuballer.mcendgame.main.util.extension

import de.fuballer.mcendgame.main.configuration.RuntimeConfig
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageType

object DamageTypeExtension {
    fun DamageType.isOf(type: ResourceKey<DamageType>): Boolean {
        val registry = RuntimeConfig.SERVER.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE)
        val key1 = registry.getResourceKey(this).orElse(null)

        return key1 == type
    }
}