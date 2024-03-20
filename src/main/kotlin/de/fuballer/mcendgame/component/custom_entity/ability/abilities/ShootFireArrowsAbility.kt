package de.fuballer.mcendgame.component.custom_entity.ability.abilities

import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import de.fuballer.mcendgame.component.custom_entity.ability.AbilitySettings
import de.fuballer.mcendgame.util.DungeonUtil
import de.fuballer.mcendgame.util.PluginUtil.runTaskLater
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

const val ARROWS_COUNT = 5
const val ARROWS_TIME_DIFFERENCE = 4L // in ticks

object ShootFireArrowsAbility : Ability {
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
        }
    }
}