package de.fuballer.mcendgame.main.component.analytics

import com.mojang.serialization.JsonOps
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

object AnalyticsUtil {
    const val UNKNOWN = "unknown"

    fun toPayloadItem(itemStack: ItemStack): PayloadItem? {
        if (itemStack.isEmpty) return null
        val id = BuiltInRegistries.ITEM.getKey(itemStack.item).toString()
        val enchants = itemStack.get(DataComponents.ENCHANTMENTS)
            ?.entrySet()
            ?.associate {
                it.key.unwrapKey().map { k -> k.identifier().toString() }.orElse(UNKNOWN)!! to it.intValue
            }
            ?: emptyMap()
        val customAttributes = CustomAttribute.CODEC.listOf()
            .encodeStart(JsonOps.INSTANCE, itemStack.getCustomAttributes())
            .getOrThrow()
            .asJsonArray
            .also { array -> array.forEach { it.asJsonObject.remove("id") } }
        return PayloadItem(id, enchants, customAttributes)
    }

    fun getArmorItems(entity: LivingEntity): List<PayloadItem?> = listOf(
        toPayloadItem(entity.getItemBySlot(EquipmentSlot.HEAD)),
        toPayloadItem(entity.getItemBySlot(EquipmentSlot.CHEST)),
        toPayloadItem(entity.getItemBySlot(EquipmentSlot.LEGS)),
        toPayloadItem(entity.getItemBySlot(EquipmentSlot.FEET)),
    )

    fun getMainhandItems(entity: LivingEntity) = toPayloadItem(entity.getItemBySlot(EquipmentSlot.MAINHAND))
    fun getOffhandItems(entity: LivingEntity) = toPayloadItem(entity.getItemBySlot(EquipmentSlot.OFFHAND))

    fun getHotbarItems(player: Player) = (0..8).map { toPayloadItem(player.inventory.getItem(it)) }

    fun getActiveEffects(entity: LivingEntity) = entity.activeEffects.associate {
        it.effect.unwrapKey().map { k -> k.identifier().toString() }.orElse(UNKNOWN)!! to it.amplifier
    }
}