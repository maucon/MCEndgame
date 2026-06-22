package de.fuballer.mcendgame.main.component.entity.types

import de.fuballer.mcendgame.main.component.entity.EntityTypeStats
import de.fuballer.mcendgame.main.component.entity.EnemyEquipmentClass
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.monster.skeleton.Skeleton

object SkeletonStats : EntityTypeStats {
    override val type: EntityType<Skeleton> = EntityType.SKELETON

    override val equipmentClass= EnemyEquipmentClass.RANGED_ATTACK_DAMAGE
    override val canBeInvisible = true

    override val health = 15.0
    override val attackDamage = 4.0
    override val movementSpeed = 0.25
    override val knockbackResistance = 0.0
}