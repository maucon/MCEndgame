package de.fuballer.mcendgame.component.custom_entity.types.elf_duelist

import de.fuballer.mcendgame.component.custom_entity.EntitySoundData
import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import de.fuballer.mcendgame.component.custom_entity.ability.abilities.*
import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.entity.EntityType

object ElfDuelistEntityType : CustomEntityType {
    override val type = EntityType.WITHER_SKELETON

    override val customName = "Elf Duelist"
    override val canHaveWeapons = false
    override val isRanged = false
    override val canHaveArmor = false
    override val hideEquipment = true

    override val baseHealth = 200.0
    override val healthPerTier = 8.0
    override val baseDamage = 15.0
    override val damagePerTier = 3.0
    override val baseSpeed = 0.35
    override val speedPerTier = 0.005

    override val sounds = EntitySoundData.create("elf_duelist")
    override val abilities: List<RandomOption<Ability>> = listOf(
        RandomOption(5, ClonesAbility),
        RandomOption(45, SidestepAbility),
        RandomOption(15, KnockbackAbility),
        RandomOption(5, StompAbility),
        RandomOption(5, ApplyDarknessAbility),
    )
}