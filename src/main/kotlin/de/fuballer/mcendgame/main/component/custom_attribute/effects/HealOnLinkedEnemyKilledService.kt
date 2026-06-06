package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getHealingFactor
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.effects.link.LinkSettings
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDeathEvent
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.getLinkedBy
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.getLinkedEntities
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity

@Injectable
class HealOnLinkedEnemyKilledService {
    @EventSubscriber(sync = true)
    fun on(event: LivingEntityDeathEvent) {
        val world = event.world as? ServerLevel ?: return
        val linkedEntity = event.entity
        val linkedUuid = linkedEntity.uuid

        val linkedBy = linkedEntity.getLinkedBy()
        linkedBy.forEach { uuid ->
            val linkOriginEntity = (world.getEntity(uuid) as? LivingEntity) ?: return@forEach

            val attributes = linkOriginEntity.getAllCustomAttributes()[CustomAttributeTypes.HEAL_ON_LINKED_ENEMY_KILLED] ?: return@forEach
            val healSum = attributes.sumOf { (it.rolls[0] as DoubleRoll).getValue() }
            if (healSum <= 0) return@forEach

            val linkedEntities = linkOriginEntity.getLinkedEntities()
            val linkDuration = linkedEntities[linkedUuid] ?: return@forEach

            val distance = linkedEntity.distanceTo(linkOriginEntity).toDouble()
            val linkConnectionTime = LinkSettings.getLinkConnectingTime(distance)
            if (linkDuration < linkConnectionTime) return@forEach

            val healFactor = linkOriginEntity.getHealingFactor()
            val totalHeal = healSum * healFactor
            linkOriginEntity.heal(totalHeal.toFloat())
        }
    }
}