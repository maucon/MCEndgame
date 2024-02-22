package de.fuballer.mcendgame.component.artifact.data

import de.fuballer.mcendgame.component.artifact.artifacts.armor.ArmorArtifactType
import de.fuballer.mcendgame.component.artifact.artifacts.armor_increase.ArmorIncreaseArtifactType
import de.fuballer.mcendgame.component.artifact.artifacts.armor_toughness.ArmorToughnessArtifactType
import de.fuballer.mcendgame.component.artifact.artifacts.attack_damage.AttackDamageArtifactType
import de.fuballer.mcendgame.component.artifact.artifacts.attack_speed.AttackSpeedArtifactType
import de.fuballer.mcendgame.component.artifact.artifacts.dodge.DodgeArtifactType
import de.fuballer.mcendgame.component.artifact.artifacts.experience.ExperienceArtifactType
import de.fuballer.mcendgame.component.artifact.artifacts.max_health.MaxHealthArtifactType
import de.fuballer.mcendgame.component.artifact.artifacts.movement_speed.MovementSpeedArtifactType
import de.fuballer.mcendgame.component.artifact.artifacts.projectile_damage.ProjectileDamageArtifactType
import de.fuballer.mcendgame.component.artifact.artifacts.wolf_companion.WolfCompanionArtifactType
import de.fuballer.mcendgame.technical.registry.Keyed
import de.fuballer.mcendgame.technical.registry.KeyedRegistry

interface ArtifactType : Keyed {
    val displayName: String
    fun getValues(tier: ArtifactTier): List<Double>
    fun getLore(tier: ArtifactTier): List<String>

    companion object {
        val REGISTRY = KeyedRegistry<ArtifactType>().also {
            it.register(ArmorArtifactType)
            it.register(ArmorIncreaseArtifactType)
            it.register(ArmorToughnessArtifactType)
            it.register(AttackDamageArtifactType)
            it.register(AttackSpeedArtifactType)
            it.register(DodgeArtifactType)
            it.register(ExperienceArtifactType)
            it.register(MaxHealthArtifactType)
            it.register(MovementSpeedArtifactType)
            it.register(ProjectileDamageArtifactType)
            it.register(WolfCompanionArtifactType)
        }
    }
}