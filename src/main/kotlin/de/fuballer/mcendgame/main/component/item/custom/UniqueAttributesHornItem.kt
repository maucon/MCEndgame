package de.fuballer.mcendgame.main.component.item.custom

import de.fuballer.mcendgame.main.component.item.custom.misc.horn.command.HornUseCommand
import de.maucon.mauconframework.command.CommandGateway
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.InstrumentItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemLore
import net.minecraft.world.level.Level

abstract class UniqueAttributesHornItem(
    val settings: Properties,
) : InstrumentItem(settings), UniqueAttributesItemInterface {
    companion object {
        const val BASE_KEY = "item.mcendgame.horn."
        const val DESCRIPTION_KEY = BASE_KEY + "description."
        const val DURATION_KEY = BASE_KEY + "duration"
        const val RANGE_KEY = BASE_KEY + "range"
        const val COOLDOWN_KEY = BASE_KEY + "cooldown"
    }

    abstract val id: String

    abstract val description: List<MutableComponent>

    abstract val baseCooldown: Int // ticks
    abstract val baseDuration: Int // ticks
    abstract val range: Double

    override fun getRolledStack(item: Item, rolls: List<Double>): ItemStack {
        val stack = super.getRolledStack(item, rolls)

        val lore = description.toMutableList()
        lore.addAll(
            listOf(
                Component.translatable(DURATION_KEY, baseDuration / 20),
                Component.translatable(RANGE_KEY, range.toInt()),
                Component.translatable(COOLDOWN_KEY, baseCooldown / 20),
            )
        )
        val styledLore = lore.map { it.withStyle { style -> style.withItalic(false).withColor(ChatFormatting.BLUE) } }
        stack.set(DataComponents.LORE, ItemLore(styledLore))

        return stack
    }

    override fun getDefaultInstance() = getRolledStack(this, true)

    override fun getName(stack: ItemStack): MutableComponent = super.getName(stack).copy().withColor(getNameColor())

    override fun use(world: Level, user: Player, hand: InteractionHand): InteractionResult {
        val result = super.use(world, user, hand)
        if (result == InteractionResult.FAIL) return result

        val command = HornUseCommand(user)
        val cmd = CommandGateway.apply(command)

        onUse(world, user, cmd)

        val itemStack = user.getItemInHand(hand)
        val cooldown = (baseCooldown * cmd.getCooldownFactor()).toInt()
        user.cooldowns.addCooldown(itemStack, cooldown)

        return result
    }

    abstract fun onUse(world: Level, user: Player, cmd: HornUseCommand)
}