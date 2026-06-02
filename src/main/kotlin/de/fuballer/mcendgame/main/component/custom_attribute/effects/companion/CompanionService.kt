package de.fuballer.mcendgame.main.component.custom_attribute.effects.companion

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.entity.custom.goals.predicates.ShouldBeAttackedByCompanionsPredicate
import de.fuballer.mcendgame.main.messaging.dungeon.WorldAttributeChangedEvent
import de.fuballer.mcendgame.main.messaging.misc.EquipmentChangeEvent
import de.fuballer.mcendgame.main.messaging.misc.PlayerAfterDimensionChangeEvent
import de.fuballer.mcendgame.main.messaging.misc.PlayerBeforeDimensionChangeEvent
import de.fuballer.mcendgame.main.messaging.misc.PlayerEntityDeathEvent
import de.fuballer.mcendgame.main.messaging.server.ServerEndTickEvent
import de.fuballer.mcendgame.main.util.extension.SlotExtension.isOrIsChildOf
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.getTargetSelector
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isCompanion
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.setCompanion
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.ai.goal.ActiveTargetGoal
import net.minecraft.entity.ai.goal.AttackWithOwnerGoal
import net.minecraft.entity.ai.goal.TrackOwnerAttackerGoal
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.passive.TameableEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.TypeFilter
import java.util.*

@Injectable
class CompanionService {
    val toSummon: MutableMap<UUID, MutableSet<CompanionType>> = mutableMapOf()

    @Initializer
    fun onPlayerDisconnect() = ServerPlayConnectionEvents.DISCONNECT.register { handler, _ ->
        removeCompanions(handler.player)
    }

    @EventSubscriber(sync = true)
    fun on(event: PlayerEntityDeathEvent) {
        val player = event.player as? ServerPlayerEntity ?: return
        removeCompanions(player)
    }

    @EventSubscriber(sync = true)
    fun on(event: PlayerBeforeDimensionChangeEvent) {
        val player = event.player as? ServerPlayerEntity ?: return
        removeCompanions(player)
    }

    @EventSubscriber(sync = true)
    fun on(event: PlayerAfterDimensionChangeEvent) {
        val id = event.player.uuid
        toSummon[id] = CompanionType.entries.toMutableSet()
    }

    @EventSubscriber(sync = true)
    fun on(event: WorldAttributeChangedEvent) {
        val companionTypes = CompanionType.entries.filter { it.attribute == event.attribute.type }
        event.world.players.forEach {
            val id = it.uuid
            val set = toSummon[id] ?: mutableSetOf()
            set.addAll(companionTypes)
            toSummon[id] = set
        }
    }

    // this also gets triggered by respawn and join
    @EventSubscriber(sync = true)
    fun on(event: EquipmentChangeEvent) {
        val player = event.entity as? PlayerEntity ?: return
        val id = player.uuid
        val attributeSlot = AttributeModifierSlot.forEquipmentSlot(event.slot)

        val set = toSummon[id] ?: mutableSetOf()
        set.addAll(getItemStackCompanions(event.oldStack, attributeSlot))
        set.addAll(getItemStackCompanions(event.newStack, attributeSlot))

        toSummon[id] = set
    }

    @EventSubscriber(sync = true)
    fun on(event: ServerEndTickEvent) {
        val iterator = toSummon.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val id = entry.key
            val types = entry.value
            iterator.remove()

            event.server.playerManager.getPlayer(id)?.let { player ->
                types.forEach { type -> resummon(player, type) }
            }
        }
    }

    private fun getItemStackCompanions(
        itemStack: ItemStack,
        slot: AttributeModifierSlot,
    ): Set<CompanionType> {
        val attributes = itemStack.getCustomAttributes().filter { slot.isOrIsChildOf(it.slot) }
        return CompanionType.entries.filter { entry -> attributes.any { attr -> attr.type == entry.attribute } }.toSet()
    }

    private fun resummon(
        player: ServerPlayerEntity,
        type: CompanionType,
    ) {
        removeCompanions(player, type.entityClass)
        summonAllOfType(player, type)
    }
    
    fun removeCompanions(player: ServerPlayerEntity) {
        CompanionType.entries.forEach { removeCompanions(player, it.entityClass) }
    }

    fun removeCompanions(
        player: ServerPlayerEntity,
        type: Class<out TameableEntity>,
    ) {
        val world = player.entityWorld as? ServerWorld ?: return

        val companions = world.getEntitiesByType(TypeFilter.instanceOf(type)) {
            it.isCompanion() && it.owner == player
        }

        companions.forEach {
            if (it == null || !it.isAlive) return@forEach
            it.remove(Entity.RemovalReason.UNLOADED_WITH_PLAYER)
        }
    }

    private fun summonAllOfType(
        player: ServerPlayerEntity,
        type: CompanionType,
    ) {
        player.getAllCustomAttributes()[type.attribute]?.forEach { summonAllFromAttribute(player, type, it) }
    }

    private fun summonAllFromAttribute(
        player: ServerPlayerEntity,
        type: CompanionType,
        attribute: CustomAttribute,
    ) {
        val count = type.getCount(attribute)
        repeat(count) { summonFromAttribute(player, type, attribute) }
    }

    private fun summonFromAttribute(
        player: ServerPlayerEntity,
        type: CompanionType,
        attribute: CustomAttribute,
    ) {
        val world = player.entityWorld
        val companion = type.entityType.create(world, SpawnReason.MOB_SUMMONED) as? TameableEntity ?: return

        companion.setPosition(player.entityPos)
        companion.setTamedBy(player)
        companion.setCompanion()
        companion.isInvulnerable = true
        companion.getAttributeInstance(EntityAttributes.FOLLOW_RANGE)?.baseValue = 24.0

        addGoals(companion)

        type.applyOther(companion, attribute)

        world.spawnEntity(companion)
    }

    fun addGoals(entity: TameableEntity) {
        val targetSelector = entity.getTargetSelector()

        targetSelector.goals
            .map { it.goal }
            .toList()
            .forEach(targetSelector::remove)

        targetSelector.add(1, TrackOwnerAttackerGoal(entity))
        targetSelector.add(2, AttackWithOwnerGoal(entity))
        targetSelector.add(
            3,
            ActiveTargetGoal(
                entity,
                LivingEntity::class.java,
                10,
                false,
                false,
                ShouldBeAttackedByCompanionsPredicate(),
            )
        )
    }
}