package de.fuballer.mcendgame.main.component.item.custom.aspect.item.special.sigil

import de.fuballer.mcendgame.main.component.entity.custom.attack.data.particle.ParticleData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.sound.SoundData
import de.fuballer.mcendgame.main.component.item.custom.aspect.item.special.key.KeyItem
import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItem
import net.minecraft.network.chat.Component

abstract class SigilItem(
    settings: Properties,
) : KeyItem(settings) {
    companion object {
        const val SIGIL_TRANSLATABLE_BASE_KEY = KEY_TRANSLATABLE_BASE_KEY + "sigil."
        const val SIGIL_TRANSLATABLE_DESCRIPTION_KEY = SIGIL_TRANSLATABLE_BASE_KEY + "description."
    }

    abstract val minBossLevel: Int
    abstract val minDropLevel: Int
    abstract fun getDropProbability(level: Int): Double

    open fun getCrystalItems(level: Int, lootMultiplier: Double, finalBoss: Boolean): List<CrystalItem> = listOf()

    override val specificDescription
        get() = mutableListOf(
            Component.translatable(SIGIL_TRANSLATABLE_DESCRIPTION_KEY + "dungeon_type").append(dungeonType.translatable),
            Component.translatable(SIGIL_TRANSLATABLE_DESCRIPTION_KEY + "min_dungeon_level", minBossLevel),
        )

    open val bossDeathSounds: List<SoundData> = listOf()
    open val bossDeathParticles: List<ParticleData> = listOf()
}