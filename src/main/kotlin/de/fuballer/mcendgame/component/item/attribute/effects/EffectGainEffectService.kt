package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.getFirstAsDouble
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

private const val DURATION = 3600
private val EFFECTS = listOf(
    RandomOption(1000, PotionEffect(PotionEffectType.STRENGTH, DURATION, 0, false)),
    RandomOption(300, PotionEffect(PotionEffectType.STRENGTH, DURATION, 1, false)),
    RandomOption(30, PotionEffect(PotionEffectType.STRENGTH, DURATION, 2, false)),
    RandomOption(2, PotionEffect(PotionEffectType.STRENGTH, DURATION, 3, false)),
    RandomOption(1000, PotionEffect(PotionEffectType.RESISTANCE, DURATION, 0, false)),
    RandomOption(300, PotionEffect(PotionEffectType.RESISTANCE, DURATION, 1, false)),
    RandomOption(30, PotionEffect(PotionEffectType.RESISTANCE, DURATION, 2, false)),
    RandomOption(2, PotionEffect(PotionEffectType.RESISTANCE, DURATION, 3, false)),
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
        val entity = event.entity.killer ?: return

        val attributes = entity.getCustomAttributes()
        val effectStealAttributes = attributes[CustomAttributeTypes.EFFECT_GAIN] ?: return

        for (effectStealAttribute in effectStealAttributes) {
            val effectStealChance = effectStealAttribute.attributeRolls.getFirstAsDouble()
            if (Random.nextDouble() > effectStealChance) continue

            val effect = RandomUtil.pick(EFFECTS).option
            entity.addPotionEffect(effect)

            entity.playSound(entity.location, Sound.BLOCK_AMETHYST_CLUSTER_BREAK, SoundCategory.PLAYERS, 1f, 1f)
        }
    }
}