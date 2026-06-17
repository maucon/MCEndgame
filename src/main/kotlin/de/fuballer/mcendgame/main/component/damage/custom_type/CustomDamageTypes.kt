package de.fuballer.mcendgame.main.component.damage.custom_type

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level

@Injectable
object CustomDamageTypes {
    val SWEEPING: ResourceKey<DamageType> = ResourceKey.create(Registries.DAMAGE_TYPE, IdentifierUtil.default("sweeping"))
    val SPELL: ResourceKey<DamageType> = ResourceKey.create(Registries.DAMAGE_TYPE, IdentifierUtil.default("spell"))
    val GENERIC_ATTACK: ResourceKey<DamageType> = ResourceKey.create(Registries.DAMAGE_TYPE, IdentifierUtil.default("generic_attack"))
    val GENERIC_ATTACK_UNBLOCKABLE: ResourceKey<DamageType> = ResourceKey.create(Registries.DAMAGE_TYPE, IdentifierUtil.default("generic_attack_unblockable"))
    val PIERCE_ATTACK: ResourceKey<DamageType> = ResourceKey.create(Registries.DAMAGE_TYPE, IdentifierUtil.default("pierce_attack"))
    val KINETIC_ATTACK: ResourceKey<DamageType> = ResourceKey.create(Registries.DAMAGE_TYPE, IdentifierUtil.default("kinetic_attack"))

    fun of(
        world: Level,
        key: ResourceKey<DamageType>,
        causingEntity: Entity,
        directEntity: Entity? = causingEntity,
    ): DamageSource {
        val damageType = world.registryAccess()
            .lookupOrThrow(Registries.DAMAGE_TYPE)
            .get(key.identifier())
            .get()

        return DamageSource(damageType, directEntity, causingEntity)
    }
}