package de.fuballer.mcendgame.main.messaging.collect_attribute

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttributeType
import net.minecraft.world.entity.LivingEntity

interface CollectAttributeStatCommand {
    val entity: LivingEntity
    val attributes: Map<CustomAttributeType, List<CustomAttribute>>
    val flat: MutableList<Double>
    val increased: MutableList<Double>
    val more: MutableList<Double>

    fun calculate(): Double {
        val base = flat.sum()
        val increasedFactor = 1 + increased.sum()
        val moreFactor = more.fold(1.0) { a, b -> a * (b + 1) }
        return base * increasedFactor * moreFactor
    }
}