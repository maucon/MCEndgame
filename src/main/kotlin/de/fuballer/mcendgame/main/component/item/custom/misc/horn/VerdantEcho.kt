package de.fuballer.mcendgame.main.component.item.custom.misc.horn

import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesHornItem
import de.fuballer.mcendgame.main.component.item.custom.misc.horn.command.HornUseCommand
import de.fuballer.mcendgame.main.component.status_effect.CustomStatusEffects
import de.fuballer.mcendgame.main.util.extension.EntityExtension.isAlly
import net.minecraft.network.chat.Component
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

class VerdantEcho(
    settings: Properties,
) : UniqueAttributesHornItem(settings) {
    override val id = "verdant_echo"

    override val description = listOf(
        Component.translatable(DESCRIPTION_KEY + id + "0"),
        Component.translatable(DESCRIPTION_KEY + id + "1"),
    )

    override val baseCooldown = 600
    override val baseDuration = 200
    override val range = 10.0

    override fun getCustomAttributes(): List<RollableCustomAttribute> = listOf()

    override fun getAttributeModifierSlot() = EquipmentSlotGroup.HAND

    override fun onUse(world: Level, user: Player, cmd: HornUseCommand) {
        val nearbyAllies = world.getEntitiesOfClass(LivingEntity::class.java, user.boundingBox.inflate(range)) { user.isAlly(it) && user.distanceTo(it) <= range }

        val duration = (baseDuration * cmd.getDurationFactor()).toInt()
        val amplifier = if (cmd.isStronger) 1 else 0
        nearbyAllies.forEach {
            val effectInstance = MobEffectInstance(CustomStatusEffects.VERDANT_ECHO, duration, amplifier, false, true, true)
            it.addEffect(effectInstance)
        }
    }
}