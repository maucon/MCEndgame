package de.fuballer.mcendgame.component.technical.extension

import de.fuballer.mcendgame.component.artifact.Artifact
import de.fuballer.mcendgame.component.attribute.RolledAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.map_device.data.MapDeviceAction
import de.fuballer.mcendgame.component.technical.persistent_data.TypeKeys
import de.fuballer.mcendgame.util.PersistentDataUtil
import org.bukkit.inventory.ItemStack

object ItemStackExtension {
    fun ItemStack.setCustomItemType(value: CustomItemType) = setPersistentData(this, TypeKeys.CUSTOM_ITEM_TYPE, value)
    fun ItemStack.getCustomItemType() = getPersistentData(this, TypeKeys.CUSTOM_ITEM_TYPE)
    fun ItemStack.setRolledAttributes(value: List<RolledAttribute>) = setPersistentData(this, TypeKeys.ATTRIBUTES, value)
    fun ItemStack.getRolledAttributes() = getPersistentData(this, TypeKeys.ATTRIBUTES)
    fun ItemStack.setUnmodifiable(value: Boolean = true) = setPersistentData(this, TypeKeys.UNMODIFIABLE, value)
    fun ItemStack.isUnmodifiable() = getPersistentDataBoolean(this, TypeKeys.UNMODIFIABLE)
    fun ItemStack.setCorruptionRounds(value: Int) = setPersistentData(this, TypeKeys.CORRUPTION_ROUNDS, value)
    fun ItemStack.getCorruptionRounds() = getPersistentData(this, TypeKeys.CORRUPTION_ROUNDS)
    fun ItemStack.setMapDevice(value: Boolean = true) = setPersistentData(this, TypeKeys.MAP_DEVICE, value)
    fun ItemStack.isMapDevice() = getPersistentDataBoolean(this, TypeKeys.MAP_DEVICE)
    fun ItemStack.setMapDeviceAction(value: MapDeviceAction) = setPersistentData(this, TypeKeys.MAP_DEVICE_ACTION, value)
    fun ItemStack.getMapDeviceAction() = getPersistentData(this, TypeKeys.MAP_DEVICE_ACTION)
    fun ItemStack.setArtifact(value: Artifact) = setPersistentData(this, TypeKeys.ARTIFACT, value)
    fun ItemStack.getArtifact() = getPersistentData(this, TypeKeys.ARTIFACT)

    private fun <T : Any> setPersistentData(item: ItemStack, key: TypeKeys.TypeKey<T>, value: T) {
        val itemMeta = item.itemMeta!!
        PersistentDataUtil.setValue(itemMeta, key, value)
        item.itemMeta = itemMeta
    }

    private fun <T : Any> getPersistentData(item: ItemStack, key: TypeKeys.TypeKey<T>): T? {
        val itemMeta = item.itemMeta!!
        return PersistentDataUtil.getValue(itemMeta, key)
    }

    private fun getPersistentDataBoolean(
        item: ItemStack,
        key: TypeKeys.TypeKey<Boolean>,
        default: Boolean = false
    ): Boolean {
        val itemMeta = item.itemMeta ?: return default
        return PersistentDataUtil.getBooleanValue(itemMeta, key, default)
    }
}