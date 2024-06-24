package de.fuballer.mcendgame.component.custom_entity.types.elf_duelist

import org.bukkit.util.Vector
import kotlin.random.Random

object ElfDuelistSettings {
    fun doesReflectArrow() = Random.nextDouble() < 0.3
    fun getReflectAddedYVelocity(distance: Double) = Vector(0.0, distance * 0.015, 0.0)
    const val REFLECT_ARROW_VELOCITY = 2.0

    const val RETREAT_VELOCITY = 1
    val RETREAT_ADDED_Y_VELOCITY = Vector(0.0, 0.2, 0.0)
}