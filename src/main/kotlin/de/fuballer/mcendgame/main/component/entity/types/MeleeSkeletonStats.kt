package de.fuballer.mcendgame.main.component.entity.types

import de.fuballer.mcendgame.main.component.entity.EntityTypeStats
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.monster.skeleton.Skeleton

object MeleeSkeletonStats : EntityTypeStats {
    override val type: EntityType<Skeleton> = EntityType.SKELETON

    override val canHaveWeapons = true
    override val isRanged = false
    override val canHaveArmor = true
    override val canBeInvisible = true

    override val health = 18.0
    override val attackDamage = 5.0
    override val movementSpeed = 0.25
    override val knockbackResistance = 0.0
}