package de.fuballer.mcendgame.main.component.entity.custom.interfaces

import net.minecraft.world.entity.LivingEntity

interface MeleeAttackMob {
    fun meleeAttack(target: LivingEntity)
}