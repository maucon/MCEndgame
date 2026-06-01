package de.fuballer.mcendgame.main.component.custom_attribute.effects.companion.spiderling_companions

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asIntRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import de.fuballer.mcendgame.main.component.entity.custom.entities.spiderling.SpiderlingEntity
import de.fuballer.mcendgame.main.functional.scheduler.Scheduler
import de.fuballer.mcendgame.main.messaging.dungeon.WorldAttributeChangedEvent
import de.fuballer.mcendgame.main.messaging.misc.EquipmentChangeEvent
import de.fuballer.mcendgame.main.messaging.misc.PlayerAfterDimensionChangeEvent
import de.fuballer.mcendgame.main.messaging.misc.PlayerBeforeDimensionChangeEvent
import de.fuballer.mcendgame.main.messaging.misc.PlayerEntityDeathEvent
import de.fuballer.mcendgame.main.messaging.server.ServerEndTickEvent
import de.fuballer.mcendgame.main.util.extension.SlotExtension.isOrIsChildOf
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isCompanion
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.setCompanion
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.TypeFilter
import java.util.*

@Injectable
class SpiderlingCompanionsService(
    val scheduler: Scheduler
) {
    val toSummon: MutableSet<UUID> = mutableSetOf()

    @Initializer
    fun onPlayerDisconnect() = ServerPlayConnectionEvents.DISCONNECT.register { handler, _ ->
        removeSpiderlings(handler.player)
    }

    @EventSubscriber(sync = true)
    fun on(event: PlayerBeforeDimensionChangeEvent) {
        removeSpiderlings(event.player)
    }

    @EventSubscriber(sync = true)
    fun on(event: PlayerAfterDimensionChangeEvent) {
        toSummon += event.player.uuid
    }

    @EventSubscriber(sync = true)
    fun on(event: PlayerEntityDeathEvent) {
        removeSpiderlings(event.player)
    }

    // this also gets triggered by respawn and join
    @EventSubscriber(sync = true)
    fun on(event: EquipmentChangeEvent) {
        val player = event.entity as? PlayerEntity ?: return
        val attributeSlot = AttributeModifierSlot.forEquipmentSlot(event.slot)
        if (!hasApplicableAttribute(event.oldStack, attributeSlot) && !hasApplicableAttribute(event.newStack, attributeSlot)) return

        toSummon += player.uuid
    }

    @EventSubscriber(sync = true)
    fun on(event: WorldAttributeChangedEvent) {
        if (event.attribute.type != CustomAttributeTypes.SPIDERLING_COMPANIONS) return

        toSummon.addAll(event.world.players.map { it.uuid })
    }

    @EventSubscriber(sync = true)
    fun on(event: ServerEndTickEvent) {
        val iterator = toSummon.iterator()
        while (iterator.hasNext()) {
            val uuid = iterator.next()
            iterator.remove()

            event.server.playerManager.getPlayer(uuid)?.let {
                resummonSpiderlings(it)
            }
        }
    }

    private fun hasApplicableAttribute(stack: ItemStack, slot: AttributeModifierSlot) =
        stack.getCustomAttributes().filter { slot.isOrIsChildOf(it.slot) }.any { it.type == CustomAttributeTypes.SPIDERLING_COMPANIONS }

    private fun resummonSpiderlings(player: PlayerEntity) {
        removeSpiderlings(player)
        summonSpiderlings(player)
    }

    private fun summonSpiderlings(
        player: PlayerEntity,
    ) {
        val world = player.entityWorld as? ServerWorld ?: return
        val attributes = player.getAllCustomAttributes()[CustomAttributeTypes.SPIDERLING_COMPANIONS] ?: return

        val count = attributes.sumOf { it.rolls[0].asIntRoll().getValue() }
        repeat(count) { summonSpiderling(player, world) }
    }

    private fun summonSpiderling(
        player: PlayerEntity,
        world: ServerWorld,
    ) {
        val spiderling = SpiderlingEntity(CustomEntities.SPIDERLING, world)
        spiderling.setPosition(player.entityPos)

        spiderling.setTamedBy(player)
        spiderling.setCompanion()
        spiderling.isInvulnerable = true

        world.spawnEntity(spiderling)
    }

    private fun removeSpiderlings(
        player: PlayerEntity,
    ) {
        val world = player.entityWorld as? ServerWorld ?: return
        val spiderlings = world.getEntitiesByType(TypeFilter.instanceOf(SpiderlingEntity::class.java)) { spiderling ->
            spiderling.owner == player && spiderling.isCompanion()
        }

        spiderlings.forEach {
            if (it == null || !it.isAlive) return@forEach
            it.remove(Entity.RemovalReason.UNLOADED_WITH_PLAYER)
        }
    }
}