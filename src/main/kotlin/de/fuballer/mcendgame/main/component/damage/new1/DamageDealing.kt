package de.fuballer.mcendgame.main.component.damage.new1

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.damage.custom_type.CustomDamageTypes
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.Entity

// TODO rename
object DamageDealing {
    fun Entity.dealDamageTODO(
        damageType: ResourceKey<DamageType>, // the kind of attack
        damageInstance: DamageInstance, // the damage dealt by category
        causingEntity: Entity?, // the entity causing the damage (e.g. skeleton)
        directEntity: Entity? = causingEntity, // the entity dealing the damage (e.g. arrow)
        extraAttributes: List<CustomAttribute> = emptyList(),
    ): Boolean {
        val serverWorld = level() as? ServerLevel ?: return false
        // damage type (blockable, dodgeable
        // damage instance

        // the attack setup
        val damageSource = CustomDamageTypes.of(serverWorld, damageType, causingEntity, directEntity)

//        val customDamageContext = CustomDamageContext(damageInstance, null, extraAttributes) // FIXME

//        val damageSourceDraft = DamageSourceDraft(VanillaDamageContext(), customDamageContext, damageSource)

//        this.hurtServer(serverWorld, damageSourceDraft, 420F)

        return true
    }
}