package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.domain.technical.persistent_data.TypeKeys
import org.bukkit.entity.Creature
import org.bukkit.entity.Entity

object SummonerUtil {
    fun getMinionEntities(summoner: Entity): Set<Creature> {
        val world = summoner.world
        val minions = PersistentDataUtil.getValue(summoner, TypeKeys.MINIONS) ?: return setOf()

        val aliveMinions = WorldUtil.getFilteredEntities(world, minions, Creature::class)
            .filter { !it.isDead }
            .toSet()
        val aliveMinionIds = aliveMinions.map { it.uniqueId }
            .toSet()

        PersistentDataUtil.setValue(summoner, TypeKeys.MINIONS, aliveMinionIds)
        return aliveMinions
    }

    fun addMinions(summoner: Entity, newMinions: Collection<Entity>) {
        val minionIds = newMinions.map { it.uniqueId }
            .toMutableSet()

        val oldMinions = PersistentDataUtil.getValue(summoner, TypeKeys.MINIONS) ?: listOf()
        minionIds.addAll(oldMinions)
        PersistentDataUtil.setValue(summoner, TypeKeys.MINIONS, minionIds)
    }
}