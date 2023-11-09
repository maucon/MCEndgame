package de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.entities.necromancer

import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.data.CustomEntityType
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.summoner.SummonerService
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.summoner.db.MinionRepository
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.summoner.db.MinionsEntity
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.entity.Spellcaster
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntitySpellCastEvent
import org.bukkit.util.Vector
import kotlin.math.min

@Component
class NecromancerService(
    private val minionRepo: MinionRepository,
    private val summonerService: SummonerService
) : Listener {
    @EventHandler
    fun onEntitySpellCast(event: EntitySpellCastEvent) {
        if (!CustomEntityType.isType(event.entity, CustomEntityType.NECROMANCER)) return

        if (event.spell == Spellcaster.Spell.SUMMON_VEX)
            summonVexSpell(event)
    }

    private fun summonVexSpell(event: EntitySpellCastEvent) {
        val minionsEntity = minionRepo.findById(event.entity.uniqueId)
            ?: MinionsEntity(event.entity.uniqueId)

        updateMinions(minionsEntity)

        val spawnAmount = min(NecromancerSettings.SPAWN_AMOUNT, NecromancerSettings.MAX_MINIONS - minionsEntity.minions.size)
        if (spawnAmount <= 0) {
            event.isCancelled = true
            return
        }

        summonerService.summonMinions(
            event.entity,
            CustomEntityType.CHUPACABRA,
            spawnAmount, weapons = true, ranged = false, armor = true,
            NecromancerSettings.MINION_HEALTH,
            Vector(0, 0, 0)
        )
        event.isCancelled = true
    }

    private fun updateMinions(minionsEntity: MinionsEntity) {
        minionsEntity.minions.removeIf { it.isDead }
    }
}
