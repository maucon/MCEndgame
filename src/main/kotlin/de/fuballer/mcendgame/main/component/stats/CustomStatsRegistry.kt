package de.fuballer.mcendgame.main.component.stats

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.stats.StatFormatter
import net.minecraft.stats.Stats

object CustomStatsRegistry {
    val ENTRIES: List<Identifier>
        get() = _entries

    private val _entries = mutableListOf<Identifier>()

    fun register(name: String, formatter: StatFormatter): Identifier {
        val identifier = IdentifierUtil.default(name)

        Registry.register(BuiltInRegistries.CUSTOM_STAT, identifier, identifier)
        Stats.CUSTOM.get(identifier, formatter)
        _entries.add(identifier)

        return identifier
    }
}