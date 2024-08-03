package de.fuballer.mcendgame.component.item.attribute

import de.fuballer.mcendgame.component.item.attribute.data.AttributeType
import de.fuballer.mcendgame.component.item.attribute.data.CustomAttributeType
import de.fuballer.mcendgame.component.item.attribute.data.VanillaAttributeType
import de.fuballer.mcendgame.technical.Order
import kotlin.reflect.KVisibility
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

object AttributeSorter {
    private val attributeOrder = mutableMapOf<AttributeType, Int>()

    init {
        VanillaAttributeTypes::class.memberProperties
            .filter { it.visibility == KVisibility.PUBLIC }
            .forEach {
                val type = it.get(VanillaAttributeTypes) as VanillaAttributeType
                val order = it.findAnnotation<Order>()?.order ?: Int.MAX_VALUE

                attributeOrder[type] = order
            }

        CustomAttributeTypes::class.memberProperties
            .filter { it.visibility == KVisibility.PUBLIC }
            .forEach {
                val type = it.get(CustomAttributeTypes) as CustomAttributeType
                val order = it.findAnnotation<Order>()?.order ?: Int.MAX_VALUE

                attributeOrder[type] = order
            }
    }

    fun getSortKey(attributeType: AttributeType) = attributeOrder[attributeType]!!
}