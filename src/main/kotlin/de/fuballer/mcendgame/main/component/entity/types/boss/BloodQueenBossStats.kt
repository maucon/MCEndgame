package de.fuballer.mcendgame.main.component.entity.types.boss

import de.fuballer.mcendgame.main.component.entity.EnemyEquipmentClass
import de.fuballer.mcendgame.main.component.entity.EntityTypeStats
import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import de.fuballer.mcendgame.main.component.entity.custom.entities.blood_queen.BloodQueenEntity
import net.minecraft.world.entity.EntityType
import kotlin.random.Random

object BloodQueenBossStats : EntityTypeStats {
    override val type: EntityType<BloodQueenEntity> = CustomEntities.BLOOD_QUEEN

    override val equipmentClass = EnemyEquipmentClass.NO_EQUIPMENT
    override val canBeInvisible = false

    override val health = 1300.0
    override val attackDamage = 10.0
    override val movementSpeed = 0.35
    override val knockbackResistance = 0.8

    override fun getRandomScale(random: Random) = 1.0
}