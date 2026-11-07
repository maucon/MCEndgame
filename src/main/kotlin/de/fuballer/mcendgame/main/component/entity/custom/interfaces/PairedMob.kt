package de.fuballer.mcendgame.main.component.entity.custom.interfaces

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityReference
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level

interface PairedMob {
    var partnerReference: EntityReference<LivingEntity>?

    fun getPartner() = EntityReference.getLivingEntity(partnerReference, getLevel())

    fun hasPartner() = getPartner() != null

    fun getLevel(): Level

    fun <T : LivingEntity> findPartner(
        entity: Entity,
        searchRadius: Double,
        baseClass: Class<T>,
        overwriteExisting: Boolean = false,
    ): LivingEntity? {
        val partner = getPartner()
        if (partner != null && partner.isAlive && !overwriteExisting) return partner

        val level = getLevel() as? ServerLevel ?: return partner

        val box = entity.boundingBox.inflate(searchRadius)
        val newPartner = level.getEntitiesOfClass(baseClass, box).minByOrNull { it.distanceToSqr(entity) } ?: return partner
        partnerReference = EntityReference.of(newPartner)
        return newPartner
    }
}