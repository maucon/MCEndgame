package de.fuballer.mcendgame.component.attribute.effects

import de.fuballer.mcendgame.component.attribute.AttributeType
import de.fuballer.mcendgame.event.DungeonEntityDeathEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.LivingEntityExtension.getCustomAttributes
import de.fuballer.mcendgame.util.random.RandomOption
import de.fuballer.mcendgame.util.random.RandomUtil
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.random.Random

private val EFFECTS = listOf(
    RandomOption(1000, PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600, 0, false)),
    RandomOption(300, PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600, 1, false)),
    RandomOption(30, PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600, 2, false)),
    RandomOption(2, PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600, 3, false)),
    RandomOption(1000, PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 0, false)),
    RandomOption(300, PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 1, false)),
    RandomOption(30, PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 2, false)),
    RandomOption(2, PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 3, false)),
    RandomOption(1000, PotionEffect(PotionEffectType.SPEED, 600, 0, false)),
    RandomOption(300, PotionEffect(PotionEffectType.SPEED, 600, 1, false)),
    RandomOption(30, PotionEffect(PotionEffectType.SPEED, 600, 2, false)),
    RandomOption(2, PotionEffect(PotionEffectType.SPEED, 600, 3, false)),
    RandomOption(1500, PotionEffect(PotionEffectType.FIRE_RESISTANCE, 600, 0, false)),
    RandomOption(300, PotionEffect(PotionEffectType.INVISIBILITY, 600, 0, false)),
)

@Component
class EffectGainEffectService : Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun on(event: DungeonEntityDeathEvent) {
        val player = event.entity.killer ?: return

        val attributes = player.getCustomAttributes()
        val effectStealAttributes = attributes[AttributeType.EFFECT_GAIN] ?: return

        for (effectStealChance in effectStealAttributes) {
            if (Random.nextDouble() > effectStealChance) continue

            val effect = RandomUtil.pick(EFFECTS).option
            player.addPotionEffect(effect)

            player.playSound(player.location, Sound.BLOCK_AMETHYST_CLUSTER_BREAK, SoundCategory.PLAYERS, 1f, 1f)
        }
    }
}