package de.fuballer.mcendgame.component.custom_entity.types.necromancer

import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.ApplyDarknessAbility
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.ApplySpeedAbility
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.KnockbackAbility
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.SummonChupacabraAbility
import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.entity.EntityType

object NecromancerEntityType : CustomEntityType {
    override val type = EntityType.SKELETON

    override val customName = "Necromancer"
    override val canHaveWeapons = true
    override val isRanged = true
    override val canHaveArmor = false
    override val hideEquipment = true

    override val baseHealth = 200.0
    override val healthPerTier = 8.0
    override val baseDamage = 15.0
    override val damagePerTier = 2.5
    override val baseSpeed = 0.4
    override val speedPerTier = 0.0025

    override val sounds = null
    override val abilities: List<RandomOption<Ability>> = listOf(
        RandomOption(5, ApplySpeedAbility),
        RandomOption(10, ApplyDarknessAbility),
        RandomOption(25, SummonChupacabraAbility),
        RandomOption(25, KnockbackAbility),
    )
}