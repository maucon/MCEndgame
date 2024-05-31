package de.fuballer.mcendgame.component.custom_entity.types.reaper

import de.fuballer.mcendgame.component.custom_entity.EntitySoundData
import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.ApplySpeedAbility
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.ClonesAbility
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.FlameBlastAbility
import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.entity.EntityType

object ReaperEntityType : CustomEntityType {
    override val type = EntityType.WITHER_SKELETON

    override val customName = "Reaper"
    override val canHaveWeapons = false
    override val isRanged = false
    override val canHaveArmor = false
    override val hideEquipment = true

    override val baseHealth = 100.0
    override val healthPerTier = 5.0
    override val baseDamage = 25.0
    override val damagePerTier = 4.0
    override val baseSpeed = 0.35
    override val speedPerTier = 0.005

    override val sounds = EntitySoundData.create("reaper")
    override val abilities: List<RandomOption<Ability>> = listOf(
        RandomOption(25, ApplySpeedAbility),
        RandomOption(25, FlameBlastAbility),
        RandomOption(25, ClonesAbility),
    )
}