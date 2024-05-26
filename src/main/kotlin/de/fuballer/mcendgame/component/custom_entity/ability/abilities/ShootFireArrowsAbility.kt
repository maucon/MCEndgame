package de.fuballer.mcendgame.component.custom_entity.ability.abilities

import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import de.fuballer.mcendgame.component.custom_entity.ability.AbilitySettings
import de.fuballer.mcendgame.util.DungeonUtil
import de.fuballer.mcendgame.util.PluginUtil.runTaskLater
import de.fuballer.mcendgame.util.extension.EntityExtension.getMapTier
import de.fuballer.mcendgame.util.extension.ProjectileExtension.setAddedBaseDamage
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

private const val ARROWS_COUNT = 5
private const val ARROWS_TIME_DIFFERENCE = 4L // in ticks

private fun getArrowAddedDamage(bossLevel: Int) = 6.0 + bossLevel * 2.0

object ShootFireArrowsAbility : Ability {
    override fun canCast(caster: LivingEntity): Boolean {
        val targets = DungeonUtil.getNearbyPlayers(caster, AbilitySettings.DEFAULT_TARGET_RANGE)
        return targets.isNotEmpty()
    }

    override fun cast(caster: LivingEntity) {
        val targets = DungeonUtil.getNearbyPlayers(caster, AbilitySettings.DEFAULT_TARGET_RANGE)

        for (target in targets) {
            for (i in 1..ARROWS_COUNT)
                ShootArrowRunnable(caster, target)
                    .runTaskLater(i * ARROWS_TIME_DIFFERENCE)
        }
    }

    private class ShootArrowRunnable(
        private val caster: LivingEntity,
        private val target: LivingEntity
    ) : BukkitRunnable() {
        override fun run() {
            val bL = caster.eyeLocation
            val tL = target.eyeLocation
            val arrow = caster.world.spawnArrow(bL, Vector(tL.x - bL.x, tL.y - bL.y, tL.z - bL.z), 2F, 4F)

            arrow.fireTicks = 100
            arrow.knockbackStrength = 2
            arrow.shooter = caster

            val addedDamage = getArrowAddedDamage(caster.getMapTier() ?: 1)
            arrow.setAddedBaseDamage(addedDamage)
        }
    }
}