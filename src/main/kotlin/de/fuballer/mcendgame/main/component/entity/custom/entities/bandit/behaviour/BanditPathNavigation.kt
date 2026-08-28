package de.fuballer.mcendgame.main.component.entity.custom.entities.bandit.behaviour

import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation
import net.minecraft.world.level.Level

class BanditPathNavigation(
    mob: Mob,
    level: Level,
) : GroundPathNavigation(mob, level) {
    override fun getMaxVerticalDistanceToWaypoint(): Float {
        if (mob.onGround()) return super.getMaxVerticalDistanceToWaypoint()
        return 2F
    }

    override fun canUpdatePath(): Boolean = true
}