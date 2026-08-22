package de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver

import com.geckolib.animatable.GeoAnimatable
import com.geckolib.animatable.GeoEntity
import com.geckolib.animatable.instance.AnimatableInstanceCache
import com.geckolib.animatable.manager.AnimatableManager
import com.geckolib.animation.AnimationController
import com.geckolib.animation.RawAnimation
import com.geckolib.animation.`object`.PlayState
import com.geckolib.constant.DefaultAnimations
import com.geckolib.util.GeckoLibUtil
import de.fuballer.mcendgame.main.component.boss_event.BossEventType
import de.fuballer.mcendgame.main.component.boss_event.BossEventTypePayload
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.addCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import de.fuballer.mcendgame.main.component.entity.custom.attack.Attack
import de.fuballer.mcendgame.main.component.entity.custom.attack.AttackPose
import de.fuballer.mcendgame.main.component.entity.custom.attack.damage.AreaAttackDamage
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.*
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.particle.DelayedParticleData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.particle.ParticleData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.sound.DelayedSoundData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.sound.RangeDefinedSoundData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.status_effect.DelayedStatusEffectData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.status_effect.StatusEffectData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.summon.*
import de.fuballer.mcendgame.main.component.entity.custom.attack.debris_explosion.DebrisExplosionAttack
import de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition.*
import de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition.can_not_reach_target.CanNotReachTargetTriggerCondition
import de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition.can_not_reach_target.CanNotReachTargetTriggerConditionInterface
import de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver.beastweaver_vine.BeastweaverVineEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver.beastweaver_wolf.BeastweaverWolfEntity
import de.fuballer.mcendgame.main.component.entity.custom.goals.*
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.BlockAbleMovementMob
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.CustomAttacksMob
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.DisableAbleGoalsMob
import de.fuballer.mcendgame.main.component.entity.types.boss.beastweaver.BeastweaverVineStats
import de.fuballer.mcendgame.main.component.entity.types.boss.beastweaver.BeastweaverWolfStats
import de.fuballer.mcendgame.main.component.particle.CustomParticleTypes
import de.fuballer.mcendgame.main.component.particle.DirectionalAttackSweepParticleEffect
import de.fuballer.mcendgame.main.component.sound.CustomSoundEvents
import de.fuballer.mcendgame.main.util.extension.EntityExtension.getDistanceToGround
import de.fuballer.mcendgame.main.util.extension.EntityExtension.getHealthPercentage
import de.fuballer.mcendgame.main.util.extension.EntityExtension.isEnemy
import de.fuballer.mcendgame.main.util.extension.EntityExtension.isFacingTowards
import de.fuballer.mcendgame.main.util.extension.EntityExtension.rotateToEntity
import de.fuballer.mcendgame.main.util.extension.EntityExtension.setAndSyncVelocity
import de.fuballer.mcendgame.main.util.extension.Vec3Extension.getYaw
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isCompanion
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isDungeonEnemy
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.setCompanion
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.setDungeonEnemy
import de.fuballer.mcendgame.main.util.minecraft.EntityUtil
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import de.fuballer.mcendgame.main.util.random.RandomOption
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.ChatFormatting
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerBossEvent
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.world.BossEvent
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.FloatGoal
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation
import net.minecraft.world.entity.ai.navigation.PathNavigation
import net.minecraft.world.entity.ai.targeting.TargetingConditions
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.npc.villager.Villager
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.ProjectileDeflection
import net.minecraft.world.level.Level
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.world.phys.Vec3
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.pow
import kotlin.random.Random

class BeastweaverEntity(
    type: EntityType<out BeastweaverEntity>,
    world: Level,
) : PathfinderMob(type, world),
    GeoEntity,
    DisableAbleGoalsMob,
    BlockAbleMovementMob<BeastweaverEntity>,
    Enemy,
    CustomAttacksMob<BeastweaverEntity>,
    CanNotReachTargetTriggerConditionInterface {
    companion object {
        private const val TRANSFORM_ADDITIONAL_SCALE = 0.25F

        private val TRANSFORM_BASE_ANIM = RawAnimation.begin().thenLoop("transform.base")
        private const val MAX_TRANSFORM_PROGRESS_PER_TICK = 0.01F

        private const val TRANSFORM_SHOULDER_SPIKES_ANIM_CONTROLLER_ID = "Transform Shoulder Spikes"
        private val TRANSFORM_SHOULDER_SPIKES_ANIM: RawAnimation = RawAnimation.begin().thenPlayAndHold("transform.shoulder_spikes")
        private const val TRANSFORM_SHOULDER_SPIKES_ID = "Transform Shoulder Spikes"

        private const val TRANSFORM_ANTLERS_ANIM_CONTROLLER_ID = "Transform Antlers"
        private val TRANSFORM_ANTLERS_ANIM: RawAnimation = RawAnimation.begin().thenPlayAndHold("transform.antlers")
        private const val TRANSFORM_ANTLERS_ID = "Transform Antlers"

        private const val TRANSFORM_SNOUT_ANIM_CONTROLLER_ID = "Transform Snout"
        private val TRANSFORM_SNOUT_ANIM: RawAnimation = RawAnimation.begin().thenPlayAndHold("transform.snout")
        private const val TRANSFORM_SNOUT_ID = "Transform Snout"

        private const val TRANSFORM_EARS_ANIM_CONTROLLER_ID = "Transform Ears"
        private val TRANSFORM_EARS_ANIM: RawAnimation = RawAnimation.begin().thenPlayAndHold("transform.ears")
        private const val TRANSFORM_EARS_ID = "Transform Ears"

        private const val TRANSFORM_HAND_CLAWS_ANIM_CONTROLLER_ID = "Transform Hand Claws"
        private val TRANSFORM_HAND_CLAWS_ANIM: RawAnimation = RawAnimation.begin().thenPlayAndHold("transform.hand_claws")
        private const val TRANSFORM_HAND_CLAWS_ID = "Transform Hand Claws"

        private const val SHOULDER_SPIKES_ANIMATION_THRESHOLD = 0.5
        val TRANSFORM_EXTRAS_DATA = listOf(
            TransformExtrasData(
                0.1,
                TRANSFORM_EARS_ANIM_CONTROLLER_ID,
                TRANSFORM_EARS_ID,
                hiddenBeforeTrigger = setOf(
                    "ears",
                ),
            ),
            TransformExtrasData(
                0.25,
                TRANSFORM_HAND_CLAWS_ANIM_CONTROLLER_ID,
                TRANSFORM_HAND_CLAWS_ID,
                hiddenBeforeTrigger = setOf(
                    "leftArmClaws",
                    "rightArmClaws",
                ),
            ),
            TransformExtrasData(
                SHOULDER_SPIKES_ANIMATION_THRESHOLD,
                TRANSFORM_SHOULDER_SPIKES_ANIM_CONTROLLER_ID,
                TRANSFORM_SHOULDER_SPIKES_ID,
                hiddenBeforeTrigger = setOf(
                    "leftArmSpikes",
                    "rightArmSpikes",
                    "leftArmPauldronRippedFront",
                    "leftArmPauldronRippedBack",
                    "rightArmPauldronRippedFront",
                    "rightArmPauldronRippedBack",
                ),
                hiddenAfterTrigger = setOf(
                    "leftArmPauldronIntact",
                    "rightArmPauldronIntact",
                ),
                hiddenAfterFinish = setOf(
                    "leftArmPauldronRippedFront",
                    "leftArmPauldronRippedBack",
                    "rightArmPauldronRippedFront",
                    "rightArmPauldronRippedBack",
                ),
            ),
            TransformExtrasData(
                0.6,
                TRANSFORM_SNOUT_ANIM_CONTROLLER_ID,
                TRANSFORM_SNOUT_ID,
                hiddenBeforeTrigger = setOf(
                    "snout",
                ),
            ),
            TransformExtrasData(
                0.75,
                TRANSFORM_ANTLERS_ANIM_CONTROLLER_ID,
                TRANSFORM_ANTLERS_ID,
                hiddenBeforeTrigger = setOf(
                    "leftAntlerBase",
                    "rightAntlerBase",
                ),
            ),
        )

        private const val ATTACK_ANIM_CONTROLLER_ID = "Attack"

        private fun getLowHealthBasedValue(
            entity: LivingEntity,
            base: Double = 1.0,
            healthPercentageStart: Double = 0.5,
            maxAdded: Double,
        ) = entity.getHealthPercentage().let { base + maxAdded * (healthPercentageStart - it).coerceAtLeast(0.0) / healthPercentageStart }

        private val BEAR_SWIPE_SOUND_DATA = listOf(
            DelayedSoundData(
                RangeDefinedSoundData(
                    SoundEvents.RESPAWN_ANCHOR_SET_SPAWN,
                    { Random.nextDouble(0.5, 0.6).toFloat() },
                    { Random.nextDouble(1.1, 1.3).toFloat() },
                    SoundSource.HOSTILE,
                    range = 24.0,
                ),
                2,
            ),
            DelayedSoundData(
                RangeDefinedSoundData(
                    SoundEvents.POLAR_BEAR_WARNING,
                    { Random.nextDouble(0.5, 0.6).toFloat() },
                    { Random.nextDouble(0.9, 1.1).toFloat() },
                    SoundSource.HOSTILE,
                    range = 24.0,
                ),
                18,
            ),
            DelayedSoundData(
                RangeDefinedSoundData(
                    SoundEvents.PLAYER_ATTACK_SWEEP,
                    { Random.nextDouble(0.8, 0.9).toFloat() },
                    { Random.nextDouble(0.7, 0.85).toFloat() },
                    SoundSource.HOSTILE,
                    range = 24.0,
                ),
                18,
            ),
            DelayedSoundData(
                RangeDefinedSoundData(
                    SoundEvents.PLAYER_ATTACK_STRONG,
                    { Random.nextDouble(0.8, 0.9).toFloat() },
                    { Random.nextDouble(0.7, 0.85).toFloat() },
                    SoundSource.HOSTILE,
                    range = 24.0,
                ),
                20,
            ),
        )

        private fun getBearSwipeParticleData(
            forwards: Double,
            sideways: Double, // positive -> right
            height: Double,
            size: Double,
        ) = DelayedParticleData(
            ParticleData(
                particle = { _, entity ->
                    if (entity !is LivingEntity) return@ParticleData ParticleTypes.ASH

                    val forwardsVector = entity.calculateViewVector(entity.xRot, entity.yBodyRot).horizontal().normalize()
                    val sidewaysVector = forwardsVector.cross(Vec3(0.0, 1.0, 0.0))

                    val scale = entity.scale

                    val dir = forwardsVector.scale(forwards)
                        .add(sidewaysVector.scale(sideways))
                        .add(0.0, height * scale - entity.eyeHeight, 0.0)

                    DirectionalAttackSweepParticleEffect(size * scale, dir.x, dir.y, dir.z)
                },
                offset = { entity ->
                    if (entity !is LivingEntity) return@ParticleData Vec3.ZERO

                    val forwardsVector = entity.calculateViewVector(entity.xRot, entity.yBodyRot).horizontal().normalize()
                    val sidewaysVector = forwardsVector.cross(Vec3(0.0, 1.0, 0.0))
                    forwardsVector.scale(forwards)
                        .add(sidewaysVector.scale(sideways))
                        .add(0.0, height, 0.0)
                },
                count = 1,
                dist = { Vec3.ZERO },
                speed = 1.0,
            ),
            delay = 18,
        )

        private val BEAR_SWIPE_RIGHT_AREA = AreaAttackDamage.DamageArea(3.25, 1.6, 1.75, -0.1, 0.35, 0.75)
        private val BEAR_SWIPE_RIGHT_ATTACK_DAMAGE = AreaAttackDamage(0.75F, 1.0, BEAR_SWIPE_RIGHT_AREA, disableBlockingShield = 3F)
        private val BEAR_SWIPE_RIGHT_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.bear_swipe_right")
        private const val BEAR_SWIPE_RIGHT_ID = "Bear Swipe Right"
        private val BEAR_SWIPE_RIGHT_ANIM_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, ATTACK_ANIM_CONTROLLER_ID, BEAR_SWIPE_RIGHT_ID)
        private val BEAR_SWIPE_RIGHT_ATTACK =
            Attack<BeastweaverEntity>(
                BEAR_SWIPE_RIGHT_ID,
                BEAR_SWIPE_RIGHT_ANIM_DATA,
                totalDuration = 28,
                cooldown = { mob -> 29 + (10 * mob.getHealthPercentage()).toInt() },
                DistanceTriggerCondition(3.0, affectedByScale = true),
                data = listOf(
                    DelayedDamageData(BEAR_SWIPE_RIGHT_ATTACK_DAMAGE, 19),

                    *BEAR_SWIPE_SOUND_DATA.toTypedArray(),

                    getBearSwipeParticleData(1.5, 0.65, 0.5, 1.5),
                    getBearSwipeParticleData(1.45, 0.6, 0.65, 1.35),
                ),
                attackSpeed = { mob -> getLowHealthBasedValue(mob, maxAdded = 0.45) },
            )

        private val BEAR_SWIPE_LEFT_AREA = AreaAttackDamage.DamageArea(3.25, 1.6, 1.75, -0.1, -0.35, 0.75)
        private val BEAR_SWIPE_LEFT_ATTACK_DAMAGE = AreaAttackDamage(0.75F, 1.0, BEAR_SWIPE_LEFT_AREA, disableBlockingShield = 3F)
        private val BEAR_SWIPE_LEFT_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.bear_swipe_left")
        private const val BEAR_SWIPE_LEFT_ID = "Bear Swipe Left"
        private val BEAR_SWIPE_LEFT_ANIM_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, ATTACK_ANIM_CONTROLLER_ID, BEAR_SWIPE_LEFT_ID)
        private val BEAR_SWIPE_LEFT_ATTACK =
            Attack<BeastweaverEntity>(
                BEAR_SWIPE_LEFT_ID,
                BEAR_SWIPE_LEFT_ANIM_DATA,
                totalDuration = 28,
                cooldown = { mob -> 29 + (10 * mob.getHealthPercentage()).toInt() },
                DistanceTriggerCondition(3.0, affectedByScale = true),
                data = listOf(
                    DelayedDamageData(BEAR_SWIPE_LEFT_ATTACK_DAMAGE, 19),

                    *BEAR_SWIPE_SOUND_DATA.toTypedArray(),

                    getBearSwipeParticleData(1.5, -0.65, 0.5, 1.5),
                    getBearSwipeParticleData(1.45, -0.6, 0.65, 1.35),
                ),
                attackSpeed = { mob -> getLowHealthBasedValue(mob, maxAdded = 0.45) },
            )

        private val TAIL_SWEEP_DAMAGE_DATA = listOf(
            DelayedDamageData(
                AreaAttackDamage(1.5F, 2.5, AreaAttackDamage.DamageArea(5.0, 1.25, 0.4, 0.1, -2.25, 0.5), knockbackWhenBlocked = true),
                minDelay = 18,
            ),
            DelayedDamageData(
                AreaAttackDamage(1.5F, 2.5, AreaAttackDamage.DamageArea(5.0, 1.0, 0.4, 0.1, 0.0, 0.5), knockbackWhenBlocked = true),
                minDelay = 19,
            ),
            DelayedDamageData(
                AreaAttackDamage(1.5F, 2.5, AreaAttackDamage.DamageArea(5.0, 1.25, 0.4, 0.1, 2.25, 0.5), knockbackWhenBlocked = true),
                minDelay = 20,
            ),
        )
        private val TAIL_SWEEP_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.tail_sweep")
        private const val TAIL_SWEEP_ID = "Tail Sweep"
        private val TAIL_SWEEP_ANIM_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, ATTACK_ANIM_CONTROLLER_ID, TAIL_SWEEP_ID)
        private val TAIL_SWEEP_ATTACK =
            Attack<BeastweaverEntity>(
                TAIL_SWEEP_ID,
                TAIL_SWEEP_ANIM_DATA,
                totalDuration = 30,
                cooldown = { mob -> 100 + (50 * mob.getHealthPercentage()).toInt() },
                TriggerConditionGroup(
                    TriggerConditionGroup.TriggerConditionJoinType.AND,
                    listOf(
                        DistanceTriggerCondition(4.5, affectedByScale = true),
                        YDistanceTriggerCondition(-1.75, 1.0)
                    )
                ),
                data = listOf(
                    *TAIL_SWEEP_DAMAGE_DATA.toTypedArray(),

                    DelayedSoundData(
                        RangeDefinedSoundData(
                            SoundEvents.AMETHYST_BLOCK_CHIME,
                            { Random.nextDouble(1.8, 2.0).toFloat() },
                            { Random.nextDouble(0.9, 1.0).toFloat() },
                            SoundSource.HOSTILE,
                            range = 32.0,
                        ),
                        8,
                    ),
                    DelayedSoundData(
                        RangeDefinedSoundData(
                            SoundEvents.BEACON_POWER_SELECT,
                            { Random.nextDouble(0.5, 0.6).toFloat() },
                            { Random.nextDouble(0.9, 1.0).toFloat() },
                            SoundSource.HOSTILE,
                            range = 32.0,
                        ),
                        8,
                    ),
                    DelayedSoundData(
                        RangeDefinedSoundData(
                            SoundEvents.PLAYER_ATTACK_SWEEP,
                            { Random.nextDouble(0.9, 1.1).toFloat() },
                            { Random.nextDouble(0.6, 0.7).toFloat() },
                            SoundSource.HOSTILE,
                            range = 32.0,
                        ),
                        19,
                    ),
                    DelayedSoundData(
                        RangeDefinedSoundData(
                            SoundEvents.ENDER_DRAGON_FLAP,
                            { Random.nextDouble(0.7, 0.8).toFloat() },
                            { Random.nextDouble(0.9, 1.0).toFloat() },
                            SoundSource.HOSTILE,
                            range = 32.0,
                        ),
                        19,
                    ),
                    DelayedSoundData(
                        RangeDefinedSoundData(
                            SoundEvents.EVOKER_CAST_SPELL,
                            { Random.nextDouble(0.7, 0.8).toFloat() },
                            { Random.nextDouble(1.0, 1.1).toFloat() },
                            SoundSource.HOSTILE,
                            range = 32.0,
                        ),
                        19,
                    ),
                ),
                blockMovementDuration = 30,
                attackSpeed = { mob -> getLowHealthBasedValue(mob, maxAdded = 0.35) },
            )

        private val WINGS_LAUNCH_AREA = AreaAttackDamage.DamageArea(10.0, 5.0, 0.75, -5.0, 0.0, 0.5, offsetToGround = true)
        private val WINGS_LAUNCH_ATTACK_DAMAGE = AreaAttackDamage(0.1F, 3.5, WINGS_LAUNCH_AREA, knockbackWhenBlocked = true)
        private val WINGS_LAUNCH_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.wings_launch")
        private const val WINGS_LAUNCH_ID = "Wings Launch"
        private val WINGS_LAUNCH_ANIM_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.AIRBORNE, ATTACK_ANIM_CONTROLLER_ID, WINGS_LAUNCH_ID)
        private val WINGS_LAUNCH_ATTACK =
            Attack<BeastweaverEntity>(
                WINGS_LAUNCH_ID,
                WINGS_LAUNCH_ANIM_DATA,
                totalDuration = 35,
                cooldown = { mob -> 200 + (100 * mob.getHealthPercentage()).toInt() },
                TriggerConditionGroup(
                    TriggerConditionGroup.TriggerConditionJoinType.AND,
                    listOf(
                        HealthTriggerCondition(0.0, 0.85),
                        CanMoveUpTriggerCondition(6.0),
                        HorizontalDistanceTriggerCondition(8.0, affectedByScale = true),
                    )
                ),
                data = listOf(
                    DelayedDamageData(WINGS_LAUNCH_ATTACK_DAMAGE, 23),

                    DelayedDurationTransformData(0, 35) { _, attacker, target, age, _ ->
                        target?.also { attacker.rotateToEntity(it) }

                        if (age == 1) attacker.isNoGravity = true

                        if (age <= 16) {
                            val levitateProgress = age / 16.0
                            val yVelocity = (1 - levitateProgress) * 0.05
                            attacker.setAndSyncVelocity(Vec3(0.0, yVelocity, 0.0))
                        } else if (age in 21..35) {
                            val t = age - 20
                            val launchProgress = t / 15.0
                            val yVelocity = (1 - launchProgress).pow(1.5) * 1.0
                            attacker.setAndSyncVelocity(Vec3(0.0, yVelocity, 0.0))
                        }
                    },

                    DelayedParticleData(
                        ParticleData(
                            particle = { _, _ -> CustomParticleTypes.CLOUD_TORUS },
                            offset = { entity ->
                                val distanceToGround = entity.getDistanceToGround()
                                Vec3(0.0, -distanceToGround + 0.35, 0.0)
                            },
                            count = 100,
                            dist = { Vec3(1.0, 0.1, 1.0) },
                            speed = 1.0,
                        ),
                        delay = 21,
                    ),

                    DelayedSoundData(
                        RangeDefinedSoundData(
                            SoundEvents.AMETHYST_BLOCK_CHIME,
                            { Random.nextDouble(1.8, 2.0).toFloat() },
                            { Random.nextDouble(0.9, 1.0).toFloat() },
                            SoundSource.HOSTILE,
                            range = 32.0,
                        ),
                        3,
                    ),
                    DelayedSoundData(
                        RangeDefinedSoundData(
                            SoundEvents.ENDER_DRAGON_FLAP,
                            { Random.nextDouble(1.2, 1.3).toFloat() },
                            { Random.nextDouble(0.9, 1.0).toFloat() },
                            SoundSource.HOSTILE,
                            range = 48.0,
                        ),
                        20,
                    ),
                    DelayedSoundData(
                        RangeDefinedSoundData(
                            SoundEvents.BREEZE_IDLE_GROUND,
                            { Random.nextDouble(1.2, 1.3).toFloat() },
                            { Random.nextDouble(0.9, 1.0).toFloat() },
                            SoundSource.HOSTILE,
                            range = 32.0,
                        ),
                        13,
                    ),
                ),
                blockMovementDuration = 35,
            )

        private val ELEPHANT_STOMP_MAIN_EXPLOSION_AREA = AreaAttackDamage.DamageArea(9.0, 4.5, 2.5, -4.5, 0.0, 0.0)
        private val ELEPHANT_STOMP_MAIN_EXPLOSION_ATTACK_DAMAGE = AreaAttackDamage(2.0F, 2.5, ELEPHANT_STOMP_MAIN_EXPLOSION_AREA, blockable = false)
        private val ELEPHANT_STOMP_DEBRIS_AREA_AREA = AreaAttackDamage.DamageArea(4.0, 2.0, 2.0, -2.0, 0.0, 0.0)
        private val ELEPHANT_STOMP_DEBRIS_ATTACK_DAMAGE = AreaAttackDamage(1.0F, 0.5, ELEPHANT_STOMP_DEBRIS_AREA_AREA, blockable = false)
        private val ELEPHANT_STOMP_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.elephant_stomp")
        private const val ELEPHANT_STOMP_ID = "Elephant Stomp"
        private val ELEPHANT_STOMP_ANIM_DATA = AttackAnimationData(AttackPose.AIRBORNE, AttackPose.DEFAULT, ATTACK_ANIM_CONTROLLER_ID, ELEPHANT_STOMP_ID)
        private val ELEPHANT_STOMP_ATTACK =
            DebrisExplosionAttack<BeastweaverEntity>(
                ELEPHANT_STOMP_ID,
                ELEPHANT_STOMP_ANIM_DATA,
                totalDuration = 40,
                cooldown = 0,
                AlwaysTrueTriggerCondition(),
                data = listOf(
                    object : DelayedAttackData(10) {
                        override fun apply(level: ServerLevel, entity: Mob, target: LivingEntity?) {
                            entity.isNoGravity = false
                        }
                    },

                    DelayedSoundData(
                        RangeDefinedSoundData(
                            SoundEvents.AMETHYST_BLOCK_CHIME,
                            { Random.nextDouble(1.8, 2.0).toFloat() },
                            { Random.nextDouble(0.6, 0.65).toFloat() },
                            SoundSource.HOSTILE,
                            range = 48.0,
                        ),
                        3,
                    ),
                    DelayedSoundData(
                        RangeDefinedSoundData(
                            SoundEvents.BREEZE_LAND,
                            { 1.5f },
                            { Random.nextDouble(0.8, 0.85).toFloat() },
                            SoundSource.HOSTILE,
                            range = 48.0,
                        ),
                        20,
                    ),
                    DelayedSoundData(
                        RangeDefinedSoundData(
                            SoundEvents.POLAR_BEAR_STEP,
                            { 1.5f },
                            { Random.nextDouble(0.8, 0.85).toFloat() },
                            SoundSource.HOSTILE,
                            range = 64.0,
                        ),
                        26,
                    ),
                ),
                delay = 26,
                mainExplosionDamage = ELEPHANT_STOMP_MAIN_EXPLOSION_ATTACK_DAMAGE,
                mainExplosionParticles = ParticleData(
                    particle = { _, _ -> ParticleTypes.EXPLOSION },
                    offset = { entity ->
                        val distanceToGround = entity.getDistanceToGround()
                        Vec3(0.0, -distanceToGround + 0.2, 0.0)
                    },
                    count = 30,
                    dist = { Vec3(3.0, 1.5, 3.0) },
                    speed = 1.0,
                ),
                mainExplosionSound = RangeDefinedSoundData(
                    SoundEvents.GENERIC_EXPLODE.value(),
                    { Random.nextDouble(1.2, 1.3).toFloat() },
                    { Random.nextDouble(0.9, 1.0).toFloat() },
                    SoundSource.HOSTILE,
                    range = 64.0,
                ),
                debrisExplosionDamage = ELEPHANT_STOMP_DEBRIS_ATTACK_DAMAGE,
                debrisExplosionParticles = ParticleData(
                    particle = { _, _ -> ParticleTypes.EXPLOSION },
                    offset = { Vec3.ZERO },
                    count = 5,
                    dist = { Vec3(2.0, 2.0, 2.0) },
                    speed = 1.0,
                ),
                debrisExplosionSound = RangeDefinedSoundData(
                    SoundEvents.GENERIC_EXPLODE.value(),
                    { Random.nextDouble(0.8, 0.9).toFloat() },
                    { Random.nextDouble(1.0, 1.1).toFloat() },
                    SoundSource.HOSTILE,
                    range = 16.0,
                ),
                debrisCreateRadiusRange = Pair(2.0, 6.0),
                debrisCreateProbabilityFromDistanceToOrigin = { 0.3 },
                debrisVelocity = { 1.0 + Random.nextDouble() * 0.3 },
                blockMovementDuration = 40,
            )

        private val GET_SUMMON_TARGETS: (ServerLevel, LivingEntity, LivingEntity?) -> List<LivingEntity> = { level, summoner, target ->
            val targets = if (target == null) mutableSetOf() else mutableSetOf(target)
            targets.addAll(
                level.getNearbyEntities(
                    LivingEntity::class.java,
                    TargetingConditions.forCombat().selector { entity, _ -> entity.isEnemy(summoner) && !entity.isCompanion() },
                    summoner,
                    summoner.boundingBox.inflate(30.0, 10.0, 30.0),
                )
            )
            targets.toList()
        }

        private val WOLF_SUMMON_MOVEMENT_SPEED_IDENTIFIER = IdentifierUtil.default("beastweaver_wolf_summon_movement_speed")
        private val WOLF_SUMMON_MAX_HEALTH_IDENTIFIER = IdentifierUtil.default("beastweaver_wolf_summon_max_health")
        private val WOLF_SUMMON_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.wolf_summon")
        private const val WOLF_SUMMON_ID = "Wolf Summon"
        private val WOLF_SUMMON_ANIM_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, ATTACK_ANIM_CONTROLLER_ID, WOLF_SUMMON_ID)
        private val WOLF_SUMMON_ATTACK =
            Attack<BeastweaverEntity>(
                WOLF_SUMMON_ID,
                WOLF_SUMMON_ANIM_DATA,
                totalDuration = 70,
                cooldown = { mob -> 900 + (300 * mob.getHealthPercentage()).toInt() },
                CompanionLimitTriggerCondition(
                    companionLimit = { targetCount -> targetCount * 1 },
                    getTargetCount = { level, summoner, target -> GET_SUMMON_TARGETS(level, summoner, target).count() },
                    searchRange = 50.0,
                    filter = { entity -> entity is BeastweaverWolfEntity }
                ),
                data = listOf(
                    DelayedSummonData(
                        SummonScatteredPerTargetData(
                            factory = { level, summoner, target ->
                                val wolf = BeastweaverWolfEntity(CustomEntities.BEASTWEAVER_WOLF, level)
                                EntityUtil.setStats(wolf, BeastweaverWolfStats)

                                if (summoner.isDungeonEnemy()) wolf.setDungeonEnemy()
                                wolf.addCustomAttribute(CustomAttribute(CustomAttributeTypes.MORE_DAMAGE, roll = DoubleRoll(DoubleBounds(-0.4))))

                                val moreSpeed = getLowHealthBasedValue(summoner, base = 0.0, maxAdded = 0.25)
                                val speedModifier = AttributeModifier(WOLF_SUMMON_MOVEMENT_SPEED_IDENTIFIER, moreSpeed, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                                wolf.attributes.getInstance(Attributes.MOVEMENT_SPEED)?.also { it.addPermanentModifier(speedModifier) }

                                val moreMaxHealth = getLowHealthBasedValue(summoner, base = 0.0, maxAdded = 0.5)
                                val maxHealthModifier = AttributeModifier(WOLF_SUMMON_MAX_HEALTH_IDENTIFIER, moreMaxHealth, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                                wolf.attributes.getInstance(Attributes.MAX_HEALTH)?.also { it.addPermanentModifier(maxHealthModifier) }

                                val scale = getLowHealthBasedValue(summoner, base = Random.nextDouble(0.9, 1.1), maxAdded = 0.2)
                                wolf.getAttribute(Attributes.SCALE)?.baseValue = scale

                                wolf.setCompanion()
                                wolf.owner = summoner
                                wolf.target = target
                                wolf
                            },
                            getTargets = GET_SUMMON_TARGETS,
                            getCountPerTarget = { targetCount -> if (targetCount <= 3) 3 else 2 },
                            spawnPositionsSearchSteps = 15,
                            maxSpawnDistanceToTarget = 25.0,
                        ),
                        60,
                    ),

                    DelayedSoundData(
                        RangeDefinedSoundData(
                            CustomSoundEvents.WOLF_HOWL,
                            { Random.nextDouble(1.2, 1.3).toFloat() },
                            { Random.nextDouble(0.9, 1.0).toFloat() },
                            SoundSource.HOSTILE,
                            range = 64.0,
                        ),
                        25,
                    ),
                ),
                blockMovementDuration = 70,
                attackSpeed = { mob -> getLowHealthBasedValue(mob, maxAdded = 0.4) },
            )

        private val SUMMON_VINES_PARTICLE_DATA = ParticleData(
            particle = { _, _ -> ParticleTypes.SPORE_BLOSSOM_AIR },
            offset = { _ -> Vec3(0.0, 1.4, 0.0) },
            count = 3,
            dist = { Vec3(0.1, 0.4, 0.1) },
            speed = 2.0,
        )

        private val VINE_FACTORY: (ServerLevel, LivingEntity) -> Entity = { level, summoner ->
            val vine = BeastweaverVineEntity(CustomEntities.BEASTWEAVER_VINE, level)
            EntityUtil.setStats(vine, BeastweaverVineStats)

            vine.setCompanion()
            vine.setOwner(summoner)
            if (summoner.isDungeonEnemy()) vine.setDungeonEnemy()

            val scale = getLowHealthBasedValue(summoner, base = Random.nextDouble(0.9, 1.3), maxAdded = 0.5)
            vine.getAttribute(Attributes.SCALE)?.baseValue = scale

            val attackSpeed = getLowHealthBasedValue(summoner, maxAdded = 0.5)
            vine.setAttackSpeed(attackSpeed)

            vine
        }

        private val SUMMON_VINES_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.summon_vines")
        private const val SUMMON_VINES_ID = "Summon Vines"
        private val SUMMON_VINES_ANIM_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, ATTACK_ANIM_CONTROLLER_ID, SUMMON_VINES_ID)
        private val SUMMON_VINES_ATTACK =
            Attack<BeastweaverEntity>(
                SUMMON_VINES_ID,
                SUMMON_VINES_ANIM_DATA,
                totalDuration = 95,
                cooldown = { mob -> 800 + (200 * mob.getHealthPercentage()).toInt() },
                HealthTriggerCondition(0.0, 0.65),
                data = listOf(
                    DelayedDurationTransformData(0, 80) { _, attacker, _, age, attackSpeed ->
                        if (age == 1) attacker.isNoGravity = true

                        val endAge = (80 / attackSpeed).toInt()
                        if (age <= endAge) {
                            val levitateProgress = age / endAge.toDouble()
                            val yVelocity = (1 - levitateProgress) * 0.02 * attackSpeed
                            attacker.setAndSyncVelocity(Vec3(0.0, yVelocity, 0.0))

                            if (age == endAge) attacker.isNoGravity = false
                        }
                    },

                    DelayedDurationSummonData(
                        durationStart = 0,
                        durationEnd = 40,
                        DurationSummonData(
                            factory = VINE_FACTORY,
                            getTargets = GET_SUMMON_TARGETS,
                            getCount = { targetCount -> 15 + targetCount * 8 },
                            spawnPositionsSearchSteps = 48,
                            minDistanceBetweenSummons = 5.0,
                            validSpawnPosition = { level, blockPos ->
                                (1..2).none {
                                    val pos = blockPos.above(it)
                                    val state = level.getBlockState(pos)
                                    !state.getCollisionShape(level, pos).isEmpty
                                }
                            }
                        ),
                    ),

                    DelayedDurationTransformData(
                        durationStart = 8,
                        durationEnd = 40,
                    ) { level, mob, _, age, attackSpeed ->
                        val particleCount = (age * attackSpeed).toInt() / 8
                        SUMMON_VINES_PARTICLE_DATA.applyWithCount(level, mob, particleCount)
                    },

                    DelayedSoundData(
                        RangeDefinedSoundData(
                            SoundEvents.EVOKER_PREPARE_SUMMON,
                            { Random.nextDouble(1.2, 1.3).toFloat() },
                            { Random.nextDouble(0.9, 1.0).toFloat() },
                            SoundSource.HOSTILE,
                            range = 48.0,
                        ),
                        10,
                    ),
                ),
                blockMovementDuration = 95,
                attackSpeed = { mob -> getLowHealthBasedValue(mob, maxAdded = 0.75) },
            )

        private const val SUMMON_TARGETED_VINE_ID = "Summon Targeted Vine"
        private val SUMMON_TARGETED_VINE_ATTACK = Attack<BeastweaverEntity>(
            SUMMON_TARGETED_VINE_ID,
            animationData = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, "", ""),
            totalDuration = 10,
            cooldown = 50,
            TriggerConditionGroup(
                TriggerConditionGroup.TriggerConditionJoinType.AND,
                listOf(
                    TargetMaxDistanceToGroundTriggerCondition(1.5),
                    CanNotReachTargetTriggerCondition(3.0),
                    NoEntityNearTargetTriggerCondition(CustomEntities.BEASTWEAVER_VINE, 3.0),
                )
            ),
            listOf(
                DelayedSummonData(
                    SummonAtTargetData(
                        factory = { level, attacker, _ -> VINE_FACTORY(level, attacker) },
                        searchForGroundDistance = true,
                    ),
                )
            ),
        )

        private val RHINO_CHARGE_ANIM: RawAnimation = RawAnimation.begin().thenLoop("attack.rhino_charge")
        private const val RHINO_CHARGE_ID = "Rhino Charge"
        private val RHINO_CHARGE_END_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.rhino_charge_end")
        private const val RHINO_CHARGE_END_ID = "Rhino Charge End"
        private val RHINO_CHARGE_HIT_WALL_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.rhino_charge_hit_wall")
        private const val RHINO_CHARGE_HIT_WALL_ID = "Rhino Charge Hit Wall"
        private val RHINO_CHARGE_ANIM_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, ATTACK_ANIM_CONTROLLER_ID, RHINO_CHARGE_ID)
        private val RHINO_CHARGE_ATTACK =
            Attack<BeastweaverEntity>(
                RHINO_CHARGE_ID,
                RHINO_CHARGE_ANIM_DATA,
                totalDuration = -1,
                cooldown = { mob -> 300 + (100 * mob.getHealthPercentage()).toInt() },
                TriggerConditionGroup(
                    TriggerConditionGroup.TriggerConditionJoinType.AND,
                    listOf(
                        DistanceTriggerCondition(10.0, 50.0, affectedByScale = false),
                        StraightPathToTargetTriggerCondition(),
                    ),
                ),
                data = listOf(
                    object : DelayedAttackData() {
                        override fun apply(level: ServerLevel, entity: Mob, target: LivingEntity?) {
                            (entity as BeastweaverEntity).startRhinoCharge()
                        }
                    },

                    DelayedSoundData(
                        RangeDefinedSoundData(
                            SoundEvents.RAVAGER_AMBIENT,
                            { Random.nextDouble(0.8, 0.85).toFloat() },
                            { Random.nextDouble(0.95, 1.05).toFloat() },
                            SoundSource.HOSTILE,
                            range = 64.0,
                        ),
                        0,
                    ),
                ),
            )

        private val IS_RHINO_CHARGING: EntityDataAccessor<Boolean> = SynchedEntityData.defineId(BeastweaverEntity::class.java, EntityDataSerializers.BOOLEAN)

        private val RHINO_CHARGE_MOVEMENT_SPEED_MODIFIER_ID = IdentifierUtil.default("rhino_charge_movement_speed")
        private val RHINO_CHARGE_STEP_HEIGHT_MODIFIER_ID = IdentifierUtil.default("rhino_charge_step_height")
        private val RHINO_CHARGE_STEP_HEIGHT_MODIFIER = AttributeModifier(
            RHINO_CHARGE_STEP_HEIGHT_MODIFIER_ID,
            0.5,
            AttributeModifier.Operation.ADD_VALUE,
        )

        private val RHINO_CHARGE_ATTACK_AREA = AreaAttackDamage.DamageArea(3.0, 1.5, 1.25, 0.0, 0.0, 1.0)
        private val RHINO_CHARGE_ATTACK_DAMAGE = AreaAttackDamage(
            damageFactor = 1.25f,
            knockbackFactor = 3.0,
            area = RHINO_CHARGE_ATTACK_AREA,
            knockbackType = AreaAttackDamage.KnockbackType.BEASTWEAVER_RHINO_CHARGE,
            blockable = false,
            disableBlockingShield = 5f,
        )
        private val RHINO_CHARGE_STEP_SOUND_DATA = RangeDefinedSoundData(
            SoundEvents.POLAR_BEAR_STEP,
            { Random.nextDouble(0.9, 1.0).toFloat() },
            { Random.nextDouble(1.0, 1.05).toFloat() },
            SoundSource.HOSTILE,
            range = 48.0,
        )
        private val RHINO_CHARGE_STEP_EXPLODE_SOUND_DATA = RangeDefinedSoundData(
            SoundEvents.GENERIC_EXPLODE.value(),
            { Random.nextDouble(0.15, 0.16).toFloat() },
            { Random.nextDouble(0.95, 1.05).toFloat() },
            SoundSource.HOSTILE,
            range = 48.0,
        )
        private val RHINO_CHARGE_END_SOUND_DATA = RangeDefinedSoundData(
            SoundEvents.RAVAGER_STUNNED,
            { 1f },
            { Random.nextDouble(0.95, 1.0).toFloat() },
            SoundSource.HOSTILE,
            range = 32.0,
        )

        private val RHINO_CHARGE_HIT_WALL_ATTACK_AREA = AreaAttackDamage.DamageArea(8.0, 4.0, 3.0, -4.0, 0.0, 1.0)
        private val RHINO_CHARGE_HIT_WALL_ATTACK_DAMAGE = AreaAttackDamage(
            damageFactor = 1.8f,
            knockbackFactor = 4.5,
            area = RHINO_CHARGE_HIT_WALL_ATTACK_AREA,
            blockable = false,
        )
        private val RHINO_CHARGE_HIT_WALL_PARTICLE_DATA = ParticleData(
            { _, _ -> ParticleTypes.EXPLOSION },
            { _ -> Vec3.ZERO },
            20,
            { _ -> Vec3(3.0, 3.0, 3.0) },
            1.0,
            true,
        )
        private val RHINO_CHARGE_HIT_WALL_SOUND_DATA = RangeDefinedSoundData(
            SoundEvents.GENERIC_EXPLODE.value(),
            { Random.nextDouble(0.8, 0.9).toFloat() },
            { Random.nextDouble(0.85, 0.9).toFloat() },
            SoundSource.HOSTILE,
            range = 48.0,
        )
        private val RHINO_CHARGE_END_HIT_WALL_SOUND_DATA = RangeDefinedSoundData(
            SoundEvents.RAVAGER_DEATH,
            { 1f },
            { Random.nextDouble(0.95, 1.0).toFloat() },
            SoundSource.HOSTILE,
            range = 48.0,
        )
        private const val RHINO_CHARGE_ARROW_DEFLECT_ANGLE = 90.0

        private const val SPEED_BOOST_ID = "Speed Boost"
        private val SPEED_BOOST_ATTACK = Attack<BeastweaverEntity>(
            SPEED_BOOST_ID,
            animationData = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, "", ""),
            totalDuration = 0,
            cooldown = { mob -> 200 + (200 * mob.getHealthPercentage()).toInt() },
            AttackOnCooldownTriggerCondition(RHINO_CHARGE_ATTACK, 100),
            listOf(
                DelayedStatusEffectData(
                    StatusEffectData(
                        type = MobEffects.SPEED,
                        amplifier = 0,
                        duration = 100,
                        particles = false,
                    ),
                )
            ),
        )

        private val DEATH_PARTICLE_DATA_SMOKE = ParticleData(
            particle = { _, _ -> ParticleTypes.LARGE_SMOKE },
            offset = { _ -> Vec3(0.0, 1.0, 0.0) },
            count = 30,
            dist = { Vec3.ZERO },
            speed = 0.5,
        )
        private val DEATH_PARTICLE_DATA__WHITE_SMOKE = ParticleData(
            particle = { _, _ -> ParticleTypes.WHITE_SMOKE },
            offset = { _ -> Vec3(0.0, 1.0, 0.0) },
            count = 100,
            dist = { Vec3.ZERO },
            speed = 0.5,
        )
        private val DEATH_PARTICLE_DATA_SPORES = ParticleData(
            particle = { _, _ -> ParticleTypes.SPORE_BLOSSOM_AIR },
            offset = { _ -> Vec3(0.0, 1.0, 0.0) },
            count = 100,
            dist = { Vec3(0.5, 0.5, 0.5) },
            speed = 1.0,
        )
        private val DEATH_SOUND_DATA = RangeDefinedSoundData(
            SoundEvents.UI_TOAST_CHALLENGE_COMPLETE,
            { 1f },
            { 1F },
            SoundSource.PLAYERS,
            range = 64.0,
        )

        private val ATTACKS: List<RandomOption<out Attack<BeastweaverEntity>>> = listOf(
            RandomOption(1, BEAR_SWIPE_RIGHT_ATTACK),
            RandomOption(1, BEAR_SWIPE_LEFT_ATTACK),
            RandomOption(1, TAIL_SWEEP_ATTACK),
            RandomOption(1, WINGS_LAUNCH_ATTACK),
            RandomOption(1, ELEPHANT_STOMP_ATTACK),
            RandomOption(1, WOLF_SUMMON_ATTACK),
            RandomOption(1000, SUMMON_VINES_ATTACK),
            RandomOption(1, RHINO_CHARGE_ATTACK),
            RandomOption(1, SPEED_BOOST_ATTACK),
            RandomOption(100, SUMMON_TARGETED_VINE_ATTACK),
        )

        val ANIMATION_DURATIONS = mapOf(
            BEAR_SWIPE_LEFT_ANIM.animationStages.first().animationName to 1.38F,
            BEAR_SWIPE_RIGHT_ANIM.animationStages.first().animationName to 1.38F,
            TAIL_SWEEP_ANIM.animationStages.first().animationName to 1.46F,
            WINGS_LAUNCH_ANIM.animationStages.first().animationName to 1.75F,
            ELEPHANT_STOMP_ANIM.animationStages.first().animationName to 2.0F,
            WOLF_SUMMON_ANIM.animationStages.first().animationName to 3.5F,
            SUMMON_VINES_ANIM.animationStages.first().animationName to 4.75F,
            RHINO_CHARGE_ANIM.animationStages.first().animationName to 1000F,
            RHINO_CHARGE_END_ANIM.animationStages.first().animationName to 2.0F,
            RHINO_CHARGE_HIT_WALL_ANIM.animationStages.first().animationName to 2.75F,
        )

        fun createAttributes(): AttributeSupplier.Builder {
            return createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 128.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5)
                .add(Attributes.ARMOR, 0.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8)
                .add(Attributes.MOVEMENT_EFFICIENCY, 0.85)
                .add(Attributes.SAFE_FALL_DISTANCE, 10.0)
                .add(Attributes.FALL_DAMAGE_MULTIPLIER, 0.1)
                .add(Attributes.WATER_MOVEMENT_EFFICIENCY, 0.65)
        }

        private const val TRANSFORM_PROGRESS_ID = "transform_progress"
        private val TRANSFORM_PROGRESS: EntityDataAccessor<Float> = SynchedEntityData.defineId(BeastweaverEntity::class.java, EntityDataSerializers.FLOAT)

        private val ATTACK_ANIMATION_SPEED: EntityDataAccessor<Float> = SynchedEntityData.defineId(BeastweaverEntity::class.java, EntityDataSerializers.FLOAT)
    }

    override var blockAbleMovementEntity = this
    override var blockedMovementTicks = 0
    override var blockedMovementAirborne = false

    override var attackPose = AttackPose.DEFAULT
    override var attackDuration = 0
    override val attacks = ATTACKS
    override val attackCooldowns: MutableMap<Attack<BeastweaverEntity>, Int> = mutableMapOf()
    override val attackDataInstances = mutableListOf<DelayedAttackDataInstance>()

    private val bossEvent: ServerBossEvent =
        ServerBossEvent(
            Mth.createInsecureUUID(random),
            displayName.copy()
                .withStyle(ChatFormatting.WHITE)
                .withStyle(ChatFormatting.BOLD),
            BossEvent.BossBarColor.GREEN,
            BossEvent.BossBarOverlay.PROGRESS,
        )

    private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)
    override fun getAnimatableInstanceCache() = cache

    private val canNotReachTargetTriggerConditionNavigation: PathNavigation = GroundPathNavigation(this, level())
    override fun getCanNotReachTargetTriggerConditionNavigation() = canNotReachTargetTriggerConditionNavigation

    private var transformProgress = 0F
    private var previousTransformProgress = 0F

    private var rhinoChargeDuration = -1
    private var rhinoChargeStepSoundBuildup = 0.0
    private var rhinoChargeStepSoundRepeatTimer = 0
    private var rhinoChargeDustParticleBuildup = 0.0
    private var isRhinoChargeEnding = false
    private var rhinoChargeEndDuration = 0

    private val transformShoulderSpikesAnimationController =
        AnimationController<GeoAnimatable>(TRANSFORM_SHOULDER_SPIKES_ANIM_CONTROLLER_ID) { _ -> PlayState.STOP }
            .triggerableAnim(TRANSFORM_SHOULDER_SPIKES_ID, TRANSFORM_SHOULDER_SPIKES_ANIM)

    private val transformAntlersAnimationController =
        AnimationController<GeoAnimatable>(TRANSFORM_ANTLERS_ANIM_CONTROLLER_ID) { _ -> PlayState.STOP }
            .triggerableAnim(TRANSFORM_ANTLERS_ID, TRANSFORM_ANTLERS_ANIM)

    private val transformSnoutAnimationController =
        AnimationController<GeoAnimatable>(TRANSFORM_SNOUT_ANIM_CONTROLLER_ID) { _ -> PlayState.STOP }
            .triggerableAnim(TRANSFORM_SNOUT_ID, TRANSFORM_SNOUT_ANIM)

    private val transformEarsAnimationController =
        AnimationController<GeoAnimatable>(TRANSFORM_EARS_ANIM_CONTROLLER_ID) { _ -> PlayState.STOP }
            .triggerableAnim(TRANSFORM_EARS_ID, TRANSFORM_EARS_ANIM)

    private val transformHandClawsAnimationController =
        AnimationController<GeoAnimatable>(TRANSFORM_HAND_CLAWS_ANIM_CONTROLLER_ID) { _ -> PlayState.STOP }
            .triggerableAnim(TRANSFORM_HAND_CLAWS_ID, TRANSFORM_HAND_CLAWS_ANIM)

    private val transformAnimationControllers = listOf(
        transformShoulderSpikesAnimationController,
        transformAntlersAnimationController,
        transformSnoutAnimationController,
        transformEarsAnimationController,
        transformHandClawsAnimationController,
    )

    private val attackAnimationController =
        AnimationController<GeoAnimatable>(ATTACK_ANIM_CONTROLLER_ID, 0) { _ -> PlayState.STOP }
            .triggerableAnim(BEAR_SWIPE_RIGHT_ID, BEAR_SWIPE_RIGHT_ANIM)
            .triggerableAnim(BEAR_SWIPE_LEFT_ID, BEAR_SWIPE_LEFT_ANIM)
            .triggerableAnim(TAIL_SWEEP_ID, TAIL_SWEEP_ANIM)
            .triggerableAnim(WINGS_LAUNCH_ID, WINGS_LAUNCH_ANIM)
            .triggerableAnim(ELEPHANT_STOMP_ID, ELEPHANT_STOMP_ANIM)
            .triggerableAnim(WOLF_SUMMON_ID, WOLF_SUMMON_ANIM)
            .triggerableAnim(SUMMON_VINES_ID, SUMMON_VINES_ANIM)
            .triggerableAnim(RHINO_CHARGE_ID, RHINO_CHARGE_ANIM)
            .triggerableAnim(RHINO_CHARGE_END_ID, RHINO_CHARGE_END_ANIM)
            .triggerableAnim(RHINO_CHARGE_HIT_WALL_ID, RHINO_CHARGE_HIT_WALL_ANIM)

    private fun getAnimationController(name: String) = transformAnimationControllers.find { it.name == name }

    private fun isControllerActive(name: String) = getAnimationController(name)?.isPlayingTriggeredAnimation == true

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
        controllers.add(
            AnimationController<BeastweaverEntity>("Walk/Idle", 5)
            { test ->
                test.setAndContinue(if (test.isMoving) DefaultAnimations.WALK else DefaultAnimations.IDLE)
            },

            transformShoulderSpikesAnimationController,
            transformAntlersAnimationController,
            transformSnoutAnimationController,
            transformEarsAnimationController,
            transformHandClawsAnimationController,

            attackAnimationController,

            AnimationController<BeastweaverEntity>("Transform Base", 0)
            { test -> test.setAndContinue(TRANSFORM_BASE_ANIM) }
                .additiveAnimations(),
        )
    }

    override fun setAttackAnimationSpeed(attackSpeed: Float) {
        entityData.set(ATTACK_ANIMATION_SPEED, attackSpeed)
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(TRANSFORM_PROGRESS, 0F)
        builder.define(IS_RHINO_CHARGING, false)
        builder.define(ATTACK_ANIMATION_SPEED, 1F)
    }

    override fun onSyncedDataUpdated(accessor: EntityDataAccessor<*>) {
        super.onSyncedDataUpdated(accessor)

        if (!level().isClientSide) return
        if (IS_RHINO_CHARGING == accessor) rhinoChargeDuration = 0
        if (ATTACK_ANIMATION_SPEED == accessor) attackAnimationController.animationSpeed = entityData.get(ATTACK_ANIMATION_SPEED).toDouble()
    }

    fun getTransformProgress(tickProgress: Float) = previousTransformProgress + (transformProgress - previousTransformProgress) * tickProgress

    fun getShoulderSpikesAnimTime(tickProgress: Float): Float {
        val transform = getTransformProgress(tickProgress)
        if (transform < SHOULDER_SPIKES_ANIMATION_THRESHOLD) return 0F

        val animTime = transformShoulderSpikesAnimationController.currentAnimationTime
        if (animTime == 0.0) return 1F

        return animTime.toFloat() + tickProgress / 20F
    }

    fun getCurrentAttackAnimName(): String? {
        if (!attackAnimationController.isPlayingTriggeredAnimation) return null
        val rawAnim = attackAnimationController.currentRawAnimation ?: return null
        return rawAnim.animationStages.firstOrNull()?.animationName
    }

    fun getCurrentAttackAnimTime(tickProgress: Float): Float {
        return attackAnimationController.currentAnimationTime.toFloat() + tickProgress / 20F
    }

    private val attackGoal = CustomAttacksGoal(this)
    private val stayInMeleeRangeGoal = StayInRangeGoal(this, 1.0, 2.5)
    private val wanderGoal = DisableAbleWanderAroundFarGoal(this, 0.7)
    private val lookAtPlayerGoal = DisableAbleLookAtEntityGoal(this, Player::class.java, 8F)
    private val lookAroundGoal = DisableAbleLookAroundGoal(this)

    init {
        initDynamicGoals()
        moveControl = BeastweaverMoveControl(this)
    }

    private fun initDynamicGoals() {
        goalSelector.addGoal(2, attackGoal)
        goalSelector.addGoal(3, stayInMeleeRangeGoal)
        goalSelector.addGoal(5, wanderGoal)
        goalSelector.addGoal(6, lookAtPlayerGoal)
        goalSelector.addGoal(7, lookAroundGoal)
    }

    override fun registerGoals() {
        goalSelector.addGoal(0, BeastweaverRhinoChargeControlGoal(this))
        goalSelector.addGoal(1, ChangeTargetGoal(this, probability = 0.4, tryIntervalTicks = 20, 100, { e -> e is Player || e is Villager }))
        goalSelector.addGoal(4, FloatGoal(this))

        targetSelector.addGoal(0, HurtByTargetGoal(this))
        targetSelector.addGoal(1, NearestAttackableTargetGoal(this, Player::class.java, false))
    }

    override fun updateGoals() {
        val movementBlocked = isMovementBlocked()
        attackGoal.isDisabled = movementBlocked
        stayInMeleeRangeGoal.isDisabled = movementBlocked
        wanderGoal.isDisabled = movementBlocked
        lookAtPlayerGoal.isDisabled = movementBlocked
        lookAroundGoal.isDisabled = movementBlocked
    }

    override fun tick() {
        super.tick()
        tickTransformProgress()
        tickRhinoCharge()

        val level = level() as? ServerLevel ?: return
        tickBlockedMovement()
        tickAttacks(level, this)

        bossEvent.setProgress(health / maxHealth)
    }

    private fun tickTransformProgress() {
        if (level().isClientSide) {
            previousTransformProgress = transformProgress
            transformProgress = entityData.get(TRANSFORM_PROGRESS)

            if (transformProgress == previousTransformProgress) return
            refreshDimensions()

            if (transformProgress - previousTransformProgress > MAX_TRANSFORM_PROGRESS_PER_TICK) previousTransformProgress = transformProgress
        } else {
            val healthPercentage = (health / maxHealth).coerceIn(0F, 1F)
            val targetValue = 1 - healthPercentage

            val current = entityData.get(TRANSFORM_PROGRESS)
            val change = (targetValue - current).coerceIn(0.0F, MAX_TRANSFORM_PROGRESS_PER_TICK)
            if (change <= 0) return
            val updated = current + change
            entityData.set(TRANSFORM_PROGRESS, updated)

            tickTransformExtras(current, updated)
        }
    }

    private fun tickTransformExtras(
        previousProgress: Float,
        currentProgress: Float,
    ) {
        TRANSFORM_EXTRAS_DATA.forEach { data ->
            if (!data.isTriggered(currentProgress) || data.isTriggered(previousProgress)) return@forEach
            triggerAnim(data.animControllerId, data.animId)
        }
    }

    override fun getHurtSound(source: DamageSource): SoundEvent = SoundEvents.PLAYER_HURT

    override fun getDeathSound(): SoundEvent = SoundEvents.PLAYER_DEATH

    data class TransformExtrasData(
        val animationTriggerThreshold: Double,
        val animControllerId: String,
        val animId: String,
        val hiddenBeforeTrigger: Set<String> = setOf(),
        val hiddenAfterTrigger: Set<String> = setOf(),
        val hiddenAfterFinish: Set<String> = setOf(),
    ) {
        fun isTriggered(progress: Float) = progress >= animationTriggerThreshold

        fun getHiddenBones(progress: Float, entity: BeastweaverEntity): Set<String> {
            if (!isTriggered(progress)) return hiddenBeforeTrigger
            if (hiddenAfterFinish.isEmpty() || entity.isControllerActive(animControllerId)) return hiddenAfterTrigger
            return hiddenAfterTrigger + hiddenAfterFinish
        }
    }

    fun getHiddenBones(): Set<String> {
        return TRANSFORM_EXTRAS_DATA.flatMap { it.getHiddenBones(transformProgress, this) }.toSet()
    }

    override fun getDefaultDimensions(pose: Pose): EntityDimensions {
        return super.getDefaultDimensions(pose).scale(getTransformDimensionsYScale())
    }

    private fun getTransformDimensionsYScale() = 1F + entityData.get(TRANSFORM_PROGRESS) * 0.065F

    override fun sanitizeScale(scale: Float): Float {
        return super.sanitizeScale(scale) * 1F + entityData.get(TRANSFORM_PROGRESS) * TRANSFORM_ADDITIONAL_SCALE
    }

    fun startRhinoCharge() {
        entityData.set(IS_RHINO_CHARGING, true)

        attributes.getInstance(Attributes.STEP_HEIGHT)?.also { it.addTransientModifier(RHINO_CHARGE_STEP_HEIGHT_MODIFIER) }
    }

    fun endRhinoCharge() {
        entityData.set(IS_RHINO_CHARGING, false)
        isRhinoChargeEnding = false
        rhinoChargeDuration = 0
        rhinoChargeEndDuration = 0
        attackDuration = 0

        attributes.getInstance(Attributes.STEP_HEIGHT)?.also { it.removeModifier(RHINO_CHARGE_STEP_HEIGHT_MODIFIER_ID) }
        attributes.getInstance(Attributes.MOVEMENT_SPEED)?.also { it.removeModifier(RHINO_CHARGE_MOVEMENT_SPEED_MODIFIER_ID) }
    }

    private fun startEndingRhinoCharge() {
        if (isRhinoChargeEnding) return

        isRhinoChargeEnding = true
        rhinoChargeEndDuration = 0

        val serverLevel = level() as? ServerLevel ?: return
        RHINO_CHARGE_END_SOUND_DATA.apply(serverLevel, this)

        triggerAnim(ATTACK_ANIM_CONTROLLER_ID, RHINO_CHARGE_END_ID)
    }

    private fun testRhinoChargeWallHit(
        serverLevel: ServerLevel,
    ): Boolean {
        if (!horizontalCollision) return false

        endRhinoCharge()

        RHINO_CHARGE_HIT_WALL_ATTACK_DAMAGE.apply(serverLevel, this, null)
        RHINO_CHARGE_HIT_WALL_PARTICLE_DATA.apply(serverLevel, this)
        RHINO_CHARGE_HIT_WALL_SOUND_DATA.apply(serverLevel, this)
        RHINO_CHARGE_END_HIT_WALL_SOUND_DATA.apply(serverLevel, this)

        blockedMovementTicks = 55
        attackDuration = 55
        triggerAnim(ATTACK_ANIM_CONTROLLER_ID, RHINO_CHARGE_HIT_WALL_ID)

        return true
    }

    private fun tickRhinoCharge() {
        if (!isRhinoCharging()) return
        rhinoChargeDuration++

        val level = level()
        if (level.isClientSide) tickRhinoChargeClient(level)
        else tickRhinoChargeServer(level as ServerLevel)
    }

    private fun tickRhinoChargeServer(serverLevel: ServerLevel) {
        if (isRhinoChargeEnding) rhinoChargeEndDuration++
        else if (rhinoChargeDuration > 200 || (rhinoChargeDuration > 40 && (target == null || !isFacingTowards(target!!)))) startEndingRhinoCharge()

        val speedMultiplier = getRhinoChargeSpeedMultiplier(rhinoChargeDuration, rhinoChargeEndDuration)
        if (isRhinoChargeEnding && speedMultiplier <= 0.1) {
            endRhinoCharge()
            return
        }
        if (testRhinoChargeWallHit(serverLevel)) return

        val moreMovementSpeed = speedMultiplier - 1.0
        val movementSpeedModifier = AttributeModifier(RHINO_CHARGE_MOVEMENT_SPEED_MODIFIER_ID, moreMovementSpeed, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
        attributes.getInstance(Attributes.MOVEMENT_SPEED)?.also { it.addOrUpdateTransientModifier(movementSpeedModifier) }

        if (speedMultiplier > 1.0 && tickCount % 2 == 0) RHINO_CHARGE_ATTACK_DAMAGE.apply(serverLevel, this, null)
    }

    private fun tickRhinoChargeClient(level: Level) {
        val horizontalMovement = position().subtract(oldPosition()).horizontalDistance()

        rhinoChargeStepSoundBuildup += horizontalMovement
        if (rhinoChargeStepSoundBuildup > 5) {
            RHINO_CHARGE_STEP_SOUND_DATA.applyClient(level, this, false)
            rhinoChargeStepSoundBuildup = 0.0
            rhinoChargeStepSoundRepeatTimer = 4

            RHINO_CHARGE_STEP_EXPLODE_SOUND_DATA.applyClient(level, this, false)
            spawnRhinoChargeStepExplodeParticles(level)
        }
        if (rhinoChargeStepSoundRepeatTimer > 0 && --rhinoChargeStepSoundRepeatTimer == 0) RHINO_CHARGE_STEP_SOUND_DATA.applyClient(level, this, false)

        rhinoChargeDustParticleBuildup += min(horizontalMovement * horizontalMovement, 1.0)
        while (rhinoChargeDustParticleBuildup > 0.5) {
            spawnRhinoChargeSmokeParticles(level)
            rhinoChargeDustParticleBuildup -= 0.5
        }
    }

    private fun spawnRhinoChargeSmokeParticles(level: Level) {
        level.addAlwaysVisibleParticle(
            ParticleTypes.CAMPFIRE_COSY_SMOKE,
            true,
            x + (random.nextDouble() - 0.5) * 1.5,
            y + random.nextDouble(),
            z + (random.nextDouble() - 0.5) * 1.5,
            0.03,
            0.05,
            0.03,
        )
        level.addAlwaysVisibleParticle(
            ParticleTypes.LARGE_SMOKE,
            true,
            x + (random.nextDouble() - 0.5) * 1.5,
            y + random.nextDouble(),
            z + (random.nextDouble() - 0.5) * 1.5,
            0.03,
            0.05,
            0.03,
        )
    }

    private fun spawnRhinoChargeStepExplodeParticles(level: Level) {
        level.addAlwaysVisibleParticle(
            ParticleTypes.EXPLOSION,
            true,
            x + (random.nextDouble() - 0.5),
            y + random.nextDouble() * 0.5,
            z + (random.nextDouble() - 0.5),
            0.0,
            0.0,
            0.0,
        )
    }

    private fun getRhinoChargeSpeedMultiplier(
        ticksSinceStart: Int,
        ticksSinceEnd: Int,
    ): Double {
        val accelerationTicks = ticksSinceStart - ticksSinceEnd
        val accelerationProgress = (accelerationTicks / 100.0).coerceIn(0.0, 1.0)
        val easedAcceleration = accelerationProgress * accelerationProgress * (3.0 - 2.0 * accelerationProgress)

        val decelerationProgress = (ticksSinceEnd / 40.0).coerceIn(0.0, 1.0)
        val easedDeceleration = decelerationProgress * decelerationProgress * (3.0 - 2.0 * decelerationProgress)

        return (0.5 + easedAcceleration * 1.25) * (1 - easedDeceleration)
    }

    fun isRhinoCharging() = entityData.get(IS_RHINO_CHARGING)

    fun getRhinoChargeDurationSeconds(tickProgress: Float) = if (isRhinoCharging()) (rhinoChargeDuration + tickProgress) / 20F else -1F

    override fun knockback(power: Double, xd: Double, zd: Double, source: DamageSource, damage: Float, comesFromEffect: Boolean) {
        if (isRhinoCharging()) return
        super.knockback(power, xd, zd, source, damage, comesFromEffect)
    }

    fun getRhinoChargeMaxYawChange() = if (!isRhinoCharging()) 360f else if (!isRhinoChargeEnding) 4f else 0f

    override fun deflection(
        projectile: Projectile,
    ): ProjectileDeflection {
        if (!isRhinoCharging()) return ProjectileDeflection.NONE

        val distanceVector = projectile.position().subtract(position())
        val yawToProj = distanceVector.getYaw()
        val yawDifference = abs(yBodyRot - yawToProj) % 360
        val angle = min(yawDifference, 360 - yawDifference)
        if (angle > RHINO_CHARGE_ARROW_DEFLECT_ANGLE) return ProjectileDeflection.NONE

        return ProjectileDeflection.AIM_DEFLECT
    }

    override fun die(source: DamageSource) {
        super.die(source)

        val level = level() as? ServerLevel ?: return
        DEATH_PARTICLE_DATA_SMOKE.apply(level, this)
        DEATH_PARTICLE_DATA__WHITE_SMOKE.apply(level, this)
        DEATH_PARTICLE_DATA_SPORES.apply(level, this)
        DEATH_SOUND_DATA.apply(level, this)
    }

    override fun setNoAi(flag: Boolean) {
        super.setNoAi(flag)
        bossEvent.isVisible = !flag
    }

    override fun startSeenByPlayer(player: ServerPlayer) {
        super.startSeenByPlayer(player)
        bossEvent.addPlayer(player)

        val payload = BossEventTypePayload(bossEvent.id, BossEventType.BEASTWEAVER)
        ServerPlayNetworking.send(player, payload)
    }

    override fun stopSeenByPlayer(player: ServerPlayer) {
        super.stopSeenByPlayer(player)
        bossEvent.removePlayer(player)
    }

    override fun addAdditionalSaveData(output: ValueOutput) {
        super.addAdditionalSaveData(output)
        output.putFloat(TRANSFORM_PROGRESS_ID, entityData.get(TRANSFORM_PROGRESS))

        addAttackCooldownsSaveData(output)
    }

    override fun readAdditionalSaveData(input: ValueInput) {
        super.readAdditionalSaveData(input)
        entityData.set(TRANSFORM_PROGRESS, input.getFloatOr(TRANSFORM_PROGRESS_ID, 0F))

        readAttackCooldownsSaveData(input)

        isNoGravity = false
    }
}