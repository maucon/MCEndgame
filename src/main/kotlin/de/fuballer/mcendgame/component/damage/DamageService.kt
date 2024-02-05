package de.fuballer.mcendgame.component.damage

import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

@Component
class DamageService : Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun on(event: EntityDamageByEntityEvent) {
        val player: Player
//        val damager = event.damager as LivingEntity
//        val mainHandItem = damager.equipment!!.getItem(EquipmentSlot.HAND)
//        mainHandItem.getCustomItemType()!!.attributes

        // MELEE CRIT: if (player.getFallDistance() > 0 && !player.isOnGround()) event.setDamage(event.getDamage() * 1.5F);
        // before strength in Java Edition
        /*
        The requirements for a melee critical hit are:
        - A player must be falling.
        - A player must not be on the ground.
        - A player must not be on a ladder or any type of vine.
        - A player must not be in water.
        - A player must not be affected by Blindness.
        - A player must not be affected by Slow Falling.
        - A player must not be riding an entity.
        - A player must not be faster than walking (like flying or sprinting. [Java Edition only]
        - A base attack must not be reduced below 90% damage due to cooldown. [Java Edition only]
         */
        // BOW CRIT: arrow.isCritical

        // damager = projectile -> source -> shooter (projectile)
        // minion -> minion

        // enchants (smite, ...)
        // crit
        // projectile speed
        // wither skull?
        // player.absorptionAmount

        /*
        damageType (MELEE, PROJECTILE, MAGIC)
        damager (entity -> potionsEffects)
        damaged (entity -> potionsEffects)
        flatDamage
        increaseDamage
        moreDamage
        armor
         */

//        val damageEvent = DamageCalculationEvent()
//        EventGateway.apply(damageEvent)
//
//        DamageModifier.entries.toTypedArray()
//            .filter { event.isApplicable(it) }
//            .forEach { event.setDamage(it, 0.0) }
//
//        event.setDamage(DamageModifier.BASE, 1.0)
    }
}