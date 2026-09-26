package de.fuballer.mcendgame.main.component.item.custom

import de.fuballer.mcendgame.main.component.entity.custom.entities.bandit.BanditEntity
import de.fuballer.mcendgame.main.component.item.custom.misc.horn.command.HornUseCommand
import de.fuballer.mcendgame.main.util.extension.EntityExtension.isAlly
import de.maucon.mauconframework.command.CommandGateway
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Instrument
import net.minecraft.world.item.InstrumentItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemLore
import net.minecraft.world.level.Level
import net.minecraft.world.level.gameevent.GameEvent
import java.util.*

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

        val serverWorld = world as? ServerLevel ?: return result

        val command = HornUseCommand(user)
        val cmd = CommandGateway.apply(command)

        onUse(serverWorld, user, cmd)

        val itemStack = user.getItemInHand(hand)
        val cooldown = (baseCooldown * cmd.getCooldownFactor()).toInt()
        user.cooldowns.addCooldown(itemStack, cooldown)

        return result
    }

    fun banditUse(
        level: ServerLevel,
        bandit: BanditEntity,
        hand: InteractionHand,
    ): InteractionResult {
        val itemStack = bandit.getItemInHand(hand)
        val instrumentHolder = getInstrument(itemStack)
        if (!instrumentHolder.isPresent) return InteractionResult.FAIL

        val instrument = instrumentHolder.get().value()
        bandit.startUsingItem(hand)
        play(level, bandit, instrument)

        val command = HornUseCommand(bandit)
        val cmd = CommandGateway.apply(command)

        onUse(level, bandit, cmd)

        val cooldown = (baseCooldown * cmd.getCooldownFactor()).toInt()
        bandit.getCooldowns().addCooldown(itemStack, cooldown)

        return InteractionResult.CONSUME
    }

    private fun getInstrument(itemStack: ItemStack): Optional<Holder<Instrument>> {
        val instrument = itemStack.get(DataComponents.INSTRUMENT) ?: return Optional.empty()
        return Optional.of(instrument.instrument())
    }

    private fun play(
        level: Level,
        user: LivingEntity,
        instrument: Instrument,
    ) {
        val soundEvent = instrument.soundEvent().value()
        val volume = instrument.range() / 16.0F
        level.playSound(user, user, soundEvent, SoundSource.RECORDS, volume, 1.0F)
        level.gameEvent(GameEvent.INSTRUMENT_PLAY, user.position(), GameEvent.Context.of(user))
    }

    abstract fun onUse(world: ServerLevel, user: LivingEntity, cmd: HornUseCommand)

    fun getNearbyAllies(
        world: Level,
        user: LivingEntity,
    ) = world.getEntitiesOfClass(LivingEntity::class.java, user.boundingBox.inflate(range)) { user.isAlly(it) && user.distanceTo(it) <= range }
}