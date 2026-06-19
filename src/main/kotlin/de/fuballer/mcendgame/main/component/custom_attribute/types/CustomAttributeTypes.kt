package de.fuballer.mcendgame.main.component.custom_attribute.types

import de.fuballer.mcendgame.main.component.custom_attribute.AttributeFormats
import de.fuballer.mcendgame.main.component.custom_attribute.affinity.AttributeAffinities
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttributeType
import de.fuballer.mcendgame.main.component.custom_attribute.sign_based_keyword.SignBasedKeywords
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

object CustomAttributeTypes {
    private val ERROR = CustomAttributeType("error", AttributeFormats.EMPTY_ROLL, AttributeFormats.EMPTY_BOUNDS, AttributeAffinities.EMPTY)

    // DEFENSE
    val WARD = CustomAttributeType("ward", AttributeFormats.SIGNED_DOUBLE_ROLL, AttributeFormats.DOUBLE_BOUNDS, AttributeAffinities.BENEFICIAL)
    val DODGE = CustomAttributeType("dodge", AttributeFormats.PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL)
    val DODGE_PER_MAX_HEART_BELOW_TEN = CustomAttributeType("dodge_per_max_heart_below_ten", AttributeFormats.PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL)
    val DODGE_IF_NOT_DODGED_IN_LAST_SECONDS =
        CustomAttributeType(
            "dodge_if_not_dodged_in_last_seconds",
            AttributeFormats.PERCENT_AND_INT_ROLL,
            AttributeFormats.PERCENT_AND_INT_BOUNDS,
            AttributeAffinities.BENEFICIAL_CONDITIONAL_DETRIMENTAL
        )
    val PROJECTILE_DODGE = CustomAttributeType("projectile_dodge", AttributeFormats.PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL)
    val DODGED_PROJECTILE_REFLECT = CustomAttributeType("dodged_projectile_reflect", AttributeFormats.EMPTY_ROLL, AttributeFormats.EMPTY_BOUNDS, AttributeAffinities.BENEFICIAL)
    val MORE_DAMAGE_TAKEN = CustomAttributeType("more_damage_taken", AttributeFormats.PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.DETRIMENTAL, SignBasedKeywords.MORE)
    val GAIN_ENEMY_ARMOR_ON_KILL =
        CustomAttributeType(
            "gain_enemy_armor_on_kill",
            AttributeFormats.PERCENT_AND_INT_ROLL,
            AttributeFormats.PERCENT_AND_INT_BOUNDS,
            AttributeAffinities.BENEFICIAL_CONDITIONAL_BENEFICIAL
        )

    // OFFENSE
    val SPELL_DAMAGE = CustomAttributeType("elemental_damage", AttributeFormats.SIGNED_DOUBLE_ROLL, AttributeFormats.DOUBLE_BOUNDS, AttributeAffinities.BENEFICIAL)
    val INCREASED_DAMAGE =
        CustomAttributeType("increased_damage", AttributeFormats.SIGNED_PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL, SignBasedKeywords.INCREASED)
    val MORE_DAMAGE = CustomAttributeType("more_damage", AttributeFormats.PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL, SignBasedKeywords.MORE)
    val MORE_DAMAGE_PER_MISSING_HEART =
        CustomAttributeType("more_damage_per_missing_heart", AttributeFormats.PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL, SignBasedKeywords.MORE)
    val INCREASED_SPELL_DAMAGE =
        CustomAttributeType("increased_elemental_damage", AttributeFormats.SIGNED_PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL, SignBasedKeywords.INCREASED)
    val INCREASED_PROJECTILE_DAMAGE =
        CustomAttributeType("increased_projectile_damage", AttributeFormats.SIGNED_PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL, SignBasedKeywords.INCREASED)
    val MORE_PROJECTILE_DAMAGE =
        CustomAttributeType("more_projectile_damage", AttributeFormats.PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL, SignBasedKeywords.MORE)
    val MORE_BACKSTAB_DAMAGE =
        CustomAttributeType("more_backstab_damage", AttributeFormats.PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL, SignBasedKeywords.MORE)
    val CRITICAL_DAMAGE_MULTIPLIER =
        CustomAttributeType("critical_damage_multiplier", AttributeFormats.SIGNED_PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL)
    val MORE_ATTACK_DAMAGE_PER_ARMOR =
        CustomAttributeType("more_attack_damage_per_armor", AttributeFormats.PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL, SignBasedKeywords.MORE)
    val MELEE_DAMAGE_CAN_NOT_BE_DODGED = CustomAttributeType("melee_damage_can_not_be_dodged", AttributeFormats.EMPTY_ROLL, AttributeFormats.EMPTY_BOUNDS, AttributeAffinities.BENEFICIAL)

    // BOW
    val BOW_PULL_TICKS = CustomAttributeType("bow_pull_ticks", AttributeFormats.SIGNED_INT_ROLL, AttributeFormats.INT_BOUNDS, AttributeAffinities.DETRIMENTAL)
    val ADDITIONAL_PROJECTILES = CustomAttributeType("additional_projectiles", AttributeFormats.SIGNED_INT_ROLL, AttributeFormats.INT_BOUNDS, AttributeAffinities.BENEFICIAL)
    val PIERCE_ALL = CustomAttributeType("pierce_all", AttributeFormats.EMPTY_ROLL, AttributeFormats.EMPTY_BOUNDS, AttributeAffinities.EMPTY)

    // POISON
    val INCREASED_DAMAGE_WHILE_POISONED =
        CustomAttributeType(
            "increased_damage_while_poisoned",
            AttributeFormats.SIGNED_PERCENT_ROLL,
            AttributeFormats.PERCENT_BOUNDS,
            AttributeAffinities.BENEFICIAL,
            SignBasedKeywords.INCREASED
        )
    val INCREASED_ATTACK_DAMAGE_WHILE_POISONED = CustomAttributeType(
        "increased_attack_damage_while_poisoned", AttributeFormats.SIGNED_PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL,
        SignBasedKeywords.INCREASED
    )
    val INCREASED_SPELL_DAMAGE_WHILE_POISONED = CustomAttributeType(
        "increased_elemental_damage_while_poisoned", AttributeFormats.SIGNED_PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL,
        SignBasedKeywords.INCREASED
    )
    val MORE_DAMAGE_TAKEN_WHILE_POISONED =
        CustomAttributeType(
            "more_damage_taken_while_poisoned",
            AttributeFormats.SIGNED_PERCENT_ROLL,
            AttributeFormats.PERCENT_BOUNDS,
            AttributeAffinities.DETRIMENTAL,
            SignBasedKeywords.MORE
        )
    val DODGE_WHILE_POISONED = CustomAttributeType("dodge_while_poisoned", AttributeFormats.PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL)
    val INCREASED_MOVEMENT_SPEED_WHILE_POISONED = CustomAttributeType(
        "increased_movement_speed_while_poisoned", AttributeFormats.SIGNED_PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL,
        SignBasedKeywords.INCREASED
    )
    val MAGIC_FIND_WHILE_POISONED = CustomAttributeType("magic_find_while_poisoned", AttributeFormats.SIGNED_INT_ROLL, AttributeFormats.INT_BOUNDS, AttributeAffinities.BENEFICIAL)
    val POISON_DAMAGE_IMMUNITY = CustomAttributeType("poison_damage_immunity", AttributeFormats.EMPTY_ROLL, AttributeFormats.EMPTY_BOUNDS, AttributeAffinities.EMPTY)

    // WITHER
    val INCREASED_DAMAGE_WHILE_WITHERED =
        CustomAttributeType(
            "increased_damage_while_withered",
            AttributeFormats.SIGNED_PERCENT_ROLL,
            AttributeFormats.PERCENT_BOUNDS,
            AttributeAffinities.BENEFICIAL,
            SignBasedKeywords.INCREASED
        )
    val INCREASED_ATTACK_DAMAGE_WHILE_WITHERED = CustomAttributeType(
        "increased_attack_damage_while_withered", AttributeFormats.SIGNED_PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL,
        SignBasedKeywords.INCREASED
    )
    val ARMOR_WHILE_WITHERED = CustomAttributeType("armor_while_withered", AttributeFormats.SIGNED_DOUBLE_ROLL, AttributeFormats.DOUBLE_BOUNDS, AttributeAffinities.BENEFICIAL)
    val WITHER_DAMAGE_IMMUNITY = CustomAttributeType("wither_damage_immunity", AttributeFormats.EMPTY_ROLL, AttributeFormats.EMPTY_BOUNDS, AttributeAffinities.EMPTY)

    // COMPANION
    val WOLF_COMPANION = CustomAttributeType("wolf_companion", AttributeFormats.STRING_ROLL, AttributeFormats.STRING_SHOW_ALL_OPTIONS, AttributeAffinities.BENEFICIAL)

    val SPIDERLING_COMPANIONS = CustomAttributeType("spiderling_companions", AttributeFormats.INT_ROLL, AttributeFormats.INT_BOUNDS, AttributeAffinities.BENEFICIAL)

    val COMPANION_ATTACK_DAMAGE = CustomAttributeType("companion_attack_damage", AttributeFormats.SIGNED_DOUBLE_ROLL, AttributeFormats.DOUBLE_BOUNDS, AttributeAffinities.BENEFICIAL)
    val INCREASED_COMPANION_DAMAGE = CustomAttributeType(
        "increased_companion_damage",
        AttributeFormats.SIGNED_PERCENT_ROLL,
        AttributeFormats.PERCENT_BOUNDS,
        AttributeAffinities.BENEFICIAL,
        SignBasedKeywords.INCREASED
    )

    // MISC
    val INCREASED_DAMAGE_AGAINST_FULL_HEALTH = CustomAttributeType(
        "increased_damage_against_full_health", AttributeFormats.SIGNED_PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL,
        SignBasedKeywords.INCREASED
    )
    val INCREASED_DAMAGE_WHILE_LOW_HEALTH =
        CustomAttributeType(
            "increased_damage_while_low_health",
            AttributeFormats.SIGNED_PERCENT_ROLL,
            AttributeFormats.PERCENT_BOUNDS,
            AttributeAffinities.BENEFICIAL,
            SignBasedKeywords.INCREASED
        )
    val MORE_DAMAGE_TAKEN_WHILE_HIGH_HEALTH =
        CustomAttributeType("more_damage_taken_while_high_health", AttributeFormats.PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.DETRIMENTAL, SignBasedKeywords.MORE)

    val TWINFIRE_DUAL_WIELD_MORE_DAMAGE = CustomAttributeType(
        "twinfire_more_damage",
        AttributeFormats.SIGNED_PERCENT_ROLL,
        AttributeFormats.PERCENT_BOUNDS,
        AttributeAffinities.BENEFICIAL,
        SignBasedKeywords.MORE
    )

    val MAGIC_FIND = CustomAttributeType("magic_find", AttributeFormats.SIGNED_INT_ROLL, AttributeFormats.INT_BOUNDS, AttributeAffinities.BENEFICIAL)
    val MAGIC_FIND_PER_MAX_HEART = CustomAttributeType("magic_find_per_max_heart", AttributeFormats.SIGNED_INT_ROLL, AttributeFormats.INT_BOUNDS, AttributeAffinities.BENEFICIAL)
    val INCREASED_MOVEMENT_SPEED_AFTER_DODGING =
        CustomAttributeType(
            "increased_movement_speed_after_dodging",
            AttributeFormats.SIGNED_PERCENT_AND_INT_ROLL,
            AttributeFormats.PERCENT_AND_INT_BOUNDS,
            AttributeAffinities.BENEFICIAL_CONDITIONAL_BENEFICIAL,
            SignBasedKeywords.INCREASED
        )

    val MORE_ATTACK_KNOCKBACK =
        CustomAttributeType("more_attack_knockback", AttributeFormats.PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL, SignBasedKeywords.MORE)

    val SHOOT_WITHER_SKULL_WHEN_HIT_BY_PROJECTILE =
        CustomAttributeType("shoot_wither_skull_when_hit_by_projectile", AttributeFormats.SIGNED_PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL)
    val EXPLODE_WHEN_TAKING_DAMAGE = CustomAttributeType("explode_when_taking_damage", AttributeFormats.PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL)

    val BURNING_ENEMIES_EXPLODE_WHEN_KILLED =
        CustomAttributeType("burning_enemies_explode_when_killed", AttributeFormats.PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL)

    val INCREASED_HEALTH_RECOVERY =
        CustomAttributeType("increased_health_recovery", AttributeFormats.SIGNED_PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL, SignBasedKeywords.INCREASED)
    val MORE_HEALTH_RECOVERY =
        CustomAttributeType("more_health_recovery", AttributeFormats.PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL, SignBasedKeywords.MORE)

    val FURY_ON_KILL = CustomAttributeType("fury_on_kill", AttributeFormats.EMPTY_ROLL, AttributeFormats.EMPTY_BOUNDS, AttributeAffinities.EMPTY)
    val RESILIENCE_ON_DAMAGE_TAKEN = CustomAttributeType("resilience_on_damage_taken", AttributeFormats.EMPTY_ROLL, AttributeFormats.EMPTY_BOUNDS, AttributeAffinities.EMPTY)

    val GHOSTLY_APPEARANCE = CustomAttributeType("ghostly_appearance", AttributeFormats.EMPTY_ROLL, AttributeFormats.EMPTY_BOUNDS, AttributeAffinities.EMPTY)

    val ENTITY_PHASING = CustomAttributeType("entity_phasing", AttributeFormats.EMPTY_ROLL, AttributeFormats.EMPTY_BOUNDS, AttributeAffinities.EMPTY)
    val BLOCK_PHASING = CustomAttributeType("block_phasing", AttributeFormats.EMPTY_ROLL, AttributeFormats.EMPTY_BOUNDS, AttributeAffinities.EMPTY)

    val LINK_NEARBY_ENEMIES = CustomAttributeType("link_nearby_enemies", AttributeFormats.INT_ROLL, AttributeFormats.INT_BOUNDS, AttributeAffinities.BENEFICIAL)
    val DAMAGE_LINKED_ENEMIES = CustomAttributeType("damage_linked_enemies", AttributeFormats.PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL)
    val HEAL_ON_LINKED_ENEMY_KILLED = CustomAttributeType("heal_on_linked_enemy_killed", AttributeFormats.DOUBLE_ROLL, AttributeFormats.DOUBLE_BOUNDS, AttributeAffinities.BENEFICIAL)

    val CHANGE_GAINED_STATUS_EFFECT =
        CustomAttributeType("change_gained_status_effect", AttributeFormats.TWO_STRING_ROLL, AttributeFormats.TWO_STRING_SHOW_ALL_OPTIONS, AttributeAffinities.EMPTY)

    val MORE_DAMAGE_TAKEN_PER_MAX_HEALTH_ABOVE_TWENTY = CustomAttributeType(
        "more_damage_taken_per_max_health_above_twenty", AttributeFormats.PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS,
        AttributeAffinities.DETRIMENTAL, SignBasedKeywords.MORE
    )

    val MORE_DAMAGE_TAKEN_PER_NEARBY_ENEMY = CustomAttributeType(
        "more_damage_taken_per_nearby_enemy", AttributeFormats.PERCENT_AND_INT_ROLL, AttributeFormats.PERCENT_AND_INT_BOUNDS,
        AttributeAffinities.DETRIMENTAL, SignBasedKeywords.MORE
    )

    val MORE_HORN_EFFECT_DURATION =
        CustomAttributeType("more_horn_effect_duration", AttributeFormats.PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL, SignBasedKeywords.MORE)
    val MORE_HORN_COOLDOWN =
        CustomAttributeType("more_horn_cooldown", AttributeFormats.PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.DETRIMENTAL, SignBasedKeywords.MORE)
    val STRONGER_HORNS = CustomAttributeType("stronger_horns", AttributeFormats.EMPTY_ROLL, AttributeFormats.EMPTY_BOUNDS, AttributeAffinities.BENEFICIAL)

    val RESISTANCE_WHEN_LOW_HEALTH = CustomAttributeType("resistance_when_low_health", AttributeFormats.INT_ROLL, AttributeFormats.INT_BOUNDS, AttributeAffinities.BENEFICIAL)
    val REGENERATION_WHEN_HIT_BY_ENEMY = CustomAttributeType("regeneration_when_hit_by_enemy", AttributeFormats.TWO_INT_ROLL, AttributeFormats.TWO_INT_BOUNDS, AttributeAffinities.BENEFICIAL)

    val INCREASED_MOVEMENT_SPEED_MODIFIERS_AFFECT_DAMAGE =
        CustomAttributeType("increased_movement_speed_modifiers_affect_damage", AttributeFormats.PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL)
    val MORE_MOVEMENT_SPEED_MODIFIERS_AFFECT_DAMAGE =
        CustomAttributeType("more_movement_speed_modifiers_affect_damage", AttributeFormats.PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL)

    val SHIELD_DISABLED_ON_BLOCKING_HIT = CustomAttributeType("shield_disabled_on_blocking_hit", AttributeFormats.INT_ROLL, AttributeFormats.INT_BOUNDS, AttributeAffinities.DETRIMENTAL)
    val INCREASED_DAMAGE_WHILE_SHIELD_DISABLED = CustomAttributeType(
        "increased_damage_while_shield_disabled", AttributeFormats.SIGNED_PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS,
        AttributeAffinities.BENEFICIAL, SignBasedKeywords.INCREASED
    )

    val INCREASED_MOVEMENT_SPEED_ON_KILL = CustomAttributeType(
        "increased_movement_speed_on_kill",
        AttributeFormats.SIGNED_PERCENT_AND_INT_ROLL,
        AttributeFormats.PERCENT_AND_INT_BOUNDS,
        AttributeAffinities.BENEFICIAL_CONDITIONAL_BENEFICIAL,
        SignBasedKeywords.INCREASED
    )

    val HEAL_NEARBY_ALLIES_ON_MELEE_HIT = CustomAttributeType(
        "heal_nearby_allies_on_melee_hit",
        AttributeFormats.INT_AND_DOUBLE_ROLL,
        AttributeFormats.INT_AND_DOUBLE_BOUNDS,
        AttributeAffinities.BENEFICIAL_CONDITIONAL_BENEFICIAL
    )
    val INCREASED_HEALING_PER_SPELL_DAMAGE =
        CustomAttributeType(
            "increased_healing_per_elemental_damage",
            AttributeFormats.SIGNED_PERCENT_ROLL,
            AttributeFormats.PERCENT_BOUNDS,
            AttributeAffinities.BENEFICIAL,
            SignBasedKeywords.INCREASED
        )

    val INCREASED_HEALING =
        CustomAttributeType("increased_healing", AttributeFormats.SIGNED_PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL, SignBasedKeywords.INCREASED)
    val MORE_HEALING =
        CustomAttributeType("more_healing", AttributeFormats.PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL, SignBasedKeywords.MORE)
    val CHANCE_TO_HEAL_MORE =
        CustomAttributeType(
            "chance_to_heal_more",
            AttributeFormats.TWO_PERCENT_ROLL,
            AttributeFormats.TWO_PERCENT_BOUNDS,
            AttributeAffinities.BENEFICIAL_CONDITIONAL_BENEFICIAL,
            SignBasedKeywords.EMPTY_MORE,
        )

    val STEALTH = CustomAttributeType("stealth", AttributeFormats.EMPTY_ROLL, AttributeFormats.EMPTY_BOUNDS, AttributeAffinities.BENEFICIAL)
    val MORE_DAMAGE_AGAINST_ISOLATED =
        CustomAttributeType("more_damage_against_isolated", AttributeFormats.PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.BENEFICIAL, SignBasedKeywords.MORE)

    val STACKING_MORE_ATTACK_SPEED_ON_MELEE_HIT = CustomAttributeType(
        "stacking_more_attack_speed_on_melee_hit",
        AttributeFormats.PERCENT_AND_INT_ROLL,
        AttributeFormats.PERCENT_AND_INT_BOUNDS,
        AttributeAffinities.BENEFICIAL_CONDITIONAL_BENEFICIAL,
        SignBasedKeywords.MORE,
    )

    val BURN_ENEMY_ON_WALK =
        CustomAttributeType(
            "burn_enemy_on_walk",
            AttributeFormats.DOUBLE_AND_INT_AND_PERCENT_ROLL,
            AttributeFormats.DOUBLE_AND_INT_AND_PERCENT_BOUNDS,
            AttributeAffinities.TRIPLE_BENEFICIAL,
        )

    val SLOWNESS_ON_HIT = CustomAttributeType("slowness_on_hit", AttributeFormats.TWO_INT_ROLL, AttributeFormats.TWO_INT_BOUNDS, AttributeAffinities.BENEFICIAL_CONDITIONAL_BENEFICIAL)
    val STRENGTH_ON_KILL = CustomAttributeType("strength_on_kill", AttributeFormats.TWO_INT_ROLL, AttributeFormats.TWO_INT_BOUNDS, AttributeAffinities.BENEFICIAL_CONDITIONAL_BENEFICIAL)
    val SPEED_ON_KILL = CustomAttributeType("speed_on_kill", AttributeFormats.TWO_INT_ROLL, AttributeFormats.TWO_INT_BOUNDS, AttributeAffinities.BENEFICIAL_CONDITIONAL_BENEFICIAL)
    val HASTE_ON_KILL = CustomAttributeType("haste_on_kill", AttributeFormats.TWO_INT_ROLL, AttributeFormats.TWO_INT_BOUNDS, AttributeAffinities.BENEFICIAL_CONDITIONAL_BENEFICIAL)

    val NO_ATTACK_DAMAGE = CustomAttributeType("no_attack_damage", AttributeFormats.EMPTY_ROLL, AttributeFormats.EMPTY_BOUNDS, AttributeAffinities.DETRIMENTAL)

    // for use on enemies (don't use for players)
    val DROP_INCREASED_LOOT =
        CustomAttributeType("drop_increased_loot", AttributeFormats.SIGNED_PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.EMPTY, SignBasedKeywords.INCREASED)
    val DROP_MORE_LOOT =
        CustomAttributeType("drop_more_loot", AttributeFormats.PERCENT_ROLL, AttributeFormats.PERCENT_BOUNDS, AttributeAffinities.EMPTY, SignBasedKeywords.MORE)

    // region get by key
    fun getByKey(key: String): CustomAttributeType {
        return attributeTypes[key] ?: ERROR
    }

    private val attributeTypes = CustomAttributeTypes::class.memberProperties
        .filter { it.visibility == KVisibility.PUBLIC }
        .map { it(CustomAttributeTypes) as CustomAttributeType }
        .associateBy { it.key }
    // endregion
}