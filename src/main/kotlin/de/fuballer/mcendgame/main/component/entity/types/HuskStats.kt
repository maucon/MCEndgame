package de.fuballer.mcendgame.main.component.entity.types

import de.fuballer.mcendgame.main.component.entity.EnemyEquipmentClass
import de.fuballer.mcendgame.main.component.entity.EntityTypeStats
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.monster.zombie.Husk

object HuskStats : EntityTypeStats {
    override val type: EntityType<Husk> = EntityType.HUSK

    override val equipmentClass = EnemyEquipmentClass.MELEE_ATTACK_DAMAGE
    override val canBeInvisible = true

    override val health = 25.0
    override val attackDamage = 5.0
    override val movementSpeed = 0.23
    override val knockbackResistance = 0.0

    override fun applyMisc(mob: Mob) {
        val huskEntity = mob as? Husk ?: return
        huskEntity.isBaby = false
    }
}