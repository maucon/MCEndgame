package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.LivingEntityExtension.getCustomAttributes
import de.fuballer.mcendgame.util.random.RandomOption
import de.fuballer.mcendgame.util.random.RandomUtil
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.random.Random

private const val DURATION = 12000
private val EFFECTS = listOf(
    RandomOption(1000, PotionEffect(PotionEffectType.INCREASE_DAMAGE, DURATION, 0, false)),
    RandomOption(300, PotionEffect(PotionEffectType.INCREASE_DAMAGE, DURATION, 1, false)),
    RandomOption(30, PotionEffect(PotionEffectType.INCREASE_DAMAGE, DURATION, 2, false)),
    RandomOption(2, PotionEffect(PotionEffectType.INCREASE_DAMAGE, DURATION, 3, false)),
    RandomOption(1000, PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, DURATION, 0, false)),
    RandomOption(300, PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, DURATION, 1, false)),
    RandomOption(30, PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, DURATION, 2, false)),
    RandomOption(2, PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, DURATION, 3, false)),
    RandomOption(1000, PotionEffect(PotionEffectType.SPEED, DURATION, 0, false)),
    RandomOption(300, PotionEffect(PotionEffectType.SPEED, DURATION, 1, false)),
    RandomOption(30, PotionEffect(PotionEffectType.SPEED, DURATION, 2, false)),
    RandomOption(2, PotionEffect(PotionEffectType.SPEED, DURATION, 3, false)),
    RandomOption(1500, PotionEffect(PotionEffectType.FIRE_RESISTANCE, DURATION, 0, false)),
    RandomOption(300, PotionEffect(PotionEffectType.INVISIBILITY, DURATION, 0, false)),
)

@Component
class EffectGainEffectService : Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun on(event: EntityDeathEvent) {
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