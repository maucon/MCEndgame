package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttributeType
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.messaging.collect_attribute.CollectGenericIncreasedDamageCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items

@Injectable
class IncreasedDamageWhileShieldDisabledService {
    @CommandHandler
    fun on(cmd: DamageCalculationCommand) {
        val player = cmd.damager as? Player ?: return
        cmd.increasedDamage.addAll(getIncreasedDamage(player, cmd.damagerAttributes))
    }

    @CommandHandler
    fun on(cmd: CollectGenericIncreasedDamageCommand) {
        val player = cmd.entity as? Player ?: return
        cmd.increased.addAll(getIncreasedDamage(player, cmd.attributes))
    }

    private fun getIncreasedDamage(
        player: Player,
        attributes: Map<CustomAttributeType, List<CustomAttribute>>,
    ): List<Double> {
        if (!player.cooldowns.isOnCooldown(Items.SHIELD.defaultInstance)) return listOf() // only check default shield since cooldowns should be synced anyway

        val attr = attributes[CustomAttributeTypes.INCREASED_DAMAGE_WHILE_SHIELD_DISABLED] ?: return listOf()
        return attr.map { (it.rolls[0] as DoubleRoll).getValue() }
    }
}