package de.fuballer.mcendgame.main.component.block.blocks.totem_statue

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import de.fuballer.mcendgame.main.component.block.CustomBlockEntityTypes
import de.fuballer.mcendgame.main.component.dungeon.generation.encounter.encounters.totem.TotemEncounterSettings
import de.fuballer.mcendgame.main.configuration.RuntimeConfig
import de.fuballer.mcendgame.main.messaging.totem_encounter.TotemEncounterActivatedEvent
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getDungeonLevel
import de.maucon.mauconframework.command.CommandGateway
import de.maucon.mauconframework.event.EventGateway
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.UUIDUtil
import net.minecraft.core.Vec3i
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import java.util.*

private const val DATA_KEY = "totem_statue_data"

private const val SPAWN_PREPARATION_PARTICLE_DELAY = 50
private const val SPAWN_DELAY = 100
private const val ACTIVE_PARTICLE_DELAY = 20
private const val ACTIVE_PARTICLE_CYCLE = 10
private const val AMBIENT_SOUND_DELAY = 20
private const val AMBIENT_SOUND_CYCLE = 50

private val SOUND_CATEGORY = SoundSource.BLOCKS

private const val COMPLETION_CHECK_CYCLE = 5

class TotemStatueBlockEntity(
    pos: BlockPos,
    state: BlockState,
) : BlockEntity(CustomBlockEntityTypes.TOTEM_STATUE, pos, state) {
    private var activeTicks = -1
    private var spawnPositions = listOf<BlockPos>()
    private var activeEnemies = mutableListOf<UUID>()

    private data class TotemStatueBlockEntityData(
        val activeTicks: Int = -1,
        val activeEnemies: MutableList<UUID> = mutableListOf(),
    ) {
        companion object {
            val CODEC: Codec<TotemStatueBlockEntityData> = RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.INT.fieldOf("active_ticks").forGetter(TotemStatueBlockEntityData::activeTicks),
                    UUIDUtil.AUTHLIB_CODEC.listOf().fieldOf("active_enemies").forGetter(TotemStatueBlockEntityData::activeEnemies),
                ).apply(instance) { activeTicks, activeEnemies ->
                    TotemStatueBlockEntityData(activeTicks, activeEnemies.toMutableList())
                }
            }
        }
    }

    companion object {
        private const val NEARBY = 2
        private val NEARBY_BLOCKS_OFFSET = getNearbyBlockOffsets()

        private fun getNearbyBlockOffsets() =
            (-NEARBY..NEARBY).flatMap { x -> (-NEARBY..NEARBY).map { z -> Vec3i(x, 0, z) } }.filter { it.x != 0 || it.z != 0 }

        fun tick(
            world: Level,
            entity: TotemStatueBlockEntity,
        ) {
            if (!entity.isActive()) return
            entity.activeTicks++

            val serverWorld = world as? ServerLevel ?: return
            val ticks = entity.activeTicks
            when (ticks) {
                1 -> entity.playActivationEffects(serverWorld)
                SPAWN_PREPARATION_PARTICLE_DELAY -> entity.createSpawnPreparationParticles(serverWorld)
                SPAWN_DELAY -> entity.spawnEnemies(serverWorld)
            }

            if (ticks >= ACTIVE_PARTICLE_DELAY && ticks % ACTIVE_PARTICLE_CYCLE == 0) entity.createActiveParticles(serverWorld)
            if (ticks >= AMBIENT_SOUND_DELAY && ticks % AMBIENT_SOUND_CYCLE == 0) entity.playActiveAmbientSound(serverWorld)

            if (ticks % COMPLETION_CHECK_CYCLE == 0) entity.checkCompleted(serverWorld)
        }
    }

    fun getActiveTicks() = activeTicks

    fun isActive() = activeTicks >= 0

    fun tryActivate(player: Player) {
        if (isActive()) return
        activeTicks = 0
        sync()
        activate(player)
    }

    private fun activate(player: Player) {
        val serverWorld = level as? ServerLevel ?: return
        val level = serverWorld.getDungeonLevel()
        val enemyCount = TotemEncounterSettings.getEnemyCount(level)
        spawnPositions = getNearbyBlockPos(enemyCount)

        val event = TotemEncounterActivatedEvent(player)
        EventGateway.publish(event)
    }

    private fun getNearbyBlockPos(count: Int): List<BlockPos> {
        val nearbyBlockPos = NEARBY_BLOCKS_OFFSET.map { worldPosition.offset(it) }
        val validNearbyBlockPos = nearbyBlockPos.filter {
            if (level!!.getBlockState(it).isRedstoneConductor(level!!, it)) return@filter false
            val upPos = it.offset(0, 1, 0)
            return@filter !level!!.getBlockState(upPos).isRedstoneConductor(level!!, upPos)
        }

        val fullListCount = count / validNearbyBlockPos.size
        val chosenBlockPos = List(fullListCount) { validNearbyBlockPos }.flatten().toMutableList()

        val remaining = count % validNearbyBlockPos.size
        val randomPicks = validNearbyBlockPos.shuffled().take(remaining)
        chosenBlockPos.addAll(randomPicks)

        return chosenBlockPos
    }

    private fun playActivationEffects(world: ServerLevel) {
        world.sendParticles(ParticleTypes.REVERSE_PORTAL, worldPosition.x + 0.5, worldPosition.y + 0.5, worldPosition.z + 0.5, 35, 0.0, 0.0, 0.0, 0.1)
        world.playSound(null, worldPosition, SoundEvents.EVOKER_PREPARE_SUMMON, SOUND_CATEGORY, 1.5F, 1F)
    }

    private fun createActiveParticles(world: ServerLevel) =
        world.sendParticles(ParticleTypes.END_ROD, worldPosition.x + 0.5, worldPosition.y + 0.5, worldPosition.z + 0.5, 1, 0.0, 0.0, 0.0, 0.1)

    private fun playActiveAmbientSound(world: ServerLevel) =
        world.playSound(null, worldPosition, SoundEvents.BEACON_AMBIENT, SOUND_CATEGORY, 2F, 1F)

    private fun createSpawnPreparationParticles(world: ServerLevel) {
        spawnPositions.forEach { world.sendParticles(ParticleTypes.PORTAL, it.x + 0.5, it.y + 0.5, it.z + 0.5, 15, 0.0, 0.0, 0.0, 0.7) }
        world.playSound(null, worldPosition, SoundEvents.PORTAL_TRIGGER, SOUND_CATEGORY, 0.75F, 1.5F)
    }

    private fun spawnEnemies(world: ServerLevel) {
        spawnPositions.forEach {
            world.sendParticles(ParticleTypes.CLOUD, it.x + 0.5, it.y + 0.5, it.z + 0.5, 10, 0.1, 0.1, 0.1, 0.04)
            world.playSound(null, worldPosition, SoundEvents.ZOMBIE_INFECT, SOUND_CATEGORY, 1.2F, 1F)
        }

        val command = TotemStatueSpawnEnemiesCommand(world, spawnPositions)
        val cmd = CommandGateway.apply(command)

        activeEnemies.addAll(cmd.enemies.map { it.uuid })
    }

    private fun checkCompleted(world: ServerLevel) {
        if (activeTicks <= SPAWN_DELAY) return

        activeEnemies.removeAll { uuid -> world.getEntity(uuid)?.isAlive != true }
        if (activeEnemies.isNotEmpty()) return

        complete(world)
    }

    private fun complete(world: ServerLevel) {
        world.sendParticles(ParticleTypes.CLOUD, worldPosition.x + 0.5, worldPosition.y + 0.5, worldPosition.z + 0.5, 20, 0.1, 0.1, 0.1, 0.1)
        world.playSound(null, worldPosition, SoundEvents.TOTEM_USE, SOUND_CATEGORY, 1F, 1F)

        world.setBlockAndUpdate(worldPosition, Blocks.AIR.defaultBlockState())

        val stack = TotemEncounterSettings.getTotemReward(world.getDungeonLevel())
        val itemEntity = ItemEntity(world, worldPosition.x + 0.5, worldPosition.y + 0.5, worldPosition.z + 0.5, stack)
        RuntimeConfig.SERVER.execute { world.addFreshEntity(itemEntity) }
    }

    override fun saveAdditional(view: ValueOutput) {
        super.saveAdditional(view)
        view.store(DATA_KEY, TotemStatueBlockEntityData.CODEC, TotemStatueBlockEntityData(activeTicks, activeEnemies))
    }

    override fun loadAdditional(view: ValueInput) {
        super.loadAdditional(view)

        val data = view.read<TotemStatueBlockEntityData>(DATA_KEY, TotemStatueBlockEntityData.CODEC)
            .orElseGet { TotemStatueBlockEntityData() }
        activeTicks = data.activeTicks
        activeEnemies = data.activeEnemies
    }

    override fun getUpdatePacket(): Packet<ClientGamePacketListener> = ClientboundBlockEntityDataPacket.create(this)

    override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag = saveWithoutMetadata(registries)

    private fun sync() {
        val serverWorld = level as? ServerLevel ?: return
        serverWorld.chunkSource.blockChanged(worldPosition)
    }
}