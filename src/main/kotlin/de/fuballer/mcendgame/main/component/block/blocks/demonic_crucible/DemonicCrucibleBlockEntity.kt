package de.fuballer.mcendgame.main.component.block.blocks.demonic_crucible

import com.mojang.logging.LogUtils
import de.fuballer.mcendgame.main.component.block.CustomBlockEntityTypes
import de.fuballer.mcendgame.main.component.corruption.CorruptionExtensions.isCorrupted
import de.fuballer.mcendgame.main.component.corruption.CorruptionService
import de.fuballer.mcendgame.main.component.particle.CustomParticleTypes
import de.fuballer.mcendgame.main.util.extension.EntityExtension.setAndSyncVelocity
import de.fuballer.mcendgame.main.util.extension.ItemStackExtension.isForgeable
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Prediction
import net.minecraft.util.ProblemReporter.ScopedCollector
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.TagValueOutput
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.world.phys.Vec3
import org.slf4j.Logger
import kotlin.jvm.optionals.getOrDefault
import kotlin.math.min
import kotlin.random.Random

class DemonicCrucibleBlockEntity(
    blockPos: BlockPos,
    blockState: BlockState,
) : BlockEntity(CustomBlockEntityTypes.DEMONIC_CRUCIBLE, blockPos, blockState) {
    companion object {
        val LOGGER: Logger = LogUtils.getLogger()
        const val PROGRESS_DURATION = 200
        private const val PARTICLE_LIFETIME = 30
        private val DROPPED_ITEM_VELOCITY = Vec3(0.0, 0.2, 0.0)
    }

    var stack: ItemStack = ItemStack.EMPTY
    var prevStack: ItemStack = ItemStack.EMPTY
    var progressTicks = 0
    private var nextHeartbeat = getHeartbeatCooldown()
    private fun getHeartbeatCooldown() = 15 - min(10, (13 * getProgress()).toInt())

    fun setStack(
        itemStack: ItemStack,
        player: Player,
        level: ServerLevel,
    ) {
        if (!stack.isEmpty) {
            level.playSound(null, player, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.PLAYERS, 1f, 1f)
            player.playSound(SoundEvents.ITEM_FRAME_REMOVE_ITEM)
            if (!player.inventory.add(stack)) player.drop(stack, false, Prediction.PREDICTED)
        }

        if (isStackValid(itemStack)) {
            stack = itemStack.consumeAndReturn(1, player)
            level.playSound(null, player, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.PLAYERS, 1f, 1f)
        } else {
            stack = ItemStack.EMPTY
        }

        markUpdated()
    }

    private fun isStackValid(itemStack: ItemStack): Boolean {
        if (itemStack.isEmpty) return false
        if (!itemStack.isForgeable()) return false
        if (itemStack.isCorrupted()) return false
        return true
    }

    fun tick(
        level: Level,
        pos: BlockPos,
    ) {
        if (prevStack != stack) {
            prevStack = stack
            progressTicks = 0
        }

        if (hasStack()) progressTicks++

        if (level !is ServerLevel) {
            tickClientEffects(level)
            return
        }

        if (progressTicks < PROGRESS_DURATION) return


        level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState())
        level.sendParticles(ParticleTypes.CLOUD, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, 10, 1.0, 1.0, 1.0, 0.1)
        level.sendParticles(ParticleTypes.EXPLOSION, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, 20, 2.5, 2.5, 2.5, 1.0)
        level.sendParticles(ParticleTypes.FLAME, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, 10, 2.5, 2.5, 2.5, 0.15)
        level.sendParticles(ParticleTypes.SMOKE, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, 20, 2.5, 2.5, 2.5, 0.2)
        level.playSound(null, worldPosition, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.BLOCKS, 1.5f, 1f)
        level.playSound(null, worldPosition, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.8f, 1f)

        val corruptedStack = CorruptionService.corrupt(stack, 2)
        val stackEntity = ItemEntity(level, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, corruptedStack)
        level.addFreshEntity(stackEntity)
        stackEntity.setAndSyncVelocity(DROPPED_ITEM_VELOCITY)

        setRemoved()
    }

    fun tickClientEffects(level: Level) {
        if (progressTicks == 0) nextHeartbeat = getHeartbeatCooldown()

        if (Random.nextInt(20) == 0) {
            level.playLocalSound(
                worldPosition.x + 0.5,
                worldPosition.y + 0.5,
                worldPosition.z + 0.5,
                SoundEvents.PORTAL_AMBIENT,
                SoundSource.BLOCKS,
                0.6f + 0.2f * Random.nextFloat(),
                Random.nextFloat() * 0.7f + 0.6f,
                false
            )
        }

        if (progressTicks == 0 && Random.nextDouble() < 0.8) return
        if (PROGRESS_DURATION - progressTicks > PARTICLE_LIFETIME) {
            repeat(1 + (5 * getProgress()).toInt()) {
                level.addParticle(
                    CustomParticleTypes.DEMONIC_CRUCIBLE_CHARGE,
                    worldPosition.x + 0.5,
                    worldPosition.y + 0.5,
                    worldPosition.z + 0.5,
                    (Random.nextFloat() - 0.5) * 4.0,
                    (Random.nextFloat() - 0.5) * 4.0,
                    (Random.nextFloat() - 0.5) * 4.0,
                )
            }
        }

        if (progressTicks == 0) return
        if (--nextHeartbeat == 0) {
            nextHeartbeat = getHeartbeatCooldown()
            level.playLocalSound(
                worldPosition.x + 0.5,
                worldPosition.y + 0.5,
                worldPosition.z + 0.5,
                SoundEvents.WARDEN_HEARTBEAT,
                SoundSource.BLOCKS,
                4.0f + 2.0f * getProgress().toFloat(),
                0.7f + 1.5f * getProgress().toFloat(),
                false
            )
        }
    }

    private fun hasStack() = !stack.isEmpty

    private fun getProgress() = progressTicks / PROGRESS_DURATION.toDouble()

    fun markUpdated() {
        setChanged()
        getLevel()?.sendBlockUpdated(blockPos, blockState, blockState, 3)
    }

    override fun getUpdatePacket(): Packet<ClientGamePacketListener> = ClientboundBlockEntityDataPacket.create(this)

    override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag {
        ScopedCollector(problemPath(), LOGGER).use { reporter ->
            val output = TagValueOutput.createWithContext(reporter, registries)
            saveAdditional(output)
            return output.buildResult()
        }
    }

    override fun loadAdditional(view: ValueInput) {
        super.loadAdditional(view)
        stack = view.read("stack", ItemStack.CODEC).getOrDefault(ItemStack.EMPTY)
    }

    override fun saveAdditional(view: ValueOutput) {
        super.saveAdditional(view)
        view.store("stack", ItemStack.CODEC, stack)
    }
}