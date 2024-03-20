package de.fuballer.mcendgame.component.custom_entity.ability.abilities

import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import de.fuballer.mcendgame.component.custom_entity.ability.AbilitySettings
import de.fuballer.mcendgame.component.custom_entity.types.vine.VineEntityType
import de.fuballer.mcendgame.event.DungeonEnemySpawnedEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.util.DungeonUtil
import de.fuballer.mcendgame.util.EntityUtil
import de.fuballer.mcendgame.util.SummonerUtil
import de.fuballer.mcendgame.util.extension.EntityExtension.getMapTier
import de.fuballer.mcendgame.util.extension.EntityExtension.setIsMinion
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector
import kotlin.random.Random

fun getSummonVineAmount(bossLevel: Int, targets: Int) = (2 + bossLevel / 5) * targets

object SummonVinesAbility : Ability {
    override fun cast(caster: LivingEntity) {
        val mapTier = caster.getMapTier() ?: 1

        val targets = DungeonUtil.getNearbyPlayers(caster, AbilitySettings.DEFAULT_TARGET_RANGE)

        val amount = getSummonVineAmount(mapTier, targets.size)

        val vines = mutableSetOf<LivingEntity>()
        for (i in 0 until amount) {
            val vine = EntityUtil.spawnCustomEntity(VineEntityType, caster.location, mapTier) as LivingEntity
            vine.setIsMinion()

            vine.velocity = Vector(1 - Random.nextDouble() * 2, 0.2 + Random.nextDouble() * 0.1, 1 - Random.nextDouble() * 2)

            vines.add(vine)
        }

        SummonerUtil.addMinions(caster, vines)

        val event = DungeonEnemySpawnedEvent(caster.world, vines)
        EventGateway.apply(event)
    }
}