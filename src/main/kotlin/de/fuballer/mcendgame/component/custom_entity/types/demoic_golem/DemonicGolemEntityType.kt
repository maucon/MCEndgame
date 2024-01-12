package de.fuballer.mcendgame.component.custom_entity.types.demoic_golem

import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.ApplyDarknessAbility
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.FireCascadeAbility
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.SummonGravitationPillarAbility
import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.entity.EntityType

object DemonicGolemEntityType : CustomEntityType {
    override val type = EntityType.RAVAGER

    override val customName = "Demonic Golem"
    override val canHaveWeapons = false
    override val isRanged = false
    override val canHaveArmor = false
    override val dropBaseLoot = false
    override val hideEquipment = true
    override val isSilent = true

    override val baseHealth = 150.0
    override val healthPerTier = 8.0
    override val baseDamage = 20.0
    override val damagePerTier = 3.0
    override val baseSpeed = 0.2
    override val speedPerTier = 0.0

    override val abilities: List<RandomOption<Ability>> = listOf(
        RandomOption(50, FireCascadeAbility),
        RandomOption(40, SummonGravitationPillarAbility),
        RandomOption(30, ApplyDarknessAbility),
    )
}