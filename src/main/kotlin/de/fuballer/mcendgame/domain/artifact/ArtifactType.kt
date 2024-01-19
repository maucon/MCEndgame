package de.fuballer.mcendgame.domain.artifact

enum class ArtifactType(
    val displayName: String,
    val displayLore: String,
    val values: Map<Int, List<Double>>
) {
    SLOW_WHEN_HIT(
        "Artifact of Hinder",
        "Slows nearby enemies with slowness %s\\for %s seconds when you're hit",
        mapOf(
            // TIER = AMPLIFIER + 1, DURATION in seconds
            0 to listOf(1.0, 2.0),
            1 to listOf(1.0, 3.5),
            2 to listOf(1.0, 5.0),
            3 to listOf(2.0, 5.0)
        )
    ),
    HEAL_ON_BLOCK(
        "Artifact of Recoup",
        "%s%% chance to heal %s health\\when you block damage with a shield\\(%s seconds cooldown)",
        mapOf(
            // TIER = CHANCE in %
            0 to listOf(50.0, 1.0, 1.5),
            1 to listOf(75.0, 1.0, 1.5),
            2 to listOf(100.0, 1.0, 1.5),
            3 to listOf(100.0, 2.0, 1.5),
        )
    ),
    WOLF_COMPANION(
        "Artifact of Company",
        "You are accompanied by %s invincible wolf/s.\\Your wolfs gains permanent strength %s",
        mapOf(
            //TIER = COUNT, STRENGTH AMPLIFIER + 1
            0 to listOf(1.0, 3.0),
            1 to listOf(2.0, 3.0),
            2 to listOf(3.0, 3.0),
            3 to listOf(4.0, 5.0),
        )
    ),
    INC_DMG_PER_MISSING_HEALTH(
        "Artifact of Berserk",
        "You deal %s%% increased damage\\for each missing heart",
        mapOf(
            //TIER = INCREASED DAMAGE in % (per 2 health)
            0 to listOf(4.0),
            1 to listOf(6.0),
            2 to listOf(8.0),
            3 to listOf(12.0),
        )
    ),
    INC_DMG_AGAINST_FULL_LIFE(
        "Artifact of Commencement",
        "You deal %s%% increased damage\\against enemies with full health",
        mapOf(
            //TIER = INCREASED DAMAGE in %
            0 to listOf(50.0),
            1 to listOf(75.0),
            2 to listOf(100.0),
            3 to listOf(150.0),
        )
    ),
    MOVEMENT_SPEED(
        "Artifact of Swiftness",
        "You have %s%% increased base movement speed",
        mapOf(
            //TIER = INCREASED BASE SPEED in %
            0 to listOf(5.0),
            1 to listOf(10.0),
            2 to listOf(15.0),
            3 to listOf(25.0),
        )
    ),
    ATTACK_SPEED(
        "Artifact of Frenzy",
        "You have %s additional attacks per second",
        mapOf(
            //TIER = ATTACK SPEED (additional attacks/second) (base: axe 0.8, sword 1.6, hoe 4.0)
            0 to listOf(0.1),
            1 to listOf(0.2),
            2 to listOf(0.3),
            3 to listOf(0.5),
        )
    ),
    ATTACK_DAMAGE(
        "Artifact of Force",
        "You deal %s additional base damage\\with melee attacks",
        mapOf(
            //TIER = BASE ATTACK DAMAGE
            0 to listOf(1.0),
            1 to listOf(2.0),
            2 to listOf(3.0),
            3 to listOf(5.0),
        )
    ),
    MAX_HEALTH(
        "Artifact of Thickness", //name chosen by xX20Erik01Xx
        "You have %s more health",
        mapOf(
            //TIER = HEALTH
            0 to listOf(1.0),
            1 to listOf(2.0),
            2 to listOf(3.0),
            3 to listOf(5.0),
        )
    ),
    ADDITIONAL_ARROWS(
        "Artifact of Barrage",
        "You shoot %s additional arrows\\dealing %s%% of the damage.\\Your arrows don't damage other players",
        mapOf(
            //TIER = ADDITIONAL ARROWS, dealing % of DAMAGE
            0 to listOf(2.0, 30.0),
            1 to listOf(2.0, 60.0),
            2 to listOf(2.0, 100.0),
            3 to listOf(4.0, 100.0),
        )
    ),
    BOW_DAMAGE(
        "Artifact of Impact",
        "Your arrows deal %s%% increased damage.",
        mapOf(
            //TIER = increased DAMAGE in %
            0 to listOf(15.0),
            1 to listOf(30.0),
            2 to listOf(45.0),
            3 to listOf(75.0),
        )
    ),
    EFFECT_STEAL(
        "Artifact of Rampage",
        "You have a %s%% chance to steal one effect\\for %s seconds when killing an enemy.\\The maximum stolen effect level is %s",
        mapOf(
            //TIER = CHANCE IN %, DURATION, MAX AMPLIFIER + 1
            0 to listOf(25.0, 30.0, 1.0),
            1 to listOf(25.0, 30.0, 2.0),
            2 to listOf(25.0, 30.0, 3.0),
            3 to listOf(25.0, 30.0, 4.0),
        )
    ),
    TAUNT(
        "Artifact of Disturbance",
        "You have a %s%% chance\\to taunt an enemy on hit",
        mapOf(
            //TIER = CHANCE IN %
            0 to listOf(25.0),
            1 to listOf(50.0),
            2 to listOf(75.0),
            3 to listOf(100.0),
        )
    ),
}
