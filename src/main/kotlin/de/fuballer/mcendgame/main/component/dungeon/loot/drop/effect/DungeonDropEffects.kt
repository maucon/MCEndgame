package de.fuballer.mcendgame.main.component.dungeon.loot.drop.effect

import de.fuballer.mcendgame.main.component.dungeon.loot.drop.ItemColor
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundEvents

object DungeonDropEffects {
    val UNIQUE =
        DungeonDropEffect(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, ParticleTypes.FLAME, ItemColor.UNIQUE)
            .withVolume(2F)
            .withParticleCount(20)
            .withParticleSpeed(0.03)
    val UNIQUE_PLAYER_DROPPED = DungeonDropEffect(glowColor = ItemColor.UNIQUE)

    val ASPECT =
        DungeonDropEffect(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, ParticleTypes.SOUL_FIRE_FLAME, ItemColor.ASPECT)
            .withVolume(2F)
            .withParticleCount(20)
            .withParticleSpeed(0.03)
    val ASPECT_PLAYER_DROPPED = DungeonDropEffect(glowColor = ItemColor.ASPECT)

    val CRYSTAL =
        DungeonDropEffect(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, ParticleTypes.DRAGON_BREATH, ItemColor.CRYSTAL)
            .withVolume(2F)
            .withParticleCount(20)
            .withParticleSpeed(0.03)
    val CRYSTAL_PLAYER_DROPPED = DungeonDropEffect(glowColor = ItemColor.CRYSTAL)

    val TOTEM_BASIC =
        DungeonDropEffect(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, ParticleTypes.TOTEM_OF_UNDYING, ItemColor.TOTEM_BASIC)
            .withVolume(2F)
            .withParticleCount(20)
            .withParticleSpeed(0.5)
    val TOTEM_BASIC_PLAYER_DROPPED = DungeonDropEffect(glowColor = ItemColor.TOTEM_BASIC)

    val TOTEM_EFFECT =
        DungeonDropEffect(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, ParticleTypes.TOTEM_OF_UNDYING, ItemColor.TOTEM_EFFECT)
            .withVolume(2F)
            .withParticleCount(20)
            .withParticleSpeed(0.5)
    val TOTEM_EFFECT_PLAYER_DROPPED = DungeonDropEffect(glowColor = ItemColor.TOTEM_EFFECT)

    val TOTEM_ULTIMATE =
        DungeonDropEffect(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, ParticleTypes.TOTEM_OF_UNDYING, ItemColor.TOTEM_ULTIMATE)
            .withVolume(2F)
            .withParticleCount(20)
            .withParticleSpeed(0.5)
    val TOTEM_ULTIMATE_PLAYER_DROPPED = DungeonDropEffect(glowColor = ItemColor.TOTEM_ULTIMATE)
}