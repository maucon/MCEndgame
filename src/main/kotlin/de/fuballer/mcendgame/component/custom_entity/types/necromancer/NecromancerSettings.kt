package de.fuballer.mcendgame.component.custom_entity.types.necromancer

import org.bukkit.util.Vector
import kotlin.random.Random

object NecromancerSettings {
    const val BATS_AMOUNT = 7
    fun getBatDuration() = (75 + Random.nextDouble() * 50).toLong()
    fun doesBatBlockArrow() = Random.nextDouble() < 0.25
    fun getBatVelocity() = Vector(Random.nextDouble() * 3.0 - 1.5, Random.nextDouble(), Random.nextDouble() * 3.0 - 1.5)
}