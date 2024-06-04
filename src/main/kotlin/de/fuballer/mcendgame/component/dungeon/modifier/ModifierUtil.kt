package de.fuballer.mcendgame.component.dungeon.modifier

import de.fuballer.mcendgame.technical.persistent_data.TypeKeys
import de.fuballer.mcendgame.util.PersistentDataUtil
import org.bukkit.entity.Entity

object ModifierUtil {
    fun Entity.addModifier(modifier: Modifier) {
        val modifiers = getModifiers().toMutableList()
        modifiers.add(modifier)

        setModifiers(modifiers)
    }

    fun Entity.removeModifiersBySource(source: String) {
        val modifiers = getModifiers().toMutableList()
            .filter { it.source != source }

        setModifiers(modifiers)
    }

    fun getModifierMultiplier(
        entity: Entity?,
        type: ModifierType
    ): Double {
        if (entity == null) return 1.0

        var increase = 0.0
        var more = 1.0

        entity.getModifiers()
            .filter { it.type == type }
            .forEach {
                when (it.operation) {
                    ModifierOperation.INCREASE -> increase += it.value
                    ModifierOperation.MORE -> more *= 1 + it.value
                }
            }

        return (1 + increase) * more
    }

    private fun Entity.setModifiers(value: List<Modifier>) {
        PersistentDataUtil.setValue(this, TypeKeys.MODIFIERS, value)
    }

    private fun Entity.getModifiers() = PersistentDataUtil.getValue(this, TypeKeys.MODIFIERS) ?: listOf()
}