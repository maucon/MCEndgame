package de.fuballer.mcendgame.domain.entity.demoic_golem

import de.fuballer.mcendgame.component.custom_entity.EntitySoundData
import de.fuballer.mcendgame.domain.ability.Ability
import de.fuballer.mcendgame.domain.ability.ApplyDarknessAbility
import de.fuballer.mcendgame.domain.ability.FireCascadeAbility
import de.fuballer.mcendgame.domain.ability.SummonGravitationPillarAbility
import de.fuballer.mcendgame.domain.entity.CustomEntityType
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.entity.EntityType

object DemonicGolemEntityType : CustomEntityType {
    override val type = EntityType.RAVAGER

    override val customName = "Demonic Golem"
    override val canHaveWeapons = false
    override val isRanged = false
    override val canHaveArmor = false
    override val hideEquipment = true

    override val baseHealth = 150.0
    override val healthPerTier = 8.0
    override val baseDamage = 20.0
    override val damagePerTier = 3.0
    override val baseSpeed = 0.2
    override val speedPerTier = 0.0

    override val sounds = EntitySoundData.create("demonic_golem")
    override val abilities: List<RandomOption<Ability>> = listOf(
        RandomOption(50, FireCascadeAbility),
        RandomOption(40, SummonGravitationPillarAbility),
        RandomOption(30, ApplyDarknessAbility),
    )
}