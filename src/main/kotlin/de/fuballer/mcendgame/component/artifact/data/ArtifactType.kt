package de.fuballer.mcendgame.component.artifact.data

import de.fuballer.mcendgame.component.artifact.ArtifactSettings

enum class ArtifactType(
    val displayName: String,
    val displayLore: List<String>,
    val values: Map<Int, List<Double>>
) {
    SLOW_WHEN_HIT(
        "Artifact of Hinder",
        listOf(ArtifactSettings.ARTIFACT_LORE_FIRST_LINE, "Slows nearby enemies with slowness (0)", "for (1) seconds when you're hit."),
        mutableMapOf(
            // TIER = AMPLIFIER + 1, DURATION in seconds
            0 to listOf(1.0, 2.0),
            1 to listOf(1.0, 3.5),
            2 to listOf(1.0, 5.0),
            3 to listOf(2.0, 5.0)
        )
    ),
    HEAL_ON_BLOCK(
        "Artifact of Recoup",
        listOf(ArtifactSettings.ARTIFACT_LORE_FIRST_LINE, "(0)% chance to heal (1) health", "when you block damage with a shield."),
        mutableMapOf(
            // TIER = CHANCE in %
            0 to listOf(50.0, 1.0),
            1 to listOf(75.0, 1.0),
            2 to listOf(100.0, 1.0),
            3 to listOf(100.0, 2.0),
        )
    ),
    WOLF_COMPANION(
        "Artifact of Company",
        listOf(ArtifactSettings.ARTIFACT_LORE_FIRST_LINE, "You are accompanied by (0) invincible wolf/s.", "Your wolfs gains permanent strength (1)."),
        mutableMapOf(
            //TIER = COUNT, STRENGTH AMPLIFIER + 1
            0 to listOf(1.0, 3.0),
            1 to listOf(2.0, 3.0),
            2 to listOf(3.0, 3.0),
            3 to listOf(4.0, 5.0),
        )
    ),
    INC_DMG_PER_MISSING_HEALTH(
        "Artifact of Berserk",
        listOf(ArtifactSettings.ARTIFACT_LORE_FIRST_LINE, "You deal (0)% increased damage", "for each missing heart."),
        mutableMapOf(
            //TIER = INCREASED DAMAGE in % (per 2 health)
            0 to listOf(4.0),
            1 to listOf(6.0),
            2 to listOf(8.0),
            3 to listOf(12.0),
        )
    ),
    INC_DMG_AGAINST_FULL_LIFE(
        "Artifact of Commencement",
        listOf(ArtifactSettings.ARTIFACT_LORE_FIRST_LINE, "You deal (0)% increased damage", "against enemies with full health."),
        mutableMapOf(
            //TIER = INCREASED DAMAGE in %
            0 to listOf(50.0),
            1 to listOf(75.0),
            2 to listOf(100.0),
            3 to listOf(150.0),
        )
    ),
    MOVEMENT_SPEED(
        "Artifact of Swiftness",
        listOf(ArtifactSettings.ARTIFACT_LORE_FIRST_LINE, "You have (0)% increased base movement speed."),
        mutableMapOf(
            //TIER = INCREASED BASE SPEED in %
            0 to listOf(5.0),
            1 to listOf(10.0),
            2 to listOf(15.0),
            3 to listOf(25.0),
        )
    ),
    ATTACK_SPEED(
        "Artifact of Frenzy",
        listOf(ArtifactSettings.ARTIFACT_LORE_FIRST_LINE, "You have (0) additional attacks per second."),
        mutableMapOf(
            //TIER = ATTACK SPEED (additional attacks/second) (base: axe 0.8, sword 1.6, hoe 4.0)
            0 to listOf(0.1),
            1 to listOf(0.2),
            2 to listOf(0.3),
            3 to listOf(0.5),
        )
    ),
    ATTACK_DAMAGE(
        "Artifact of Force",
        listOf(ArtifactSettings.ARTIFACT_LORE_FIRST_LINE, "You deal (0) additional base damage", "with melee attacks."),
        mutableMapOf(
            //TIER = BASE ATTACK DAMAGE
            0 to listOf(1.0),
            1 to listOf(2.0),
            2 to listOf(3.0),
            3 to listOf(5.0),
        )
    ),
    MAX_HEALTH(
        "Artifact of Thickness", //name chosen by xX20Erik01Xx
        listOf(ArtifactSettings.ARTIFACT_LORE_FIRST_LINE, "You have (0) more health."),
        mutableMapOf(
            //TIER = HEALTH
            0 to listOf(2.0),
            1 to listOf(4.0),
            2 to listOf(6.0),
            3 to listOf(10.0),
        )
    ),
    ADDITIONAL_ARROWS(
        "Artifact of Barrage",
        listOf(
            ArtifactSettings.ARTIFACT_LORE_FIRST_LINE,
            "You shoot (0) additional arrows",
            "dealing (1)% of the damage.",
            "Your arrows dont damage other players."
        ),
        mutableMapOf(
            //TIER = ADDITIONAL ARROWS, dealing % of DAMAGE
            0 to listOf(2.0, 30.0),
            1 to listOf(2.0, 60.0),
            2 to listOf(2.0, 100.0),
            3 to listOf(4.0, 100.0),
        )
    ),
    BOW_DAMAGE(
        "Artifact of Impact",
        listOf(
            ArtifactSettings.ARTIFACT_LORE_FIRST_LINE,
            "Your arrows deal (0)% increased damage."
        ),
        mutableMapOf(
            //TIER = increased DAMAGE in %
            0 to listOf(15.0),
            1 to listOf(30.0),
            2 to listOf(45.0),
            3 to listOf(75.0),
        )
    ),
    EFFECT_STEAL(
        "Artifact of Rampage",
        listOf(
            ArtifactSettings.ARTIFACT_LORE_FIRST_LINE,
            "You have a (0)% chance to steal 1 effect",
            "for (1) seconds when killing an enemy.",
            "The maximum stolen effect level is (2)."
        ),
        mutableMapOf(
            //TIER = CHANCE IN %, DURATION, MAX AMPLIFIER + 1
            0 to listOf(25.0, 30.0, 1.0),
            1 to listOf(25.0, 30.0, 2.0),
            2 to listOf(25.0, 30.0, 3.0),
            3 to listOf(25.0, 30.0, 4.0),
        )
    ),
    TAUNT(
        "Artifact of Disturbance",
        listOf(
            ArtifactSettings.ARTIFACT_LORE_FIRST_LINE,
            "You have a (0)% chance",
            "to taunt an enemy on hit."
        ),
        mutableMapOf(
            //TIER = CHANCE IN %
            0 to listOf(25.0),
            1 to listOf(50.0),
            2 to listOf(75.0),
            3 to listOf(100.0),
        )
    )
}
