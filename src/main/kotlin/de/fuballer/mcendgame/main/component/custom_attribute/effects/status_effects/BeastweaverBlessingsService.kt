package de.fuballer.mcendgame.main.component.custom_attribute.effects.status_effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.AttributeType
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.status_effect.CustomStatusEffects
import de.fuballer.mcendgame.main.messaging.misc.EquipmentChangeEvent
import de.fuballer.mcendgame.main.messaging.server.ServerEndTickEvent
import de.fuballer.mcendgame.main.util.extension.SlotExtension.isOrIsChildOf
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.Avatar
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.Mob
import net.minecraft.world.item.ItemStack
import java.util.*

private val EFFECTS = mapOf<AttributeType, Holder<MobEffect>>(
    CustomAttributeTypes.BLESSING_OF_THE_BEAR to CustomStatusEffects.BLESSING_OF_THE_BEAR,
    CustomAttributeTypes.BLESSING_OF_THE_EAGLE to CustomStatusEffects.BLESSING_OF_THE_EAGLE,
    CustomAttributeTypes.BLESSING_OF_THE_MAMMOTH to CustomStatusEffects.BLESSING_OF_THE_MAMMOTH,
    CustomAttributeTypes.BLESSING_OF_THE_RHINO to CustomStatusEffects.BLESSING_OF_THE_RHINO,
    CustomAttributeTypes.BLESSING_OF_THE_SERPENT to CustomStatusEffects.BLESSING_OF_THE_SERPENT,
    CustomAttributeTypes.BLESSING_OF_THE_STAG to CustomStatusEffects.BLESSING_OF_THE_STAG,
    CustomAttributeTypes.BLESSING_OF_THE_WOLF to CustomStatusEffects.BLESSING_OF_THE_WOLF,
)

@Injectable
class BeastweaverBlessingsService {
    private val toUpdateBlessings: MutableMap<UUID, ToUpdateBlessingsData> = mutableMapOf()

    private data class ToUpdateBlessingsData(
        val level: ServerLevel,
        val blessings: MutableSet<AttributeType>,
    )

    @EventSubscriber(sync = true)
    fun on(event: EquipmentChangeEvent) {
        val level = event.entity.level() as? ServerLevel ?: return

        val id = event.entity.uuid
        val slot = EquipmentSlotGroup.bySlot(event.slot)

        val data = toUpdateBlessings[id] ?: ToUpdateBlessingsData(level, mutableSetOf())
        data.blessings.addAll(getPresentBlessingAttributes(event.oldStack, slot))
        data.blessings.addAll(getPresentBlessingAttributes(event.newStack, slot))
        toUpdateBlessings[id] = data
    }

    private fun getPresentBlessingAttributes(
        stack: ItemStack,
        slot: EquipmentSlotGroup,
    ) = stack.getCustomAttributes()
        .filter { slot.isOrIsChildOf(it.slot) }
        .filter { EFFECTS.containsKey(it.type) }
        .map { it.type }

    @EventSubscriber(sync = true)
    fun on(event: ServerEndTickEvent) {
        val iterator = toUpdateBlessings.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val id = entry.key
            val data = entry.value
            iterator.remove()

            data.level.getEntity(id)?.let { updateBlessings(it, data.blessings) }
        }
    }

    private fun updateBlessings(
        entity: Entity,
        blessings: Set<AttributeType>,
    ) {
        if (entity !is Mob && entity !is Avatar) return

        val attributes = entity.getAllCustomAttributes()
        blessings.forEach { blessing ->
            val effect = EFFECTS[blessing] ?: return@forEach
            entity.removeEffect(effect)

            val blessingLevel = attributes[blessing]?.size ?: return@forEach
            val amplifier = (blessingLevel - 1).coerceIn(MobEffectInstance.MIN_AMPLIFIER, MobEffectInstance.MAX_AMPLIFIER)
            val effectInstance = MobEffectInstance(effect, MobEffectInstance.INFINITE_DURATION, amplifier, false, true, true)
            entity.addEffect(effectInstance)
        }
    }
}