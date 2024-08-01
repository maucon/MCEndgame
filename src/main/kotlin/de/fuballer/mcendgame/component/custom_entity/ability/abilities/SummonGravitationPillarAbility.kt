package de.fuballer.mcendgame.component.custom_entity.ability.abilities

import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import de.fuballer.mcendgame.component.custom_entity.summoner.SummonerUtil
import de.fuballer.mcendgame.component.custom_entity.types.stone_pillar.StonePillarEntityType
import de.fuballer.mcendgame.util.DungeonUtil
import de.fuballer.mcendgame.util.EntityUtil
import de.fuballer.mcendgame.util.PluginUtil.runTaskLater
import de.fuballer.mcendgame.util.extension.EntityExtension.getMapTier
import de.fuballer.mcendgame.util.extension.EntityExtension.setIsMinion
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

private const val GRAVITATION_PILLAR_RANGE = 20.0
private const val GRAVITATION_PILLAR_COOLDOWN = 40L // in ticks

object SummonGravitationPillarAbility : Ability {
    override fun cast(caster: LivingEntity) {
        val mapTier = caster.getMapTier() ?: 1

        val pillar = EntityUtil.spawnCustomEntity(StonePillarEntityType, caster.location, mapTier) as LivingEntity
        pillar.setAI(false)
        pillar.setIsMinion()

        SummonerUtil.addMinions(caster, setOf(pillar))

        GravitationPillarPullRunnable(pillar).runTaskLater(GRAVITATION_PILLAR_COOLDOWN)
    }

    private class GravitationPillarPullRunnable(
        private val pillar: LivingEntity,
    ) : BukkitRunnable() {
        override fun run() {
            if (pillar.isDead) {
                this.cancel()
                return
            }

            val players = DungeonUtil.getNearbyPlayers(pillar, GRAVITATION_PILLAR_RANGE)
            for (player in players) {
                val vec = pillar.location.subtract(player.location).multiply(0.1)
                player.velocity = Vector(vec.x, vec.y + vec.length() / 5, vec.z)
            }

            pillar.world.playSound(pillar.location, Sound.BLOCK_BASALT_BREAK, SoundCategory.PLAYERS, 1.5f, 0.5f)

            GravitationPillarPullRunnable(pillar).runTaskLater(GRAVITATION_PILLAR_COOLDOWN)
        }
    }
}