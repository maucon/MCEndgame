package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asIntRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDeathEvent
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.addTemporaryAttributeModifier
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil.defaultJava
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes

@Injectable
class GainEnemyArmorOnKillService {
    private val attributeModifierIdentifierBase = "gain_enemy_armor_on_kill_"

    @EventSubscriber(sync = true)
    fun on(cmd: LivingEntityDeathEvent) {
        val killer = cmd.killer ?: return
        val attributes = killer.getAllCustomAttributes()[CustomAttributeTypes.GAIN_ENEMY_ARMOR_ON_KILL] ?: return
        val killed = cmd.entity
        val armor = killed.getAttributeValue(EntityAttributes.ARMOR)
        attributes.forEach {
            val armorPercent = it.rolls[0].asDoubleRoll().getValue()
            val duration = it.rolls[1].asIntRoll().getValue() * 20
            val identifier = defaultJava(attributeModifierIdentifierBase + it.id + "_" + killed.id)

            killer.addTemporaryAttributeModifier(
                EntityAttributes.ARMOR,
                identifier,
                duration,
                armor * armorPercent,
                EntityAttributeModifier.Operation.ADD_VALUE
            )
        }
    }
}