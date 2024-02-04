package de.fuballer.mcendgame.domain.entity.naga

import de.fuballer.mcendgame.domain.entity.poison_spit.PoisonSpitEntityType
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.EntityExtension.getCustomEntityType
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.EntityType
import org.bukkit.entity.LlamaSpit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent

@Component
class NagaService : Listener {
    @EventHandler
    fun onEntityShootBow(event: EntityShootBowEvent) {
        val entity = event.entity
        if (entity.getCustomEntityType() != NagaEntityType) return

        val spit = entity.world.spawnEntity(event.projectile.location, EntityType.LLAMA_SPIT, false) as LlamaSpit
        spit.velocity = event.projectile.velocity

        spit.shooter = entity
        spit.customName = PoisonSpitEntityType.customName

        entity.world.playSound(entity.location, Sound.ENTITY_SPIDER_HURT, SoundCategory.HOSTILE, 1f, 1f)

        event.isCancelled = true
    }
}
