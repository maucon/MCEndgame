package de.fuballer.mcendgame.main.component.entity.types.boss.beastweaver

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.addCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.entity.EnemyEquipmentClass
import de.fuballer.mcendgame.main.component.entity.EntityTypeStats
import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver.BeastweaverEntity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import java.util.*

object BeastweaverBossStats : EntityTypeStats {
    override val type: EntityType<BeastweaverEntity> = CustomEntities.BEASTWEAVER

    override val equipmentClass = EnemyEquipmentClass.NO_EQUIPMENT
    override val canBeInvisible = false

    override val health = 1300.0
    override val attackDamage = 10.0
    override val movementSpeed = 0.35
    override val knockbackResistance = 0.8

    override fun getRandomScale(random: Random) = 1.0

    override fun applyMisc(entity: LivingEntity) {
        entity.addCustomAttribute(CustomAttribute(CustomAttributeTypes.MORE_DAMAGE_TAKEN_WHILE_ON_FIRE, roll = DoubleRoll(DoubleBounds(0.1))))
    }
}