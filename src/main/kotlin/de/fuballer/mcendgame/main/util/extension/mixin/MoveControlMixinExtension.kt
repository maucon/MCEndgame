package de.fuballer.mcendgame.main.util.extension.mixin

import de.fuballer.mcendgame.main.mixin.move_control.MoveControlAccessor
import net.minecraft.world.entity.ai.control.MoveControl

object MoveControlMixinExtension {
    fun MoveControl<*>.getStrafeRight() = (this as? MoveControlAccessor)?.`mcendgame$getStrafeRight`() ?: 0.0f
    fun MoveControl<*>.getStrafeForwards() = (this as? MoveControlAccessor)?.`mcendgame$getStrafeForwards`() ?: 0.0f
}