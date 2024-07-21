package de.fuballer.mcendgame.component.custom_entity.types.demonic_golem

import de.fuballer.mcendgame.component.custom_entity.EntitySoundData
import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.ApplyDarknessAbility
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.FireCascadeAbility
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.FlameBlastAbility
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
    override val hideEquipment = true

    override val baseHealth = 300.0
    override val healthPerTier = 12.0
    override val baseDamage = 15.0
    override val damagePerTier = 2.5
    override val baseSpeed = 0.2
    override val speedPerTier = 0.0
    override val knockbackResistance = 0.9

    override val sounds = EntitySoundData.create("demonic_golem")
    override val abilities: List<RandomOption<Ability>> = listOf(
        RandomOption(35, FireCascadeAbility),
        RandomOption(25, SummonGravitationPillarAbility),
        RandomOption(10, ApplyDarknessAbility),
        RandomOption(30, FlameBlastAbility),
    )
}