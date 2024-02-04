package de.fuballer.mcendgame.component.artifact

import de.fuballer.mcendgame.event.DungeonEntityDeathEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.persistent_data.TypeKeys
import de.fuballer.mcendgame.util.ArtifactUtil
import de.fuballer.mcendgame.util.PersistentDataUtil
import de.fuballer.mcendgame.util.random.RandomUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import kotlin.random.Random

@Component
class ArtifactDropService : Listener {
    @EventHandler
    fun on(event: DungeonEntityDeathEvent) {
        val entity = event.entity

        if (!PersistentDataUtil.getBooleanValue(entity, TypeKeys.IS_ENEMY)) return
        if (PersistentDataUtil.getBooleanValue(entity, TypeKeys.IS_MINION)) return

        if (Random.nextDouble() > ArtifactSettings.ARTIFACT_DROP_CHANCE) return

        val mapTier = PersistentDataUtil.getValue(entity, TypeKeys.MAP_TIER) ?: 1
        val type = RandomUtil.pick(ArtifactSettings.ARTIFACT_TYPES).option
        val tier = RandomUtil.pick(ArtifactSettings.ARTIFACT_TIERS, mapTier).option

        val artifact = Artifact(type, tier)
        val artifactItem = ArtifactUtil.getItem(artifact)

        entity.world.dropItemNaturally(entity.location, artifactItem)
    }
}