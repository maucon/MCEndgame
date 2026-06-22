package de.fuballer.mcendgame.main.component.entity.types.boss

import de.fuballer.mcendgame.main.component.entity.EntityTypeStats
import de.fuballer.mcendgame.main.component.entity.EnemyEquipmentClass
import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import de.fuballer.mcendgame.main.component.entity.custom.entities.elf_duelist.ElfDuelistEntity
import net.minecraft.world.entity.EntityType

object ElfDuelistBossStats : EntityTypeStats {
    override val type: EntityType<ElfDuelistEntity> = CustomEntities.ELF_DUELIST

    override val equipmentClass= EnemyEquipmentClass.NO_EQUIPMENT
    override val canBeInvisible = false

    override val health = 100.0
    override val attackDamage = 10.0
    override val movementSpeed = 0.33
    override val knockbackResistance = 0.0
}