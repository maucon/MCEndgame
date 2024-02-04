package de.fuballer.mcendgame.domain.ability

import de.fuballer.mcendgame.domain.entity.vine.VineEntityType
import de.fuballer.mcendgame.event.DungeonEnemySpawnedEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.technical.extension.EntityExtension.setIsMinion
import de.fuballer.mcendgame.technical.persistent_data.TypeKeys
import de.fuballer.mcendgame.util.EntityUtil
import de.fuballer.mcendgame.util.PersistentDataUtil
import de.fuballer.mcendgame.util.SummonerUtil
import org.bukkit.entity.LivingEntity

fun getSummonVineAmount(bossLevel: Int) = bossLevel / 5

object SummonVinesAbility : Ability {
    override fun cast(caster: LivingEntity, target: LivingEntity) {
        val mapTier = PersistentDataUtil.getValue(caster, TypeKeys.MAP_TIER) ?: 1

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