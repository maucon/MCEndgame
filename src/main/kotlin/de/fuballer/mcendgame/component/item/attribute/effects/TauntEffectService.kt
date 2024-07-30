package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.getFirstAsDouble
import org.bukkit.entity.Monster
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import kotlin.random.Random

@Component
class TauntEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val tauntAttributes = event.damagerAttributes[CustomAttributeTypes.TAUNT] ?: return

        val damaged = event.damaged as? Monster ?: return
        val tauntChance = tauntAttributes.sumOf { it.attributeRolls.getFirstAsDouble() }

        if (Random.nextDouble() > tauntChance) return

        damaged.target = event.damager
    }
}