package de.fuballer.mcendgame.main.component.item.custom.aspect.item.sigil.scorched

import de.fuballer.mcendgame.main.component.dungeon.type.DungeonType
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.particle.ParticleData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.sound.RangeDefinedSoundData
import de.fuballer.mcendgame.main.component.item.custom.aspect.item.sigil.SigilItem
import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItem
import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItems
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.phys.Vec3
import kotlin.random.Random

class ScorchedSigil(
    settings: Properties,
) : SigilItem(settings) {
    companion object {
        private const val BASE_DROP_PROBABILITY = 0.05
        private const val DROP_PROBABILITY_PER_LEVEL = 0.0025
    }

    override val minBossLevel = 10
    override val minDropLevel = 10
    override fun getDropProbability(level: Int) = if (level < minDropLevel) 0.0 else BASE_DROP_PROBABILITY + DROP_PROBABILITY_PER_LEVEL * (level - minDropLevel)

    override val dungeonType = DungeonType.BURNING_CATHEDRAL

    override fun getCrystalItems(
        level: Int,
        lootMultiplier: Double,
        finalBoss: Boolean,
    ): List<CrystalItem> {
        if (!finalBoss) return listOf()

        val probability = level / 10.0 * lootMultiplier
        val count = probability.toInt() + if (Random.nextDouble() < probability % 1) 1 else 0
        return List(count) { CrystalItems.CORRUPTION_CRYSTAL } // TODO add double corrupt
    }

    override val description = mutableListOf(
        Component.translatable(TRANSLATABLE_DESCRIPTION_KEY + "scorched_0"),
        Component.translatable(TRANSLATABLE_DESCRIPTION_KEY + "scorched_1", minBossLevel),
    )

    override val bossDeathParticles = listOf(
        ParticleData(
            particle = { _, _ -> ParticleTypes.LARGE_SMOKE },
            offset = { _ -> Vec3(0.0, 1.0, 0.0) },
            count = 25,
            dist = { Vec3.ZERO },
            speed = 0.5,
        ),
        ParticleData(
            particle = { _, _ -> ParticleTypes.SMOKE },
            offset = { _ -> Vec3(0.0, 1.0, 0.0) },
            count = 35,
            dist = { Vec3.ZERO },
            speed = 0.5,
        ),
        ParticleData(
            particle = { _, _ -> ParticleTypes.FLAME },
            offset = { _ -> Vec3(0.0, 1.0, 0.0) },
            count = 50,
            dist = { Vec3.ZERO },
            speed = 0.5,
        ),
    )

    override val bossDeathSounds = listOf(
        RangeDefinedSoundData(
            SoundEvents.UI_TOAST_CHALLENGE_COMPLETE,
            { 1f },
            { 1F },
            SoundSource.PLAYERS,
            range = 64.0,
        )
    )
}