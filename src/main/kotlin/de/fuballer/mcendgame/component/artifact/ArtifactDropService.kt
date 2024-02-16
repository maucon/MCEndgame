package de.fuballer.mcendgame.component.artifact

import de.fuballer.mcendgame.component.artifact.data.Artifact
import de.fuballer.mcendgame.event.DungeonEntityDeathEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.EntityExtension.getMapTier
import de.fuballer.mcendgame.util.extension.EntityExtension.isEnemy
import de.fuballer.mcendgame.util.extension.EntityExtension.isMinion
import de.fuballer.mcendgame.util.random.RandomUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import kotlin.random.Random

@Component
class ArtifactDropService : Listener {
    @EventHandler
    fun on(event: DungeonEntityDeathEvent) {
        val entity = event.entity

        if (!entity.isEnemy()) return
        if (entity.isMinion()) return

        if (Random.nextDouble() > ArtifactSettings.ARTIFACT_DROP_CHANCE) return

        val mapTier = entity.getMapTier() ?: 1
        val type = RandomUtil.pick(ArtifactSettings.ARTIFACT_TYPES).option
        val tier = RandomUtil.pick(ArtifactSettings.ARTIFACT_TIERS, mapTier).option

        val artifact = Artifact(type, tier)
        val artifactItem = artifact.toItem()

        entity.world.dropItemNaturally(entity.location, artifactItem)
    }
}