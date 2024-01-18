package de.fuballer.mcendgame.domain.persistent_data

import de.fuballer.mcendgame.component.map_device.MapDeviceAction
import de.fuballer.mcendgame.domain.entity.CustomEntityType
import de.fuballer.mcendgame.domain.item.CustomItemType
import de.fuballer.mcendgame.domain.persistent_data.types.*
import de.fuballer.mcendgame.util.PluginUtil.createNamespacedKey
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataType

object TypeKeys {
    // entities
    val DROP_EQUIPMENT = TypeKey(createNamespacedKey("drop-equipment"), PersistentDataType.BOOLEAN)
    val MAP_TIER = TypeKey(createNamespacedKey("map-tier"), PersistentDataType.INTEGER)
    val IS_MINION = TypeKey(createNamespacedKey("is-minion"), PersistentDataType.BOOLEAN)
    val HIDE_EQUIPMENT = TypeKey(createNamespacedKey("hide-equipment"), PersistentDataType.BOOLEAN)
    val CUSTOM_ENTITY_TYPE = TypeKey(createNamespacedKey("custom-entity-type"), PersistentObjectClass(CustomEntityType::class.java))
    val IS_ENEMY = TypeKey(createNamespacedKey("is-enemy"), PersistentDataType.BOOLEAN)
    val MINIONS = TypeKey(createNamespacedKey("minions"), PersistentUuidSet)

    // player
    val LAST_MAP_DEVICE = TypeKey(createNamespacedKey("last-map-device"), PersistentUuid)
    val ARTIFACTS = TypeKey(createNamespacedKey("artifacts"), PersistentArtifactList)

    // items
    val CUSTOM_ITEM_TYPE = TypeKey(createNamespacedKey("custom-entity-type"), PersistentObjectClass(CustomItemType::class.java))
    val ATTRIBUTES = TypeKey(createNamespacedKey("attributes"), PersistentRolledAttributeList)
    val UNMODIFIABLE = TypeKey(createNamespacedKey("unmodifiable"), PersistentDataType.BOOLEAN)
    val CORRUPTION_ROUNDS = TypeKey(createNamespacedKey("corruption-rounds"), PersistentDataType.INTEGER)
    val MAP_DEVICE = TypeKey(createNamespacedKey("map-device"), PersistentDataType.BOOLEAN)
    val MAP_DEVICE_ACTION = TypeKey(createNamespacedKey("map-device-action"), PersistentEnum(MapDeviceAction::class))
    val ARTIFACT = TypeKey(createNamespacedKey("artifact"), PersistentArtifact)

    class TypeKey<T>(
        val key: NamespacedKey,
        val dataType: PersistentDataType<*, T>
    )
}