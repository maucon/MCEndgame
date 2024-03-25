package de.fuballer.mcendgame.util.extension

import de.fuballer.mcendgame.component.attribute.RolledAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.map_device.MapDeviceAction
import de.fuballer.mcendgame.component.totem.data.Totem
import de.fuballer.mcendgame.technical.persistent_data.TypeKeys
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
    fun ItemStack.setRefinement(value: Boolean = true) = setPersistentData(this, TypeKeys.REFINEMENT, value)
    fun ItemStack.isRefinement() = getPersistentDataBoolean(this, TypeKeys.REFINEMENT)
    fun ItemStack.setReshaping(value: Boolean = true) = setPersistentData(this, TypeKeys.RESHAPING, value)
    fun ItemStack.isReshaping() = getPersistentDataBoolean(this, TypeKeys.RESHAPING)
    fun ItemStack.setTransfiguration(value: Boolean = true) = setPersistentData(this, TypeKeys.TRANSFIGURATION, value)
    fun ItemStack.isTransfiguration() = getPersistentDataBoolean(this, TypeKeys.TRANSFIGURATION)
    fun ItemStack.setImitation(value: Boolean = true) = setPersistentData(this, TypeKeys.IMITATION, value)
    fun ItemStack.isImitation() = getPersistentDataBoolean(this, TypeKeys.IMITATION)
    fun ItemStack.setCraftingItem(value: Boolean = true) = setPersistentData(this, TypeKeys.CRAFTING_ITEM, value)
    fun ItemStack.isCraftingItem() = getPersistentDataBoolean(this, TypeKeys.CRAFTING_ITEM)
    fun ItemStack.setMapDevice(value: Boolean = true) = setPersistentData(this, TypeKeys.MAP_DEVICE, value)
    fun ItemStack.isMapDevice() = getPersistentDataBoolean(this, TypeKeys.MAP_DEVICE)
    fun ItemStack.setMapDeviceAction(value: MapDeviceAction) = setPersistentData(this, TypeKeys.MAP_DEVICE_ACTION, value)
    fun ItemStack.getMapDeviceAction() = getPersistentData(this, TypeKeys.MAP_DEVICE_ACTION)
    fun ItemStack.setTotem(value: Totem) = setPersistentData(this, TypeKeys.TOTEM, value)
    fun ItemStack.getTotem() = getPersistentData(this, TypeKeys.TOTEM)

    private fun <T : Any> setPersistentData(item: ItemStack, key: TypeKeys.TypeKey<T>, value: T) {
        val itemMeta = item.itemMeta!!
        PersistentDataUtil.setValue(itemMeta, key, value)
        item.itemMeta = itemMeta
    }

    private fun <T : Any> getPersistentData(item: ItemStack, key: TypeKeys.TypeKey<T>): T? {
        val itemMeta = item.itemMeta ?: return null
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