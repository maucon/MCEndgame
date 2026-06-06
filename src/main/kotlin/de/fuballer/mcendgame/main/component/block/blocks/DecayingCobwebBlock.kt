package de.fuballer.mcendgame.main.component.block.blocks

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.ItemTags
import net.minecraft.util.RandomSource
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.InsideBlockEffectApplier
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.phys.Vec3
import kotlin.math.min

class DecayingCobwebBlock(
    settings: Properties,
) : Block(settings) {
    companion object {
        const val ID = "decaying_cobweb"
        val CODEC: MapCodec<DecayingCobwebBlock> = simpleCodec(::DecayingCobwebBlock)

        private const val MAX_AGE = 5
        const val TICK_INTERVAL = 20
        val AGE: IntegerProperty = IntegerProperty.create("age", 0, MAX_AGE)
    }

    init {
        registerDefaultState(stateDefinition.any().setValue(AGE, 0))
    }

    override fun createBlockStateDefinition(
        builder: StateDefinition.Builder<Block, BlockState>
    ) {
        super.createBlockStateDefinition(builder)
        builder.add(AGE)
    }

    override fun entityInside(
        state: BlockState,
        world: Level,
        pos: BlockPos,
        entity: Entity,
        handler: InsideBlockEffectApplier,
        moved: Boolean,
    ) {
        var vec3d = Vec3(0.25, 0.05, 0.25)
        if (entity is LivingEntity && entity.hasEffect(MobEffects.WEAVING)) {
            vec3d = Vec3(0.5, 0.25, 0.5)
        }

        entity.makeStuckInBlock(state, vec3d)
    }

    override fun setPlacedBy(
        world: Level,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        itemStack: ItemStack
    ) {
        super.setPlacedBy(world, pos, state, placer, itemStack)
        if (world.isClientSide) return
        world.scheduleTick(pos, this, TICK_INTERVAL)
    }

    override fun tick(
        state: BlockState,
        world: ServerLevel,
        pos: BlockPos,
        random: RandomSource,
    ) {
        super.tick(state, world, pos, random)

        spawnParticles(world, pos)

        var age = state.getValue(AGE)
        if (++age == MAX_AGE) {
            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState())
        } else {
            world.setBlock(pos, state.setValue(AGE, min(age, MAX_AGE)), UPDATE_INVISIBLE)
            world.scheduleTick(pos, this, TICK_INTERVAL)
        }
    }

    private fun spawnParticles(
        world: ServerLevel,
        pos: BlockPos,
    ) {
        world.sendParticles(
            ParticleTypes.CLOUD,
            pos.x + 0.5,
            pos.y + 0.5,
            pos.z + 0.5,
            2,
            0.35,
            0.35,
            0.35,
            0.01
        )
    }

    override fun getDestroyProgress(
        state: BlockState,
        player: Player,
        world: BlockGetter,
        pos: BlockPos
    ): Float {
        val itemStack = player.mainHandItem
        if (!itemStack.`is`(ItemTags.SWORDS)) return super.getDestroyProgress(state, player, world, pos)

        val hardness = state.getDestroySpeed(world, pos)
        if (hardness == -1.0f) return 0.0f

        val delta = player.getDestroySpeed(state) / hardness / 30 // 30 simulates canHarvest() = true
        return delta * 15 // mimics the factor in ToolMaterial.applySwordSettings
    }
}