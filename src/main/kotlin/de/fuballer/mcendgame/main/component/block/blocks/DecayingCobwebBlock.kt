package de.fuballer.mcendgame.main.component.block.blocks

import com.mojang.serialization.MapCodec
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.tag.ItemTags
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.IntProperty
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.random.Random
import net.minecraft.world.BlockView
import net.minecraft.world.World
import kotlin.math.min

class DecayingCobwebBlock(
    settings: Settings,
) : Block(settings) {
    companion object {
        const val ID = "decaying_cobweb"
        val CODEC: MapCodec<DecayingCobwebBlock> = createCodec(::DecayingCobwebBlock)

        private const val MAX_AGE = 5
        const val TICK_INTERVAL = 20
        val AGE: IntProperty = IntProperty.of("age", 0, MAX_AGE)
    }

    init {
        defaultState = stateManager.defaultState.with(AGE, 0)
    }

    override fun appendProperties(
        builder: StateManager.Builder<Block, BlockState>
    ) {
        super.appendProperties(builder)
        builder.add(AGE)
    }

    override fun onEntityCollision(
        state: BlockState,
        world: World,
        pos: BlockPos,
        entity: Entity
    ) {
        var vec3d = Vec3d(0.25, 0.05, 0.25)
        if (entity is LivingEntity && entity.hasStatusEffect(StatusEffects.WEAVING)) {
            vec3d = Vec3d(0.5, 0.25, 0.5)
        }

        entity.slowMovement(state, vec3d)
    }

    override fun onPlaced(
        world: World,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        itemStack: ItemStack
    ) {
        super.onPlaced(world, pos, state, placer, itemStack)
        if (world.isClient) return
        world.scheduleBlockTick(pos, this, TICK_INTERVAL)
    }

    override fun scheduledTick(
        state: BlockState,
        world: ServerWorld,
        pos: BlockPos,
        random: Random,
    ) {
        super.scheduledTick(state, world, pos, random)

        spawnParticles(world, pos)

        var age = state.get(AGE) as Int
        if (++age == MAX_AGE) {
            world.setBlockState(pos, Blocks.AIR.defaultState)
        } else {
            world.setBlockState(pos, state.with(AGE, min(age, MAX_AGE)), NO_REDRAW)
            world.scheduleBlockTick(pos, this, TICK_INTERVAL)
        }
    }

    private fun spawnParticles(
        world: ServerWorld,
        pos: BlockPos,
    ) {
        world.spawnParticles(
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

    override fun calcBlockBreakingDelta(
        state: BlockState,
        player: PlayerEntity,
        world: BlockView,
        pos: BlockPos
    ): Float {
        val itemStack = player.mainHandStack
        if (!itemStack.isIn(ItemTags.SWORDS)) return super.calcBlockBreakingDelta(state, player, world, pos)

        val hardness = state.getHardness(world, pos)
        if (hardness == -1.0f) return 0.0f

        val delta = player.getBlockBreakingSpeed(state) / hardness / 30 // 30 simulates canHarvest() = true
        return delta * 15 // mimics the factor in ToolMaterial.applySwordSettings
    }
}