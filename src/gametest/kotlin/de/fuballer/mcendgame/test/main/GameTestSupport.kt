package de.fuballer.mcendgame.test.main

import net.minecraft.gametest.framework.GameTestHelper
import net.minecraft.world.Difficulty
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3
import kotlin.math.abs

internal const val DAMAGE_EPSILON = 0.01f
internal const val VECTOR_EPSILON = 0.001

internal fun GameTestHelper.prepareNormalDifficulty() {
    level.server.worldData.difficulty = Difficulty.NORMAL
}

internal fun prepareVictim(victim: LivingEntity) {
    victim.health = victim.maxHealth
    victim.absorptionAmount = 0.0f
    victim.removeAllEffects()
    victim.isNoGravity = true
}

internal fun GameTestHelper.assertDamageEquals(
    actualDamage: Float,
    expectedDamage: Float,
    label: String,
    epsilon: Float = DAMAGE_EPSILON,
) {
    assertTrue(
        abs(actualDamage - expectedDamage) <= epsilon,
        "$label: expected $expectedDamage but was $actualDamage"
    )
}

internal fun GameTestHelper.assertVectorEquals(
    actual: Vec3,
    expected: Vec3,
    label: String,
    epsilon: Double = VECTOR_EPSILON,
) {
    assertTrue(
        abs(actual.x - expected.x) <= epsilon
                && abs(actual.y - expected.y) <= epsilon
                && abs(actual.z - expected.z) <= epsilon,
        "$label: expected $expected but was $actual"
    )
}
