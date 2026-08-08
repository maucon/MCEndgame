package de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver.beastweaver_vine

import com.geckolib.animatable.GeoEntity
import com.geckolib.animatable.instance.AnimatableInstanceCache
import com.geckolib.animatable.manager.AnimatableManager
import com.geckolib.util.GeckoLibUtil
import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.level.Level
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import java.util.*

class BeastweaverVineEntity(
    type: EntityType<out BeastweaverVineEntity>,
    level: Level,
) : Mob(type, level), GeoEntity, Enemy, OwnableEntity {
    constructor(level: Level) : this(CustomEntities.BEASTWEAVER_VINE, level)

    companion object {
        fun createAttributes(): AttributeSupplier.Builder {
            return createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5)
                .add(Attributes.ARMOR, 0.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
        }

        val DATA_OWNERUUID_ID = SynchedEntityData.defineId(BeastweaverVineEntity::class.java, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE)
    }

    private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)
    override fun getAnimatableInstanceCache() = cache

    override fun defineSynchedData(entityData: SynchedEntityData.Builder) {
        super.defineSynchedData(entityData)
        entityData.define(DATA_OWNERUUID_ID, Optional.empty())
    }

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {

    }

    override fun baseTick() {
        super.baseTick()

        val level = level() as? ServerLevel ?: return
        val owner = owner
        if (owner == null || !owner.isAlive) kill(level)
    }

    fun setOwner(owner: LivingEntity) {
        val reference = EntityReference.of(owner)
        setOwnerReference(reference)
    }

    fun setOwnerReference(owner: EntityReference<LivingEntity>?) {
        entityData.set(DATA_OWNERUUID_ID, Optional.ofNullable(owner))
    }

    override fun getOwnerReference(): EntityReference<LivingEntity>? = entityData.get(DATA_OWNERUUID_ID).orElse(null)

    override fun addAdditionalSaveData(output: ValueOutput) {
        super.addAdditionalSaveData(output)
        EntityReference.store(ownerReference, output, "Owner")
    }

    override fun readAdditionalSaveData(input: ValueInput) {
        super.readAdditionalSaveData(input)
        val owner = EntityReference.readWithOldOwnerConversion<LivingEntity>(input, "Owner", level())
        if (owner == null) entityData.set(DATA_OWNERUUID_ID, Optional.empty())
        else entityData.set(DATA_OWNERUUID_ID, Optional.of(owner))
    }
}