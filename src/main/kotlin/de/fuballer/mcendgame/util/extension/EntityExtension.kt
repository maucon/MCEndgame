package de.fuballer.mcendgame.util.extension

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.technical.persistent_data.TypeKeys
import de.fuballer.mcendgame.util.PersistentDataUtil
import org.bukkit.Location
import org.bukkit.entity.Entity
import java.util.*

object EntityExtension {
    fun Entity.setDisableDropEquipment(value: Boolean = true) {
        PersistentDataUtil.setValue(this, TypeKeys.DISABLE_DROP_EQUIPMENT, value)
    }

    fun Entity.isDropEquipmentDisabled() = PersistentDataUtil.getBooleanValue(this, TypeKeys.DISABLE_DROP_EQUIPMENT)

    fun Entity.setMapTier(value: Int) {
        PersistentDataUtil.setValue(this, TypeKeys.MAP_TIER, value)
    }

    fun Entity.getMapTier() = PersistentDataUtil.getValue(this, TypeKeys.MAP_TIER)

    fun Entity.setIsMinion(value: Boolean = true) {
        PersistentDataUtil.setValue(this, TypeKeys.IS_MINION, value)
    }

    fun Entity.isMinion() = PersistentDataUtil.getBooleanValue(this, TypeKeys.IS_MINION)

    fun Entity.setHideEquipment(value: Boolean = true) {
        PersistentDataUtil.setValue(this, TypeKeys.HIDE_EQUIPMENT, value)
    }

    fun Entity.isHideEquipment() = PersistentDataUtil.getBooleanValue(this, TypeKeys.HIDE_EQUIPMENT)

    fun Entity.setCustomEntityType(value: CustomEntityType) {
        PersistentDataUtil.setValue(this, TypeKeys.CUSTOM_ENTITY_TYPE, value)
    }

    fun Entity.getCustomEntityType() = PersistentDataUtil.getValue(this, TypeKeys.CUSTOM_ENTITY_TYPE)

    fun Entity.setIsEnemy(value: Boolean = true) {
        PersistentDataUtil.setValue(this, TypeKeys.IS_ENEMY, value)
    }

    fun Entity.isEnemy() = PersistentDataUtil.getBooleanValue(this, TypeKeys.IS_ENEMY)

    fun Entity.setMinionIds(value: List<UUID>) {
        PersistentDataUtil.setValue(this, TypeKeys.MINION_IDS, value)
    }

    fun Entity.getMinionIds() = PersistentDataUtil.getValue(this, TypeKeys.MINION_IDS)

    fun Entity.setIsPortal(value: Boolean = true) {
        PersistentDataUtil.setValue(this, TypeKeys.IS_PORTAL, value)
    }

    fun Entity.isPortal() = PersistentDataUtil.getBooleanValue(this, TypeKeys.IS_PORTAL)

    fun Entity.setIsBoss(value: Boolean = true) {
        PersistentDataUtil.setValue(this, TypeKeys.IS_BOSS, value)
    }

    fun Entity.isBoss() = PersistentDataUtil.getBooleanValue(this, TypeKeys.IS_BOSS)

    fun Entity.setPortalLocation(value: Location) {
        PersistentDataUtil.setValue(this, TypeKeys.PORTAL_LOCATION, value)
    }

    fun Entity.getPortalLocation() = PersistentDataUtil.getValue(this, TypeKeys.PORTAL_LOCATION)
}