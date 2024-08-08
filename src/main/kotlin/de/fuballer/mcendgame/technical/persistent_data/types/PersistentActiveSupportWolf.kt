package de.fuballer.mcendgame.technical.persistent_data.types

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent.SlotType
import de.fuballer.mcendgame.component.item.attribute.effects.summon_suport_wolf.ActiveSupportWolf
import de.fuballer.mcendgame.component.item.attribute.effects.summon_suport_wolf.SupportWolfType
import de.fuballer.mcendgame.technical.persistent_data.types.generic.PersistentEnum
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

private val TYPE_KEY = PluginUtil.createNamespacedKey("type")
private val SLOT_KEY = PluginUtil.createNamespacedKey("slot")

object PersistentActiveSupportWolf : PersistentDataType<PersistentDataContainer, ActiveSupportWolf> {
    override fun getPrimitiveType() = PersistentDataContainer::class.java

    override fun getComplexType() = ActiveSupportWolf::class.java

    override fun toPrimitive(complex: ActiveSupportWolf, context: PersistentDataAdapterContext): PersistentDataContainer {
        val container = context.newPersistentDataContainer()

        container.set(TYPE_KEY, PersistentEnum(SupportWolfType::class), complex.type)
        container.set(SLOT_KEY, PersistentEnum(SlotType::class), complex.slot)

        return container
    }

    override fun fromPrimitive(primitive: PersistentDataContainer, context: PersistentDataAdapterContext): ActiveSupportWolf {
        val type = primitive.get(TYPE_KEY, PersistentEnum(SupportWolfType::class))!!
        val slot = primitive.get(SLOT_KEY, PersistentEnum(SlotType::class))!!

        return ActiveSupportWolf(type, slot)
    }
}