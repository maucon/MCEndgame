package de.fuballer.mcendgame.main.component.custom_attribute.effects.companion.wolf_companion

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asStringRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.configuration.RuntimeConfig
import de.fuballer.mcendgame.main.messaging.dungeon.WorldAttributeChangedEvent
import de.fuballer.mcendgame.main.messaging.misc.EquipmentChangeEvent
import de.fuballer.mcendgame.main.messaging.misc.PlayerAfterDimensionChangeEvent
import de.fuballer.mcendgame.main.messaging.misc.PlayerBeforeDimensionChangeEvent
import de.fuballer.mcendgame.main.messaging.misc.PlayerEntityDeathEvent
import de.fuballer.mcendgame.main.util.extension.SlotExtension.isOrIsChildOf
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.addAllyAuraStatusEffect
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.addEnemyAuraStatusEffect
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isCompanion
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.setCompanion
import de.fuballer.mcendgame.main.util.extension.mixin.WolfMixinExtension.setCollarColor
import de.fuballer.mcendgame.main.util.extension.mixin.WolfMixinExtension.setVariant
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.passive.WolfEntity
import net.minecraft.entity.passive.WolfVariant
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.TypeFilter

@Injectable
class WolfCompanionService {
    @Initializer
    fun onPlayerDisconnect() = ServerPlayConnectionEvents.DISCONNECT.register { handler, _ ->
        removeWolfCompanions(handler.player)
    }

    @EventSubscriber(sync = true)
    fun on(event: PlayerBeforeDimensionChangeEvent) {
        removeWolfCompanions(event.player, event.world)
    }

    @EventSubscriber(sync = true)
    fun on(event: PlayerAfterDimensionChangeEvent) {
        summonWolfCompanions(event.player, event.newWorld)
    }

    @EventSubscriber(sync = true)
    fun on(event: PlayerEntityDeathEvent) {
        if (event.isClient) return
        val serverWorld = event.world as? ServerWorld ?: return
        removeWolfCompanions(event.player, serverWorld)
    }

    // this also gets triggered by respawn and join
    @EventSubscriber(sync = true)
    fun on(event: EquipmentChangeEvent) {
        val player = event.entity as? PlayerEntity ?: return
        val attributeSlot = AttributeModifierSlot.forEquipmentSlot(event.slot)
        if (!hasApplicableAttribute(event.oldStack, attributeSlot) && !hasApplicableAttribute(event.newStack, attributeSlot)) return

        resummonWolfCompanions(player)
    }

    @EventSubscriber(sync = true)
    fun on(event: WorldAttributeChangedEvent) {
        if (event.attribute.type != CustomAttributeTypes.WOLF_COMPANION) return
        event.world.players.forEach {
            resummonWolfCompanions(it)
        }
    }

    private fun hasApplicableAttribute(stack: ItemStack, slot: AttributeModifierSlot) =
        stack.getCustomAttributes().filter { slot.isOrIsChildOf(it.slot) }.any { it.type == CustomAttributeTypes.WOLF_COMPANION }

    private fun resummonWolfCompanions(player: PlayerEntity) {
        removeWolfCompanions(player)
        summonWolfCompanions(player)
    }

    private fun summonWolfCompanions(
        player: PlayerEntity,
    ) {
        val world = player.entityWorld as? ServerWorld ?: return
        summonWolfCompanions(player, world)
    }

    private fun summonWolfCompanions(
        player: PlayerEntity,
        world: ServerWorld,
    ) {
        val attributes = player.getAllCustomAttributes()[CustomAttributeTypes.WOLF_COMPANION] ?: return

        val registry = world.registryManager.getOrThrow(RegistryKeys.WOLF_VARIANT)
        attributes.forEach { attribute ->
            val type = WolfCompanionType.getByName(attribute.rolls[0].asStringRoll().getValue()) ?: return@forEach
            summonWolfCompanion(player, world, type, registry)
        }
    }

    private fun summonWolfCompanion(
        player: PlayerEntity,
        world: ServerWorld,
        type: WolfCompanionType,
        registry: Registry<WolfVariant>,
    ) {
        val wolf = WolfEntity(EntityType.WOLF, world)
        wolf.setPosition(player.entityPos)

        wolf.setTamedBy(player)
        wolf.setCompanion()

        for (effect in type.allyAuraStatusEffects) {
            wolf.addAllyAuraStatusEffect(effect)
        }
        for (effect in type.enemyAuraStatusEffects) {
            wolf.addEnemyAuraStatusEffect(effect)
        }
        for (effectType in type.selfEffects.keys) {
            wolf.addStatusEffect(
                StatusEffectInstance(
                    effectType,
                    StatusEffectInstance.INFINITE,
                    type.selfEffects[effectType] ?: 0,
                    true,
                    true
                )
            )
        }
        type.applyExtras(wolf)

        val variantEntry = registry.getOrThrow(type.variant)
        wolf.setVariant(variantEntry)
        wolf.setCollarColor(type.color)

        wolf.getAttributeInstance(EntityAttributes.SCALE)?.baseValue = type.scale

        wolf.isInvulnerable = true

        RuntimeConfig.SERVER.execute { world.spawnEntity(wolf) }
    }

    private fun removeWolfCompanions(
        player: PlayerEntity,
    ) {
        val world = player.entityWorld as? ServerWorld ?: return
        removeWolfCompanions(player, world)
    }

    private fun removeWolfCompanions(
        player: PlayerEntity,
        world: ServerWorld,
    ) {
        val wolfs = world.getEntitiesByType(TypeFilter.instanceOf(WolfEntity::class.java)) { wolf ->
            wolf.isCompanion() && wolf.owner == player
        }

        wolfs.forEach {
            if (it == null || !it.isAlive) return@forEach
            it.remove(Entity.RemovalReason.UNLOADED_WITH_PLAYER)
        }
    }
}