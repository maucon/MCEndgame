package de.fuballer.mcendgame.main.component.entity.types

import de.fuballer.mcendgame.main.component.entity.EntityTypeStats
import de.fuballer.mcendgame.main.component.entity.EnemyEquipmentClass
import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import de.fuballer.mcendgame.main.component.entity.custom.entities.skeleton_mage.SkeletonMageEntity
import net.minecraft.world.entity.EntityType

object SkeletonMageStats : EntityTypeStats {
    override val type: EntityType<SkeletonMageEntity> = CustomEntities.SKELETON_MAGE

    override val equipmentClass= EnemyEquipmentClass.RANGED_SPELL_DAMAGE
    override val canBeInvisible = true

    override val health = 15.0
    override val attackDamage = 1.0
    override val spellDamage: Double
        get() = 3.0
    override val movementSpeed = 0.25
    override val knockbackResistance = 0.0
}