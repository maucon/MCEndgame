package de.fuballer.mcendgame.main.component.dungeon.generation.encounter.encounters.scarred_one

import de.fuballer.mcendgame.main.util.extension.EntityExtension.isOrIsTameableOf
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isDungeonBoss
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isDungeonEnemy
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text
import java.util.function.Predicate

private const val TRANSLATION_KEY_BASE = "text.mcendgame.scarred_one.effect_targets."

enum class ScarredOneEffectTargetGroup(
    val predicate: Predicate<LivingEntity>,
    val text: Text,
) {
    ALLIES(
        { it.isOrIsTameableOf(PlayerEntity::class.java) },
        Text.translatable(TRANSLATION_KEY_BASE + "allies"),
    ),
    ENEMIES(
        { it.isDungeonEnemy() },
        Text.translatable(TRANSLATION_KEY_BASE + "enemies"),
    ),
    NON_BOSS_ENEMIES(
        { it.isDungeonEnemy() && !it.isDungeonBoss() },
        Text.translatable(TRANSLATION_KEY_BASE + "non_boss_enemies"),
    ),
    BOSSES(
        { it.isDungeonBoss() },
        Text.translatable(TRANSLATION_KEY_BASE + "bosses"),
    ),
}