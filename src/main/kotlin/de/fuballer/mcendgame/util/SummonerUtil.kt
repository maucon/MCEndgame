package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.component.custom_entity.summoner.SummonerService
import de.fuballer.mcendgame.util.extension.EntityExtension.getMinionIds
import de.fuballer.mcendgame.util.extension.EntityExtension.setMinionIds
import org.bukkit.entity.Creature
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

object SummonerUtil {
    lateinit var summonerService: SummonerService // FIXME

    fun getMinionEntities(summoner: Entity): List<Creature> {
        val world = summoner.world
        val minionIds = summoner.getMinionIds() ?: return listOf()

        val aliveMinions = WorldUtil.getFilteredEntities(world, minionIds, Creature::class)
            .filter { !it.isDead }
        val aliveMinionIds = aliveMinions.map { it.uniqueId }

        summoner.setMinionIds(aliveMinionIds)
        return aliveMinions
    }

    fun addMinions(summoner: Entity, newMinions: Collection<Entity>) {
        val minionIds = newMinions.map { it.uniqueId }
            .toMutableList()
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