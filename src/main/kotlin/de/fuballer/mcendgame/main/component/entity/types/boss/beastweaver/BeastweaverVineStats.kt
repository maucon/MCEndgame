package de.fuballer.mcendgame.main.component.entity.types.boss.beastweaver

import de.fuballer.mcendgame.main.component.entity.EnemyEquipmentClass
import de.fuballer.mcendgame.main.component.entity.EntityTypeStats
import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver.beastweaver_vine.BeastweaverVineEntity
import net.minecraft.world.entity.EntityType

object BeastweaverVineStats : EntityTypeStats {
    override val type: EntityType<BeastweaverVineEntity> = CustomEntities.BEASTWEAVER_VINE

    override val equipmentClass = EnemyEquipmentClass.NO_EQUIPMENT
    override val canBeInvisible = false

    override val health = 1.0
    override val attackDamage = 10.0
    override val movementSpeed = 0.0
    override val knockbackResistance = 1.0
}