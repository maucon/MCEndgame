package de.fuballer.mcendgame.domain.entity.cerberus

import de.fuballer.mcendgame.domain.ability.Ability
import de.fuballer.mcendgame.domain.ability.ApplyDarknessAbility
import de.fuballer.mcendgame.domain.ability.FireCascadeAbility
import de.fuballer.mcendgame.domain.ability.ShootFireArrowsAbility
import de.fuballer.mcendgame.domain.entity.CustomEntityType
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.entity.EntityType

object CerberusEntityType : CustomEntityType {
    override val type = EntityType.RAVAGER

    override val customName = "Cerberus"
    override val canHaveWeapons = false
    override val isRanged = false
    override val canHaveArmor = false
    override val hideEquipment = true

    override val baseHealth = 100.0
    override val healthPerTier = 5.0
    override val baseDamage = 15.0
    override val damagePerTier = 3.5
    override val baseSpeed = 0.35
    override val speedPerTier = 0.0

    override val sounds = null
    override val abilities: List<RandomOption<Ability>> = listOf(
        RandomOption(50, FireCascadeAbility),
        RandomOption(30, ShootFireArrowsAbility),
        RandomOption(25, ApplyDarknessAbility),
    )
}