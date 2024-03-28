package de.fuballer.mcendgame.util.extension

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.util.DamageUtil
import org.bukkit.entity.LivingEntity

object LivingEntityExtension {
    fun LivingEntity.getCustomAttributes(): Map<AttributeType, List<Double>> {
        return DamageUtil.getEntityCustomAttributes(this)
    }
}