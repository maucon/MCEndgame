package de.fuballer.mcendgame.component.dungeon.artifact

import de.fuballer.mcendgame.component.custom_entity.data.DataTypeKeys
import de.fuballer.mcendgame.component.dungeon.artifact.data.ArtifactType
import de.fuballer.mcendgame.event.PlayerDungeonJoinEvent
import de.fuballer.mcendgame.event.PlayerDungeonLeaveEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.PersistentDataUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.Color
import org.bukkit.DyeColor
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.*
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*
import kotlin.math.PI
import kotlin.math.min

@Component
class ArtifactEffectsService(
    private val artifactService: ArtifactService
) : Listener {
    private val random = Random()

    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        val entity = event.entity
        if (WorldUtil.isNotDungeonWorld(entity.world)) return
        if (entity is Player) return

        val player = entity.killer ?: return
        onEntityKilledByPlayer(event, player)
    }

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (WorldUtil.isNotDungeonWorld(event.entity.world)) return

        if (event.entity is Player) {
            onPlayerDamageByEntity(event)
        }
        if (event.damager is Player) {
            onEntityDamageByPlayer(event)
        }
        if (event.damager is Arrow && (event.damager as Arrow).shooter is Player) {
            onEntityDamageByArrow(event)
        }
    }

    @EventHandler
    fun onShootBow(event: EntityShootBowEvent) {
        val player = event.entity as? Player ?: return
        if (WorldUtil.isNotDungeonWorld(player.world)) return

        val arrowsTier = artifactService.highestArtifactLevel(player.uniqueId, ArtifactType.ADDITIONAL_ARROWS) ?: return

        val additionalArrowsAmount = ArtifactType.ADDITIONAL_ARROWS.values[arrowsTier]!![0]
        val damagePercentage = ArtifactType.ADDITIONAL_ARROWS.values[arrowsTier]!![1] / 100.0

        val arrow = event.projectile as Arrow

        val arrows = mutableListOf<Arrow>()
        for (i in 1..(additionalArrowsAmount.toInt() / 2)) {
            arrows.add(player.launchProjectile(Arrow::class.java, arrow.velocity.clone().rotateAroundY(i * 5.0 * PI / 180.0)))
            arrows.add(player.launchProjectile(Arrow::class.java, arrow.velocity.clone().rotateAroundY(-i * 5.0 * PI / 180.0)))
        }

        for (a in arrows) {
            a.damage = arrow.damage * damagePercentage
            a.isCritical = arrow.isCritical
            a.isShotFromCrossbow = arrow.isShotFromCrossbow
            a.pierceLevel = arrow.pierceLevel
            a.knockbackStrength = arrow.knockbackStrength
            a.shooter = arrow.shooter
            a.fireTicks = arrow.fireTicks
            a.isVisualFire = arrow.isVisualFire
            a.pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
        }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (WorldUtil.isDungeonWorld(event.player.world)) {
            testForMovementSpeedJoin(event.player)
            testForAttackSpeedJoin(event.player)
            testForAttackDamageJoin(event.player)
            testForMaxHealthJoin(event.player)
        } else {
            testForMovementSpeedLeave(event.player)
            testForAttackSpeedLeave(event.player)
            testForAttackDamageLeave(event.player)
            testForMaxHealthLeave(event.player)
        }
    }

    @EventHandler
    fun onEntityBreed(event: PlayerInteractEntityEvent) {
        if (WorldUtil.isNotDungeonWorld(event.rightClicked.world)) return
        if (event.rightClicked !is Animals) return
        event.isCancelled = true
    }

    @EventHandler
    fun onPlayerDungeonJoin(event: PlayerDungeonJoinEvent) {
        val player = event.player

        testForWolfsJoin(event)
        testForMovementSpeedJoin(player)
        testForAttackSpeedJoin(player)
        testForAttackDamageJoin(player)
        testForMaxHealthJoin(player)
    }

    @EventHandler
    fun onPlayerDungeonLeave(event: PlayerDungeonLeaveEvent) {
        val player = event.player

        testForWolfsLeave(player)
        testForMovementSpeedLeave(player)
        testForAttackSpeedLeave(player)
        testForAttackDamageLeave(player)
        testForMaxHealthLeave(player)
    }

    @EventHandler
    fun onEntityTarget(event: EntityTargetEvent) {
        val entity = event.entity

        if (WorldUtil.isNotDungeonWorld(event.entity.world)) return
        if (entity !is Wolf) return
        if (PersistentDataUtil.getValue(entity, DataTypeKeys.IS_ENEMY) == true) return

        if (event.target !is Player) return

        event.isCancelled = true
    }

    private fun onPlayerDamageByEntity(event: EntityDamageByEntityEvent) {
        testSlowArtifact(event)
        testShieldBlock(event)
    }

    private fun onEntityDamageByPlayer(event: EntityDamageByEntityEvent) {
        val player = event.damager as Player
        testIncDmgPerMissingHealth(event, player)
        testIncDmgAgainstFullLife(event, player)
        testTaunt(event, player)
    }

    private fun onEntityDamageByArrow(event: EntityDamageByEntityEvent) {
        val player = (event.damager as Arrow).shooter as Player
        testAdditionalArrowsFriendlyFire(event, player)
        testArrowDamage(event, player)
        testIncDmgPerMissingHealth(event, player)
        testIncDmgAgainstFullLife(event, player)
        testTaunt(event, player)
    }

    private fun onEntityKilledByPlayer(event: EntityDeathEvent, player: Player) {
        testEffectStealArtifact(event, player)
    }

    private fun testEffectStealArtifact(event: EntityDeathEvent, player: Player) {
        val tier = artifactService.highestArtifactLevel(player.uniqueId, ArtifactType.EFFECT_STEAL) ?: return

        val (chance, duration, maxAmplifier) = ArtifactType.EFFECT_STEAL.values[tier]!!
        if (random.nextDouble() > chance / 100.0) return

        val activeEffects = event.entity.activePotionEffects
        if (activeEffects.isEmpty()) return

        player.playSound(player.location, Sound.BLOCK_AMETHYST_CLUSTER_BREAK, 1f, 1f)

        val effect = activeEffects.random()

        val realMaxAmplifier = maxAmplifier.toInt() + 1
        val amplifier = min(effect.amplifier, realMaxAmplifier)
        val tickDuration = duration.toInt() * 20

        val modifiedEffect = PotionEffect(effect.type, tickDuration, amplifier, true)
        player.addPotionEffect(modifiedEffect)
    }

    private fun testSlowArtifact(event: EntityDamageByEntityEvent) {
        val tier = artifactService.highestArtifactLevel(event.entity.uniqueId, ArtifactType.SLOW_WHEN_HIT) ?: return

        val (amplifier, duration) = ArtifactType.SLOW_WHEN_HIT.values[tier]!!
        val realAmplifier = amplifier.toInt() - 1
        val realDuration = (duration * 20).toInt()

        val slowEffect = PotionEffect(PotionEffectType.SLOW, realDuration, realAmplifier, true)

        event.entity.getNearbyEntities(4.0, 4.0, 4.0)
            .filterIsInstance<Monster>()
            .forEach { it.addPotionEffect(slowEffect) }
    }

    private fun testShieldBlock(event: EntityDamageByEntityEvent) {
        if (event.getDamage(EntityDamageEvent.DamageModifier.BLOCKING) == 0.0) return

        val entity = event.entity
        val tier = artifactService.highestArtifactLevel(entity.uniqueId, ArtifactType.HEAL_ON_BLOCK) ?: return

        val (blockChance, health) = ArtifactType.HEAL_ON_BLOCK.values[tier]!!
        val realBlockChance = blockChance / 100.0
        if (random.nextDouble() > realBlockChance) return

        val loc = entity.location
        val dustOptions = DustOptions(Color.fromRGB(50, 255, 50), 1.0f)
        entity.world.spawnParticle(
            Particle.REDSTONE,
            loc.x, loc.y + 1, loc.z,
            15, 0.2, 0.2, 0.2, 0.01, dustOptions
        )

        val player = event.entity as Player
        val maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
        val newHealth = min(player.health + health, maxHealth)
        player.health = newHealth
    }

    private fun testAdditionalArrowsFriendlyFire(event: EntityDamageByEntityEvent, player: Player) {
        if (event.entity !is Player) return
        artifactService.highestArtifactLevel(player.uniqueId, ArtifactType.ADDITIONAL_ARROWS) ?: return

        event.isCancelled = true
    }

    private fun testArrowDamage(event: EntityDamageByEntityEvent, player: Player) {
        val tier = artifactService.highestArtifactLevel(player.uniqueId, ArtifactType.BOW_DAMAGE) ?: return

        val (incDmg) = ArtifactType.BOW_DAMAGE.values[tier]!!
        event.damage *= incDmg
    }

    private fun testIncDmgPerMissingHealth(event: EntityDamageByEntityEvent, player: Player) {
        val tier = artifactService.highestArtifactLevel(player.uniqueId, ArtifactType.INC_DMG_PER_MISSING_HEALTH) ?: return

        val (incDmgPerHealth) = ArtifactType.INC_DMG_PER_MISSING_HEALTH.values[tier]!!
        val realIncDmgPerHealth = incDmgPerHealth / 100

        val missingHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value - player.health
        val dmgMultiplier = 1 + realIncDmgPerHealth * (missingHealth / 2).toInt()
        val incDamage = event.damage * dmgMultiplier

        event.damage = incDamage
    }

    private fun testIncDmgAgainstFullLife(event: EntityDamageByEntityEvent, player: Player) {
        val tier = artifactService.highestArtifactLevel(player.uniqueId, ArtifactType.INC_DMG_AGAINST_FULL_LIFE) ?: return

        val entity = event.entity as? LivingEntity ?: return
        if (entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value > entity.health + 0.1) return

        val loc = entity.location
        val dustOptions = DustOptions(Color.fromRGB(255, 50, 50), 1.0f)
        entity.world.spawnParticle(
            Particle.REDSTONE,
            loc.x, loc.y + 1.3, loc.z,
            25, 0.2, 0.3, 0.2, 0.01, dustOptions
        )

        val (increasedDamage) = ArtifactType.INC_DMG_AGAINST_FULL_LIFE.values[tier]!!
        val dmgMultiplier = 1 + increasedDamage / 100

        event.damage *= dmgMultiplier
    }

    private fun testTaunt(event: EntityDamageByEntityEvent, player: Player) {
        val tier = artifactService.highestArtifactLevel(player.uniqueId, ArtifactType.TAUNT) ?: return

        val (tauntProbability) = ArtifactType.TAUNT.values[tier]!!
        if (random.nextDouble() * 100 > tauntProbability) return

        val entity = event.entity as? Monster ?: return
        entity.target = player
    }

    private fun testForMovementSpeedJoin(player: Player) {
        val tier = artifactService.highestArtifactLevel(player.uniqueId, ArtifactType.MOVEMENT_SPEED) ?: return

        val (speedMultiplier) = ArtifactType.MOVEMENT_SPEED.values[tier]!!
        val realSpeedMultiplier = 1 + speedMultiplier / 100.0

        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.1 * realSpeedMultiplier
    }

    private fun testForMovementSpeedLeave(player: Player) {
        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.1
    }

    private fun testForAttackSpeedJoin(player: Player) {
        val tier = artifactService.highestArtifactLevel(player.uniqueId, ArtifactType.ATTACK_SPEED) ?: return

        val (addedAttackSpeed) = ArtifactType.ATTACK_SPEED.values[tier]!!
        val realAttackSpeed = 4.0 + addedAttackSpeed

        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = realAttackSpeed
    }

    private fun testForAttackSpeedLeave(player: Player) {
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 4.0
    }

    private fun testForAttackDamageJoin(player: Player) {
        val tier = artifactService.highestArtifactLevel(player.uniqueId, ArtifactType.ATTACK_DAMAGE) ?: return

        val (addedAttackDamage) = ArtifactType.ATTACK_DAMAGE.values[tier]!!
        val realAttackDamage = 1.0 + addedAttackDamage

        player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = realAttackDamage
    }

    private fun testForAttackDamageLeave(player: Player) {
        player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 1.0
    }

    private fun testForMaxHealthJoin(player: Player) {
        val tier = artifactService.highestArtifactLevel(player.uniqueId, ArtifactType.MAX_HEALTH) ?: return

        val (addedHealth) = ArtifactType.MAX_HEALTH.values[tier]!!
        val realHealth = 20.0 + addedHealth

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = realHealth
        player.health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value!!
    }

    private fun testForMaxHealthLeave(player: Player) {
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = 20.0
    }

    private fun testForWolfsJoin(event: PlayerDungeonJoinEvent) {
        val player = event.player
        val tier = artifactService.highestArtifactLevel(player.uniqueId, ArtifactType.WOLF_COMPANION) ?: return

        val (count, strength) = ArtifactType.WOLF_COMPANION.values[tier]!!
        val realStrength = strength - 1

        for (i in 1..count.toInt()) {
            val targetWorld = event.locationToTeleport.world!!
            val wolf = targetWorld.spawnEntity(event.locationToTeleport, EntityType.WOLF) as Wolf
            wolf.owner = player

            val potionEffect = PotionEffect(PotionEffectType.INCREASE_DAMAGE, Int.MAX_VALUE, realStrength.toInt(), false, false)
            wolf.addPotionEffect(potionEffect)

            wolf.isInvulnerable = true
            wolf.collarColor = DyeColor.entries.toTypedArray().random()
        }
    }

    private fun testForWolfsLeave(player: Player) =
        player.world.getEntitiesByClass(Wolf::class.java)
            .filter { it.owner == player }
            .forEach { it.remove() }
}
