package de.fuballer.mcendgame.main.component.entity.types

import de.fuballer.mcendgame.main.component.entity.EnemyEquipmentClass
import de.fuballer.mcendgame.main.component.entity.EntityTypeStats
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.monster.zombie.Zombie

object ZombieStats : EntityTypeStats {
    override val type: EntityType<Zombie> = EntityType.ZOMBIE

    override val equipmentClass = EnemyEquipmentClass.MELEE_ATTACK_DAMAGE
    override val canBeInvisible = true

    override val health = 20.0
    override val attackDamage = 5.0
    override val movementSpeed = 0.23
    override val knockbackResistance = 0.0

    override fun applyMisc(entity: LivingEntity) {
        val zombieEntity = entity as? Zombie ?: return
        zombieEntity.isBaby = false
    }
}