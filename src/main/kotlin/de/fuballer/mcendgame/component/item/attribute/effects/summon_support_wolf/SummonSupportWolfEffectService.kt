package de.fuballer.mcendgame.component.item.attribute.effects.summon_support_wolf

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.getFirstAsString
import de.fuballer.mcendgame.util.extension.EntityExtension.getActiveSupportWolf
import de.fuballer.mcendgame.util.extension.LivingEntityExtension.getCustomAttributes
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Wolf
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.math.pow

private val SLOWNESS_EFFECT = PotionEffect(PotionEffectType.SLOWNESS, 100, 1, true, false)
private val WEAKNESS_EFFECT = PotionEffect(PotionEffectType.WEAKNESS, 100, 1, true, false)
private val SPEED_EFFECT = PotionEffect(PotionEffectType.SPEED, 200, 1, true, false)
private const val LIFE_STEAL_ON_HIT = 1.0
private const val LIFE_STEAL_ON_KILL = 2.0
private const val PLAYER_INC_DAMAGE = 0.1
private const val WOLF_MORE_DAMAGE = 0.25

@Service
class SummonSupportWolfEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val damager = event.damager
        val player = damager as? Player ?: (damager as? Wolf ?: return).owner as? Player ?: return

        val supportWolfAttributes = player.getCustomAttributes()[CustomAttributeTypes.SUMMON_SUPPORT_WOLF] ?: return
        val incitingWolfAmount = supportWolfAttributes
            .filter {
                val supportWolfTypeString = it.attributeRolls.getFirstAsString()
                val supportWolfType = SupportWolfType.fromString(supportWolfTypeString)
                supportWolfType == SupportWolfType.INCITING
            }
            .size

        if (incitingWolfAmount == 0) return

        if (damager is Player) {
            event.increasedDamage.add(PLAYER_INC_DAMAGE * incitingWolfAmount)
            return
        }

        val moreDamage = (1 + WOLF_MORE_DAMAGE).pow(incitingWolfAmount) - 1
        event.moreDamage.add(moreDamage)
    }

    @EventHandler(ignoreCancelled = true)
    fun on(event: EntityDamageByEntityEvent) {
        val target = event.entity as? LivingEntity ?: return
        val wolf = event.damager as? Wolf ?: return

        val supportWolfType = wolf.getActiveSupportWolf()?.type ?: return
        if (supportWolfType == SupportWolfType.INCITING) return

        when (supportWolfType) {
            SupportWolfType.SLOWING -> onLivingEntityDamagedBySlowingWolf(target)
            SupportWolfType.WEAKENING -> onLivingEntityDamagedByWeakeningWolf(target)
            SupportWolfType.LIFE_STEALING -> onLivingEntityDamagedByLifeStealingWolf(target, wolf)
            SupportWolfType.HASTING -> onLivingEntityDamagedByHastingWolf(wolf)
            else -> return
        }
    }

    private fun onLivingEntityDamagedBySlowingWolf(target: LivingEntity) {
        if (target.isDead) return
        target.addPotionEffect(SLOWNESS_EFFECT)
    }

    private fun onLivingEntityDamagedByWeakeningWolf(target: LivingEntity) {
        if (target.isDead) return
        target.addPotionEffect(WEAKNESS_EFFECT)
    }

    private fun onLivingEntityDamagedByLifeStealingWolf(target: LivingEntity, wolf: Wolf) {
        val player = wolf.owner as? Player ?: return

        val healAmount = if (target.isDead) LIFE_STEAL_ON_KILL else LIFE_STEAL_ON_HIT
        player.heal(healAmount)
    }

    private fun onLivingEntityDamagedByHastingWolf(wolf: Wolf) {
        val player = wolf.owner as? Player ?: return
        player.addPotionEffect(SPEED_EFFECT)

        val world = player.world
        world.getEntitiesByClass(Wolf::class.java)
            .filter { it.owner == player }
            .forEach { it.addPotionEffect(SPEED_EFFECT) }
    }
}