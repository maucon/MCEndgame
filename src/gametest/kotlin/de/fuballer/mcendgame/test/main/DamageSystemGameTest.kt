package de.fuballer.mcendgame.test.main

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.addCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.dealing.DamageDealing.dealGenericAttackDamage
import de.fuballer.mcendgame.main.component.damage.dealing.DamageDealing.dealTrueDamage
import de.fuballer.mcendgame.main.component.status_effect.CustomStatusEffects
import de.fuballer.mcendgame.main.context.PierceContext
import net.fabricmc.fabric.api.gametest.v1.GameTest
import net.minecraft.gametest.framework.GameTestHelper
import net.minecraft.world.damagesource.CombatRules
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EntityTypes
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.phys.Vec3
import org.slf4j.LoggerFactory

private val VICTIM_POS = Vec3(0.5, 2.0, 0.5)
private val SECOND_VICTIM_POS = Vec3(0.5, 2.0, 3.5)
private val LOG = LoggerFactory.getLogger(DamageSystemGameTest::class.java)

/**
 * Tests the custom damage system itself, as opposed to [VanillaDamageGameTest] which checks parity
 * with vanilla damage numbers.
 */
class DamageSystemGameTest {
    @GameTest(maxTicks = 200)
    fun trueDamageIgnoresArmorAndResistance(helper: GameTestHelper) {
        LOG.info("Testing that true damage ignores armor and resistance")
        helper.prepareNormalDifficulty()

        val victim = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, VICTIM_POS)
        prepareVictim(victim)
        victim.equipIronArmor()
        // Resistance IV -> 80% damage reduction for regular damage.
        victim.addEffect(MobEffectInstance(MobEffects.RESISTANCE, 400, 3))

        helper.startSequence()
            .thenWaitUntil { helper.assertTrue(victim.tickCount > 0, "Victim should have ticked") }
            .thenExecute {
                val attackHealthBefore = victim.health
                victim.dealGenericAttackDamage(6f, victim)
                val attackDamage = attackHealthBefore - victim.health

                val trueHealthBefore = victim.health
                victim.dealTrueDamage(5f)
                val trueDamage = trueHealthBefore - victim.health

                LOG.info("Result: attack damage was {}, true damage was {}", attackDamage, trueDamage)
                helper.assertDamageEquals(trueDamage, 5f, "True damage")
                helper.assertTrue(
                    attackDamage < 5f,
                    "Armor and resistance should reduce attack damage, but it took $attackDamage"
                )
            }
            .thenSucceed()
    }

    @GameTest(maxTicks = 200)
    fun trueDamageIgnoresInvulnerabilityFrames(helper: GameTestHelper) {
        LOG.info("Testing that true damage is applied through invulnerability frames")
        helper.prepareNormalDifficulty()

        val victim = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, VICTIM_POS)
        prepareVictim(victim)

        helper.startSequence()
            .thenWaitUntil { helper.assertTrue(victim.tickCount > 0, "Victim should have ticked") }
            .thenExecute {
                val healthBefore = victim.health
                victim.dealTrueDamage(3f)
                victim.dealTrueDamage(3f)
                val actualDamage = healthBefore - victim.health

                LOG.info("Result: took {} damage from two true damage hits", actualDamage)
                helper.assertDamageEquals(actualDamage, 6f, "True damage through invulnerability frames")
            }
            .thenSucceed()
    }

    @GameTest(maxTicks = 200)
    fun attackDamageIsReducedByArmor(helper: GameTestHelper) {
        LOG.info("Testing that armor reduces attack damage")
        helper.prepareNormalDifficulty()

        val unarmored = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, VICTIM_POS)
        prepareVictim(unarmored)
        val armored = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, SECOND_VICTIM_POS)
        prepareVictim(armored)
        armored.equipIronArmor()

        helper.startSequence()
            .thenWaitUntil {
                helper.assertTrue(unarmored.tickCount > 0 && armored.tickCount > 0, "Victims should have ticked")
            }
            .thenExecute {
                val unarmoredHealthBefore = unarmored.health
                unarmored.dealGenericAttackDamage(6f, unarmored)
                val unarmoredDamage = unarmoredHealthBefore - unarmored.health

                val armoredHealthBefore = armored.health
                armored.dealGenericAttackDamage(6f, armored)
                val armoredDamage = armoredHealthBefore - armored.health

                LOG.info("Result: unarmored took {}, armored took {}", unarmoredDamage, armoredDamage)
                helper.assertTrue(
                    armoredDamage < unarmoredDamage,
                    "Armor should reduce damage: armored took $armoredDamage, unarmored took $unarmoredDamage"
                )
            }
            .thenSucceed()
    }

    @GameTest(maxTicks = 200)
    fun flatDamageTakenIsAddedOnTopOfTheDamage(helper: GameTestHelper) {
        LOG.info("Testing that the flat damage taken attribute is added to the damage")
        helper.prepareNormalDifficulty()

        val plain = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, VICTIM_POS)
        prepareVictim(plain)
        val withFlatDamage = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, SECOND_VICTIM_POS)
        prepareVictim(withFlatDamage)
        withFlatDamage.addCustomAttribute(
            CustomAttribute(
                CustomAttributeTypes.FLAT_DAMAGE_TAKEN,
                roll = DoubleRoll(DoubleBounds(3.0))
            )
        )

        helper.startSequence()
            .thenWaitUntil {
                helper.assertTrue(plain.tickCount > 0 && withFlatDamage.tickCount > 0, "Victims should have ticked")
            }
            .thenExecute {
                val plainHealthBefore = plain.health
                plain.dealGenericAttackDamage(6f, plain)
                val plainDamage = plainHealthBefore - plain.health

                val flatHealthBefore = withFlatDamage.health
                withFlatDamage.dealGenericAttackDamage(6f, plain)
                val flatDamage = flatHealthBefore - withFlatDamage.health

                LOG.info("Result: plain took {}, with +3 flat damage taken took {}", plainDamage, flatDamage)
                helper.assertDamageEquals(flatDamage - plainDamage, 3f, "Flat damage taken bonus")
            }
            .thenSucceed()
    }

    @GameTest(maxTicks = 200)
    fun ancientBlightDealsTrueDamageScaledByAmplifier(helper: GameTestHelper) {
        LOG.info("Testing that ancient blight deals true damage scaled by its amplifier")
        helper.prepareNormalDifficulty()

        val victim = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, VICTIM_POS)
        prepareVictim(victim)
        victim.equipIronArmor()
        victim.addEffect(MobEffectInstance(MobEffects.RESISTANCE, 400, 3))

        helper.startSequence()
            .thenWaitUntil { helper.assertTrue(victim.tickCount > 0, "Victim should have ticked") }
            .thenExecute {
                // The effect only damages every 20 ticks.
                victim.tickCount = 20
                val effect = CustomStatusEffects.ANCIENT_BLIGHT.value()

                val amplifierZeroHealthBefore = victim.health
                effect.applyEffectTick(helper.level, victim, 0)
                val amplifierZeroDamage = amplifierZeroHealthBefore - victim.health

                val amplifierTwoHealthBefore = victim.health
                effect.applyEffectTick(helper.level, victim, 2)
                val amplifierTwoDamage = amplifierTwoHealthBefore - victim.health

                LOG.info(
                    "Result: ancient blight dealt {} at amplifier 0 and {} at amplifier 2",
                    amplifierZeroDamage,
                    amplifierTwoDamage
                )
                helper.assertDamageEquals(amplifierZeroDamage, 1f, "Ancient blight (amplifier 0)")
                helper.assertDamageEquals(amplifierTwoDamage, 3f, "Ancient blight (amplifier 2)")
            }
            .thenSucceed()
    }

    @GameTest(maxTicks = 200)
    fun invulnerabilityFramesAbsorbWeakerHitsAndApplyStrongerOnes(helper: GameTestHelper) {
        LOG.info("Testing that invulnerability frames only let stronger hits through")
        helper.prepareNormalDifficulty()

        val victim = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, VICTIM_POS)
        prepareVictim(victim)
        val attacker = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, SECOND_VICTIM_POS)

        helper.startSequence()
            .thenWaitUntil { helper.assertTrue(victim.tickCount > 0, "Victim should have ticked") }
            .thenExecute {
                // Zombies have innate armor, so the expected post-armor damage follows vanilla rules.
                val source = helper.level.damageSources().mobAttack(attacker)
                fun afterArmor(damage: Float) = CombatRules.getDamageAfterAbsorb(
                    victim,
                    damage,
                    source,
                    victim.getAttributeValue(Attributes.ARMOR).toFloat(),
                    victim.getAttributeValue(Attributes.ARMOR_TOUGHNESS).toFloat(),
                )

                val firstHealthBefore = victim.health
                victim.dealGenericAttackDamage(5f, attacker)
                val firstDamage = firstHealthBefore - victim.health

                val weakerHealthBefore = victim.health
                victim.dealGenericAttackDamage(3f, attacker)
                val weakerDamage = weakerHealthBefore - victim.health

                val strongerHealthBefore = victim.health
                victim.dealGenericAttackDamage(8f, attacker)
                val strongerDamage = strongerHealthBefore - victim.health

                LOG.info(
                    "Result: first hit {}, weaker hit {}, stronger hit {}",
                    firstDamage,
                    weakerDamage,
                    strongerDamage
                )
                helper.assertDamageEquals(firstDamage, afterArmor(5f), "First hit")
                helper.assertDamageEquals(weakerDamage, 0f, "Weaker hit during invulnerability frames")
                // The stronger hit only deals the difference to the previous, bigger hit.
                helper.assertDamageEquals(
                    strongerDamage,
                    afterArmor(8f) - afterArmor(5f),
                    "Stronger hit during invulnerability frames"
                )
            }
            .thenSucceed()
    }

    @GameTest(maxTicks = 200)
    fun equalDamageDuringInvulnerabilityFramesIsAbsorbed(helper: GameTestHelper) {
        LOG.info("Testing that a hit of equal strength during invulnerability frames is absorbed")
        helper.prepareNormalDifficulty()

        val victim = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, VICTIM_POS)
        prepareVictim(victim)
        val attacker = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, SECOND_VICTIM_POS)

        helper.startSequence()
            .thenWaitUntil { helper.assertTrue(victim.tickCount > 0, "Victim should have ticked") }
            .thenExecute {
                val firstHealthBefore = victim.health
                val firstApplied = victim.dealGenericAttackDamage(5f, attacker)
                val firstDamage = firstHealthBefore - victim.health

                val equalHealthBefore = victim.health
                val equalApplied = victim.dealGenericAttackDamage(5f, attacker)
                val equalDamage = equalHealthBefore - victim.health

                LOG.info(
                    "Result: first hit {} (applied {}), equal hit {} (applied {})",
                    firstDamage,
                    firstApplied,
                    equalDamage,
                    equalApplied
                )
                helper.assertTrue(firstApplied, "The first hit should be applied")
                helper.assertTrue(firstDamage > 0f, "The first hit should deal damage")
                // Only strictly stronger hits win against the previous one.
                helper.assertDamageEquals(equalDamage, 0f, "Equal hit during invulnerability frames")
                helper.assertTrue(!equalApplied, "An absorbed hit should report that no damage was applied")
            }
            .thenSucceed()
    }

    @GameTest(maxTicks = 200)
    fun invulnerabilityFramesExpireAfterTheirWindow(helper: GameTestHelper) {
        LOG.info("Testing that invulnerability frames expire and later hits are applied in full again")
        helper.prepareNormalDifficulty()

        val victim = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, VICTIM_POS)
        prepareVictim(victim)
        val attacker = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, SECOND_VICTIM_POS)

        helper.startSequence()
            .thenWaitUntil { helper.assertTrue(victim.tickCount > 0, "Victim should have ticked") }
            .thenExecute {
                victim.dealGenericAttackDamage(5f, attacker)

                val absorbedHealthBefore = victim.health
                victim.dealGenericAttackDamage(3f, attacker)
                helper.assertDamageEquals(
                    absorbedHealthBefore - victim.health,
                    0f,
                    "Weaker hit while invulnerability frames last"
                )
            }
            .thenWaitUntil {
                helper.assertTrue(
                    victim.invulnerableTime <= 10,
                    "Invulnerability frames should expire (invulnerableTime was ${victim.invulnerableTime})"
                )
            }
            .thenExecute {
                // Once the frames expired, even a weaker hit is applied in full.
                val source = helper.level.damageSources().mobAttack(attacker)
                val expectedDamage = CombatRules.getDamageAfterAbsorb(
                    victim,
                    3f,
                    source,
                    victim.getAttributeValue(Attributes.ARMOR).toFloat(),
                    victim.getAttributeValue(Attributes.ARMOR_TOUGHNESS).toFloat(),
                )

                val healthBefore = victim.health
                victim.dealGenericAttackDamage(3f, attacker)
                val actualDamage = healthBefore - victim.health

                LOG.info("Result: weaker hit after the frames expired dealt {} (expected {})", actualDamage, expectedDamage)
                helper.assertDamageEquals(actualDamage, expectedDamage, "Weaker hit after invulnerability frames expired")
            }
            .thenSucceed()
    }

    @GameTest(maxTicks = 200)
    fun stabAttackClearsPierceContext(helper: GameTestHelper) {
        LOG.info("Testing that a stab attack clears the pierce context and survives a weapon without a kinetic component")
        helper.prepareNormalDifficulty()

        val attacker = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, VICTIM_POS)
        val victim = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, SECOND_VICTIM_POS)
        prepareVictim(victim)
        attacker.setItemSlot(EquipmentSlot.MAINHAND, ItemStack(Items.IRON_SWORD))

        helper.startSequence()
            .thenWaitUntil {
                helper.assertTrue(attacker.tickCount > 0 && victim.tickCount > 0, "Entities should have ticked")
            }
            .thenExecute {
                val healthBefore = victim.health
                try {
                    PierceContext.CURRENT.set(PierceContext.PierceType.KINETIC)
                    attacker.stabAttack(EquipmentSlot.MAINHAND, victim, 4.0f, true, false, false)
                } finally {
                    PierceContext.CURRENT.remove()
                }

                helper.assertTrue(victim.health < healthBefore, "The stab should still deal damage")
                helper.assertTrue(PierceContext.CURRENT.get() == null, "The pierce context should be cleared after the stab")
            }
            .thenSucceed()
    }

    @GameTest(maxTicks = 200)
    fun damageLessStabAttackClearsPierceContext(helper: GameTestHelper) {
        LOG.info("Testing that a damage-less stab attack clears the pierce context")
        helper.prepareNormalDifficulty()

        val attacker = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, VICTIM_POS)
        val victim = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, SECOND_VICTIM_POS)
        prepareVictim(victim)
        attacker.setItemSlot(EquipmentSlot.MAINHAND, ItemStack(Items.IRON_SWORD))

        helper.startSequence()
            .thenWaitUntil {
                helper.assertTrue(attacker.tickCount > 0 && victim.tickCount > 0, "Entities should have ticked")
            }
            .thenExecute {
                try {
                    PierceContext.CURRENT.set(PierceContext.PierceType.KINETIC)
                    attacker.stabAttack(EquipmentSlot.MAINHAND, victim, 4.0f, false, false, false)
                } finally {
                    PierceContext.CURRENT.remove()
                }

                helper.assertTrue(PierceContext.CURRENT.get() == null, "The pierce context should be cleared after a damage-less stab")
            }
            .thenSucceed()
    }
}

private fun LivingEntity.equipIronArmor() {
    setItemSlot(EquipmentSlot.HEAD, ItemStack(Items.IRON_HELMET))
    setItemSlot(EquipmentSlot.CHEST, ItemStack(Items.IRON_CHESTPLATE))
    setItemSlot(EquipmentSlot.LEGS, ItemStack(Items.IRON_LEGGINGS))
    setItemSlot(EquipmentSlot.FEET, ItemStack(Items.IRON_BOOTS))
}
