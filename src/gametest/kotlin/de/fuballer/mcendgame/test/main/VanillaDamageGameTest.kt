package de.fuballer.mcendgame.test.main

import io.netty.channel.embedded.EmbeddedChannel
import net.fabricmc.fabric.api.gametest.v1.GameTest
import net.minecraft.core.registries.Registries
import net.minecraft.gametest.framework.GameTestHelper
import net.minecraft.network.Connection
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.network.protocol.game.ServerboundPlayerLoadedPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.CommonListenerCookie
import net.minecraft.tags.DamageTypeTags
import net.minecraft.world.Difficulty
import net.minecraft.world.damagesource.CombatRules
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.animal.armadillo.Armadillo
import net.minecraft.world.entity.boss.enderdragon.EnderDragon
import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart
import net.minecraft.world.entity.monster.Creeper
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.*
import net.minecraft.world.phys.Vec3
import org.slf4j.LoggerFactory
import kotlin.math.abs
import kotlin.math.sqrt

private val DEFAULT_VICTIM_POS = Vec3(0.5, 2.0, 0.5)
private val DEFAULT_PLAYER_POS = Vec3(1.5, 2.0, 0.5)
private val CREEPER_POS = Vec3(4.5, 2.0, 0.5)
private val CHARGED_CREEPER_POS = Vec3(6.5, 2.0, 0.5)
private const val DAMAGE_EPSILON = 0.01f
private const val EXPLOSION_DAMAGE_EPSILON = 0.1f
private val SMASH_TEST_HEIGHTS = listOf(2.0, 3.0, 5.0, 8.0, 12.0, 20.0)
private val DIFFICULTIES = listOf(Difficulty.EASY, Difficulty.NORMAL, Difficulty.HARD)

private val LOG = LoggerFactory.getLogger(VanillaDamageGameTest::class.java)

class VanillaDamageGameTest {
    @GameTest(maxTicks = 200)
    fun playerFistHuskDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla fist damage against a husk")
        helper.prepareNormalDifficulty()

        val husk = helper.spawnWithNoFreeWill(EntityTypes.HUSK, DEFAULT_VICTIM_POS)
        prepareVictim(husk)

        helper.assertPlayerAttackDamage(husk)
    }

    @GameTest(maxTicks = 200)
    fun playerFistSkeletonDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla fist damage against a skeleton")
        helper.prepareNormalDifficulty()

        val skeleton = helper.spawnWithNoFreeWill(EntityTypes.SKELETON, DEFAULT_VICTIM_POS)
        prepareVictim(skeleton)

        helper.assertPlayerAttackDamage(skeleton)
    }

    @GameTest(maxTicks = 200)
    fun playerFistIronArmoredZombieDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla fist damage against an iron-armored zombie")
        helper.prepareNormalDifficulty()

        val zombie = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, DEFAULT_VICTIM_POS)
        prepareVictim(zombie)
        zombie.equipFullIronArmor()

        helper.assertPlayerAttackDamage(zombie)
    }

    @GameTest(maxTicks = 200)
    fun playerIronSwordIronArmoredZombieDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla iron sword damage against an iron-armored zombie")
        helper.prepareNormalDifficulty()

        val zombie = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, DEFAULT_VICTIM_POS)
        prepareVictim(zombie)
        zombie.equipFullIronArmor()

        helper.assertPlayerAttackDamage(zombie, weapon = ItemStack(Items.IRON_SWORD))
    }

    @GameTest(maxTicks = 200)
    fun creeperExplosionNextToPlayerDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla creeper explosion damage against a nearby player")
        helper.prepareNormalDifficulty()

        val player = helper.createDamageablePlayer()
        player.isNoGravity = true
        val creeper = helper.spawnCreeper(charged = false, CREEPER_POS)

        helper.assertCreeperExplosionDamage(player, creeper)
    }

    @GameTest(maxTicks = 200)
    fun chargedCreeperExplosionNextToPlayerDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla charged creeper explosion damage against a nearby player")
        helper.prepareNormalDifficulty()

        val player = helper.createDamageablePlayer()
        player.isNoGravity = true
        val creeper = helper.spawnCreeper(charged = true, CHARGED_CREEPER_POS)

        helper.assertCreeperExplosionDamage(player, creeper)
    }

    @GameTest(maxTicks = 200)
    fun pufferfishStingingPlayerDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla pufferfish sting damage against a player")
        helper.prepareNormalDifficulty()

        val pufferfish = helper.spawnWithNoFreeWill(EntityTypes.PUFFERFISH, DEFAULT_VICTIM_POS)
        pufferfish.puffState = 1
        val player = helper.createDamageablePlayer()

        val source = helper.level.damageSources().mobAttack(pufferfish)
        val rawDamage = (pufferfish.puffState + 1).toFloat()
        val expectedDamage = expectedDamageAfterReductions(helper.level, player, rawDamage, source)

        val healthBefore = player.health
        pufferfish.playerTouch(player)
        val actualDamage = healthBefore - player.health

        LOG.info("Result: expected {} pufferfish sting damage, player took {}", expectedDamage, actualDamage)
        helper.assertDamageEquals(actualDamage, expectedDamage, "Pufferfish sting damage")
        helper.succeed()
    }

    @GameTest(maxTicks = 200)
    fun playerMaceHuskDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla mace damage against a husk")
        helper.prepareNormalDifficulty()

        val husk = helper.spawnWithNoFreeWill(EntityTypes.HUSK, DEFAULT_VICTIM_POS)
        prepareVictim(husk)

        helper.assertPlayerAttackDamage(husk, weapon = ItemStack(Items.MACE))
    }

    @GameTest(maxTicks = 200)
    fun playerMaceWithBreachHuskDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla mace with Breach damage against a husk")
        helper.prepareNormalDifficulty()

        val husk = helper.spawnWithNoFreeWill(EntityTypes.HUSK, DEFAULT_VICTIM_POS)
        prepareVictim(husk)

        helper.assertPlayerAttackDamage(husk, weapon = helper.createBreachMace())
    }

    @GameTest(maxTicks = 200)
    fun playerCriticalHitHuskDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla critical hit damage against a husk")
        helper.prepareNormalDifficulty()

        val husk = helper.spawnWithNoFreeWill(EntityTypes.HUSK, DEFAULT_VICTIM_POS)
        prepareVictim(husk)

        helper.assertPlayerAttackDamage(
            husk,
            configurePlayer = { player ->
                player.fallDistance = 2.0
                player.setOnGround(false)
            },
            rawDamage = { player, victim, source ->
                val baseDamage = player.getAttributeValue(Attributes.ATTACK_DAMAGE).toFloat()
                val itemBonus = player.weaponItem.item.getAttackDamageBonus(victim, baseDamage, source)
                (baseDamage + itemBonus) * 1.5f
            }
        )
    }

    @GameTest(maxTicks = 200)
    fun playerUnchargedAttackHuskDealsVanillaCooldownDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla uncharged attack cooldown damage against a husk")
        helper.prepareNormalDifficulty()

        val husk = helper.spawnWithNoFreeWill(EntityTypes.HUSK, DEFAULT_VICTIM_POS)
        prepareVictim(husk)

        helper.assertPlayerAttackDamage(
            husk,
            configurePlayer = { player ->
                player.getAttribute(Attributes.ATTACK_SPEED)?.baseValue = 4.0
                player.resetAttackStrengthTicker()
            }
        )
    }

    @GameTest(maxTicks = 200)
    fun playerFistProtectionIronArmoredZombieDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla fist damage against a Protection IV iron-armored zombie")
        helper.prepareNormalDifficulty()

        val zombie = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, DEFAULT_VICTIM_POS)
        prepareVictim(zombie)
        zombie.equipFullIronArmor(
            helmet = helper.createProtectionArmorStack(Items.IRON_HELMET, 4),
            chestplate = helper.createProtectionArmorStack(Items.IRON_CHESTPLATE, 4),
            leggings = helper.createProtectionArmorStack(Items.IRON_LEGGINGS, 4),
            boots = helper.createProtectionArmorStack(Items.IRON_BOOTS, 4),
        )

        helper.assertPlayerAttackDamage(zombie)
    }

    @GameTest(maxTicks = 200)
    fun playerFistResistanceZombieDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla fist damage against a Resistance II zombie")
        helper.prepareNormalDifficulty()

        val zombie = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, DEFAULT_VICTIM_POS)
        prepareVictim(zombie)
        zombie.addEffect(MobEffectInstance(MobEffects.RESISTANCE, 200, 1))

        helper.assertPlayerAttackDamage(zombie)
    }

    @GameTest(maxTicks = 200)
    fun playerDensityMaceSmashDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla density mace smash damage from different heights")
        helper.prepareNormalDifficulty()

        helper.assertDensityMaceSmashDamage()
    }

    @GameTest(maxTicks = 200)
    fun skeletonArrowHittingPlayerDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla skeleton arrow damage against a player")
        val skeleton = helper.level.createEntity(EntityTypes.SKELETON)
        val arrow = helper.level.createEntity(EntityTypes.ARROW)
        helper.assertPlayerDamageScenarios("Skeleton arrow") { level, player ->
            val source = level.damageSources().arrow(arrow, skeleton)
            player.hurtServer(level, source, 2.0f)
            PlayerDamageExpectation(source, 2.0f, "skeleton arrow")
        }
    }

    @GameTest(maxTicks = 200)
    fun ghastFireballHittingPlayerDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla ghast fireball damage against a player")
        val ghast = helper.level.createEntity(EntityTypes.GHAST)
        val fireball = helper.level.createEntity(EntityTypes.FIREBALL)
        helper.assertPlayerDamageScenarios("Ghast fireball") { level, player ->
            val source = level.damageSources().fireball(fireball, ghast)
            player.hurtServer(level, source, 6.0f)
            PlayerDamageExpectation(source, 6.0f, "ghast fireball")
        }
    }

    @GameTest(maxTicks = 200)
    fun smallFireballHittingPlayerDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla small fireball damage against a player")
        val blaze = helper.level.createEntity(EntityTypes.BLAZE)
        val fireball = helper.level.createEntity(EntityTypes.SMALL_FIREBALL)
        helper.assertPlayerDamageScenarios("Small fireball") { level, player ->
            val source = level.damageSources().fireball(fireball, blaze)
            player.hurtServer(level, source, 5.0f)
            PlayerDamageExpectation(source, 5.0f, "small fireball")
        }
    }

    @GameTest(maxTicks = 200)
    fun shulkerBulletHittingPlayerDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla shulker bullet damage against a player")
        val shulker = helper.level.createEntity(EntityTypes.SHULKER)
        val bullet = helper.level.createEntity(EntityTypes.SHULKER_BULLET)
        helper.assertPlayerDamageScenarios("Shulker bullet") { level, player ->
            val source = level.damageSources().mobProjectile(bullet, shulker)
            player.hurtServer(level, source, 4.0f)
            PlayerDamageExpectation(source, 4.0f, "shulker bullet")
        }
    }

    @GameTest(maxTicks = 200)
    fun witherSkullHittingPlayerDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla wither skull damage against a player")
        val wither = helper.level.createEntity(EntityTypes.WITHER)
        val skull = helper.level.createEntity(EntityTypes.WITHER_SKULL)
        helper.assertPlayerDamageScenarios("Wither skull") { level, player ->
            val source = level.damageSources().witherSkull(skull, wither)
            player.hurtServer(level, source, 8.0f)
            PlayerDamageExpectation(source, 8.0f, "wither skull")
        }
    }

    @GameTest(maxTicks = 200)
    fun witherSkullExplosionHittingPlayerDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla wither skull explosion damage against a player")
        val skull = helper.level.createEntity(EntityTypes.WITHER_SKULL)
        val barePlayer = helper.createDamageablePlayer()
        // A second player wearing iron armor, placed far enough from the first that the explosion
        // next to one player cannot reach the other (explosion reach is radius * 2 + 1 blocks).
        val armoredPlayer = helper.createDamageablePlayer().also { player ->
            player.equipFullIronArmor()
            player.snapTo(helper.absoluteVec(Vec3(1.5, 2.0, 5.5)))
        }

        helper.startSequence()
            .thenWaitUntil {
                helper.assertTrue(
                    barePlayer.tickCount > 0 && armoredPlayer.tickCount > 0,
                    "Players should have ticked after the setup"
                )
            }
            // Each explosion knocks the player back and leaves a 20-tick invulnerability window, so
            // run every sub-case on a freshly snapped-back player 21 ticks apart.
            .thenExecute {
                barePlayer.snapTo(helper.absoluteVec(DEFAULT_PLAYER_POS))
                helper.assertWitherSkullExplosionDamage(skull, barePlayer, Difficulty.EASY)
            }
            .thenIdle(21)
            .thenExecute {
                barePlayer.snapTo(helper.absoluteVec(DEFAULT_PLAYER_POS))
                helper.assertWitherSkullExplosionDamage(skull, barePlayer, Difficulty.NORMAL)
            }
            .thenIdle(21)
            .thenExecute {
                barePlayer.snapTo(helper.absoluteVec(DEFAULT_PLAYER_POS))
                helper.assertWitherSkullExplosionDamage(skull, barePlayer, Difficulty.HARD)
            }
            .thenIdle(21)
            .thenExecute {
                armoredPlayer.snapTo(helper.absoluteVec(Vec3(1.5, 2.0, 5.5)))
                helper.assertWitherSkullExplosionDamage(skull, armoredPlayer, Difficulty.EASY)
            }
            .thenIdle(21)
            .thenExecute {
                armoredPlayer.snapTo(helper.absoluteVec(Vec3(1.5, 2.0, 5.5)))
                helper.assertWitherSkullExplosionDamage(skull, armoredPlayer, Difficulty.NORMAL)
            }
            .thenIdle(21)
            .thenExecute {
                armoredPlayer.snapTo(helper.absoluteVec(Vec3(1.5, 2.0, 5.5)))
                helper.assertWitherSkullExplosionDamage(skull, armoredPlayer, Difficulty.HARD)
            }
            .thenExecute { helper.prepareNormalDifficulty() }
            .thenSucceed()
    }

    @GameTest(maxTicks = 200)
    fun thrownTridentHittingPlayerDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla thrown trident damage against a player")
        val shooter = helper.level.createEntity(EntityTypes.DROWNED)
        val trident = helper.level.createEntity(EntityTypes.TRIDENT)
        helper.assertPlayerDamageScenarios("Thrown trident") { level, player ->
            val source = level.damageSources().trident(trident, shooter)
            player.hurtServer(level, source, 8.0f)
            PlayerDamageExpectation(source, 8.0f, "thrown trident")
        }
    }

    @GameTest(maxTicks = 200)
    fun snowballHittingPlayerDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla snowball damage against a player (should be 0)")
        val thrower = helper.level.createEntity(EntityTypes.SNOW_GOLEM)
        val snowball = helper.level.createEntity(EntityTypes.SNOWBALL)
        helper.assertPlayerDamageScenarios("Snowball") { level, player ->
            val source = level.damageSources().thrown(snowball, thrower)
            player.hurtServer(level, source, 0.0f)
            PlayerDamageExpectation(source, 0.0f, "snowball")
        }
    }

    @GameTest(maxTicks = 200)
    fun windChargeHittingPlayerDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla wind charge damage against a player")
        val breeze = helper.level.createEntity(EntityTypes.BREEZE)
        val charge = helper.level.createEntity(EntityTypes.WIND_CHARGE)
        helper.assertPlayerDamageScenarios("Wind charge") { level, player ->
            val source = level.damageSources().windCharge(charge, breeze)
            player.hurtServer(level, source, 1.0f)
            PlayerDamageExpectation(source, 1.0f, "wind charge")
        }
    }

    @GameTest(maxTicks = 200)
    fun guardianLaserHittingPlayerDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla guardian laser damage against a player")
        val guardian = helper.level.createEntity(EntityTypes.GUARDIAN)
        helper.assertPlayerDamageScenarios("Guardian laser") { level, player ->
            val source = level.damageSources().mobAttack(guardian)
            player.hurtServer(level, source, 6.0f)
            PlayerDamageExpectation(source, 6.0f, "guardian laser")
        }
    }

    @GameTest(maxTicks = 200)
    fun guardianThornsHittingPlayerDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla guardian thorns damage against a player")
        val guardian = helper.spawnWithNoFreeWill(EntityTypes.GUARDIAN, CREEPER_POS)
        guardian.isNoGravity = true
        guardian.isMoving = false
        helper.assertPlayerDamageScenarios("Guardian thorns") { level, player ->
            val source = level.damageSources().thorns(guardian)
            player.attack(guardian)
            PlayerDamageExpectation(source, 2.0f, "guardian thorns")
        }
    }

    @GameTest(maxTicks = 200)
    fun wardenSonicBoomHittingPlayerDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla warden sonic boom damage against a player")
        val warden = helper.level.createEntity(EntityTypes.WARDEN)
        helper.assertPlayerDamageScenarios("Warden sonic boom") { level, player ->
            val source = level.damageSources().sonicBoom(warden)
            player.hurtServer(level, source, 10.0f)
            PlayerDamageExpectation(source, 10.0f, "warden sonic boom")
        }
    }

    @GameTest(maxTicks = 200)
    fun witchHarmingPotionHittingPlayerDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla harming potion damage against a player")
        helper.assertPlayerDamageScenarios("Harming potion") { level, player ->
            val source = level.damageSources().magic()
            // The harming potion applies 6 damage through the magic source. Applying it directly
            // keeps the difficulty pinned in the same tick as the damage; an addEffect would apply
            // one tick later and race with other tests changing the global difficulty.
            player.hurtServer(level, source, 6.0f)
            PlayerDamageExpectation(source, 6.0f, "witch harming potion")
        }
    }

    @GameTest(maxTicks = 200)
    fun witchGettingHitByHarmingPotionDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla harming potion damage against a witch")
        helper.prepareNormalDifficulty()

        val witch = helper.spawnWithNoFreeWill(EntityTypes.WITCH, DEFAULT_VICTIM_POS)
        prepareVictim(witch)
        val source = helper.level.damageSources().magic()
        // Witches have a built-in 85% damage reduction against magic damage (vanilla
        // Witch.getDamageAfterMagicAbsorb multiplies magic damage by 0.15).
        val expectedDamage = expectedDamageAfterReductions(helper.level, witch, 6.0f, source) * 0.15f
        val healthBefore = witch.health

        helper.startSequence()
            .thenExecute { witch.addEffect(MobEffectInstance(MobEffects.INSTANT_DAMAGE, 1, 0)) }
            // Instant damage effects apply on a later tick, not synchronously with addEffect.
            .thenWaitUntil { helper.assertTrue(witch.health < healthBefore, "Witch should have been hurt by the harming effect") }
            .thenExecute {
                val actualDamage = healthBefore - witch.health
                LOG.info("Result (witch harmed by potion): expected {} damage, witch took {}", expectedDamage, actualDamage)
                helper.assertDamageEquals(actualDamage, expectedDamage, "Witch harmed by harming potion")
            }
            .thenSucceed()
    }

    @GameTest(maxTicks = 200)
    fun playerHittingEnemyWithTridentDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla trident melee damage against an enemy")
        helper.prepareNormalDifficulty()

        val husk = helper.spawnWithNoFreeWill(EntityTypes.HUSK, DEFAULT_VICTIM_POS)
        prepareVictim(husk)

        helper.assertPlayerAttackDamage(husk, weapon = ItemStack(Items.TRIDENT))
    }

    @GameTest(maxTicks = 200)
    fun playerHittingEnemyWithImpalingTridentDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla impaling trident melee damage against an aquatic enemy")
        helper.prepareNormalDifficulty()

        val guardian = helper.spawnWithNoFreeWill(EntityTypes.GUARDIAN, DEFAULT_VICTIM_POS)
        prepareVictim(guardian)
        val trident = helper.createTridentWithImpaling(level = 2)

        helper.assertPlayerAttackDamage(
            guardian,
            weapon = trident,
            rawDamage = { player, victim, source ->
                val baseDamage = player.getAttributeValue(Attributes.ATTACK_DAMAGE).toFloat()
                // The vanilla melee pipeline applies the enchantment bonus via EnchantmentHelper.modifyDamage.
                EnchantmentHelper.modifyDamage(helper.level, trident, victim, source, baseDamage)
            }
        )
    }

    @GameTest(maxTicks = 200)
    fun playerHittingEnemyWithSpearDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla spear melee damage against an enemy")
        helper.prepareNormalDifficulty()

        val husk = helper.spawnWithNoFreeWill(EntityTypes.HUSK, DEFAULT_VICTIM_POS)
        prepareVictim(husk)

        helper.assertPlayerAttackDamage(husk, weapon = ItemStack(Items.IRON_SPEAR))
    }

    @GameTest(maxTicks = 200)
    fun playerStabbingEnemyWithSpearDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla spear stab damage against an enemy at different movement speeds")
        helper.prepareNormalDifficulty()

        val spear = ItemStack(Items.IRON_SPEAR)
        val player = helper.createDamageablePlayer()
        player.setItemSlot(EquipmentSlot.MAINHAND, spear)

        helper.startSequence()
            .thenWaitUntil { helper.assertTrue(player.tickCount > 0, "Player should have ticked before the attack") }
            .thenExecute {
                // Different stab damages representing the kinetic damage at different movement speeds.
                for (stabDamage in listOf(4.0f, 8.0f, 12.0f)) {
                    val husk = helper.spawnWithNoFreeWill(EntityTypes.HUSK, DEFAULT_VICTIM_POS)
                    prepareVictim(husk)

                    // Player.stabAttack scales the passed damage by the attack-strength factor, so
                    // charge the attack fully for the stab damage to pass through unchanged.
                    player.prepareFullAttack(spear)
                    val source = spear.getDamageSource(player)
                    val expectedDamage = expectedDamageAfterReductions(helper.level, husk, stabDamage, source)

                    val healthBefore = husk.health
                    player.stabAttack(EquipmentSlot.MAINHAND, husk, stabDamage, true, false, false)
                    val actualDamage = healthBefore - husk.health

                    LOG.info("Result (spear stab {}): expected {} damage, victim took {}", stabDamage, expectedDamage, actualDamage)
                    helper.assertDamageEquals(actualDamage, expectedDamage, "Spear stab damage ($stabDamage)")
                }
            }
            .thenSucceed()
    }

    @GameTest(maxTicks = 200)
    fun playerHittingEnderDragonDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla ender dragon part damage against the dragon")
        helper.prepareNormalDifficulty()

        // The dragons are created without being added to the level, so they don't tick, fall, or
        // despawn in the gametest. Each part is damaged once via the dragon's public hurt(...),
        // which applies the vanilla part reduction (head full, body damage/4 + min(damage, 1)).
        val headDragon = helper.level.createEntity(EntityTypes.ENDER_DRAGON)
        val bodyDragon = helper.level.createEntity(EntityTypes.ENDER_DRAGON)
        val player = helper.createDamageablePlayer()
        player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack(Items.IRON_SWORD))

        helper.startSequence()
            .thenWaitUntil { helper.assertTrue(player.tickCount > 0, "Player should have ticked before the attack") }
            .thenExecute {
                val rawDamage = 6.0f
                helper.assertDragonPartAttack(player, headDragon, headDragon.head, rawDamage, "dragon head")
                val bodyPart = bodyDragon.subEntities.first { it != bodyDragon.head }
                helper.assertDragonPartAttack(player, bodyDragon, bodyPart, rawDamage, "dragon body")
            }
            .thenSucceed()
    }

    @GameTest(maxTicks = 200)
    fun snowballHittingBlazeDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla snowball damage against a blaze")
        helper.prepareNormalDifficulty()

        val blaze = helper.spawnWithNoFreeWill(EntityTypes.BLAZE, DEFAULT_VICTIM_POS)
        prepareVictim(blaze)
        val thrower = helper.level.createEntity(EntityTypes.SNOW_GOLEM)
        val snowball = helper.level.createEntity(EntityTypes.SNOWBALL)
        val source = helper.level.damageSources().thrown(snowball, thrower)
        val expectedDamage = expectedDamageAfterReductions(helper.level, blaze, 3.0f, source)

        val healthBefore = blaze.health
        blaze.hurtServer(helper.level, source, 3.0f)
        val actualDamage = healthBefore - blaze.health

        LOG.info("Result (snowball vs blaze): expected {} damage, blaze took {}", expectedDamage, actualDamage)
        helper.assertDamageEquals(actualDamage, expectedDamage, "Snowball vs blaze")
        helper.succeed()
    }

    @GameTest(maxTicks = 200)
    fun playerHittingScaredArmadilloDealsVanillaDamage(helper: GameTestHelper) {
        LOG.info("Testing vanilla damage against a scared (rolled up) armadillo")
        helper.prepareNormalDifficulty()

        // The armadillo is kept stationary (no AI) and put into the rolled-up (scared) state
        // directly; rolling naturally would require the armadillo's brain to detect the mock player.
        val armadillo = helper.spawnWithNoFreeWill(EntityTypes.ARMADILLO, DEFAULT_VICTIM_POS)
        prepareVictim(armadillo)
        val player = helper.createDamageablePlayer()
        player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack(Items.IRON_SWORD))

        helper.startSequence()
            .thenWaitUntil {
                helper.assertTrue(player.tickCount > 0 && armadillo.tickCount > 0, "Entities should have ticked before the attack")
            }
            .thenExecute {
                armadillo.switchToState(Armadillo.ArmadilloState.SCARED)
                helper.assertTrue(armadillo.isScared, "Armadillo should be in the scared state")

                val source = player.weaponItem.getDamageSource(player)
                val rawDamage = 6.0f
                // A scared (rolled up) armadillo reduces incoming damage to (amount - 1) / 2 before
                // the normal reductions (vanilla Armadillo.hurtServer), so a 6-damage hit becomes
                // (6 - 1) / 2 = 2.5.
                val scaredReducedDamage = (rawDamage - 1.0f) / 2.0f
                val expectedDamage = expectedDamageAfterReductions(helper.level, armadillo, scaredReducedDamage, source)

                val healthBefore = armadillo.health
                armadillo.hurtServer(helper.level, source, rawDamage)
                val actualDamage = healthBefore - armadillo.health

                LOG.info("Result (scared armadillo): expected {} damage, armadillo took {}", expectedDamage, actualDamage)
                helper.assertDamageEquals(actualDamage, expectedDamage, "Scared armadillo")
            }
            .thenSucceed()
    }
}

private class PlayerDamageExpectation(
    val source: DamageSource,
    val rawDamage: Float,
    val labelSuffix: String,
)

private fun GameTestHelper.prepareNormalDifficulty() {
    level.server.worldData.difficulty = Difficulty.NORMAL
}

private fun prepareVictim(victim: LivingEntity) {
    victim.health = victim.maxHealth
    victim.absorptionAmount = 0.0f
    victim.removeAllEffects()
    victim.isNoGravity = true
}

private fun GameTestHelper.createDamageablePlayer(): ServerPlayer {
    val player = makeMockServerPlayer(GameType.SURVIVAL) as ServerPlayer
    val cookie = CommonListenerCookie.createInitial(player.gameProfile, false)
    val connection = Connection(PacketFlow.SERVERBOUND)
    EmbeddedChannel(connection)
    level.server.playerList.placeNewPlayer(connection, player, cookie)
    // The mock player has no real client, so ServerPlayer.isInvulnerableTo() reports it as
    // invulnerable while connection.hasClientLoaded() is false. Mark the connection as
    // client-loaded so the player can actually take damage in the tests.
    ServerboundPlayerLoadedPacket().handle(player.connection)

    return player.apply {
        abilities.invulnerable = false
        getAttribute(Attributes.MAX_HEALTH)?.baseValue = 1000.0
        health = maxHealth
        absorptionAmount = 0.0f
        removeAllEffects()
        snapTo(absoluteVec(DEFAULT_PLAYER_POS))
        isNoGravity = true
    }
}

private fun GameTestHelper.assertPlayerAttackDamage(
    victim: LivingEntity,
    weapon: ItemStack = ItemStack.EMPTY,
    configurePlayer: (ServerPlayer) -> Unit = {},
    rawDamage: ((ServerPlayer, LivingEntity, DamageSource) -> Float)? = null,
) {
    val player = createDamageablePlayer()
    if (!weapon.isEmpty) {
        player.setItemSlot(EquipmentSlot.MAINHAND, weapon)
    }

    // Wait until both entities have ticked once so equipment attribute modifiers are applied.
    startSequence()
        .thenWaitUntil { assertTrue(victim.tickCount > 0 && player.tickCount > 0, "Entities should have ticked before the attack") }
        .thenExecute {
            player.prepareFullAttack(weapon)
            configurePlayer(player)

            val source = player.weaponItem.getDamageSource(player)
            val rawPlayerDamage = rawDamage?.invoke(player, victim, source)
                ?: getFullAttackRawDamage(player, victim, source)
            val expectedDamage = expectedDamageAfterReductions(level, victim, rawPlayerDamage, source)

            val healthBefore = victim.health
            player.attack(victim)
            val actualDamage = healthBefore - victim.health

            LOG.info("Result: expected {} damage, victim took {} (raw {})", expectedDamage, actualDamage, rawPlayerDamage)
            assertDamageEquals(actualDamage, expectedDamage, "Player attack damage")
        }
        .thenSucceed()
}

private fun GameTestHelper.assertDensityMaceSmashDamage() {
    val mace = createDensityMace(level = 2)
    val player = createDamageablePlayer()
    player.setItemSlot(EquipmentSlot.MAINHAND, mace)

    // Wait until the player has ticked once so the mace's attack damage modifier is applied.
    startSequence()
        .thenWaitUntil { assertTrue(player.tickCount > 0, "Player should have ticked before the attack") }
        .thenExecute {
            for (height in SMASH_TEST_HEIGHTS) {
                val husk = spawnWithNoFreeWill(EntityTypes.HUSK, DEFAULT_VICTIM_POS)
                prepareVictim(husk)
                // High max health so the husk survives even the highest smash.
                husk.getAttribute(Attributes.MAX_HEALTH)?.baseValue = 500.0
                husk.health = husk.maxHealth

                player.prepareFullAttack(mace)
                player.fallDistance = height
                player.setOnGround(false)

                val source = player.weaponItem.getDamageSource(player)
                val rawPlayerDamage = getSmashAttackRawDamage(player, husk, source)
                val expectedDamage = expectedDamageAfterReductions(level, husk, rawPlayerDamage, source)

                val healthBefore = husk.health
                player.attack(husk)
                val actualDamage = healthBefore - husk.health

                LOG.info("Result (density mace smash from {} blocks): expected {} damage, victim took {} (raw {})", height, expectedDamage, actualDamage, rawPlayerDamage)
                assertDamageEquals(actualDamage, expectedDamage, "Density mace smash damage from $height blocks")
            }
        }
        .thenSucceed()
}

private fun ServerPlayer.prepareFullAttack(weapon: ItemStack) {
    if (!weapon.isEmpty) {
        setItemSlot(EquipmentSlot.MAINHAND, weapon)
    }

    getAttribute(Attributes.ATTACK_SPEED)?.baseValue = 100.0
    resetAttackStrengthTicker()
    fallDistance = 0.0
    setOnGround(true)
}

private fun getFullAttackRawDamage(
    player: ServerPlayer,
    victim: LivingEntity,
    source: DamageSource,
): Float {
    val baseDamage = player.getAttributeValue(Attributes.ATTACK_DAMAGE).toFloat()
    val cooldown = player.getAttackStrengthScale(0.5f)
    val baseDamageScale = 0.2f + cooldown * cooldown * 0.8f
    val scaledBaseDamage = baseDamage * baseDamageScale
    val itemBonus = player.weaponItem.item.getAttackDamageBonus(victim, scaledBaseDamage, source)

    return scaledBaseDamage + itemBonus
}

private fun getSmashAttackRawDamage(
    player: ServerPlayer,
    victim: LivingEntity,
    source: DamageSource,
): Float {
    val baseDamage = player.getAttributeValue(Attributes.ATTACK_DAMAGE).toFloat()
    val cooldown = player.getAttackStrengthScale(0.5f)
    val baseDamageScale = 0.2f + cooldown * cooldown * 0.8f
    val scaledBaseDamage = baseDamage * baseDamageScale
    // For a mace this already includes the smash factor and the Density bonus
    // (EnchantmentHelper.modifyFallBasedDamage) scaled by the fall distance.
    val smashBonus = player.weaponItem.item.getAttackDamageBonus(victim, scaledBaseDamage, source)
    // A falling smash attack is always a critical hit (1.5x).
    return (scaledBaseDamage + smashBonus) * 1.5f
}

private fun expectedDamageAfterReductions(
    level: ServerLevel,
    victim: LivingEntity,
    rawDamage: Float,
    source: DamageSource,
): Float {
    var damage = rawDamage

    if (!source.`is`(DamageTypeTags.BYPASSES_ARMOR)) {
        val armor = victim.getAttributeValue(Attributes.ARMOR).toFloat()
        val armorToughness = victim.getAttributeValue(Attributes.ARMOR_TOUGHNESS).toFloat()
        damage = CombatRules.getDamageAfterAbsorb(victim, damage, source, armor, armorToughness)
    }

    if (!source.`is`(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
        val protection = EnchantmentHelper.getDamageProtection(level, victim, source)
        damage = CombatRules.getDamageAfterMagicAbsorb(damage, protection)
    }

    if (!source.`is`(DamageTypeTags.BYPASSES_EFFECTS)
        && victim.hasEffect(MobEffects.RESISTANCE)
        && !source.`is`(DamageTypeTags.BYPASSES_RESISTANCE)
    ) {
        val resistanceLevel = (victim.getEffect(MobEffects.RESISTANCE)?.amplifier ?: -1) + 1
        val resistedDamage = minOf(damage * resistanceLevel * 0.2f, damage)
        damage -= resistedDamage
    }

    return damage
}

private fun GameTestHelper.assertCreeperExplosionDamage(player: ServerPlayer, creeper: Creeper) {
    val level = this.level
    val radius = if (creeper.isPowered) 6.0f else 3.0f
    val center = creeper.position()
    val source = level.damageSources().explosion(creeper, creeper)

    startSequence()
        .thenExecute {
            // The explosion damage type scales with difficulty, so pin the difficulty to NORMAL in
            // the same tick as the (synchronous) explosion and derive the expected from it.
            prepareDifficulty(Difficulty.NORMAL)
            val expectedExplosion = ServerExplosion(
                level,
                creeper,
                source,
                EntityBasedExplosionDamageCalculator(creeper),
                center,
                radius,
                false,
                Explosion.BlockInteraction.KEEP,
            )
            val seenPercent = ServerExplosion.getSeenPercent(center, player)
            val rawExplosionDamage = EntityBasedExplosionDamageCalculator(creeper).getEntityDamageAmount(expectedExplosion, player, seenPercent)
            val expectedDamage = expectedPlayerDamage(level, player, rawExplosionDamage, source, Difficulty.NORMAL)

            val healthBefore = player.health
            level.explode(creeper, center.x, center.y, center.z, radius, false, Level.ExplosionInteraction.TRIGGER)
            val actualDamage = healthBefore - player.health

            LOG.info("Result: expected {} explosion damage, player took {} (raw {})", expectedDamage, actualDamage, rawExplosionDamage)
            assertDamageEquals(actualDamage, expectedDamage, "Creeper explosion damage", EXPLOSION_DAMAGE_EPSILON)
        }
        .thenSucceed()
}

private fun GameTestHelper.spawnCreeper(charged: Boolean, position: Vec3): Creeper {
    val creeper = spawnWithNoFreeWill(EntityTypes.CREEPER, position)
    creeper.health = creeper.maxHealth
    creeper.absorptionAmount = 0.0f
    creeper.removeAllEffects()
    creeper.isNoGravity = true

    if (charged) {
        val lightningBolt = EntityTypes.LIGHTNING_BOLT.create(level, EntitySpawnReason.TRIGGERED)
            ?: error("Could not create lightning bolt")
        creeper.thunderHit(level, lightningBolt)
        creeper.clearFire()
    }

    return creeper
}

private fun GameTestHelper.createBreachMace(): ItemStack {
    val breach = level.registryAccess()
        .lookupOrThrow(Registries.ENCHANTMENT)
        .getOrThrow(Enchantments.BREACH)

    return ItemStack(Items.MACE).apply { enchant(breach, 1) }
}

private fun GameTestHelper.createDensityMace(level: Int): ItemStack {
    val density = this.level.registryAccess()
        .lookupOrThrow(Registries.ENCHANTMENT)
        .getOrThrow(Enchantments.DENSITY)

    return ItemStack(Items.MACE).apply { enchant(density, level) }
}

private fun GameTestHelper.createProtectionArmorStack(item: Item, level: Int): ItemStack {
    val protection = this.level.registryAccess()
        .lookupOrThrow(Registries.ENCHANTMENT)
        .getOrThrow(Enchantments.PROTECTION)

    return ItemStack(item).apply { enchant(protection, level) }
}

private fun LivingEntity.equipFullIronArmor(
    helmet: ItemStack = ItemStack(Items.IRON_HELMET),
    chestplate: ItemStack = ItemStack(Items.IRON_CHESTPLATE),
    leggings: ItemStack = ItemStack(Items.IRON_LEGGINGS),
    boots: ItemStack = ItemStack(Items.IRON_BOOTS),
) {
    setItemSlot(EquipmentSlot.HEAD, helmet)
    setItemSlot(EquipmentSlot.CHEST, chestplate)
    setItemSlot(EquipmentSlot.LEGS, leggings)
    setItemSlot(EquipmentSlot.FEET, boots)
}

private fun GameTestHelper.assertDamageEquals(
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

private fun GameTestHelper.prepareDifficulty(difficulty: Difficulty) {
    level.server.worldData.difficulty = difficulty
    assertTrue(level.difficulty == difficulty, "Level difficulty should be set to $difficulty")
}

// Vanilla difficulty scaling of damage a player receives (matches vanilla Player.hurtServer).
private fun scalePlayerDamage(difficulty: Difficulty, rawDamage: Float): Float {
    return when (difficulty) {
        Difficulty.EASY -> minOf(rawDamage / 2.0f + 1.0f, rawDamage)
        Difficulty.HARD -> rawDamage * 1.5f
        else -> rawDamage
    }
}

// Expected damage a player takes: vanilla difficulty scaling + vanilla reductions. The difficulty
// must be passed explicitly so the expected matches the difficulty the damage was applied at.
private fun expectedPlayerDamage(
    level: ServerLevel,
    player: LivingEntity,
    rawDamage: Float,
    source: DamageSource,
    difficulty: Difficulty = level.difficulty,
): Float {
    val scaled = if (source.scalesWithDifficulty()) scalePlayerDamage(difficulty, rawDamage) else rawDamage
    return expectedDamageAfterReductions(level, player, scaled, source)
}

private fun <T : Entity> ServerLevel.createEntity(type: EntityType<T>): T {
    return type.create(this, EntitySpawnReason.TRIGGERED) ?: error("Could not create $type")
}

// Explodes a wither-skull-sized explosion next to the given player and asserts the player took the
// vanilla-expected damage at the given difficulty (the explosion damage type scales with difficulty).
private fun GameTestHelper.assertWitherSkullExplosionDamage(
    skull: Entity,
    player: ServerPlayer,
    difficulty: Difficulty,
) {
    val level = this.level
    val radius = 1.0f
    val center = player.position().add(1.0, 0.0, 0.0)
    val seenPercent = ServerExplosion.getSeenPercent(center, player)
    val d = (1.0 - sqrt(player.distanceToSqr(center)) / (radius * 2.0)) * seenPercent
    val rawDamage = (((d * d + d) / 2.0) * 7.0 * (radius * 2.0) + 1.0).toFloat()
    val source = level.damageSources().explosion(skull, skull)

    prepareDifficulty(difficulty)
    val healthBefore = player.health
    level.explode(skull, center.x, center.y, center.z, radius, false, Level.ExplosionInteraction.TRIGGER)
    val actualDamage = healthBefore - player.health
    val expectedDamage = expectedPlayerDamage(level, player, rawDamage, source, difficulty)

    LOG.info("Result (wither skull explosion): expected {} damage, player took {} (raw {}, difficulty {})", expectedDamage, actualDamage, rawDamage, difficulty)
    assertDamageEquals(actualDamage, expectedDamage, "Wither skull explosion ($difficulty)")
}

// Creates fresh (optionally iron-armored) mock players, waits until each has ticked once after the
// setup (so armor attribute modifiers are applied), then applies the given damage for every
// difficulty and asserts that the player took exactly the vanilla-expected damage.
private fun GameTestHelper.assertPlayerDamageScenarios(
    scenarioName: String,
    applyDamage: (ServerLevel, ServerPlayer) -> PlayerDamageExpectation,
) {
    val playersByArmor = listOf(false, true).map { armored ->
        DIFFICULTIES.map {
            createDamageablePlayer().also { player ->
                if (armored) player.equipFullIronArmor()
            }
        }
    }
    val initialTicks = playersByArmor.flatten().associateWith { it.tickCount }

    startSequence()
        .thenWaitUntil {
            assertTrue(
                playersByArmor.flatten().all { it.tickCount > initialTicks.getValue(it) },
                "Players should have ticked after the setup"
            )
        }
        .thenExecute {
            for ((armorIndex, armored) in listOf(false, true).withIndex()) {
                for ((difficultyIndex, difficulty) in DIFFICULTIES.withIndex()) {
                    prepareDifficulty(difficulty)
                    val player = playersByArmor[armorIndex][difficultyIndex]

                    val healthBefore = player.health
                    val expectation = applyDamage(level, player)
                    val actualDamage = healthBefore - player.health
                    val expectedDamage = expectedPlayerDamage(level, player, expectation.rawDamage, expectation.source, difficulty)

                    LOG.info(
                        "Result ({}): expected {} damage, player took {} (raw {}, difficulty {}, armor {})",
                        expectation.labelSuffix, expectedDamage, actualDamage, expectation.rawDamage, difficulty, armored
                    )
                    assertDamageEquals(actualDamage, expectedDamage, "$scenarioName: ${expectation.labelSuffix}")
                }
            }
            // The difficulty is global server state, so restore it to NORMAL after the loop to
            // avoid leaking a leftover difficulty (e.g. HARD) into concurrently running tests.
            prepareDifficulty(Difficulty.NORMAL)
        }
        .thenSucceed()
}

// Damages the given dragon part with the given raw damage and asserts the dragon took the
// vanilla-expected damage (full damage on the head, reduced damage of damage/4 + min(damage, 1)
// on body parts). It uses the dragon's public hurt(...) method directly (the same method the game
// calls when a dragon part is hit) rather than player.attack(), whose cooldown and
// part-invulnerability checks are fragile in a gametest.
private fun GameTestHelper.assertDragonPartAttack(player: ServerPlayer, dragon: EnderDragon, part: EnderDragonPart, rawDamage: Float, label: String) {
    val source = player.weaponItem.getDamageSource(player)
    val damageAfterPartReduction = if (part == dragon.head) rawDamage else rawDamage / 4.0f + minOf(rawDamage, 1.0f)
    val expectedDamage = expectedDamageAfterReductions(level, dragon, damageAfterPartReduction, source)

    val healthBefore = dragon.health
    dragon.hurt(level, part, source, rawDamage)
    val actualDamage = healthBefore - dragon.health

    LOG.info("Result ({}): expected {} damage, dragon took {}", label, expectedDamage, actualDamage)
    assertDamageEquals(actualDamage, expectedDamage, label)
}

private fun GameTestHelper.createTridentWithImpaling(level: Int): ItemStack {
    val impaling = this.level.registryAccess()
        .lookupOrThrow(Registries.ENCHANTMENT)
        .getOrThrow(Enchantments.IMPALING)

    return ItemStack(Items.TRIDENT).apply { enchant(impaling, level) }
}