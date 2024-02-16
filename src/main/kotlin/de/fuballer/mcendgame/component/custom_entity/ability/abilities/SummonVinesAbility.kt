package de.fuballer.mcendgame.component.custom_entity.ability.abilities

import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import de.fuballer.mcendgame.component.custom_entity.types.vine.VineEntityType
import de.fuballer.mcendgame.event.DungeonEnemySpawnedEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.util.EntityUtil
import de.fuballer.mcendgame.util.SummonerUtil
import de.fuballer.mcendgame.util.extension.EntityExtension.getMapTier
import de.fuballer.mcendgame.util.extension.EntityExtension.setIsMinion
import org.bukkit.entity.LivingEntity

fun getSummonVineAmount(bossLevel: Int) = bossLevel / 5

object SummonVinesAbility : Ability {
    override fun cast(caster: LivingEntity, target: LivingEntity) {
        val mapTier = caster.getMapTier() ?: 1

        val amount = getSummonVineAmount(mapTier)

        val vines = mutableSetOf<LivingEntity>()
        for (i in 0 until amount) {
            val vine = EntityUtil.spawnCustomEntity(VineEntityType, caster.location, mapTier) as LivingEntity
            vine.setIsMinion()

            vines.add(vine)
        }

        SummonerUtil.addMinions(caster, vines)

        val event = DungeonEnemySpawnedEvent(caster.world, vines)
        EventGateway.apply(event)
    }
}