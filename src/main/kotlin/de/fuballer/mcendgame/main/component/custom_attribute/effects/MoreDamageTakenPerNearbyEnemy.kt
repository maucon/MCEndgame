package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asIntRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.util.extension.EntityExtension.isEnemy
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
class MoreDamageTakenPerNearbyEnemy {
    @CommandHandler
    fun on(cmd: DamageCalculationCommand) {
        val attributes = cmd.damagedAttributes[CustomAttributeTypes.MORE_DAMAGE_TAKEN_PER_NEARBY_ENEMY] ?: return
        val damaged = cmd.damaged

        attributes.forEach { attribute ->
            val moreDamageTakenPerEnemy = attribute.rolls[0].asDoubleRoll().getValue()
            val range = attribute.rolls[1].asIntRoll().getValue()

            val nearbyEnemies = cmd.world.getEntities(damaged, damaged.boundingBox.inflate(range.toDouble())) { damaged.isEnemy(it) }
            val totalMoreDamage = moreDamageTakenPerEnemy * nearbyEnemies.size
            cmd.moreDamageTaken.add(totalMoreDamage)
        }
    }
}