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
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityTypes
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.monster.Creeper
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.EntityBasedExplosionDamageCalculator
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.GameType
import net.minecraft.world.level.ServerExplosion
import net.minecraft.world.phys.Vec3
import org.slf4j.LoggerFactory
import kotlin.math.abs

private val DEFAULT_VICTIM_POS = Vec3(0.5, 2.0, 0.5)
private val DEFAULT_PLAYER_POS = Vec3(1.5, 2.0, 0.5)
private val CREEPER_POS = Vec3(4.5, 2.0, 0.5)
private val CHARGED_CREEPER_POS = Vec3(6.5, 2.0, 0.5)
private const val DAMAGE_EPSILON = 0.01f
private const val EXPLOSION_DAMAGE_EPSILON = 0.1f
private val SMASH_TEST_HEIGHTS = listOf(2.0, 3.0, 5.0, 8.0, 12.0, 20.0)

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
}

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
    val expectedDamage = expectedDamageAfterReductions(level, player, rawExplosionDamage, source)
    val healthBefore = player.health

    startSequence()
        .thenExecute { creeper.ignite() }
        .thenWaitUntil { assertTrue(!creeper.isAlive, "Creeper should have exploded") }
        .thenExecute {
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
