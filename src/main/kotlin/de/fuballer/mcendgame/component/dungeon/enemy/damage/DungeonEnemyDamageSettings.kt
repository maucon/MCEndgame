package de.fuballer.mcendgame.component.dungeon.enemy.damage

object DungeonEnemyDamageSettings {
    const val DAMAGE_PER_STRENGTH = 3.0

    fun getPowerDamageMulti(level: Int) = 1 + (0.25 * (level + 1))
}