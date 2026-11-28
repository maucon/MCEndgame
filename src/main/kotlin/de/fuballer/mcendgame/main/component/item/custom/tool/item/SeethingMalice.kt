package de.fuballer.mcendgame.main.component.item.custom.tool.item

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.entity.custom.entities.magma_spike.MagmaSpikesUtil
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItem
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.SwingAnimation
import net.minecraft.world.level.Level

class SeethingMalice(
    settings: Properties,
) : UniqueAttributesItem(settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.MAGMA_SPIKES_SLAM_ON_USE, 0, DoubleBounds(1.2, 1.5)),
        RollableCustomAttribute(CustomAttributeTypes.SPELL_DAMAGE, 0, DoubleBounds(2.0, 3.0)),
        RollableCustomAttribute(VanillaAttributeTypes.INCREASED_ENTITY_INTERACTION_RANGE, 0, DoubleBounds(0.15, 0.15)),
    )

    override fun getAttributeModifierSlot() = EquipmentSlotGroup.MAINHAND

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        if (level.isClientSide) {
            player.swing(hand, SwingAnimation.DEFAULT, false)
            level.playLocalSound(player, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 0.5f, 0.8f + 0.3f * player.random.nextFloat())
            level.playLocalSound(player, SoundEvents.RAVAGER_STEP, SoundSource.PLAYERS, 0.6f, 0.8f + 0.3f * player.random.nextFloat())
            return super.use(level, player, hand)
        }

        val serverLevel = level as? ServerLevel ?: return super.use(level, player, hand)

        player.swing(hand, SwingAnimation.DEFAULT, false)
        serverLevel.playSound(player, player.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 0.5f, 0.8f + 0.3f * player.random.nextFloat())
        serverLevel.playSound(player, player.blockPosition(), SoundEvents.RAVAGER_STEP, SoundSource.PLAYERS, 0.6f, 0.8f + 0.3f * player.random.nextFloat())

        val stack = player.getItemInHand(hand) // can only be Seething Malice
        val damagePercentage = getAttributeSpellDamagePercentage(stack)
        MagmaSpikesUtil.castMagmaSpikesSlam(
            player,
            damagePercentage,
            ringDelay = 6,
            minScale = 0.75f,
            maxScale = 1.5f,
            ringSpikeCount = listOf(8, 10, 12),
            ringRadius = 1.25f,
            serverLevel,
        )

        return InteractionResult.SUCCESS
    }

    private fun getAttributeSpellDamagePercentage(
        stack: ItemStack,
    ): Double {
        val attribute = stack.getCustomAttributes().first { it.type == CustomAttributeTypes.MAGMA_SPIKES_SLAM_ON_USE }
        return attribute.rolls[0].asDoubleRoll().getValue()
    }
}