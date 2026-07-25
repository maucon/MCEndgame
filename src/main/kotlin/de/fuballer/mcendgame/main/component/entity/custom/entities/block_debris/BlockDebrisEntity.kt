package de.fuballer.mcendgame.main.component.entity.custom.entities.block_debris

import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import de.fuballer.mcendgame.main.component.entity.custom.attack.damage.AreaAttackDamage
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.ParticleData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.SoundData
import net.minecraft.core.BlockPos
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerEntity
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.MoverType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

private val DEFAULT_BLOCK_STATE = Blocks.STONE.defaultBlockState()

class BlockDebrisEntity(
    type: EntityType<out BlockDebrisEntity>,
    level: Level,
) : Entity(type, level) {
    private var owner: Mob? = null
    private var blockState: BlockState = DEFAULT_BLOCK_STATE
    private var explosionDamage: AreaAttackDamage? = null
    private var explosionParticles: ParticleData? = null
    private var explosionSound: SoundData? = null

    constructor(
        level: Level,
        owner: Mob,
        blockState: BlockState,
        explosionDamage: AreaAttackDamage,
        explosionParticles: ParticleData,
        explosionSound: SoundData,
    ) : this(CustomEntities.BLOCK_DEBRIS, level) {
        this.owner = owner
        this.blockState = blockState
        this.explosionDamage = explosionDamage
        this.explosionParticles = explosionParticles
        this.explosionSound = explosionSound
    }

    init {
        setStartPos(blockPosition())
        noPhysics = true
    }

    companion object {
        val DATA_START_POS = SynchedEntityData.defineId(BlockDebrisEntity::class.java, EntityDataSerializers.BLOCK_POS)
    }

    private var lifetime = 100

    var rotX = random.nextFloat() * 360f
    var rotY = random.nextFloat() * 360f
    var rotZ = random.nextFloat() * 360f

    private val rotVelX = random.nextFloat() * 20f - 10f
    private val rotVelY = random.nextFloat() * 20f - 10f
    private val rotVelZ = random.nextFloat() * 20f - 10f

    override fun defineSynchedData(entityData: SynchedEntityData.Builder) {
        entityData.define(DATA_START_POS, BlockPos.ZERO)
    }

    fun setStartPos(pos: BlockPos) {
        entityData.set(DATA_START_POS, pos)
    }

    fun getStartPos() = entityData.get(DATA_START_POS)

    override fun tick() {
        super.tick()

        applyGravity()
        move(MoverType.SELF, deltaMovement)
        deltaMovement = deltaMovement.scale(0.98)
        rotX += rotVelX
        rotY += rotVelY
        rotZ += rotVelZ

        val serverLevel = level() as? ServerLevel ?: return

        if (--lifetime <= 0) {
            explode(serverLevel)
            return
        }

        checkCollisions(serverLevel)
    }

    private fun checkCollisions(serverLevel: ServerLevel) {
        if (checkBlockCollision(serverLevel)) explode(serverLevel)

        val entities = serverLevel.getEntities(this, boundingBox.inflate(0.1)).filter { it != owner }
        if (entities.any { it.isPickable && it !is BlockDebrisEntity }) explode(serverLevel)
    }

    private fun checkBlockCollision(serverLevel: ServerLevel): Boolean {
        val entityBox = boundingBox

        return BlockPos.betweenClosedStream(entityBox)
            .anyMatch { pos ->
                val state = serverLevel.getBlockState(pos)

                val shape = state.getCollisionShape(serverLevel, pos)
                if (shape.isEmpty) false
                else shape.toAabbs().any { box ->
                    box.move(pos).intersects(entityBox)
                }
            }
    }

    override fun getDefaultGravity(): Double = 0.04

    override fun hurtServer(level: ServerLevel, source: DamageSource, damage: Float) = false

    override fun isPickable() = false

    override fun displayFireAnimation() = false

    override fun addAdditionalSaveData(output: ValueOutput) {
        output.store("BlockState", BlockState.CODEC, blockState)
        output.putInt("Lifetime", lifetime)
    }

    override fun readAdditionalSaveData(input: ValueInput) {
        blockState = input.read("BlockState", BlockState.CODEC)
            .orElse(DEFAULT_BLOCK_STATE) as BlockState

        lifetime = input.getIntOr("Lifetime", 40)
    }

    override fun getAddEntityPacket(serverEntity: ServerEntity): Packet<ClientGamePacketListener> =
        ClientboundAddEntityPacket(this, serverEntity, Block.getId(blockState))

    override fun recreateFromPacket(packet: ClientboundAddEntityPacket) {
        super.recreateFromPacket(packet)
        blockState = Block.stateById(packet.data)
        setPos(packet.x, packet.y, packet.z)
        setStartPos(blockPosition())
    }

    fun getBlockState() = blockState

    private fun explode(
        serverLevel: ServerLevel,
    ) {
        owner?.let { explosionDamage?.applyAtOtherEntity(serverLevel, owner!!, this) }
        explosionParticles?.apply(serverLevel, this)
        explosionSound?.apply(serverLevel, this)

        discard()
    }
}