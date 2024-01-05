package de.fuballer.mcendgame.component.player_projectile

object PlayerProjectileSettings {
    fun getPowerDamageMulti(level: Int): Double {
        if (level == 0) return 1.0
        return 1.25 + level * 0.25
    }
}
