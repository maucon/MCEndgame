package de.fuballer.mcendgame.component.custom_entity.types.mandragora

import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.PoisonCloudAbility
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.SummonVinesAbility
import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.entity.EntityType

object MandragoraEntityType : CustomEntityType {
    override val type = EntityType.RAVAGER

    override val customName = "Mandragora"
    override val canHaveWeapons = false
    override val isRanged = false
    override val canHaveArmor = false
    override val hideEquipment = true
    override val isSilent = true

    override val baseHealth = 120.0
    override val healthPerTier = 6.0
    override val baseDamage = 12.0
    override val damagePerTier = 3.0
    override val baseSpeed = 0.3
    override val speedPerTier = 0.0

    override val abilities: List<RandomOption<Ability>> = listOf(
        RandomOption(30, PoisonCloudAbility),
        RandomOption(30, SummonVinesAbility),
    )
}