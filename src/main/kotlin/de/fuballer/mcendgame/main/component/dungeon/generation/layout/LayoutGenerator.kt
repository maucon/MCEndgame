package de.fuballer.mcendgame.main.component.dungeon.generation.layout

import de.fuballer.mcendgame.main.component.dungeon.generation.data.Layout
import kotlin.random.Random

interface LayoutGenerator {
    fun generateDungeon(random: Random, dungeonLevel: Int, bossCount: Int, enemyCount: Int): Layout
}