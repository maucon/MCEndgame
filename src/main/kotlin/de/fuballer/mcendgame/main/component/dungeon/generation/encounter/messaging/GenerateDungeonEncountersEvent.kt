package de.fuballer.mcendgame.main.component.dungeon.generation.encounter.messaging

import de.fuballer.mcendgame.main.component.dungeon.generation.data.EncounterLocation
import de.fuballer.mcendgame.main.component.dungeon.generation.encounter.EncounterType
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import net.minecraft.server.level.ServerLevel

data class GenerateDungeonEncountersEvent(
    val world: ServerLevel,
    val dungeonLevel: Int,
    val availableLocations: MutableList<EncounterLocation>,
    val availableStartLocations: MutableList<EncounterLocation>,
    val aspects: Map<AspectItem, Int>,
    val encounters: Map<EncounterType, Int> = mapOf(),
) {
    fun takeLocations(count: Int): List<EncounterLocation> {
        val limitedCount = count.coerceAtMost(availableLocations.size)
        val chosen = availableLocations.shuffled().take(limitedCount)
        availableLocations.removeAll(chosen)
        return chosen
    }

    fun takeStartLocations(count: Int): List<EncounterLocation> {
        val limitedCount = count.coerceAtMost(availableStartLocations.size)
        val chosen = availableStartLocations.shuffled().take(limitedCount)
        availableStartLocations.removeAll(chosen)
        return chosen
    }
}
