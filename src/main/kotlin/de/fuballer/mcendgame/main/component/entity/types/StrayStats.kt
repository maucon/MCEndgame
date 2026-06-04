package de.fuballer.mcendgame.main.component.entity.types

import de.fuballer.mcendgame.main.component.entity.EntityTypeStats
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.monster.skeleton.Stray

object StrayStats : EntityTypeStats {
    override val type: EntityType<Stray> = EntityType.STRAY

    override val canHaveWeapons = true
    override val isRanged = true
    override val canHaveArmor = true
    override val canBeInvisible = true

    override val health = 15.0
    override val attackDamage = 4.0
    override val movementSpeed = 0.25
    override val knockbackResistance = 0.0
}