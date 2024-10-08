package de.fuballer.mcendgame.util.extension

import de.fuballer.mcendgame.component.item.attribute.data.AttributeRoll
import de.fuballer.mcendgame.component.item.attribute.data.DoubleRoll
import de.fuballer.mcendgame.component.item.attribute.data.IntRoll
import de.fuballer.mcendgame.component.item.attribute.data.StringRoll
import java.util.function.Consumer
import java.util.function.Function

object AttributeRollExtension {
    fun AttributeRoll<*>.run(doubleRollConsumer: Consumer<DoubleRoll>, stringRollConsumer: Consumer<StringRoll>, intRollConsumer: Consumer<IntRoll>) {
        when (this) {
            is DoubleRoll -> doubleRollConsumer.accept(this)
            is StringRoll -> stringRollConsumer.accept(this)
            is IntRoll -> intRollConsumer.accept(this)
        }
    }

    fun <R> AttributeRoll<*>.extract(doubleRollFunction: Function<DoubleRoll, R>, stringRollFunction: Function<StringRoll, R>, intRollFunction: Function<IntRoll, R>): R =
        when (this) {
            is DoubleRoll -> doubleRollFunction.apply(this)
            is StringRoll -> stringRollFunction.apply(this)
            is IntRoll -> intRollFunction.apply(this)
            else -> throw IllegalStateException("Invalid ${AttributeRoll::class.simpleName} instance")
        }

    fun List<AttributeRoll<*>>.getFirstAsDouble(): Double = (get(0) as DoubleRoll).getRoll()
    fun List<AttributeRoll<*>>.getFirstAsInt(): Int = (get(0) as IntRoll).getRoll()
    fun List<AttributeRoll<*>>.getFirstAsString(): String = (get(0) as StringRoll).getRoll()
    fun List<AttributeRoll<*>>.getSecondAsDouble(): Double = (get(1) as DoubleRoll).getRoll()
    fun List<AttributeRoll<*>>.getSecondAsInt(): Int = (get(1) as IntRoll).getRoll()
}