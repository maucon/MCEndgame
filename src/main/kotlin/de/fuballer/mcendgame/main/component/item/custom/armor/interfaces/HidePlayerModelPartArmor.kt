package de.fuballer.mcendgame.main.component.item.custom.armor.interfaces

import net.minecraft.world.entity.player.PlayerModelPart

interface HidePlayerModelPartArmor {
    val hiddenPlayerModelParts: List<PlayerModelPart>

    fun hidesModelPart(modelPart: PlayerModelPart) = hiddenPlayerModelParts.contains(modelPart)
}