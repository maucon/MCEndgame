package de.fuballer.mcendgame.main.component.item.custom

import de.fuballer.mcendgame.main.component.item.custom.misc.horn.command.HornUseCommand
import de.maucon.mauconframework.command.CommandGateway
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.LoreComponent
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.GoatHornItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.tag.InstrumentTags
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

abstract class UniqueAttributesHornItem(
    val settings: Settings,
) : GoatHornItem(settings, InstrumentTags.GOAT_HORNS), UniqueAttributesItemInterface {
    companion object {
        const val BASE_KEY = "item.mcendgame.horn."
        const val DESCRIPTION_KEY = BASE_KEY + "description."
        const val DURATION_KEY = BASE_KEY + "duration"
        const val RANGE_KEY = BASE_KEY + "range"
        const val COOLDOWN_KEY = BASE_KEY + "cooldown"
    }

    abstract val id: String

    abstract val description: List<MutableText>

    abstract val baseCooldown: Int // ticks
    abstract val baseDuration: Int // ticks
    abstract val range: Double

    override fun getRolledStack(item: Item, rolls: List<Double>): ItemStack {
        val stack = super.getRolledStack(item, rolls)

        val lore = description.toMutableList()
        lore.addAll(
            listOf(
                Text.translatable(DURATION_KEY, (baseDuration / 20).toInt()),
                Text.translatable(RANGE_KEY, (range).toInt()),
                Text.translatable(COOLDOWN_KEY, (baseCooldown / 20).toInt()),
            )
        )
        val styledLore = lore.map { it.styled { style -> style.withItalic(false).withColor(Formatting.BLUE) } }
        stack.set(DataComponentTypes.LORE, LoreComponent(styledLore))

        return stack
    }

    override fun getDefaultStack() = getRolledStack(this, true)

    override fun getName(stack: ItemStack): MutableText = super.getName(stack).copy().withColor(getNameColor())

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val itemStack = user.getStackInHand(hand)
        val result = super.use(world, user, hand)
        if (result.result == ActionResult.FAIL) return result

        val command = HornUseCommand(user)
        val cmd = CommandGateway.apply(command)

        onUse(world, user, cmd)

        val cooldown = (baseCooldown * cmd.getCooldownFactor()).toInt()
        user.itemCooldownManager.set(itemStack.item, cooldown)

        return result
    }

    abstract fun onUse(world: World, user: PlayerEntity, cmd: HornUseCommand)
}