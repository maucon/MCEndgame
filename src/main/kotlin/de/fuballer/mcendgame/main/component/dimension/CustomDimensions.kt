package de.fuballer.mcendgame.main.component.dimension

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TimelineTags
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.attribute.BackgroundMusic
import net.minecraft.world.attribute.BedRule
import net.minecraft.world.attribute.EnvironmentAttributeMap
import net.minecraft.world.attribute.EnvironmentAttributes
import net.minecraft.world.clock.WorldClock
import net.minecraft.world.clock.WorldClocks
import net.minecraft.world.level.CardinalLighting
import net.minecraft.world.level.dimension.DimensionType
import net.minecraft.world.timeline.Timeline
import java.util.*

@Injectable
object CustomDimensions {
    val DUNGEON: ResourceKey<DimensionType> = ResourceKey.create(Registries.DIMENSION_TYPE, IdentifierUtil.default("dungeon"))

    fun bootstrap(context: BootstrapContext<DimensionType>) {
        val timelines = context.lookup(Registries.TIMELINE)
        val clocks = context.lookup(Registries.WORLD_CLOCK)

        context.register(DUNGEON, createDungeonDimensionType(context, timelines, clocks))
    }

    private fun createDungeonDimensionType(
        context: BootstrapContext<DimensionType>,
        timelines: HolderGetter<Timeline>,
        clocks: HolderGetter<WorldClock>,
    ): DimensionType {
        val attributes = EnvironmentAttributeMap.builder()
            .set(EnvironmentAttributes.AMBIENT_LIGHT_COLOR, -16119286)
            .set(EnvironmentAttributes.BACKGROUND_MUSIC, BackgroundMusic.OVERWORLD)
            .set(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, false)
            .set(EnvironmentAttributes.PIGLINS_ZOMBIFY, false)
            .set(EnvironmentAttributes.CAN_START_RAID, false)
            .set(EnvironmentAttributes.BED_RULE, BedRule.CAN_SLEEP_WHEN_DARK)
            .build()

        val blockLookup = context.lookup(Registries.BLOCK)
        val infiniburn = blockLookup.getOrThrow(BlockTags.INFINIBURN_OVERWORLD)

        return DimensionType(
            false, // hasFixedTime
            true, // hasSkyLight
            false, // hasCeiling
            false, // hasEnderDragonFight
            1.0, // coordinateScale
            -64, // minY
            384, // height
            384, // logicalHeight
            infiniburn, // infiniburn
            0.0F, // ambientLight
            DimensionType.MonsterSettings(UniformInt.of(0, 7), 0), // monsterSettings
            DimensionType.Skybox.OVERWORLD, // skybox
            CardinalLighting.Type.DEFAULT, // cardinalLightType
            attributes, // attributes
            timelines.getOrThrow(TimelineTags.IN_OVERWORLD), // timelines
            Optional.of(clocks.getOrThrow(WorldClocks.OVERWORLD)) // defaultClock
        )
    }
}