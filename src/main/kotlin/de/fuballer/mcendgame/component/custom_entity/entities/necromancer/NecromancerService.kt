package de.fuballer.mcendgame.component.custom_entity.entities.necromancer

import de.fuballer.mcendgame.component.custom_entity.CustomEntityType
import de.fuballer.mcendgame.component.custom_entity.summoner.SummonerService
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.SummonerUtil
import org.bukkit.entity.Spellcaster
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntitySpellCastEvent
import org.bukkit.util.Vector
import kotlin.math.min

@Component
class NecromancerService(
    private val summonerService: SummonerService
) : Listener {
    @EventHandler
    fun onEntitySpellCast(event: EntitySpellCastEvent) {
        if (!CustomEntityType.isType(event.entity, CustomEntityType.NECROMANCER)) return

        if (event.spell == Spellcaster.Spell.SUMMON_VEX)
            summonVexSpell(event)
    }

    private fun summonVexSpell(event: EntitySpellCastEvent) {
        event.isCancelled = true

        val necromancer = event.entity
        val minions = SummonerUtil.getMinionEntities(necromancer)

        val spawnAmount = min(NecromancerSettings.SPAWN_AMOUNT, NecromancerSettings.MAX_MINIONS - minions.size)
        if (spawnAmount <= 0) return

        summonerService.summonMinions(
            event.entity,
            CustomEntityType.CHUPACABRA,
            spawnAmount, weapons = true, ranged = false, armor = true,
            NecromancerSettings.MINION_HEALTH,
            Vector(0, 0, 0)
        )
    }
}
