package de.fuballer.mcendgame.component.custom_entity.ability.runner

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import org.bukkit.entity.Creature

data class EntityRunner(
    var entity: Creature,
    var customEntityType: CustomEntityType,
    var abilityCooldown: Int,
    var cancelCallback: () -> Unit
) {
    private var ability = AbilityRunner(entity, customEntityType, abilityCooldown)
    private var inactivity = InactivityRunner(entity, ::inactivityCallback)
    private var changeTarget = ChangeTargetRunner(entity)

    private var cancelled = false

    init {
        ability.run()
        inactivity.run()
        changeTarget.run()
    }

    fun cancel() {
        cancelled = true

        ability.cancel()
        inactivity.cancel()
        changeTarget.cancel()
    }

    fun isCancelled() = cancelled

    private fun inactivityCallback() {
        cancel()

        cancelCallback.invoke()
    }
}