package de.fuballer.mcendgame.main.component.custom_attribute.effects.absorb_stat_from_boss

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.setCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.particle.AbsorbBossPowerParticleEffect
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonBossDeathEvent
import de.fuballer.mcendgame.main.util.extension.EntityExtension.getEquipment
import de.fuballer.mcendgame.main.util.extension.ItemStackExtension.addAbsorbedBoss
import de.fuballer.mcendgame.main.util.extension.ItemStackExtension.hasAbsorbedBoss
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player

@Injectable
class AbsorbStatFromSlainBossService {
    @EventSubscriber(sync = true)
    fun on(event: DungeonBossDeathEvent) {
        val serverLevel = event.world as? ServerLevel ?: return

        val equipment = event.killer?.getEquipment() ?: return

        var hasAttribute = false
        var hasAbsorbed = false
        equipment.forEach { (_, stack) ->
            val bossType = event.bossEntity.type

            val attributes = stack.getCustomAttributes().toMutableList()
            val absorbAttribute = attributes.firstOrNull { it.type == CustomAttributeTypes.ABSORB_STAT_FROM_SLAIN_BOSS } ?: return@forEach
            hasAttribute = true

            if (stack.hasAbsorbedBoss(bossType)) return@forEach

            val newAttributes = AbsorbStatFromSlainBossSettings.getAttributeForBoss(bossType, absorbAttribute.slot) ?: return@forEach

            val index = attributes.indexOf(absorbAttribute)
            attributes.removeAt(index)
            attributes.addAll(index, newAttributes)

            stack.addAbsorbedBoss(bossType)
            stack.setCustomAttributes(attributes, absorbAttribute.slot)
            hasAbsorbed = true
        }

        if (!hasAttribute) return

        if (hasAbsorbed) {
            (event.killer as? Player)?.sendSystemMessage(AbsorbStatFromSlainBossSettings.ATTRIBUTE_ABSORBED_MESSAGE)
            createAbsorbParticles(event.bossEntity, event.killer, serverLevel)
            return
        }

        (event.killer as? Player)?.sendSystemMessage(AbsorbStatFromSlainBossSettings.CANNOT_ABSORB_ATTRIBUTE_MESSAGE)
    }

    private fun createAbsorbParticles(
        boss: LivingEntity,
        killer: LivingEntity,
        level: ServerLevel,
    ) {
        level.sendParticles(
            AbsorbBossPowerParticleEffect(killer.uuid),
            boss.x,
            boss.y + boss.bbHeight / 2.0,
            boss.z,
            35,
            0.0,
            0.0,
            0.0,
            1.0,
        )
    }
}