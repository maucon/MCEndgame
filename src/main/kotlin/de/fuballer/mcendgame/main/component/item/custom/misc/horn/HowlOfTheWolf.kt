package de.fuballer.mcendgame.main.component.item.custom.misc.horn

import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver.beastweaver_wolf.BeastweaverWolfEntity
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesHornItem
import de.fuballer.mcendgame.main.component.item.custom.misc.horn.command.HornUseCommand
import de.fuballer.mcendgame.main.component.status_effect.CustomStatusEffects
import de.fuballer.mcendgame.main.util.BlockPosUtil
import de.fuballer.mcendgame.main.util.extension.EntityExtension.updateCompanionGoals
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isDungeonEnemy
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.setCompanion
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.setDungeonEnemy
import net.minecraft.network.chat.Component
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level

private const val SEARCH_SPAWN_POS_STEPS = 10

class HowlOfTheWolf(
    settings: Properties,
) : UniqueAttributesHornItem(settings) {
    override val id = "howl_of_the_wolf"

    override val description = listOf(
        Component.translatable(DESCRIPTION_KEY + id + "_0"),
        Component.translatable(DESCRIPTION_KEY + id + "_1"),
    )

    override val baseCooldown = 1200
    override val baseDuration = 600
    override val range = 10.0

    override fun getCustomAttributes(): List<RollableCustomAttribute> = listOf()

    override fun getAttributeModifierSlot() = EquipmentSlotGroup.HAND

    override fun onUse(
        world: Level,
        user: LivingEntity,
        cmd: HornUseCommand,
    ) {
        val possiblePositions = BlockPosUtil.findEmptyAboveSolid(world, user.blockPosition(), SEARCH_SPAWN_POS_STEPS)

        val wolfCount = if (cmd.isStronger) 3 else 2
        possiblePositions.shuffled().take(wolfCount).forEach { pos ->
            val wolf = BeastweaverWolfEntity(CustomEntities.BEASTWEAVER_WOLF, world)

            wolf.setTame(true, false)
            wolf.setCompanion()
            wolf.owner = user
            if (user.isDungeonEnemy()) wolf.setDungeonEnemy()

            wolf.maxDuration = baseDuration
            wolf.setDieWithoutTarget(false)
            wolf.isInvulnerable = true

            wolf.updateCompanionGoals(user)

            wolf.setPos(pos.x + 0.5, pos.y.toDouble(), pos.z + 0.5)
            world.addFreshEntity(wolf)
        }

        val nearbyAllies = getNearbyAllies(world, user)

        val duration = (baseDuration * cmd.getDurationFactor()).toInt()
        val amplifier = if (cmd.isStronger) 1 else 0
        nearbyAllies.forEach {
            val effectInstance = MobEffectInstance(CustomStatusEffects.HOWL_OF_THE_WOLF, duration, amplifier, false, true, true)
            it.addEffect(effectInstance)
        }
    }
}