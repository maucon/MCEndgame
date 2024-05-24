package de.fuballer.mcendgame.component.custom_entity.types.buff_cow

import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.ChangeTargetAbility
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.PullAbility
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.ShootSnowballsAbility
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.StompAbility
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

    override val baseHealth = 200.0
    override val healthPerTier = 10.0
    override val baseDamage = 0.0
    override val damagePerTier = 0.0
    override val baseSpeed = 0.15
    override val speedPerTier = 0.0

    override val sounds = null
    override val abilities: List<RandomOption<Ability>> = listOf(
        RandomOption(25, ChangeTargetAbility),
        RandomOption(25, StompAbility),
        RandomOption(25, ShootSnowballsAbility),
        RandomOption(25, PullAbility),
    )
}