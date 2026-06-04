package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.world.entity.LivingEntity

data class MagicFindCommand(
    val entity: LivingEntity,
    var magicFind: Int = 0
)