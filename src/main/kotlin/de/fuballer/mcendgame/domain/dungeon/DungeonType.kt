package de.fuballer.mcendgame.domain.dungeon

import de.fuballer.mcendgame.component.dungeon.type.data.RolledDungeonType
import de.fuballer.mcendgame.domain.entity.CustomEntityType
import de.fuballer.mcendgame.domain.entity.blaze.BlazeEntityType
import de.fuballer.mcendgame.domain.entity.buff_allay.BuffAllayEntityType
import de.fuballer.mcendgame.domain.entity.buff_cow.BuffCowEntityType
import de.fuballer.mcendgame.domain.entity.buff_villager.BuffVillagerEntityType
import de.fuballer.mcendgame.domain.entity.buff_witch.BuffWitchEntityType
import de.fuballer.mcendgame.domain.entity.cerberus.CerberusEntityType
import de.fuballer.mcendgame.domain.entity.cyclops.CyclopsEntityType
import de.fuballer.mcendgame.domain.entity.demoic_golem.DemonicGolemEntityType
import de.fuballer.mcendgame.domain.entity.dryad.DryadEntityType
import de.fuballer.mcendgame.domain.entity.forest_skeleton.ForestSkeletonEntityType
import de.fuballer.mcendgame.domain.entity.harpy.HarpyEntityType
import de.fuballer.mcendgame.domain.entity.husk.HuskEntityType
import de.fuballer.mcendgame.domain.entity.imp.ImpEntityType
import de.fuballer.mcendgame.domain.entity.incubus.IncubusEntityType
import de.fuballer.mcendgame.domain.entity.magma_cube.MagmaCubeEntityType
import de.fuballer.mcendgame.domain.entity.mandragora.MandragoraEntityType
import de.fuballer.mcendgame.domain.entity.melee_forest_skeleton.MeleeForestSkeletonEntityType
import de.fuballer.mcendgame.domain.entity.melee_skeleton.MeleeSkeletonEntityType
import de.fuballer.mcendgame.domain.entity.minotaur.MinotaurEntityType
import de.fuballer.mcendgame.domain.entity.mushroom.MushroomEntityType
import de.fuballer.mcendgame.domain.entity.naga.NagaEntityType
import de.fuballer.mcendgame.domain.entity.necromancer.NecromancerEntityType
import de.fuballer.mcendgame.domain.entity.piglin_brute.PiglinBruteEntityType
import de.fuballer.mcendgame.domain.entity.reaper.ReaperEntityType
import de.fuballer.mcendgame.domain.entity.skeleton.SkeletonEntityType
import de.fuballer.mcendgame.domain.entity.stalker.StalkerEntityType
import de.fuballer.mcendgame.domain.entity.stray.StrayEntityType
import de.fuballer.mcendgame.domain.entity.succubus.SuccubusEntityType
import de.fuballer.mcendgame.domain.entity.wendigo.WendigoEntityType
import de.fuballer.mcendgame.domain.entity.witch.WitchEntityType
import de.fuballer.mcendgame.domain.entity.wither_skeleton.WitherSkeletonEntityType
import de.fuballer.mcendgame.domain.entity.zombie.ZombieEntityType
import de.fuballer.mcendgame.util.random.RandomOption
import de.fuballer.mcendgame.util.random.RandomUtil

enum class DungeonType(
    private val mapTypes: List<RandomOption<DungeonMapType>>,
    private val entityTypes: List<RandomOption<CustomEntityType>>,
    private val specialEntityTypes: List<RandomOption<CustomEntityType>>,
    private val bossEntityTypes: List<RandomOption<CustomEntityType>>
) {
    HELL(
        listOf(
            RandomOption(1, DungeonMapType.MINE),
        ),
        listOf(
            RandomOption(10, WitherSkeletonEntityType),
            RandomOption(150, MeleeForestSkeletonEntityType),
            RandomOption(90, SkeletonEntityType),
            RandomOption(20, MagmaCubeEntityType),
            RandomOption(20, BlazeEntityType),
            RandomOption(15, ImpEntityType),
        ),
        listOf(
            RandomOption(1, SuccubusEntityType),
            RandomOption(1, IncubusEntityType),
        ),
        listOf(RandomOption(1, CerberusEntityType))
    ),
    UNDEAD(
        listOf(
            RandomOption(5, DungeonMapType.MINE),
            RandomOption(3, DungeonMapType.CATACOMBS),
            RandomOption(1, DungeonMapType.CATACOMBS_ALTERNATIVE),
            RandomOption(1, DungeonMapType.ICE_CAVE),
            RandomOption(1, DungeonMapType.LUSH_CAVE)
        ),
        listOf(
            RandomOption(10, WitherSkeletonEntityType),
            RandomOption(50, ZombieEntityType),
            RandomOption(30, HuskEntityType),
            RandomOption(50, SkeletonEntityType),
            RandomOption(20, MeleeSkeletonEntityType),
            RandomOption(30, StrayEntityType),
            RandomOption(12, NecromancerEntityType),

            ),
        listOf(
            RandomOption(3, ReaperEntityType)
        ),
        listOf(RandomOption(1, DemonicGolemEntityType))
    ),
    MYTHICAL(
        listOf(
            RandomOption(1, DungeonMapType.ICE_CAVE),
            RandomOption(1, DungeonMapType.LUSH_CAVE)
        ),
        listOf(
            RandomOption(60, PiglinBruteEntityType),
            RandomOption(50, CyclopsEntityType),
            RandomOption(20, HuskEntityType),
            RandomOption(5, WitchEntityType),
            RandomOption(10, HarpyEntityType),
            RandomOption(10, NagaEntityType)
        ),
        listOf(
            RandomOption(1, ReaperEntityType)
        ),
        listOf(RandomOption(1, MinotaurEntityType))
    ),
    FOREST(
        listOf(
            RandomOption(1, DungeonMapType.LUSH_CAVE)
        ),
        listOf(
            RandomOption(50, ForestSkeletonEntityType),
            RandomOption(40, MeleeForestSkeletonEntityType),
            RandomOption(15, MushroomEntityType),
            RandomOption(8, StalkerEntityType),
            RandomOption(4, DryadEntityType)
        ),
        listOf(
            RandomOption(1, WendigoEntityType)
        ),
        listOf(RandomOption(1, MandragoraEntityType))
    ),
    VILLAGE(
        listOf(
            RandomOption(1, DungeonMapType.ICE_CAVE)
        ),
        listOf(
            RandomOption(50, BuffVillagerEntityType),
            RandomOption(20, BuffWitchEntityType),
            RandomOption(8, BuffAllayEntityType),
        ),
        listOf(
            RandomOption(1, BuffCowEntityType)
        ),
        listOf(RandomOption(1, MinotaurEntityType))
    );

    fun roll() = RolledDungeonType(
        RandomUtil.pick(mapTypes).option,
        entityTypes,
        specialEntityTypes,
        RandomUtil.pick(bossEntityTypes).option
    )
}