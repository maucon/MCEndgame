package de.fuballer.mcendgame.component.dungeon.enemy.generation

import org.bukkit.Location
import org.bukkit.World

data class EnemySpawnLocations(
    val world: World,
    val normalEnemyLocations: List<Location>,
    val possibleSpecialEnemyLocations: List<Location>
)