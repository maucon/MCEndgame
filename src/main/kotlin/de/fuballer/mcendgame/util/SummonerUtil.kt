package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.component.technical.extension.EntityExtension.getMinionIds
import de.fuballer.mcendgame.component.technical.extension.EntityExtension.setMinionIds
import org.bukkit.entity.Creature
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

object SummonerUtil {
    fun getMinionEntities(summoner: Entity): Set<Creature> {
        val world = summoner.world
        val minionIds = summoner.getMinionIds() ?: return setOf()

        val aliveMinions = WorldUtil.getFilteredEntities(world, minionIds, Creature::class)
            .filter { !it.isDead }
            .toSet()
        val aliveMinionIds = aliveMinions.map { it.uniqueId }
            .toSet()

        summoner.setMinionIds(aliveMinionIds)
        return aliveMinions
    }

    fun addMinions(summoner: Entity, newMinions: Collection<Entity>) {
        val minionIds = newMinions.map { it.uniqueId }
            .toMutableSet()
        val oldMinionsIds = summoner.getMinionIds() ?: listOf()

        minionIds.addAll(oldMinionsIds)
        summoner.setMinionIds(minionIds)
    }

    fun setMinionsTarget(summoner: Creature, minions: Collection<LivingEntity>) {
        val target = summoner.target ?: return

        minions.mapNotNull { it as? Creature }
            .forEach { it.target = target }
    }
}