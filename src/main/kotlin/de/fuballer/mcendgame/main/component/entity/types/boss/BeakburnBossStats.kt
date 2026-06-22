package de.fuballer.mcendgame.main.component.entity.types.boss

import de.fuballer.mcendgame.main.component.entity.EntityTypeStats
import de.fuballer.mcendgame.main.component.entity.EnemyEquipmentClass
import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import de.fuballer.mcendgame.main.component.entity.custom.entities.beakburn.BeakburnEntity
import net.minecraft.world.entity.EntityType

object BeakburnBossStats : EntityTypeStats {
    override val type: EntityType<BeakburnEntity> = CustomEntities.BEAKBURN

    override val equipmentClass= EnemyEquipmentClass.NO_EQUIPMENT
    override val canBeInvisible = false

    override val health = 130.0
    override val attackDamage = 10.0
    override val movementSpeed = 0.3
    override val knockbackResistance = 0.8
}