package de.fuballer.mcendgame.main.component.entity.types.boss

import de.fuballer.mcendgame.main.component.entity.EnemyEquipmentClass
import de.fuballer.mcendgame.main.component.entity.EntityTypeStats
import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver.BeastweaverEntity
import net.minecraft.world.entity.EntityType

object BeastweaverBossStats : EntityTypeStats {
    override val type: EntityType<BeastweaverEntity> = CustomEntities.BEASTWEAVER

    override val equipmentClass = EnemyEquipmentClass.NO_EQUIPMENT
    override val canBeInvisible = false

    override val health = 200.0
    override val attackDamage = 10.0
    override val movementSpeed = 0.35
    override val knockbackResistance = 0.8
}