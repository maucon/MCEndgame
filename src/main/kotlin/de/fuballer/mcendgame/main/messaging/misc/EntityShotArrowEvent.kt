package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.arrow.AbstractArrow
import net.minecraft.world.item.ItemStack

data class EntityShotArrowEvent(
    val arrow: AbstractArrow,
    val owner: LivingEntity,
    val weapon: ItemStack?,
) {
    companion object {
        fun of(arrow: AbstractArrow, owner: LivingEntity) = EntityShotArrowEvent(arrow, owner, arrow.weaponItem)
    }
}