package de.fuballer.mcendgame.component.totem.data

import de.fuballer.mcendgame.component.totem.totems.armor.ArmorTotemType
import de.fuballer.mcendgame.component.totem.totems.armor_toughness.ArmorToughnessTotemType
import de.fuballer.mcendgame.component.totem.totems.attack_damage.AttackDamageTotemType
import de.fuballer.mcendgame.component.totem.totems.attack_speed.AttackSpeedTotemType
import de.fuballer.mcendgame.component.totem.totems.dodge.DodgeTotemType
import de.fuballer.mcendgame.component.totem.totems.experience.ExperienceTotemType
import de.fuballer.mcendgame.component.totem.totems.less_damage_taken.LessDamageTakenTotemType
import de.fuballer.mcendgame.component.totem.totems.max_health.MaxHealthTotemType
import de.fuballer.mcendgame.component.totem.totems.movement_speed.MovementSpeedTotemType
import de.fuballer.mcendgame.component.totem.totems.projectile_damage.ProjectileDamageTotemType
import de.fuballer.mcendgame.component.totem.totems.wolf_companion.WolfCompanionTotemType
import de.fuballer.mcendgame.technical.registry.Keyed
import de.fuballer.mcendgame.technical.registry.KeyedRegistry
import net.kyori.adventure.text.Component

interface TotemType : Keyed {
    val displayName: String
    fun getValues(tier: TotemTier): List<Double>
    fun getLore(tier: TotemTier): List<Component>

    companion object {
        val REGISTRY = KeyedRegistry<TotemType>().also {
            it.register(ArmorTotemType)
            it.register(LessDamageTakenTotemType)
            it.register(ArmorToughnessTotemType)
            it.register(AttackDamageTotemType)
            it.register(AttackSpeedTotemType)
            it.register(DodgeTotemType)
            it.register(ExperienceTotemType)
            it.register(MaxHealthTotemType)
            it.register(MovementSpeedTotemType)
            it.register(ProjectileDamageTotemType)
            it.register(WolfCompanionTotemType)
        }
    }
}