package de.fuballer.mcendgame.main.component.item.custom.aspect.item.grove

import de.fuballer.mcendgame.main.component.entity.custom.attack.data.particle.ParticleData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.sound.RangeDefinedSoundData
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItems
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.phys.Vec3

class AspectOfTheGrove(
    settings: Properties,
) : AspectItem(settings) {
    companion object {
        const val MIN_DUNGEON_LEVEL = 10

        const val MIN_DROP_LEVEL = 10
        private const val BASE_DROP_PROBABILITY = 0.05
        private const val DROP_PROBABILITY_PER_LEVEL = 0.0025
        fun getDropProbability(level: Int) = if (level < MIN_DROP_LEVEL) 0.0 else BASE_DROP_PROBABILITY + DROP_PROBABILITY_PER_LEVEL * (level - MIN_DROP_LEVEL)

        val BOSS_DEATH_SOUND = RangeDefinedSoundData(
            SoundEvents.UI_TOAST_CHALLENGE_COMPLETE,
            { 1f },
            { 1F },
            SoundSource.PLAYERS,
            range = 64.0,
        )
        val BOSS_DEATH_PARTICLES = listOf(
            ParticleData(
                particle = { _, _ -> ParticleTypes.LARGE_SMOKE },
                offset = { _ -> Vec3(0.0, 1.0, 0.0) },
                count = 30,
                dist = { Vec3.ZERO },
                speed = 0.5,
            ),
            ParticleData(
                particle = { _, _ -> ParticleTypes.WHITE_SMOKE },
                offset = { _ -> Vec3(0.0, 1.0, 0.0) },
                count = 100,
                dist = { Vec3.ZERO },
                speed = 0.5,
            ),
            ParticleData(
                particle = { _, _ -> ParticleTypes.SPORE_BLOSSOM_AIR },
                offset = { _ -> Vec3(0.0, 1.0, 0.0) },
                count = 100,
                dist = { Vec3(0.5, 0.5, 0.5) },
                speed = 1.0,
            ),
        )
    }

    override val tier = 0
    override val limit = 1
    override val description = mutableListOf(
        Component.translatable(TRANSLATABLE_DESCRIPTION_KEY + "grove_0"),
        Component.translatable(TRANSLATABLE_DESCRIPTION_KEY + "grove_1", MIN_DUNGEON_LEVEL),
    )
    override val disabledAspects = listOf(
        AspectItems.ASPECT_OF_GHOSTS,
        AspectItems.ASPECT_OF_DUALITY,
        AspectItems.ASPECT_OF_TYRANNY,
        AspectItems.ASPECT_OF_GREED,
        AspectItems.ASPECT_OF_DOMINION,
        AspectItems.ASPECT_OF_HORDES,
        AspectItems.ASPECT_OF_CURIO,
        AspectItems.ASPECT_OF_FORTUNE,
        AspectItems.ASPECT_OF_EMINENCE,
        AspectItems.ASPECT_OF_ANCESTORS,
        AspectItems.ASPECT_OF_ZEAL,
    )
}