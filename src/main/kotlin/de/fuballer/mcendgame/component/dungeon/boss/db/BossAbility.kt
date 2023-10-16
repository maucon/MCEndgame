package de.fuballer.mcendgame.component.dungeon.boss.db

enum class BossAbility(
    val minLevel: Int
) {
    ARROWS(0),
    FIRE_ARROWS(3),
    SPEED(5),
    FIRE_CASCADE(8),
    DARKNESS(10),
    LEAP(0),
}
