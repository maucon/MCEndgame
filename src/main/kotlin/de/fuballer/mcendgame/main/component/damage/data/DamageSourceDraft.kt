package de.fuballer.mcendgame.main.component.damage.data

import net.minecraft.core.Holder
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.Entity

class DamageSourceDraft(
    val vanillaDamageContext: VanillaDamageContext,
    val customDamageContext: CustomDamageContext,
    type: Holder<DamageType>,
    directEntity: Entity?,
    causingEntity: Entity?
) : DamageSource(type, directEntity, causingEntity) {
    constructor(
        vanillaDamageContext: VanillaDamageContext,
        customDamageContext: CustomDamageContext,
        damageSource: DamageSource
    ) : this(
        vanillaDamageContext,
        customDamageContext,
        damageSource.typeHolder(),
        damageSource.directEntity,
        damageSource.entity
    )

    constructor(damageSource: DamageSource) : this(VanillaDamageContext(), CustomDamageContext(), damageSource)
}