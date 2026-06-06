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
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.entity.EntityTypeTest
import java.util.*

@Injectable
class CompanionService {
    private val toSummon: MutableMap<UUID, MutableSet<CompanionType>> = mutableMapOf()

    @Initializer
    fun onPlayerDisconnect() = ServerPlayConnectionEvents.DISCONNECT.register { handler, _ ->
        removeCompanions(handler.player)
    }

    @EventSubscriber(sync = true)
    fun on(event: PlayerEntityDeathEvent) {
        val player = event.player as? ServerPlayer ?: return
        removeCompanions(player)
    }

    @EventSubscriber(sync = true)
    fun on(event: PlayerBeforeDimensionChangeEvent) {
        val player = event.player as? ServerPlayer ?: return
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
        event.world.players().forEach {
            val id = it.uuid
            val set = toSummon[id] ?: mutableSetOf()
            set.addAll(companionTypes)
            toSummon[id] = set
        }
    }

    // this also gets triggered by respawn and join
    @EventSubscriber(sync = true)
    fun on(event: EquipmentChangeEvent) {
        val player = event.entity as? Player ?: return
        val id = player.uuid
        val attributeSlot = EquipmentSlotGroup.bySlot(event.slot)

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

            event.server.playerList.getPlayer(id)?.let { player ->
                types.forEach { type -> resummon(player, type) }
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
        player: ServerPlayer,
        type: CompanionType,
    ) {
        removeCompanions(player, type.entityClass)
        summonAllOfType(player, type)
    }

    fun removeCompanions(player: ServerPlayer) {
        CompanionType.entries.forEach { removeCompanions(player, it.entityClass) }
    }

    fun removeCompanions(
        player: ServerPlayer,
        type: Class<out TamableAnimal>,
    ) {
        val world = player.level()

        val companions = world.getEntities(EntityTypeTest.forClass(type)) {
            it.isCompanion() && it.owner == player
        }

        companions.forEach {
            if (!it.isAlive) return@forEach
            it.remove(Entity.RemovalReason.UNLOADED_WITH_PLAYER)
        }
    }

    private fun summonAllOfType(
        player: ServerPlayer,
        type: CompanionType,
    ) {
        player.getAllCustomAttributes()[type.attribute]?.forEach { summonAllFromAttribute(player, type, it) }
    }

    private fun summonAllFromAttribute(
        player: ServerPlayer,
        type: CompanionType,
        attribute: CustomAttribute,
    ) {
        val count = type.getCount(attribute)
        repeat(count) { summonFromAttribute(player, type, attribute) }
    }

    private fun summonFromAttribute(
        player: ServerPlayer,
        type: CompanionType,
        attribute: CustomAttribute,
    ) {
        val world = player.level()
        val companion = type.entityType.create(world, EntitySpawnReason.MOB_SUMMONED) ?: return

        companion.setPos(player.position())
        companion.tame(player)
        companion.setCompanion()
        companion.isInvulnerable = true
        companion.getAttribute(Attributes.FOLLOW_RANGE)?.baseValue = 24.0

        addGoals(companion)

        type.applyOther(companion, attribute)

        world.addFreshEntity(companion)
    }

    fun addGoals(entity: TamableAnimal) {
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
                ShouldBeAttackedByCompanionsPredicate(),
            )
        )
    }
}