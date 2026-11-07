package de.fuballer.mcendgame.main.component.item.custom.aspect.item.sigil

import de.fuballer.mcendgame.main.component.dungeon.type.DungeonType
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.particle.ParticleData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.sound.SoundData
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItem

abstract class SigilItem(
    settings: Properties,
) : AspectItem(settings) {
    override val tier = 0
    override val limit = 1
    override val disabledAspects = listOf<AspectItem>() // handled in AspectService

    abstract val minBossLevel: Int
    abstract val minDropLevel: Int
    abstract fun getDropProbability(level: Int): Double
    abstract val dungeonType: DungeonType

    open fun getCrystalItems(level: Int, lootMultiplier: Double, finalBoss: Boolean): List<CrystalItem> = listOf()

    open val bossDeathSounds: List<SoundData> = listOf()
    open val bossDeathParticles: List<ParticleData> = listOf()
}