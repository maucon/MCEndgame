package de.fuballer.mcendgame.technical.extension

import de.fuballer.mcendgame.technical.persistent_data.TypeKeys
import de.fuballer.mcendgame.util.PersistentDataUtil
import org.bukkit.entity.Entity

object EntityExtension {
    fun Entity.setIsMinion(isMinion: Boolean = true) {
        PersistentDataUtil.setValue(this, TypeKeys.IS_MINION, isMinion)
    }

    fun Entity.isMinion() = PersistentDataUtil.getBooleanValue(this, TypeKeys.IS_MINION)
}