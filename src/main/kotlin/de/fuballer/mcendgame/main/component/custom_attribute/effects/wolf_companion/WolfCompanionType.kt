package de.fuballer.mcendgame.main.component.custom_attribute.effects.wolf_companion

import de.fuballer.mcendgame.main.component.custom_attribute.effects.data.AuraStatusEffect
import de.fuballer.mcendgame.main.component.status_effect.CustomStatusEffects
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.setVisualFire
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.passive.WolfEntity
import net.minecraft.entity.passive.WolfVariant
import net.minecraft.entity.passive.WolfVariants
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.DyeColor

enum class WolfCompanionType(
    val displayName: String,
    val variant: RegistryKey<WolfVariant>,
    val color: DyeColor,
    val scale: Double,
    val allyAuraStatusEffects: List<AuraStatusEffect> = listOf(),
    val enemyAuraStatusEffects: List<AuraStatusEffect> = listOf(),
    val selfEffects: Map<RegistryEntry<StatusEffect>, Int> = mapOf(),
    val applyExtras: (wolf: WolfEntity) -> Unit = {},
) {
    SLOWING(
        "Slowing", WolfVariants.SNOWY, DyeColor.LIGHT_BLUE, 1.05,
        enemyAuraStatusEffects = listOf(AuraStatusEffect(StatusEffects.SLOWNESS, 0, 199, 15)),
    ),
    GUARDING(
        "Guarding", WolfVariants.CHESTNUT, DyeColor.PURPLE, 1.1,
        allyAuraStatusEffects = listOf(AuraStatusEffect(CustomStatusEffects.RESILIENCE, 3, 199, 15)),
    ),
    INTIMIDATING(
        "Intimidating", WolfVariants.BLACK, DyeColor.BLACK, 1.15,
        enemyAuraStatusEffects = listOf(AuraStatusEffect(StatusEffects.WEAKNESS, 1, 199, 15)),
    ),
    INCITING(
        "Inciting", WolfVariants.SPOTTED, DyeColor.RED, 1.0,
        allyAuraStatusEffects = listOf(AuraStatusEffect(StatusEffects.STRENGTH, 0, 199, 15)),
    ),
    HASTING(
        "Hasting", WolfVariants.STRIPED, DyeColor.YELLOW, 0.9,
        allyAuraStatusEffects = listOf(AuraStatusEffect(StatusEffects.SPEED, 0, 199, 15)),
    ),
    REJUVENATING(
        "Rejuvenating", WolfVariants.WOODS, DyeColor.GREEN, 1.0,
        allyAuraStatusEffects = listOf(AuraStatusEffect(StatusEffects.REGENERATION, 0, 199, 15)),
    ),
    SCORCHING(
        "Scorching", WolfVariants.RUSTY, DyeColor.ORANGE, 0.95,
        allyAuraStatusEffects = listOf(AuraStatusEffect(StatusEffects.FIRE_RESISTANCE, 0, 199, 15)),
        selfEffects = mapOf(CustomStatusEffects.SCORCH to 0),
        applyExtras = { wolf -> wolf.setVisualFire() }
    );

    companion object {
        fun getNames() = WolfCompanionType.entries.map { it.displayName }

        fun getByName(name: String) = WolfCompanionType.entries.firstOrNull { it.displayName == name }
    }
}