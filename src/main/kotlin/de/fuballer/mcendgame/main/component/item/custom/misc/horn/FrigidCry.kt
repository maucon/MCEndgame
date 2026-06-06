package de.fuballer.mcendgame.main.component.item.custom.misc.horn

import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesHornItem
import de.fuballer.mcendgame.main.component.item.custom.misc.horn.command.HornUseCommand
import de.fuballer.mcendgame.main.util.extension.EntityExtension.isEnemy
import net.minecraft.network.chat.Component
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

class FrigidCry(
    settings: Properties,
) : UniqueAttributesHornItem(settings) {
    override val id = "frigid_cry"

    override val description = listOf(
        Component.translatable(DESCRIPTION_KEY + id),
    )

    override val baseCooldown = 600
    override val baseDuration = 100
    override val range = 8.0

    override fun getCustomAttributes(): List<RollableCustomAttribute> = listOf()

    override fun getAttributeModifierSlot() = EquipmentSlotGroup.HAND

    override fun onUse(world: Level, user: Player, cmd: HornUseCommand) {
        val nearbyEnemies = world.getEntitiesOfClass(LivingEntity::class.java, user.boundingBox.inflate(range)) { user.isEnemy(it) && user.distanceTo(it) <= range }
        if (nearbyEnemies.isEmpty()) return

        val duration = (baseDuration * cmd.getDurationFactor()).toInt()
        val amplifier = if (cmd.isStronger) 2 else 1
        nearbyEnemies.forEach {
            val effectInstance = MobEffectInstance(MobEffects.SLOWNESS, duration, amplifier, false, true, true)
            it.addEffect(effectInstance)
        }
    }
}