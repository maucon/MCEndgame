package de.fuballer.mcendgame.component.dungeon.generation.layout_generator

import de.fuballer.mcendgame.component.dungeon.generation.data.Layout
import kotlin.random.Random

interface LayoutGenerator {
    fun generateDungeon(random: Random, mapTier: Int): Layout
}