package de.fuballer.mcendgame.test.main

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.addCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.effects.knockback.AttackKnockbackUtil.takeKnockbackFrom
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.dealing.DamageDealing.dealGenericAttackDamage
import net.fabricmc.fabric.api.gametest.v1.GameTest
import net.minecraft.gametest.framework.GameTestHelper
import net.minecraft.world.entity.EntityTypes
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.phys.Vec3
import org.slf4j.LoggerFactory
import kotlin.math.abs

private val VICTIM_POS = Vec3(0.5, 2.0, 0.5)
private val SECOND_VICTIM_POS = Vec3(0.5, 2.0, 3.5)
private val ATTACKER_POS = Vec3(4.5, 2.0, 0.5)
private val SECOND_ATTACKER_POS = Vec3(6.5, 2.0, 0.5)
private const val VANILLA_KNOCKBACK_POWER = 0.4
private val LOG = LoggerFactory.getLogger(KnockbackGameTest::class.java)

/**
 * Tests that knockback goes through [de.fuballer.mcendgame.main.component.custom_attribute.effects.knockback.AttackKnockbackUtil]
 * behaves like vanilla, is not applied twice by the custom damage API and is scaled by
 * MORE_ATTACK_KNOCKBACK.
 */
class KnockbackGameTest {
    @GameTest(maxTicks = 200)
    fun takeKnockbackFromMatchesVanillaKnockback(helper: GameTestHelper) {
        LOG.info("Testing that takeKnockbackFrom behaves exactly like vanilla knockback")
        helper.prepareNormalDifficulty()

        val vanillaVictim = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, VICTIM_POS)
        prepareVictim(vanillaVictim)
        val utilVictim = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, SECOND_VICTIM_POS)
        prepareVictim(utilVictim)
        val attacker = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, ATTACKER_POS)

        helper.startSequence()
            .thenWaitUntil { helper.assertTrue(vanillaVictim.tickCount > 0, "Victims should have ticked") }
            .thenExecute {
                vanillaVictim.deltaMovement = Vec3.ZERO
                utilVictim.deltaMovement = Vec3.ZERO

                val source = helper.level.damageSources().mobAttack(attacker)
                vanillaVictim.knockback(VANILLA_KNOCKBACK_POWER, 2.0, 1.0, source, 0f)
                utilVictim.takeKnockbackFrom(attacker, VANILLA_KNOCKBACK_POWER, 2.0, 1.0)

                LOG.info("Result: vanilla {} vs util {}", vanillaVictim.deltaMovement, utilVictim.deltaMovement)
                helper.assertVectorEquals(
                    utilVictim.deltaMovement,
                    vanillaVictim.deltaMovement,
                    "takeKnockbackFrom matches vanilla knockback"
                )
            }
            .thenSucceed()
    }

    @GameTest(maxTicks = 200)
    fun knockbackResistanceReducesKnockback(helper: GameTestHelper) {
        LOG.info("Testing that knockback resistance reduces the applied knockback")
        helper.prepareNormalDifficulty()

        val normalVictim = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, VICTIM_POS)
        prepareVictim(normalVictim)
        val resistantVictim = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, SECOND_VICTIM_POS)
        prepareVictim(resistantVictim)
        resistantVictim.getAttribute(Attributes.KNOCKBACK_RESISTANCE)?.baseValue = 0.5
        val attacker = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, ATTACKER_POS)

        helper.startSequence()
            .thenWaitUntil { helper.assertTrue(normalVictim.tickCount > 0, "Victims should have ticked") }
            .thenExecute {
                normalVictim.deltaMovement = Vec3.ZERO
                resistantVictim.deltaMovement = Vec3.ZERO

                normalVictim.takeKnockbackFrom(attacker, VANILLA_KNOCKBACK_POWER, 2.0, 1.0)
                resistantVictim.takeKnockbackFrom(attacker, VANILLA_KNOCKBACK_POWER, 2.0, 1.0)

                val normalKnockback = normalVictim.deltaMovement.length()
                val resistantKnockback = resistantVictim.deltaMovement.length()
                val ratio = resistantKnockback / normalKnockback

                LOG.info("Result: knockback ratio with 50% resistance was {}", ratio)
                helper.assertTrue(
                    abs(ratio - 0.5) < 0.001,
                    "50% knockback resistance should halve the knockback, but the ratio was $ratio"
                )
            }
            .thenSucceed()
    }

    @GameTest(maxTicks = 200)
    fun customAttackDamageAppliesNoKnockback(helper: GameTestHelper) {
        LOG.info("Testing that custom attack damage does not apply knockback on its own")
        helper.prepareNormalDifficulty()

        val victim = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, VICTIM_POS)
        prepareVictim(victim)
        val attacker = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, ATTACKER_POS)

        helper.startSequence()
            .thenWaitUntil { helper.assertTrue(victim.tickCount > 0, "Victim should have ticked") }
            .thenExecute {
                victim.deltaMovement = Vec3.ZERO
                victim.dealGenericAttackDamage(6f, attacker)

                // The custom attack code applies its knockback explicitly, so the damage itself must
                // not knock the victim back (otherwise it would be applied twice).
                LOG.info("Result: victim velocity after custom attack damage was {}", victim.deltaMovement)
                helper.assertVectorEquals(
                    victim.deltaMovement,
                    Vec3.ZERO,
                    "Custom attack damage must not apply knockback"
                )
            }
            .thenSucceed()
    }

    @GameTest(maxTicks = 200)
    fun absorbedHitsDoNotApplyKnockback(helper: GameTestHelper) {
        LOG.info("Testing that a hit absorbed by invulnerability frames applies no knockback")
        helper.prepareNormalDifficulty()

        val victim = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, VICTIM_POS)
        prepareVictim(victim)
        val attacker = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, ATTACKER_POS)

        helper.startSequence()
            .thenWaitUntil { helper.assertTrue(victim.tickCount > 0, "Victim should have ticked") }
            .thenExecute {
                val source = helper.level.damageSources().mobAttack(attacker)

                victim.hurtServer(helper.level, source, 5f)
                // The first hit knocks the victim back, so reset it to isolate the absorbed hit.
                victim.deltaMovement = Vec3.ZERO

                victim.hurtServer(helper.level, source, 3f)

                LOG.info("Result: velocity after an absorbed hit was {}", victim.deltaMovement)
                helper.assertVectorEquals(
                    victim.deltaMovement,
                    Vec3.ZERO,
                    "An absorbed hit must not apply knockback"
                )
            }
            .thenSucceed()
    }

    @GameTest(maxTicks = 200)
    fun defaultDamageKnockbackIsAppliedOnce(helper: GameTestHelper) {
        LOG.info("Testing that vanilla damage applies exactly one default knockback")
        helper.prepareNormalDifficulty()

        val victim = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, VICTIM_POS)
        prepareVictim(victim)
        val attacker = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, ATTACKER_POS)

        helper.startSequence()
            .thenWaitUntil { helper.assertTrue(victim.tickCount > 0, "Victim should have ticked") }
            .thenExecute {
                victim.deltaMovement = Vec3.ZERO
                val source = helper.level.damageSources().mobAttack(attacker)
                victim.hurtServer(helper.level, source, 6f)

                // dealDefaultKnockback pushes the victim away from the damage source position with
                // a fixed power of 0.4.
                val expectedDirection = Vec3(attacker.x - victim.x, 0.0, attacker.z - victim.z).normalize()
                val expected = expectedDirection.scale(-VANILLA_KNOCKBACK_POWER)

                LOG.info("Result: default knockback was {} (expected {})", victim.deltaMovement, expected)
                helper.assertVectorEquals(victim.deltaMovement, expected, "Default damage knockback")
            }
            .thenSucceed()
    }

    @GameTest(maxTicks = 200)
    fun moreAttackKnockbackIncreasesKnockback(helper: GameTestHelper) {
        LOG.info("Testing that MORE_ATTACK_KNOCKBACK scales the knockback dealt to the victim")
        helper.prepareNormalDifficulty()

        val plainVictim = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, VICTIM_POS)
        prepareVictim(plainVictim)
        val scaledVictim = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, SECOND_VICTIM_POS)
        prepareVictim(scaledVictim)

        val plainAttacker = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, ATTACKER_POS)
        val scaledAttacker = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, SECOND_ATTACKER_POS)
        scaledAttacker.addCustomAttribute(
            CustomAttribute(
                CustomAttributeTypes.MORE_ATTACK_KNOCKBACK,
                roll = DoubleRoll(DoubleBounds(0.5))
            )
        )

        helper.startSequence()
            .thenWaitUntil { helper.assertTrue(plainVictim.tickCount > 0, "Victims should have ticked") }
            .thenExecute {
                plainVictim.deltaMovement = Vec3.ZERO
                scaledVictim.deltaMovement = Vec3.ZERO

                plainVictim.takeKnockbackFrom(plainAttacker, VANILLA_KNOCKBACK_POWER, 2.0, 1.0)
                scaledVictim.takeKnockbackFrom(scaledAttacker, VANILLA_KNOCKBACK_POWER, 2.0, 1.0)

                val ratio = scaledVictim.deltaMovement.length() / plainVictim.deltaMovement.length()

                LOG.info("Result: knockback ratio with +50% MORE_ATTACK_KNOCKBACK was {}", ratio)
                helper.assertTrue(
                    abs(ratio - 1.5) < 0.001,
                    "+50% MORE_ATTACK_KNOCKBACK should scale the knockback by 1.5, but the ratio was $ratio"
                )
            }
            .thenSucceed()
    }
}
