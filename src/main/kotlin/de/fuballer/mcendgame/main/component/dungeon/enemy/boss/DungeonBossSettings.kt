package de.fuballer.mcendgame.main.component.dungeon.enemy.boss

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.particle.ParticleData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.sound.SoundData
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.phys.Vec3

object DungeonBossSettings {
    const val MORE_LOOT_PER_KILLED_BOSS = 0.25
    const val LESS_DAMAGE_TAKEN_PER_KILLED_BOSS = 0.15
    const val MORE_DAMAGE_PER_KILLED_BOSS = 0.1

    fun getAttributePerKilledBoss(bossesKilled: Int) = listOf(
        getMoreLootAttribute(bossesKilled),
        getLessDamageTakenAttribute(bossesKilled),
        getMoreDamageAttribute(bossesKilled),
    )

    private fun getMoreLootAttribute(bossesKilled: Int) = CustomAttribute(
        CustomAttributeTypes.DROP_MORE_LOOT,
        roll = DoubleRoll(DoubleBounds(MORE_LOOT_PER_KILLED_BOSS * bossesKilled))
    )

    private fun getLessDamageTakenAttribute(bossesKilled: Int) = CustomAttribute(
        CustomAttributeTypes.MORE_DAMAGE_TAKEN,
        roll = DoubleRoll(DoubleBounds(-LESS_DAMAGE_TAKEN_PER_KILLED_BOSS * bossesKilled))
    )

    private fun getMoreDamageAttribute(bossesKilled: Int) = CustomAttribute(
        CustomAttributeTypes.MORE_DAMAGE,
        roll = DoubleRoll(DoubleBounds(MORE_DAMAGE_PER_KILLED_BOSS * bossesKilled))
    )

    val DEFAULT_DEATH_SOUNDS = listOf(
        SoundData(
            SoundEvents.PLAYER_LEVELUP,
            { 0.5f },
            { 0.5F },
            SoundSource.PLAYERS,
        )
    )
    val DEFAULT_DEATH_PARTICLES = listOf(
        ParticleData(
            particle = { _, _ -> ParticleTypes.LARGE_SMOKE },
            offset = { _ -> Vec3(0.0, 1.0, 0.0) },
            count = 20,
            dist = { Vec3.ZERO },
            speed = 0.5,
        ),
        ParticleData(
            particle = { _, _ -> ParticleTypes.WHITE_SMOKE },
            offset = { _ -> Vec3(0.0, 1.0, 0.0) },
            count = 50,
            dist = { Vec3.ZERO },
            speed = 0.5,
        )
    )
}