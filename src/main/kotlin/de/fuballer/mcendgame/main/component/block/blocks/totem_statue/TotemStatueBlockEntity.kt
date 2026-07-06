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
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtOps
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.RegistryWrapper
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Uuids
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World
import java.util.*

private const val DATA_KEY = "totem_statue_data"

private const val SPAWN_PREPARATION_PARTICLE_DELAY = 50
private const val SPAWN_DELAY = 100
private const val ACTIVE_PARTICLE_DELAY = 20
private const val ACTIVE_PARTICLE_CYCLE = 10
private const val AMBIENT_SOUND_DELAY = 20
private const val AMBIENT_SOUND_CYCLE = 50

private val SOUND_CATEGORY = SoundCategory.BLOCKS

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
                    Uuids.CODEC.listOf().fieldOf("active_enemies").forGetter(TotemStatueBlockEntityData::activeEnemies),
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
            world: World,
            blockPos: BlockPos,
            state: BlockState,
            entity: TotemStatueBlockEntity,
        ) {
            if (!entity.isActive()) return
            entity.activeTicks++

            val serverWorld = world as? ServerWorld ?: return
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

    fun tryActivate(player: PlayerEntity) {
        if (isActive()) return
        activeTicks = 0
        sync()
        activate(player)
    }

    private fun activate(player: PlayerEntity) {
        val serverWorld = world as? ServerWorld ?: return
        val level = serverWorld.getDungeonLevel()
        val enemyCount = TotemEncounterSettings.getEnemyCount(level)
        spawnPositions = getNearbyBlockPos(enemyCount)

        val event = TotemEncounterActivatedEvent(player)
        EventGateway.publish(event)
    }

    private fun getNearbyBlockPos(count: Int): List<BlockPos> {
        val nearbyBlockPos = NEARBY_BLOCKS_OFFSET.map { pos.add(it) }
        val validNearbyBlockPos = nearbyBlockPos.filter {
            if (world!!.getBlockState(it).isSolidBlock(world, it)) return@filter false
            val upPos = it.add(0, 1, 0)
            return@filter !world!!.getBlockState(upPos).isSolidBlock(world, upPos)
        }

        val fullListCount = (count / validNearbyBlockPos.size).toInt()
        val chosenBlockPos = List(fullListCount) { validNearbyBlockPos }.flatten().toMutableList()

        val remaining = count % validNearbyBlockPos.size
        val randomPicks = validNearbyBlockPos.shuffled().take(remaining)
        chosenBlockPos.addAll(randomPicks)

        return chosenBlockPos
    }

    private fun playActivationEffects(world: ServerWorld) {
        world.spawnParticles(ParticleTypes.REVERSE_PORTAL, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, 35, 0.0, 0.0, 0.0, 0.1)
        world.playSound(null, pos, SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON, SOUND_CATEGORY, 1.5F, 1F)
    }

    private fun createActiveParticles(world: ServerWorld) =
        world.spawnParticles(ParticleTypes.END_ROD, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, 1, 0.0, 0.0, 0.0, 0.1)

    private fun playActiveAmbientSound(world: ServerWorld) =
        world.playSound(null, pos, SoundEvents.BLOCK_BEACON_AMBIENT, SOUND_CATEGORY, 2F, 1F)

    private fun createSpawnPreparationParticles(world: ServerWorld) {
        spawnPositions.forEach { world.spawnParticles(ParticleTypes.PORTAL, it.x + 0.5, it.y + 0.5, it.z + 0.5, 15, 0.0, 0.0, 0.0, 0.7) }
        world.playSound(null, pos, SoundEvents.BLOCK_PORTAL_TRIGGER, SOUND_CATEGORY, 0.75F, 1.5F)
    }

    private fun spawnEnemies(world: ServerWorld) {
        spawnPositions.forEach {
            world.spawnParticles(ParticleTypes.CLOUD, it.x + 0.5, it.y + 0.5, it.z + 0.5, 10, 0.1, 0.1, 0.1, 0.04)
            world.playSound(null, pos, SoundEvents.ENTITY_ZOMBIE_INFECT, SOUND_CATEGORY, 1.2F, 1F)
        }

        val command = TotemStatueSpawnEnemiesCommand(world, spawnPositions)
        val cmd = CommandGateway.apply(command)

        activeEnemies.addAll(cmd.enemies.map { it.uuid })
    }

    private fun checkCompleted(world: ServerWorld) {
        if (activeTicks <= SPAWN_DELAY) return

        activeEnemies.removeAll { uuid -> world.getEntity(uuid)?.isAlive != true }
        if (activeEnemies.isNotEmpty()) return

        complete(world)
    }

    private fun complete(world: ServerWorld) {
        world.spawnParticles(ParticleTypes.CLOUD, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, 20, 0.1, 0.1, 0.1, 0.1)
        world.playSound(null, pos, SoundEvents.ITEM_TOTEM_USE, SOUND_CATEGORY, 1F, 1F)

        world.setBlockState(pos, Blocks.AIR.defaultState)

        val stack = TotemEncounterSettings.getTotemReward(world.getDungeonLevel())
        val itemEntity = ItemEntity(world, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, stack)
        RuntimeConfig.SERVER.execute { world.spawnEntity(itemEntity) }
    }


    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, registryLookup)
        nbt.put(DATA_KEY, TotemStatueBlockEntityData.CODEC.encodeStart(NbtOps.INSTANCE, TotemStatueBlockEntityData(activeTicks, activeEnemies)).result().get())
    }

    override fun readNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, registryLookup)

        val data = TotemStatueBlockEntityData.CODEC.parse(NbtOps.INSTANCE, nbt.get(DATA_KEY))
            .result().orElseGet { TotemStatueBlockEntityData() }
        activeTicks = data.activeTicks
        activeEnemies = data.activeEnemies
    }

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener> = BlockEntityUpdateS2CPacket.create(this)

    override fun toInitialChunkDataNbt(registries: RegistryWrapper.WrapperLookup): NbtCompound = createNbt(registries)

    private fun sync() {
        val serverWorld = world as? ServerWorld ?: return
        serverWorld.chunkManager.markForUpdate(pos)
    }
}