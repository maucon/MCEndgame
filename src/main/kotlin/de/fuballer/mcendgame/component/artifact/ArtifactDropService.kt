package de.fuballer.mcendgame.component.artifact

import de.fuballer.mcendgame.component.artifact.data.Artifact
import de.fuballer.mcendgame.domain.technical.persistent_data.TypeKeys
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ArtifactUtil
import de.fuballer.mcendgame.util.PersistentDataUtil
import de.fuballer.mcendgame.util.WorldUtil
import de.fuballer.mcendgame.util.random.RandomUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import java.util.*

@Component
class ArtifactDropService : Listener {
    private val random = Random()

    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        val entity = event.entity

        if (WorldUtil.isNotDungeonWorld(entity.world)) return
        if (!PersistentDataUtil.getBooleanValue(entity, TypeKeys.IS_ENEMY)) return
        if (PersistentDataUtil.getBooleanValue(entity, TypeKeys.IS_MINION)) return

        if (random.nextDouble() > ArtifactSettings.ARTIFACT_DROP_CHANCE) return

        val mapTier = PersistentDataUtil.getValue(entity, TypeKeys.MAP_TIER) ?: 1
        val type = RandomUtil.pick(ArtifactSettings.ARTIFACT_TYPES).option
        val tier = RandomUtil.pick(ArtifactSettings.ARTIFACT_TIERS, mapTier).option

        val artifact = Artifact(type, tier)
        val artifactItem = ArtifactUtil.getItem(artifact)

        entity.world.dropItemNaturally(entity.location, artifactItem)
    }
}