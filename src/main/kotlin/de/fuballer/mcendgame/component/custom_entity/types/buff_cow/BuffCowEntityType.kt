package de.fuballer.mcendgame.component.custom_entity.types.buff_cow

import de.fuballer.mcendgame.component.custom_entity.EntitySoundData
import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.CowStompAbility
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.PullAbility
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.ShootSnowballsAbility
import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.entity.EntityType

object BuffCowEntityType : CustomEntityType {
    override val type = EntityType.WITHER_SKELETON

    override val customName = "Buff Cow"
    override val canHaveWeapons = false
    override val isRanged = false
    override val canHaveArmor = false
    override val hideEquipment = true

    override val baseHealth = 300.0
    override val healthPerTier = 10.0
    override val baseDamage = 0.0
    override val damagePerTier = 0.0
    override val baseSpeed = 0.15
    override val speedPerTier = 0.0
    override val knockbackResistance = 0.8

    override val sounds = EntitySoundData.create("buff_cow")
    override val abilities: List<RandomOption<Ability>> = listOf(
        RandomOption(25, CowStompAbility),
        RandomOption(25, ShootSnowballsAbility),
        RandomOption(25, PullAbility),
    )
}