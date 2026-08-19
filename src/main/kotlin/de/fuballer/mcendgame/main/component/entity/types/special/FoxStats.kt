package de.fuballer.mcendgame.main.component.entity.types.special

import de.fuballer.mcendgame.main.component.entity.EnemyEquipmentClass
import de.fuballer.mcendgame.main.component.entity.EntityTypeStats
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.animal.fox.Fox

object FoxStats : EntityTypeStats {
    override val type: EntityType<Fox> = EntityType.FOX

    override val equipmentClass = EnemyEquipmentClass.NO_EQUIPMENT
    override val canBeInvisible = false
    override val canHaveEffects: Boolean
        get() = false

    override val health = 10.0
    override val attackDamage = 4.0
    override val movementSpeed = 0.3
    override val knockbackResistance = 0.0

    override fun applyMisc(entity: LivingEntity) {
        entity.isInvulnerable = true
    }
}