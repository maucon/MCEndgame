package de.fuballer.mcendgame.main.component.item.custom.misc.horn.command

import net.minecraft.world.entity.player.Player

data class HornUseCommand(
    val user: Player,
    val moreDuration: MutableList<Double> = mutableListOf(),
    val moreCooldown: MutableList<Double> = mutableListOf(),
    var isStronger: Boolean = false,
) {
    fun getDurationFactor() = moreDuration.fold(1.0) { a, b -> a * (b + 1) }
    fun getCooldownFactor() = moreCooldown.fold(1.0) { a, b -> a * (b + 1) }
}