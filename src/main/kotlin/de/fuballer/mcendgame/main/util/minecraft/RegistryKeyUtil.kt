package de.fuballer.mcendgame.main.util.minecraft

import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.equipment.EquipmentAsset
import net.minecraft.world.item.equipment.EquipmentAssets
import net.minecraft.world.level.block.Block

object RegistryKeyUtil {
    fun createItemKey(name: String): ResourceKey<Item> = createKey(Registries.ITEM, name)

    fun createBlockKey(name: String): ResourceKey<Block> = createKey(Registries.BLOCK, name)

    fun createEntityKey(name: String): ResourceKey<EntityType<*>> = createKey(Registries.ENTITY_TYPE, name)

    fun createDataComponentTypeKey(name: String): ResourceKey<DataComponentType<*>> = createKey(Registries.DATA_COMPONENT_TYPE, name)

    fun createEquipmentAssetKey(name: String): ResourceKey<EquipmentAsset> = createKey(EquipmentAssets.ROOT_ID, name)

    private fun <T : Any, R : Registry<T>> createKey(registryKey: ResourceKey<R>, name: String): ResourceKey<T> =
        ResourceKey.create(registryKey, IdentifierUtil.default(name))
}