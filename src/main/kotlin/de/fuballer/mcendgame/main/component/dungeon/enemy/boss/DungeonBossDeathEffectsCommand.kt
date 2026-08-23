package de.fuballer.mcendgame.main.component.dungeon.enemy.boss

import de.fuballer.mcendgame.main.component.entity.custom.attack.data.particle.ParticleData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.sound.SoundData
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Mob

data class DungeonBossDeathEffectsCommand(
    val level: ServerLevel,
    val boss: Mob,
    val sounds: MutableList<SoundData>,
    val particles: MutableList<ParticleData>,
)