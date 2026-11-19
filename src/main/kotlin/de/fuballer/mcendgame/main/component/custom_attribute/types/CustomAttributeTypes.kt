package de.fuballer.mcendgame.main.component.custom_attribute.types

import de.fuballer.mcendgame.main.component.custom_attribute.affinity.AttributeAffinities
import de.fuballer.mcendgame.main.component.custom_attribute.data.AttributeFormat
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttributeType
import de.fuballer.mcendgame.main.component.custom_attribute.data.ValueFormats.DOUBLE
import de.fuballer.mcendgame.main.component.custom_attribute.data.ValueFormats.INT
import de.fuballer.mcendgame.main.component.custom_attribute.data.ValueFormats.PERCENT
import de.fuballer.mcendgame.main.component.custom_attribute.data.ValueFormats.SIGNED_DOUBLE
import de.fuballer.mcendgame.main.component.custom_attribute.data.ValueFormats.SIGNED_INT
import de.fuballer.mcendgame.main.component.custom_attribute.data.ValueFormats.SIGNED_PERCENT
import de.fuballer.mcendgame.main.component.custom_attribute.data.ValueFormats.STRING
import de.fuballer.mcendgame.main.component.custom_attribute.sign_based_keyword.SignBasedKeywords
import net.minecraft.ChatFormatting
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

object CustomAttributeTypes {
    private val ERROR = CustomAttributeType("error", AttributeFormat.NONE, AttributeAffinities.EMPTY)

    // DEFENSE
    val SPELL_RESISTANCE = CustomAttributeType("spell_resistance", AttributeFormat.of(SIGNED_PERCENT), AttributeAffinities.BENEFICIAL)
    val DODGE = CustomAttributeType("dodge", AttributeFormat.of(PERCENT), AttributeAffinities.BENEFICIAL)
    val DODGE_PER_MAX_HEART_BELOW_TEN = CustomAttributeType("dodge_per_max_heart_below_ten", AttributeFormat.of(PERCENT), AttributeAffinities.BENEFICIAL)
    val DODGE_IF_NOT_DODGED_IN_LAST_SECONDS = CustomAttributeType(
        "dodge_if_not_dodged_in_last_seconds",
        AttributeFormat.of(PERCENT, INT),
        AttributeAffinities.BENEFICIAL_CONDITIONAL_DETRIMENTAL
    )
    val PROJECTILE_DODGE = CustomAttributeType("projectile_dodge", AttributeFormat.of(PERCENT), AttributeAffinities.BENEFICIAL)
    val DODGED_PROJECTILE_REFLECT = CustomAttributeType("dodged_projectile_reflect", AttributeFormat.NONE, AttributeAffinities.BENEFICIAL)
    val MORE_DAMAGE_TAKEN = CustomAttributeType("more_damage_taken", AttributeFormat.of(PERCENT), AttributeAffinities.DETRIMENTAL, SignBasedKeywords.MORE)
    val MORE_PROJECTILE_DAMAGE_TAKEN =
        CustomAttributeType("more_projectile_damage_taken", AttributeFormat.of(PERCENT), AttributeAffinities.DETRIMENTAL, SignBasedKeywords.MORE)
    val GAIN_ENEMY_ARMOR_ON_KILL = CustomAttributeType(
        "gain_enemy_armor_on_kill",
        AttributeFormat.of(PERCENT, INT),
        AttributeAffinities.BENEFICIAL_CONDITIONAL_BENEFICIAL
    )
    val MORE_DAMAGE_TAKEN_WHILE_ON_FIRE =
        CustomAttributeType("more_damage_taken_while_on_fire", AttributeFormat.of(PERCENT), AttributeAffinities.DETRIMENTAL, SignBasedKeywords.MORE)
    val FLAT_DAMAGE_TAKEN = CustomAttributeType("flat_damage_taken", AttributeFormat.of(SIGNED_DOUBLE), AttributeAffinities.DETRIMENTAL)
    val MORE_DAMAGE_TAKEN_PER_MISSING_HEART =
        CustomAttributeType("more_damage_taken_per_missing_heart", AttributeFormat.of(PERCENT), AttributeAffinities.DETRIMENTAL, SignBasedKeywords.MORE)

    // OFFENSE
    val SPELL_DAMAGE = CustomAttributeType("spell_damage", AttributeFormat.of(SIGNED_DOUBLE), AttributeAffinities.BENEFICIAL)
    val INCREASED_DAMAGE =
        CustomAttributeType("increased_damage", AttributeFormat.of(SIGNED_PERCENT), AttributeAffinities.BENEFICIAL, SignBasedKeywords.INCREASED)
    val MORE_DAMAGE = CustomAttributeType("more_damage", AttributeFormat.of(PERCENT), AttributeAffinities.BENEFICIAL, SignBasedKeywords.MORE)
    val MORE_DAMAGE_PER_MISSING_HEART =
        CustomAttributeType("more_damage_per_missing_heart", AttributeFormat.of(PERCENT), AttributeAffinities.BENEFICIAL, SignBasedKeywords.MORE)
    val INCREASED_SPELL_DAMAGE =
        CustomAttributeType("increased_spell_damage", AttributeFormat.of(SIGNED_PERCENT), AttributeAffinities.BENEFICIAL, SignBasedKeywords.INCREASED)
    val MORE_SPELL_DAMAGE =
        CustomAttributeType("more_spell_damage", AttributeFormat.of(PERCENT), AttributeAffinities.BENEFICIAL, SignBasedKeywords.MORE)
    val FLAT_PROJECTILE_DAMAGE = CustomAttributeType("flat_projectile_damage", AttributeFormat.of(SIGNED_DOUBLE), AttributeAffinities.BENEFICIAL)
    val INCREASED_PROJECTILE_DAMAGE =
        CustomAttributeType("increased_projectile_damage", AttributeFormat.of(SIGNED_PERCENT), AttributeAffinities.BENEFICIAL, SignBasedKeywords.INCREASED)
    val MORE_PROJECTILE_DAMAGE =
        CustomAttributeType("more_projectile_damage", AttributeFormat.of(PERCENT), AttributeAffinities.BENEFICIAL, SignBasedKeywords.MORE)
    val MORE_BACKSTAB_DAMAGE =
        CustomAttributeType("more_backstab_damage", AttributeFormat.of(PERCENT), AttributeAffinities.BENEFICIAL, SignBasedKeywords.MORE)
    val CRITICAL_DAMAGE_MULTIPLIER =
        CustomAttributeType("critical_damage_multiplier", AttributeFormat.of(SIGNED_PERCENT), AttributeAffinities.BENEFICIAL)
    val MORE_ATTACK_DAMAGE_PER_ARMOR =
        CustomAttributeType("more_attack_damage_per_armor", AttributeFormat.of(PERCENT), AttributeAffinities.BENEFICIAL, SignBasedKeywords.MORE)
    val MELEE_DAMAGE_CAN_NOT_BE_DODGED = CustomAttributeType("melee_damage_can_not_be_dodged", AttributeFormat.NONE, AttributeAffinities.BENEFICIAL)

    // BOW
    val BOW_PULL_TICKS = CustomAttributeType("bow_pull_ticks", AttributeFormat.of(SIGNED_INT), AttributeAffinities.DETRIMENTAL)
    val ADDITIONAL_PROJECTILES = CustomAttributeType("additional_projectiles", AttributeFormat.of(SIGNED_INT), AttributeAffinities.BENEFICIAL)
    val PIERCE_ALL = CustomAttributeType("pierce_all", AttributeFormat.NONE, AttributeAffinities.EMPTY)

    // POISON
    val INCREASED_DAMAGE_WHILE_POISONED =
        CustomAttributeType("increased_damage_while_poisoned", AttributeFormat.of(SIGNED_PERCENT), AttributeAffinities.BENEFICIAL, SignBasedKeywords.INCREASED)
    val INCREASED_ATTACK_DAMAGE_WHILE_POISONED =
        CustomAttributeType("increased_attack_damage_while_poisoned", AttributeFormat.of(SIGNED_PERCENT), AttributeAffinities.BENEFICIAL, SignBasedKeywords.INCREASED)
    val INCREASED_SPELL_DAMAGE_WHILE_POISONED =
        CustomAttributeType("increased_spell_damage_while_poisoned", AttributeFormat.of(SIGNED_PERCENT), AttributeAffinities.BENEFICIAL, SignBasedKeywords.INCREASED)
    val MORE_DAMAGE_TAKEN_WHILE_POISONED =
        CustomAttributeType("more_damage_taken_while_poisoned", AttributeFormat.of(SIGNED_PERCENT), AttributeAffinities.DETRIMENTAL, SignBasedKeywords.MORE)
    val DODGE_WHILE_POISONED = CustomAttributeType("dodge_while_poisoned", AttributeFormat.of(PERCENT), AttributeAffinities.BENEFICIAL)
    val INCREASED_MOVEMENT_SPEED_WHILE_POISONED = CustomAttributeType(
        "increased_movement_speed_while_poisoned",
        AttributeFormat.of(SIGNED_PERCENT),
        AttributeAffinities.BENEFICIAL,
        SignBasedKeywords.INCREASED
    )
    val MAGIC_FIND_WHILE_POISONED = CustomAttributeType("magic_find_while_poisoned", AttributeFormat.of(SIGNED_INT), AttributeAffinities.BENEFICIAL)
    val POISON_DAMAGE_IMMUNITY = CustomAttributeType("poison_damage_immunity", AttributeFormat.NONE, AttributeAffinities.EMPTY)

    // WITHER
    val INCREASED_DAMAGE_WHILE_WITHERED =
        CustomAttributeType("increased_damage_while_withered", AttributeFormat.of(SIGNED_PERCENT), AttributeAffinities.BENEFICIAL, SignBasedKeywords.INCREASED)
    val INCREASED_ATTACK_DAMAGE_WHILE_WITHERED =
        CustomAttributeType("increased_attack_damage_while_withered", AttributeFormat.of(SIGNED_PERCENT), AttributeAffinities.BENEFICIAL, SignBasedKeywords.INCREASED)
    val ARMOR_WHILE_WITHERED = CustomAttributeType("armor_while_withered", AttributeFormat.of(SIGNED_DOUBLE), AttributeAffinities.BENEFICIAL)
    val WITHER_DAMAGE_IMMUNITY = CustomAttributeType("wither_damage_immunity", AttributeFormat.NONE, AttributeAffinities.EMPTY)

    // COMPANION
    val WOLF_COMPANION = CustomAttributeType("wolf_companion", AttributeFormat.of(STRING), AttributeAffinities.BENEFICIAL)
    val SPIDERLING_COMPANIONS = CustomAttributeType("spiderling_companions", AttributeFormat.of(SIGNED_INT), AttributeAffinities.BENEFICIAL)
    val COMPANION_ATTACK_DAMAGE = CustomAttributeType("companion_attack_damage", AttributeFormat.of(SIGNED_DOUBLE), AttributeAffinities.BENEFICIAL)
    val INCREASED_COMPANION_DAMAGE =
        CustomAttributeType("increased_companion_damage", AttributeFormat.of(SIGNED_PERCENT), AttributeAffinities.BENEFICIAL, SignBasedKeywords.INCREASED)
    val MORE_COMPANION_DAMAGE =
        CustomAttributeType("more_companion_damage", AttributeFormat.of(PERCENT), AttributeAffinities.BENEFICIAL, SignBasedKeywords.MORE)

    // MISC
    val INCREASED_DAMAGE_AGAINST_FULL_HEALTH =
        CustomAttributeType("increased_damage_against_full_health", AttributeFormat.of(SIGNED_PERCENT), AttributeAffinities.BENEFICIAL, SignBasedKeywords.INCREASED)
    val INCREASED_DAMAGE_WHILE_LOW_HEALTH =
        CustomAttributeType("increased_damage_while_low_health", AttributeFormat.of(SIGNED_PERCENT), AttributeAffinities.BENEFICIAL, SignBasedKeywords.INCREASED)
    val MORE_DAMAGE_TAKEN_WHILE_HIGH_HEALTH =
        CustomAttributeType("more_damage_taken_while_high_health", AttributeFormat.of(PERCENT), AttributeAffinities.DETRIMENTAL, SignBasedKeywords.MORE)

    val MORE_DAMAGE_DUAL_WIELD =
        CustomAttributeType("more_damage_dual_wield", AttributeFormat.of(SIGNED_PERCENT), AttributeAffinities.BENEFICIAL, SignBasedKeywords.MORE)
    val MORE_ATTACK_SPEED_DUAL_WIELD =
        CustomAttributeType("more_attack_speed_dual_wield", AttributeFormat.of(SIGNED_PERCENT), AttributeAffinities.BENEFICIAL, SignBasedKeywords.MORE)

    val MAGIC_FIND = CustomAttributeType("magic_find", AttributeFormat.of(SIGNED_INT), AttributeAffinities.BENEFICIAL)
    val MAGIC_FIND_PER_MAX_HEART = CustomAttributeType("magic_find_per_max_heart", AttributeFormat.of(SIGNED_INT), AttributeAffinities.BENEFICIAL)
    val INCREASED_MOVEMENT_SPEED_AFTER_DODGING = CustomAttributeType(
        "increased_movement_speed_after_dodging",
        AttributeFormat.of(SIGNED_PERCENT, INT),
        AttributeAffinities.BENEFICIAL_CONDITIONAL_BENEFICIAL,
        SignBasedKeywords.INCREASED
    )

    val MORE_ATTACK_KNOCKBACK =
        CustomAttributeType("more_attack_knockback", AttributeFormat.of(PERCENT), AttributeAffinities.BENEFICIAL, SignBasedKeywords.MORE)

    val SHOOT_WITHER_SKULL_WHEN_HIT_BY_PROJECTILE =
        CustomAttributeType("shoot_wither_skull_when_hit_by_projectile", AttributeFormat.of(SIGNED_PERCENT), AttributeAffinities.BENEFICIAL)
    val EXPLODE_WHEN_TAKING_DAMAGE = CustomAttributeType("explode_when_taking_damage", AttributeFormat.of(PERCENT), AttributeAffinities.BENEFICIAL)

    val BURNING_ENEMIES_EXPLODE_WHEN_KILLED =
        CustomAttributeType("burning_enemies_explode_when_killed", AttributeFormat.of(PERCENT), AttributeAffinities.BENEFICIAL)
    val SPELL_IGNITE = CustomAttributeType("spell_ignite", AttributeFormat.NONE, AttributeAffinities.BENEFICIAL)

    val INCREASED_HEALTH_RECOVERY =
        CustomAttributeType("increased_health_recovery", AttributeFormat.of(SIGNED_PERCENT), AttributeAffinities.BENEFICIAL, SignBasedKeywords.INCREASED)
    val MORE_HEALTH_RECOVERY =
        CustomAttributeType("more_health_recovery", AttributeFormat.of(PERCENT), AttributeAffinities.BENEFICIAL, SignBasedKeywords.MORE)

    val FURY_ON_KILL = CustomAttributeType("fury_on_kill", AttributeFormat.NONE, AttributeAffinities.BENEFICIAL)
    val RESILIENCE_ON_DAMAGE_TAKEN = CustomAttributeType("resilience_on_damage_taken", AttributeFormat.NONE, AttributeAffinities.BENEFICIAL)
    val RESILIENCE_ON_DAMAGE_TAKEN_CHANCE =
        CustomAttributeType("resilience_on_damage_taken_chance", AttributeFormat.of(PERCENT), AttributeAffinities.BENEFICIAL)

    val GHOSTLY_APPEARANCE = CustomAttributeType("ghostly_appearance", AttributeFormat.NONE, AttributeAffinities.EMPTY)

    val ENTITY_PHASING = CustomAttributeType("entity_phasing", AttributeFormat.NONE, AttributeAffinities.EMPTY)
    val BLOCK_PHASING = CustomAttributeType("block_phasing", AttributeFormat.NONE, AttributeAffinities.EMPTY)

    val LINK_NEARBY_ENEMIES = CustomAttributeType("link_nearby_enemies", AttributeFormat.of(INT), AttributeAffinities.BENEFICIAL)
    val DAMAGE_LINKED_ENEMIES = CustomAttributeType("damage_linked_enemies", AttributeFormat.of(PERCENT), AttributeAffinities.BENEFICIAL)
    val HEAL_ON_LINKED_ENEMY_KILLED = CustomAttributeType("heal_on_linked_enemy_killed", AttributeFormat.of(DOUBLE), AttributeAffinities.BENEFICIAL)

    val CHANGE_GAINED_STATUS_EFFECT = CustomAttributeType("change_gained_status_effect", AttributeFormat.of(STRING, STRING), AttributeAffinities.EMPTY)

    val MORE_DAMAGE_TAKEN_PER_MAX_HEALTH_ABOVE_TWENTY = CustomAttributeType(
        "more_damage_taken_per_max_health_above_twenty",
        AttributeFormat.of(PERCENT),
        AttributeAffinities.DETRIMENTAL,
        SignBasedKeywords.MORE
    )

    val MORE_DAMAGE_TAKEN_PER_NEARBY_ENEMY = CustomAttributeType(
        "more_damage_taken_per_nearby_enemy",
        AttributeFormat.of(PERCENT, INT),
        AttributeAffinities.DETRIMENTAL,
        SignBasedKeywords.MORE
    )

    val MORE_HORN_EFFECT_DURATION =
        CustomAttributeType("more_horn_effect_duration", AttributeFormat.of(PERCENT), AttributeAffinities.BENEFICIAL, SignBasedKeywords.MORE)
    val MORE_HORN_COOLDOWN =
        CustomAttributeType("more_horn_cooldown", AttributeFormat.of(PERCENT), AttributeAffinities.DETRIMENTAL, SignBasedKeywords.MORE)
    val STRONGER_HORNS = CustomAttributeType("stronger_horns", AttributeFormat.NONE, AttributeAffinities.BENEFICIAL)

    val RESISTANCE_WHEN_LOW_HEALTH = CustomAttributeType("resistance_when_low_health", AttributeFormat.of(INT), AttributeAffinities.BENEFICIAL)
    val REGENERATION_WHEN_HIT_BY_ENEMY = CustomAttributeType("regeneration_when_hit_by_enemy", AttributeFormat.of(INT, INT), AttributeAffinities.BENEFICIAL)

    val INCREASED_MOVEMENT_SPEED_MODIFIERS_AFFECT_DAMAGE =
        CustomAttributeType("increased_movement_speed_modifiers_affect_damage", AttributeFormat.of(PERCENT), AttributeAffinities.BENEFICIAL)
    val MORE_MOVEMENT_SPEED_MODIFIERS_AFFECT_DAMAGE =
        CustomAttributeType("more_movement_speed_modifiers_affect_damage", AttributeFormat.of(PERCENT), AttributeAffinities.BENEFICIAL)
    val INCREASED_JUMP_STRENGTH_MODIFIERS_GRANT_CRITICAL_DAMAGE_MULTIPLIER =
        CustomAttributeType("increased_jump_strength_modifiers_grant_critical_damage_multiplier", AttributeFormat.of(PERCENT), AttributeAffinities.BENEFICIAL)

    val SHIELD_DISABLED_ON_BLOCKING_HIT = CustomAttributeType("shield_disabled_on_blocking_hit", AttributeFormat.of(INT), AttributeAffinities.DETRIMENTAL)
    val INCREASED_DAMAGE_WHILE_SHIELD_DISABLED = CustomAttributeType(
        "increased_damage_while_shield_disabled",
        AttributeFormat.of(SIGNED_PERCENT),
        AttributeAffinities.BENEFICIAL,
        SignBasedKeywords.INCREASED
    )

    val INCREASED_MOVEMENT_SPEED_ON_KILL = CustomAttributeType(
        "increased_movement_speed_on_kill",
        AttributeFormat.of(SIGNED_PERCENT, INT),
        AttributeAffinities.BENEFICIAL_CONDITIONAL_BENEFICIAL,
        SignBasedKeywords.INCREASED
    )

    val HEAL_ON_KILL = CustomAttributeType("heal_on_kill", AttributeFormat.of(DOUBLE), AttributeAffinities.BENEFICIAL)
    val HEAL_ON_MELEE_HIT = CustomAttributeType("heal_on_melee_hit", AttributeFormat.of(DOUBLE), AttributeAffinities.BENEFICIAL)
    val HEAL_NEARBY_ALLIES_ON_MELEE_HIT = CustomAttributeType(
        "heal_nearby_allies_on_melee_hit",
        AttributeFormat.of(INT, DOUBLE),
        AttributeAffinities.BENEFICIAL_CONDITIONAL_BENEFICIAL
    )
    val INCREASED_HEALING_PER_SPELL_DAMAGE =
        CustomAttributeType("increased_healing_per_spell_damage", AttributeFormat.of(SIGNED_PERCENT), AttributeAffinities.BENEFICIAL, SignBasedKeywords.INCREASED)

    val INCREASED_HEALING =
        CustomAttributeType("increased_healing", AttributeFormat.of(SIGNED_PERCENT), AttributeAffinities.BENEFICIAL, SignBasedKeywords.INCREASED)
    val MORE_HEALING =
        CustomAttributeType("more_healing", AttributeFormat.of(PERCENT), AttributeAffinities.BENEFICIAL, SignBasedKeywords.MORE)
    val CHANCE_TO_HEAL_MORE = CustomAttributeType(
        "chance_to_heal_more",
        AttributeFormat.of(PERCENT, PERCENT),
        AttributeAffinities.BENEFICIAL_CONDITIONAL_BENEFICIAL,
        SignBasedKeywords.EMPTY_MORE,
    )

    val STEALTH = CustomAttributeType("stealth", AttributeFormat.NONE, AttributeAffinities.BENEFICIAL)
    val MORE_DAMAGE_AGAINST_ISOLATED =
        CustomAttributeType("more_damage_against_isolated", AttributeFormat.of(PERCENT), AttributeAffinities.BENEFICIAL, SignBasedKeywords.MORE)

    val STACKING_MORE_ATTACK_SPEED_ON_MELEE_HIT = CustomAttributeType(
        "stacking_more_attack_speed_on_melee_hit",
        AttributeFormat.of(PERCENT, INT),
        AttributeAffinities.BENEFICIAL_CONDITIONAL_BENEFICIAL,
        SignBasedKeywords.MORE,
    )

    val BURN_ENEMY_ON_WALK = CustomAttributeType(
        "burn_enemy_on_walk",
        AttributeFormat.of(DOUBLE, INT, PERCENT),
        AttributeAffinities.TRIPLE_BENEFICIAL,
    )

    // only for Seething Malice
    val MAGMA_SPIKES_SLAM_ON_USE = CustomAttributeType(
        "magma_spikes_slam_on_use",
        AttributeFormat.of(PERCENT),
        AttributeAffinities.BENEFICIAL,
        color = ChatFormatting.AQUA,
        hasDetailedKey = true
    )

    val ABSORB_STAT_FROM_SLAIN_BOSS = CustomAttributeType(
        "absorb_stat_from_slain_boss",
        AttributeFormat.NONE,
        AttributeAffinities.BENEFICIAL,
        color = ChatFormatting.LIGHT_PURPLE,
    )

    val SLOWNESS_ON_HIT = CustomAttributeType("slowness_on_hit", AttributeFormat.of(INT, INT), AttributeAffinities.BENEFICIAL_CONDITIONAL_BENEFICIAL)
    val STRENGTH_ON_KILL = CustomAttributeType("strength_on_kill", AttributeFormat.of(INT, INT), AttributeAffinities.BENEFICIAL_CONDITIONAL_BENEFICIAL)
    val SPEED_ON_KILL = CustomAttributeType("speed_on_kill", AttributeFormat.of(INT, INT), AttributeAffinities.BENEFICIAL_CONDITIONAL_BENEFICIAL)
    val HASTE_ON_KILL = CustomAttributeType("haste_on_kill", AttributeFormat.of(INT, INT), AttributeAffinities.BENEFICIAL_CONDITIONAL_BENEFICIAL)

    // beastweaver blessings
    val BLESSING_OF_THE_BEAR = CustomAttributeType("blessing_of_the_bear", AttributeFormat.NONE, AttributeAffinities.BENEFICIAL)
    val BLESSING_OF_THE_EAGLE = CustomAttributeType("blessing_of_the_eagle", AttributeFormat.NONE, AttributeAffinities.BENEFICIAL)
    val BLESSING_OF_THE_MAMMOTH = CustomAttributeType("blessing_of_the_mammoth", AttributeFormat.NONE, AttributeAffinities.BENEFICIAL)
    val BLESSING_OF_THE_RHINO = CustomAttributeType("blessing_of_the_rhino", AttributeFormat.NONE, AttributeAffinities.BENEFICIAL)
    val BLESSING_OF_THE_SERPENT = CustomAttributeType("blessing_of_the_serpent", AttributeFormat.NONE, AttributeAffinities.BENEFICIAL)
    val BLESSING_OF_THE_STAG = CustomAttributeType("blessing_of_the_stag", AttributeFormat.NONE, AttributeAffinities.BENEFICIAL)
    val BLESSING_OF_THE_WOLF = CustomAttributeType("blessing_of_the_wolf", AttributeFormat.NONE, AttributeAffinities.BENEFICIAL)

    // for use on enemies (don't use for players)
    val DROP_INCREASED_LOOT =
        CustomAttributeType("drop_increased_loot", AttributeFormat.of(SIGNED_PERCENT), AttributeAffinities.EMPTY, SignBasedKeywords.INCREASED)
    val DROP_MORE_LOOT =
        CustomAttributeType("drop_more_loot", AttributeFormat.of(PERCENT), AttributeAffinities.EMPTY, SignBasedKeywords.MORE)

    // no effect, only visual
    val ATTRIBUTES_APPLY_IN_BOTH_HANDS = CustomAttributeType("attributes_apply_in_both_hands", AttributeFormat.NONE, AttributeAffinities.BENEFICIAL)

    // region get by key
    fun getByKey(key: String): CustomAttributeType = attributeTypes[key] ?: ERROR

    private val attributeTypes = CustomAttributeTypes::class.memberProperties
        .filter { it.visibility == KVisibility.PUBLIC }
        .map { it(CustomAttributeTypes) as CustomAttributeType }
        .associateBy { it.key }
    // endregion
}