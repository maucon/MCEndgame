package de.fuballer.mcendgame.component.dungeon.enemy.damage

object DungeonEnemyDamageSettings {
    const val PROJECTILE_DAMAGE_PER_STRENGTH = 1.5

    fun getPowerDamageMulti(level: Int) = 1 + (level * 0.2)
}