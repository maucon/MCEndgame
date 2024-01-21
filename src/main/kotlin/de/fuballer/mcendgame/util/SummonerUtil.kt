package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.domain.technical.persistent_data.TypeKeys
import org.bukkit.entity.Creature
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

object SummonerUtil {
    fun getMinionEntities(summoner: Entity): Set<Creature> {
        val world = summoner.world
        val minionIds = PersistentDataUtil.getValue(summoner, TypeKeys.MINION_IDS) ?: return setOf()

        val aliveMinions = WorldUtil.getFilteredEntities(world, minionIds, Creature::class)
            .filter { !it.isDead }
            .toSet()
        val aliveMinionIds = aliveMinions.map { it.uniqueId }
            .toSet()

        PersistentDataUtil.setValue(summoner, TypeKeys.MINION_IDS, aliveMinionIds)
        return aliveMinions
    }

    fun addMinions(summoner: Entity, newMinions: Collection<Entity>) {
        val minionIds = newMinions.map { it.uniqueId }
            .toMutableSet()
        val oldMinionsIds = PersistentDataUtil.getValue(summoner, TypeKeys.MINION_IDS) ?: listOf()

        minionIds.addAll(oldMinionsIds)
        PersistentDataUtil.setValue(summoner, TypeKeys.MINION_IDS, minionIds)
    }

    fun setMinionsTarget(summoner: Creature, minions: Collection<LivingEntity>) {
        val target = summoner.target ?: return

        minions.mapNotNull { it as? Creature }
            .forEach { it.target = target }
    }
}