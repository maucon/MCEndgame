package de.fuballer.mcendgame.component.artifact.data

import de.fuballer.mcendgame.component.artifact.artifacts.additional_arrows.AdditionalArrowsArtifactType
import de.fuballer.mcendgame.component.artifact.artifacts.attack_damage.AttackDamageArtifactType
import de.fuballer.mcendgame.component.artifact.artifacts.attack_speed.AttackSpeedArtifactType
import de.fuballer.mcendgame.component.artifact.artifacts.bow_damage.BowDamageArtifactType
import de.fuballer.mcendgame.component.artifact.artifacts.effect_steal.EffectStealArtifactType
import de.fuballer.mcendgame.component.artifact.artifacts.heal_on_block.HealOnBlockArtifactType
import de.fuballer.mcendgame.component.artifact.artifacts.inc_damage_against_full_life.IncDamageAgainstFullLifeArtifactType
import de.fuballer.mcendgame.component.artifact.artifacts.inc_damage_per_missing_health.IncDamagePerMissingHealthArtifactType
import de.fuballer.mcendgame.component.artifact.artifacts.max_health.MaxHealthArtifactType
import de.fuballer.mcendgame.component.artifact.artifacts.movement_speed.MovementSpeedArtifactType
import de.fuballer.mcendgame.component.artifact.artifacts.slow_when_hit.SlowWhenHitArtifactType
import de.fuballer.mcendgame.component.artifact.artifacts.taunt.TauntArtifactType
import de.fuballer.mcendgame.component.artifact.artifacts.wolf_companion.WolfCompanionArtifactType
import de.fuballer.mcendgame.technical.registry.Keyed
import de.fuballer.mcendgame.technical.registry.KeyedRegistry

interface ArtifactType : Keyed {
    val displayName: String
    fun getValues(tier: ArtifactTier): List<Double>
    fun getLore(tier: ArtifactTier): List<String>

    companion object {
        val REGISTRY = KeyedRegistry<ArtifactType>().also {
            it.register(AdditionalArrowsArtifactType)
            it.register(AttackDamageArtifactType)
            it.register(AttackSpeedArtifactType)
            it.register(BowDamageArtifactType)
            it.register(EffectStealArtifactType)
            it.register(HealOnBlockArtifactType)
            it.register(IncDamageAgainstFullLifeArtifactType)
            it.register(IncDamagePerMissingHealthArtifactType)
            it.register(MaxHealthArtifactType)
            it.register(MovementSpeedArtifactType)
            it.register(SlowWhenHitArtifactType)
            it.register(TauntArtifactType)
            it.register(WolfCompanionArtifactType)
        }
    }
}