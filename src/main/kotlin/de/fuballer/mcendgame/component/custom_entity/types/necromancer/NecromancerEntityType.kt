package de.fuballer.mcendgame.component.custom_entity.types.necromancer

import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.ApplyDarknessAbility
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.ApplySpeedAbility
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.ChangeTargetAbility
import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.entity.EntityType

object NecromancerEntityType : CustomEntityType {
    override val type = EntityType.EVOKER

    override val customName = "Necromancer"
    override val canHaveWeapons = true
    override val isRanged = true
    override val canHaveArmor = false
    override val hideEquipment = true

    override val baseHealth = 100.0
    override val healthPerTier = 5.0
    override val baseDamage = 15.0
    override val damagePerTier = 3.0
    override val baseSpeed = 0.4
    override val speedPerTier = 0.0025

    override val sounds = null
    override val abilities: List<RandomOption<Ability>> = listOf(
        RandomOption(25, ChangeTargetAbility),
        RandomOption(15, ApplySpeedAbility),
        RandomOption(25, ApplyDarknessAbility),
    )
}