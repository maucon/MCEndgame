package de.fuballer.mcendgame.main.component.custom_attribute.effects.companion

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.entity.custom.goals.predicates.ShouldBeAttackedByCompanionsPredicate
import de.fuballer.mcendgame.main.messaging.dungeon.WorldAttributeChangedEvent
import de.fuballer.mcendgame.main.messaging.misc.EquipmentChangeEvent
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDeathEvent
import de.fuballer.mcendgame.main.messaging.misc.PlayerAfterDimensionChangeEvent
import de.fuballer.mcendgame.main.messaging.misc.PlayerBeforeDimensionChangeEvent
import de.fuballer.mcendgame.main.messaging.server.ServerEndTickEvent
import de.fuballer.mcendgame.main.util.extension.SlotExtension.isOrIsChildOf
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.getTargetSelector
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isCompanion
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isDungeonEnemy
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.setCompanion
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.setDungeonEnemy
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.entity.EntityTypeTest
import java.util.*

@Injectable
class CompanionService {
    private val toSummon: MutableMap<UUID, ToSummonData> = mutableMapOf()

    private data class ToSummonData(
        val level: ServerLevel,
        val companionTypes: MutableSet<CompanionType>,
    )

    @Initializer
    fun onPlayerDisconnect() = ServerPlayConnectionEvents.DISCONNECT.register { handler, _ ->
        removeCompanions(handler.player)
    }

    @Initializer
    fun onEntityUnload() = ServerEntityEvents.ENTITY_UNLOAD.register { entity, _ ->
        val entity = entity as? LivingEntity ?: return@register
        removeCompanions(entity)
    }

    @EventSubscriber(sync = true)
    fun on(event: LivingEntityDeathEvent) {
        if (event.isClient) return
        removeCompanions(event.entity)
    }

    @EventSubscriber(sync = true)
    fun on(event: PlayerBeforeDimensionChangeEvent) {
        val player = event.player as? ServerPlayer ?: return
        removeCompanions(player)
    }

    @EventSubscriber(sync = true)
    fun on(event: PlayerAfterDimensionChangeEvent) {
        val id = event.player.uuid
        toSummon[id] = ToSummonData(event.newWorld, CompanionType.entries.toMutableSet())
    }

    @EventSubscriber(sync = true)
    fun on(event: WorldAttributeChangedEvent) {
        val companionTypes = CompanionType.entries.filter { it.attribute == event.attribute.type }
        event.world.players().forEach {
            val id = it.uuid
            val data = toSummon[id] ?: ToSummonData(it.level(), mutableSetOf())
            data.companionTypes.addAll(companionTypes)
            toSummon[id] = data
        }
    }

    // this also gets triggered by player respawn and join, entity load
    @EventSubscriber(sync = true)
    fun on(event: EquipmentChangeEvent) {
        val level = event.entity.level() as? ServerLevel ?: return
        val id = event.entity.uuid
        val attributeSlot = EquipmentSlotGroup.bySlot(event.slot)

        val data = toSummon[id] ?: ToSummonData(level, mutableSetOf())
        data.companionTypes.addAll(getItemStackCompanions(event.oldStack, attributeSlot))
        data.companionTypes.addAll(getItemStackCompanions(event.newStack, attributeSlot))

        toSummon[id] = data
    }

    @EventSubscriber(sync = true)
    fun on(event: ServerEndTickEvent) {
        val iterator = toSummon.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val id = entry.key
            val data = entry.value
            iterator.remove()

            data.level.getEntity(id)?.let { entity ->
                if (entity !is LivingEntity) return@let
                data.companionTypes.forEach { type -> resummon(entity, type) }
            }
        }
    }

    private fun getItemStackCompanions(
        itemStack: ItemStack,
        slot: EquipmentSlotGroup,
    ): Set<CompanionType> {
        val attributes = itemStack.getCustomAttributes().filter { slot.isOrIsChildOf(it.slot) }
        return CompanionType.entries.filter { entry -> attributes.any { attr -> attr.type == entry.attribute } }.toSet()
    }

    private fun resummon(
        owner: LivingEntity,
        type: CompanionType,
    ) {
        removeCompanions(owner, type.entityClass)
        summonAllOfType(owner, type)
    }

    fun removeCompanions(owner: LivingEntity) {
        CompanionType.entries.forEach { removeCompanions(owner, it.entityClass) }
    }

    fun removeCompanions(
        owner: LivingEntity,
        type: Class<out TamableAnimal>,
    ) {
        val world = owner.level() as? ServerLevel ?: return

        val companions = world.getEntities(EntityTypeTest.forClass(type)) {
            it.isCompanion() && it.owner == owner
        }

        companions.forEach {
            if (!it.isAlive) return@forEach
            it.remove(Entity.RemovalReason.DISCARDED)
        }
    }

    private fun summonAllOfType(
        owner: LivingEntity,
        type: CompanionType,
    ) {
        owner.getAllCustomAttributes()[type.attribute]?.forEach { summonAllFromAttribute(owner, type, it) }
    }

    private fun summonAllFromAttribute(
        owner: LivingEntity,
        type: CompanionType,
        attribute: CustomAttribute,
    ) {
        val count = type.getCount(attribute)
        repeat(count) { summonFromAttribute(owner, type, attribute) }
    }

    private fun summonFromAttribute(
        owner: LivingEntity,
        type: CompanionType,
        attribute: CustomAttribute,
    ) {
        val world = owner.level()
        val companion = type.entityType.create(world, EntitySpawnReason.MOB_SUMMONED) ?: return

        companion.setPos(owner.position())
        companion.setTame(true, false)
        companion.owner = owner
        companion.setCompanion()
        companion.isInvulnerable = true
        companion.getAttribute(Attributes.FOLLOW_RANGE)?.baseValue = 24.0
        if (owner.isDungeonEnemy()) companion.setDungeonEnemy()

        updateTargetGoals(owner, companion)

        type.applyOther(companion, attribute)

        world.addFreshEntity(companion)
    }

    fun updateTargetGoals(
        owner: LivingEntity,
        entity: TamableAnimal,
    ) {
        val targetSelector = entity.getTargetSelector()

        targetSelector.availableGoals
            .map { it.goal }
            .toList()
            .forEach(targetSelector::removeGoal)

        targetSelector.addGoal(1, OwnerHurtByTargetGoal(entity))
        targetSelector.addGoal(2, OwnerHurtTargetGoal(entity))
        targetSelector.addGoal(
            3,
            NearestAttackableTargetGoal(
                entity,
                LivingEntity::class.java,
                10,
                false,
                false,
                ShouldBeAttackedByCompanionsPredicate(owner.isDungeonEnemy()),
            )
        )
    }
}