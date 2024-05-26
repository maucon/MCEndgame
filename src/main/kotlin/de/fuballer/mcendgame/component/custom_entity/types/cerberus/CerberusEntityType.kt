package de.fuballer.mcendgame.component.custom_entity.types.cerberus

import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.ApplyDarknessAbility
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.FireCascadeAbility
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.FlameBlastAbility
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.ShootFireArrowsAbility
import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.entity.EntityType

object CerberusEntityType : CustomEntityType {
    override val type = EntityType.RAVAGER

    override val customName = "Cerberus"
    override val canHaveWeapons = false
    override val isRanged = false
    override val canHaveArmor = false
    override val hideEquipment = true

    override val baseHealth = 125.0
    override val healthPerTier = 5.0
    override val baseDamage = 20.0
    override val damagePerTier = 3.5
    override val baseSpeed = 0.30
    override val speedPerTier = 0.003

    override val sounds = null
    override val abilities: List<RandomOption<Ability>> = listOf(
        RandomOption(35, FireCascadeAbility),
        RandomOption(30, ShootFireArrowsAbility),
        RandomOption(25, ApplyDarknessAbility),
        RandomOption(35, FlameBlastAbility),
    )
}