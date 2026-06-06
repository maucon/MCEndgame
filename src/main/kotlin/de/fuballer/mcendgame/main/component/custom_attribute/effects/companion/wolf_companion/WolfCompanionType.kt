package de.fuballer.mcendgame.main.component.custom_attribute.effects.companion.wolf_companion

import de.fuballer.mcendgame.main.component.custom_attribute.effects.data.AuraStatusEffect
import de.fuballer.mcendgame.main.component.status_effect.CustomStatusEffects
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.setVisualFire
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.animal.wolf.Wolf
import net.minecraft.world.entity.animal.wolf.WolfVariant
import net.minecraft.world.entity.animal.wolf.WolfVariants
import net.minecraft.world.item.DyeColor

enum class WolfCompanionType(
    val displayName: String,
    val variant: ResourceKey<WolfVariant>,
    val color: DyeColor,
    val scale: Double,
    val allyAuraStatusEffects: List<AuraStatusEffect> = listOf(),
    val enemyAuraStatusEffects: List<AuraStatusEffect> = listOf(),
    val selfEffects: Map<Holder<MobEffect>, Int> = mapOf(),
    val applyExtras: (wolf: Wolf) -> Unit = {},
) {
    SLOWING(
        "Slowing", WolfVariants.SNOWY, DyeColor.LIGHT_BLUE, 1.05,
        enemyAuraStatusEffects = listOf(AuraStatusEffect(MobEffects.SLOWNESS, 0, 199, 15)),
    ),
    GUARDING(
        "Guarding", WolfVariants.CHESTNUT, DyeColor.PURPLE, 1.1,
        allyAuraStatusEffects = listOf(AuraStatusEffect(CustomStatusEffects.RESILIENCE, 3, 199, 15)),
    ),
    INTIMIDATING(
        "Intimidating", WolfVariants.BLACK, DyeColor.BLACK, 1.15,
        enemyAuraStatusEffects = listOf(AuraStatusEffect(MobEffects.WEAKNESS, 1, 199, 15)),
    ),
    INCITING(
        "Inciting", WolfVariants.SPOTTED, DyeColor.RED, 1.0,
        allyAuraStatusEffects = listOf(AuraStatusEffect(MobEffects.STRENGTH, 0, 199, 15)),
    ),
    HASTING(
        "Hasting", WolfVariants.STRIPED, DyeColor.YELLOW, 0.9,
        allyAuraStatusEffects = listOf(AuraStatusEffect(MobEffects.SPEED, 0, 199, 15)),
    ),
    REJUVENATING(
        "Rejuvenating", WolfVariants.WOODS, DyeColor.GREEN, 1.0,
        allyAuraStatusEffects = listOf(AuraStatusEffect(MobEffects.REGENERATION, 0, 199, 15)),
    ),
    SCORCHING(
        "Scorching", WolfVariants.RUSTY, DyeColor.ORANGE, 0.95,
        allyAuraStatusEffects = listOf(AuraStatusEffect(MobEffects.FIRE_RESISTANCE, 0, 199, 15)),
        selfEffects = mapOf(CustomStatusEffects.SCORCH to 0),
        applyExtras = { wolf -> wolf.setVisualFire() }
    );

    companion object {
        fun getNames() = entries.map { it.displayName }

        fun getByName(name: String) = entries.firstOrNull { it.displayName == name }
    }
}