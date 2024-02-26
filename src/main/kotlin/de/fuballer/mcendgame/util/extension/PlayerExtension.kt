package de.fuballer.mcendgame.util.extension

import de.fuballer.mcendgame.component.totem.data.Totem
import de.fuballer.mcendgame.component.totem.data.TotemType
import de.fuballer.mcendgame.technical.persistent_data.TypeKeys
import de.fuballer.mcendgame.util.PersistentDataUtil
import org.bukkit.entity.Player
import java.util.*

object PlayerExtension {
    fun Player.setLastMapDevice(value: UUID) = PersistentDataUtil.setValue(this, TypeKeys.LAST_MAP_DEVICE, value)
    fun Player.getLastMapDevice() = PersistentDataUtil.getValue(this, TypeKeys.LAST_MAP_DEVICE)
    fun Player.setTotems(value: List<Totem>) = PersistentDataUtil.setValue(this, TypeKeys.TOTEMS, value)
    fun Player.getTotems() = PersistentDataUtil.getValue(this, TypeKeys.TOTEMS)
    fun Player.setHealOnBlockActivation(value: Long) = PersistentDataUtil.setValue(this, TypeKeys.HEAL_ON_BLOCK_ACTIVATION, value)
    fun Player.getHealOnBlockActivation() = PersistentDataUtil.getValue(this, TypeKeys.HEAL_ON_BLOCK_ACTIVATION)

    fun Player.getHighestTotemTier(totemType: TotemType) =
        this.getTotems()
            ?.filter { it.type == totemType }
            ?.maxByOrNull { it.tier.tier }
            ?.tier
}