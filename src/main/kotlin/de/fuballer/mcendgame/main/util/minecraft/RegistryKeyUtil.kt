package de.fuballer.mcendgame.main.util.minecraft

import net.minecraft.block.Block
import net.minecraft.component.ComponentType
import net.minecraft.entity.EntityType
import net.minecraft.item.Item
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys

object RegistryKeyUtil {
    fun createItemKey(name: String): RegistryKey<Item> = createKey(RegistryKeys.ITEM, name)

    fun createBlockKey(name: String): RegistryKey<Block> = createKey(RegistryKeys.BLOCK, name)

    fun createEntityKey(name: String): RegistryKey<EntityType<*>> = createKey(RegistryKeys.ENTITY_TYPE, name)

    fun createDataComponentTypeKey(name: String): RegistryKey<ComponentType<*>> = createKey(RegistryKeys.DATA_COMPONENT_TYPE, name)

    private fun <T, R : Registry<T>> createKey(registryKey: RegistryKey<R>, name: String): RegistryKey<T> =
        RegistryKey.of(registryKey, IdentifierUtil.default(name))
}