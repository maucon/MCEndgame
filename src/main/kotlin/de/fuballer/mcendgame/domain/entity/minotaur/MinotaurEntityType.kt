package de.fuballer.mcendgame.domain.entity.minotaur

import de.fuballer.mcendgame.domain.ability.Ability
import de.fuballer.mcendgame.domain.ability.ApplySpeedAbility
import de.fuballer.mcendgame.domain.ability.LeapAbility
import de.fuballer.mcendgame.domain.entity.CustomEntityType
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.entity.EntityType

object MinotaurEntityType : CustomEntityType {
    override val type = EntityType.IRON_GOLEM

    override val customName = "Minotaur"
    override val canHaveWeapons = false
    override val isRanged = false
    override val canHaveArmor = false
    override val hideEquipment = true
    override val isSilent = true

    override val baseHealth = 100.0
    override val healthPerTier = 5.0
    override val baseDamage = 12.0
    override val damagePerTier = 3.0
    override val baseSpeed = 0.4
    override val speedPerTier = 0.0

    override val abilities: List<RandomOption<Ability>> = listOf(
        RandomOption(20, ApplySpeedAbility),
        RandomOption(50, LeapAbility),
    )
}