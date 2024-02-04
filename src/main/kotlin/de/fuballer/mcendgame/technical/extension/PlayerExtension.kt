package de.fuballer.mcendgame.technical.extension

import de.fuballer.mcendgame.component.artifact.Artifact
import de.fuballer.mcendgame.technical.persistent_data.TypeKeys
import de.fuballer.mcendgame.util.PersistentDataUtil
import org.bukkit.entity.Player
import java.util.*

object PlayerExtension {
    fun Player.setLastMapDevice(value: UUID) {
        PersistentDataUtil.setValue(this, TypeKeys.LAST_MAP_DEVICE, value)
    }

    fun Player.getLastMapDevice() = PersistentDataUtil.getValue(this, TypeKeys.LAST_MAP_DEVICE)

    fun Player.setArtifacts(value: List<Artifact>) {
        PersistentDataUtil.setValue(this, TypeKeys.ARTIFACTS, value)
    }

    fun Player.getArtifacts() = PersistentDataUtil.getValue(this, TypeKeys.ARTIFACTS)

    fun Player.setHealOnBlockArtifactActivation(value: Long) {
        PersistentDataUtil.setValue(this, TypeKeys.HEAL_ON_BLOCK_ARTIFACT_ACTIVATION, value)
    }

    fun Player.getHealOnBlockArtifactActivation() =
        PersistentDataUtil.getValue(this, TypeKeys.HEAL_ON_BLOCK_ARTIFACT_ACTIVATION)
}