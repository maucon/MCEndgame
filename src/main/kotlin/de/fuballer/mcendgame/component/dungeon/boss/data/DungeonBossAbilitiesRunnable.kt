package de.fuballer.mcendgame.component.dungeon.boss.data

import de.fuballer.mcendgame.component.dungeon.boss.DungeonBossSettings
import de.fuballer.mcendgame.component.dungeon.boss.db.DungeonBossRepository
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.data.CustomEntityType
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.data.DataTypeKeys
import de.fuballer.mcendgame.util.DungeonUtil
import de.fuballer.mcendgame.util.PersistentDataUtil
import de.fuballer.mcendgame.util.PluginUtil.runTaskLater
import de.fuballer.mcendgame.util.random.RandomUtil
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.entity.*
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import kotlin.math.pow
import kotlin.math.sqrt

class DungeonBossAbilitiesRunnable(
    private val dungeonBossRepo: DungeonBossRepository,
    private val boss: Creature,
    private val bossType: BossType,
    private val level: Int
) : BukkitRunnable() {
    private var ticksSinceAbility = 0
    private val abilityCooldown = DungeonBossSettings.getBossAbilityCooldown(level)
    private var noTargetCount = 0

    override fun run() {
        if (!dungeonBossRepo.exists(boss.uniqueId)) {
            this.cancel()
        }

        ticksSinceAbility += DungeonBossSettings.BOSS_ABILITY_CHECK_PERIOD
        if (ticksSinceAbility >= abilityCooldown) {
            ticksSinceAbility = 0

            noTargetCount = if (useAbility()) 0 else noTargetCount + 1
            if (noTargetCount * abilityCooldown >= 400)
                this.cancel()
        }
    }

    private fun useAbility(): Boolean {
        val target = boss.target ?: return false

        when (RandomUtil.pick(bossType.abilities).option) {
            BossAbility.ARROWS -> shootArrows(target, false)
            BossAbility.FIRE_ARROWS -> shootArrows(target, true)
            BossAbility.SPEED -> giveSpeed()
            BossAbility.FIRE_CASCADE -> castFireCascade(target)
            BossAbility.DARKNESS -> applyDarkness()
            BossAbility.LEAP -> leap(target)
            BossAbility.GRAVITATION_PILLAR -> summonGravitationPillar()
            BossAbility.POISON_CLOUD -> createPoisonClouds()
        }

        return true
    }

    private fun shootArrows(target: LivingEntity, burning: Boolean) {
        for (i in 1..DungeonBossSettings.ARROWS_COUNT)
            ShootArrowRunnable(target, boss, burning).runTaskLater(i * DungeonBossSettings.ARROWS_TIME_DIFFERENCE)
    }

    private class ShootArrowRunnable(
        private val target: LivingEntity,
        private val boss: Creature,
        private val burning: Boolean
    ) : BukkitRunnable() {
        override fun run() {
            val bL = boss.eyeLocation
            val tL = target.eyeLocation
            val arrow = boss.world.spawnArrow(bL, Vector(tL.x - bL.x, tL.y - bL.y, tL.z - bL.z), 2F, 4F)
            arrow.fireTicks = if (burning) 100 else 0
            arrow.knockbackStrength = 2
            arrow.shooter = boss
            this.cancel()
        }
    }

    private fun giveSpeed() {
        boss.addPotionEffect(DungeonBossSettings.SPEED_EFFECT)
    }

    private fun castFireCascade(target: LivingEntity) {
        val vector = target.location.subtract(boss.location).toVector()
        val amount = vector.length().toInt() / DungeonBossSettings.FIRE_CASCADE_DISTANCE + DungeonBossSettings.FIRE_CASCADE_STEPS_AFTER_PLAYER
        val addVector = vector.normalize().multiply(DungeonBossSettings.FIRE_CASCADE_DISTANCE)
        val offsetVector = addVector.clone()

        var i = 1
        while (i < amount) {
            val stepDelay = (i * DungeonBossSettings.FIRE_CASCADE_STEP_DELAY).toLong()
            val sound = (i - 1) % DungeonBossSettings.FIRE_CASCADE_STEPS_PER_SOUND == 0

            CastFireCascadeRunnable(boss, 0.0, boss.location.add(offsetVector), true, sound)
                .runTaskLater(stepDelay)

            CastFireCascadeRunnable(boss, DungeonBossSettings.FIRE_CASCADE_DAMAGE + level * DungeonBossSettings.FIRE_CASCADE_DAMAGE_PER_LEVEL, boss.location.add(offsetVector), false, sound)
                .runTaskLater(stepDelay + DungeonBossSettings.FIRE_CASCADE_ACTIVATION_DELAY)

            offsetVector.add(addVector)
            i++
        }
    }

    private class CastFireCascadeRunnable(
        private val boss: Creature,
        private val damage: Double,
        private val location: Location,
        private val indicator: Boolean,
        private val sound: Boolean
    ) : BukkitRunnable() {
        override fun run() {
            val world = location.world ?: return

            world.spawnParticle(
                Particle.FLAME,
                location.x, location.y + (if (indicator) 0 else 1), location.z,
                25 + (if (indicator) 0 else 1), 0.2, 0.1 + (if (indicator) 0 else 2), 0.2, 0.0001
            )
            if (indicator) {
                world.spawnParticle(
                    Particle.FLAME,
                    location.x, location.y, location.z,
                    15, 0.2, 0.1, 0.2, 0.0001
                )
                if (sound)
                    world.playSound(location, Sound.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 0.4f, 0.5f)
            } else {
                world.spawnParticle(
                    Particle.FLAME,
                    location.x, location.y + 1, location.z,
                    50, 0.2, 0.8, 0.2, 0.0001
                )
                if (sound)
                    world.playSound(location, Sound.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 0.7f, 1f)
                dealDamage()
            }
            this.cancel()
        }

        private fun dealDamage() {
            val world = location.world ?: return
            for (player in world.players) {
                val pLoc = player.location
                if (sqrt((location.x - pLoc.x).pow(2) + (location.z - pLoc.z).pow(2)) < 1) {
                    player.damage(damage, boss)
                    player.fireTicks = DungeonBossSettings.FIRE_CASCADE_FIRE_TICKS
                }
            }
        }
    }

    private fun applyDarkness() {
        val bLoc = boss.location
        for (player in boss.world.players) {
            val pLoc = player.location
            if (sqrt((bLoc.x - pLoc.x).pow(2) + (bLoc.z - pLoc.z).pow(2)) < DungeonBossSettings.DARKNESS_EFFECT_RADIUS) {
                player.addPotionEffect(DungeonBossSettings.DARKNESS_EFFECT)
            }
        }
    }

    private fun leap(target: LivingEntity) {
        val vec = target.location.subtract(boss.location).multiply(0.25)
        boss.velocity = Vector(vec.x, vec.y + vec.length() / 4, vec.z)
    }

    private fun summonGravitationPillar() {
        val pillar = boss.world.spawnEntity(boss.location, CustomEntityType.STONE_PILLAR.type) as LivingEntity

        pillar.customName = CustomEntityType.STONE_PILLAR.customName
        pillar.setAI(false)
        pillar.isSilent = true

        if (pillar is Ageable) pillar.setAdult()
        pillar.equipment?.also { it.clear() }

        PersistentDataUtil.setValue(pillar, DataTypeKeys.IS_MINION, true)
        PersistentDataUtil.setValue(pillar, DataTypeKeys.DROP_BASE_LOOT, false)

        val attributeInstance = pillar.getAttribute(Attribute.GENERIC_MAX_HEALTH)
        if (attributeInstance != null) {
            attributeInstance.baseValue = 1.0
            pillar.health = 1.0
        }

        GravitationPillarPullRunnable(pillar).runTaskLater(DungeonBossSettings.GRAVITATION_PILLAR_COOLDOWN)
    }

    private class GravitationPillarPullRunnable(
        private val pillar: LivingEntity,
    ) : BukkitRunnable() {
        override fun run() {
            if (pillar.isDead) {
                this.cancel()
                return
            }

            val players = DungeonUtil.getNearbyPlayers(pillar, DungeonBossSettings.GRAVITATION_PILLAR_RANGE)
            for (player in players) {
                val vec = pillar.location.subtract(player.location).multiply(0.1)
                player.velocity = Vector(vec.x, vec.y + vec.length() / 5, vec.z)
            }

            pillar.world.playSound(pillar.location, Sound.BLOCK_BASALT_BREAK, SoundCategory.PLAYERS, 1.5f, 0.5f)

            GravitationPillarPullRunnable(pillar).runTaskLater(DungeonBossSettings.GRAVITATION_PILLAR_COOLDOWN)
        }
    }

    private fun createPoisonClouds() {
        DungeonUtil.getNearbyPlayers(boss, DungeonBossSettings.POISON_CLOUD_RANGE)
            .forEach {
                val spawnLoc = Location(it.location.world, it.location.x, it.location.blockY.toDouble(), it.location.z)
                while (it.world.getBlockAt(spawnLoc).isPassable) {
                    spawnLoc.subtract(0.0, 1.0, 0.0)
                }
                spawnLoc.add(0.0, 1.0, 0.0)

                val cloud = it.world.spawnEntity(spawnLoc, EntityType.AREA_EFFECT_CLOUD) as AreaEffectCloud
                cloud.color = Color.GREEN
                cloud.duration = DungeonBossSettings.POISON_CLOUD_DURATION
                cloud.radius = DungeonBossSettings.POISON_CLOUD_RADIUS
                cloud.radiusPerTick = DungeonBossSettings.POISON_CLOUD_RADIUS_PER_TICK
                cloud.addCustomEffect(DungeonBossSettings.POISON_CLOUD_EFFECT, true)
                cloud.reapplicationDelay = DungeonBossSettings.POISON_CLOUD_REAPPLICATION_DELAY
                cloud.waitTime = DungeonBossSettings.POISON_CLOUD_REAPPLICATION_DELAY
            }
    }
}