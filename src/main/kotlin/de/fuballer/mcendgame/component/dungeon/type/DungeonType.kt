package de.fuballer.mcendgame.component.dungeon.type

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.component.custom_entity.types.buff_allay.BuffAllayEntityType
import de.fuballer.mcendgame.component.custom_entity.types.buff_cow.BuffCowEntityType
import de.fuballer.mcendgame.component.custom_entity.types.buff_villager.BuffVillagerEntityType
import de.fuballer.mcendgame.component.custom_entity.types.buff_witch.BuffWitchEntityType
import de.fuballer.mcendgame.component.custom_entity.types.cerberus.CerberusEntityType
import de.fuballer.mcendgame.component.custom_entity.types.cyclops.CyclopsEntityType
import de.fuballer.mcendgame.component.custom_entity.types.demoic_golem.DemonicGolemEntityType
import de.fuballer.mcendgame.component.custom_entity.types.dryad.DryadEntityType
import de.fuballer.mcendgame.component.custom_entity.types.forest_skeleton.ForestSkeletonEntityType
import de.fuballer.mcendgame.component.custom_entity.types.harpy.HarpyEntityType
import de.fuballer.mcendgame.component.custom_entity.types.husk.HuskEntityType
import de.fuballer.mcendgame.component.custom_entity.types.imp.ImpEntityType
import de.fuballer.mcendgame.component.custom_entity.types.incubus.IncubusEntityType
import de.fuballer.mcendgame.component.custom_entity.types.magma_cube.MagmaCubeEntityType
import de.fuballer.mcendgame.component.custom_entity.types.mandragora.MandragoraEntityType
import de.fuballer.mcendgame.component.custom_entity.types.melee_forest_skeleton.MeleeForestSkeletonEntityType
import de.fuballer.mcendgame.component.custom_entity.types.melee_skeleton.MeleeSkeletonEntityType
import de.fuballer.mcendgame.component.custom_entity.types.minotaur.MinotaurEntityType
import de.fuballer.mcendgame.component.custom_entity.types.mushroom.MushroomEntityType
import de.fuballer.mcendgame.component.custom_entity.types.naga.NagaEntityType
import de.fuballer.mcendgame.component.custom_entity.types.necromancer.NecromancerEntityType
import de.fuballer.mcendgame.component.custom_entity.types.piglin_brute.PiglinBruteEntityType
import de.fuballer.mcendgame.component.custom_entity.types.reaper.ReaperEntityType
import de.fuballer.mcendgame.component.custom_entity.types.skeleton.SkeletonEntityType
import de.fuballer.mcendgame.component.custom_entity.types.stalker.StalkerEntityType
import de.fuballer.mcendgame.component.custom_entity.types.stray.StrayEntityType
import de.fuballer.mcendgame.component.custom_entity.types.succubus.SuccubusEntityType
import de.fuballer.mcendgame.component.custom_entity.types.wendigo.WendigoEntityType
import de.fuballer.mcendgame.component.custom_entity.types.witch.WitchEntityType
import de.fuballer.mcendgame.component.custom_entity.types.wither_skeleton.WitherSkeletonEntityType
import de.fuballer.mcendgame.component.custom_entity.types.zombie.ZombieEntityType
import de.fuballer.mcendgame.component.dungeon.type.data.RolledDungeonType
import de.fuballer.mcendgame.util.random.RandomOption
import de.fuballer.mcendgame.util.random.RandomUtil

enum class DungeonType(
    private val mapTypes: List<RandomOption<DungeonMapType>>,
    private val entityTypes: List<RandomOption<CustomEntityType>>,
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
            RandomOption(20, BuffAllayEntityType),
            RandomOption(3, SuccubusEntityType),
            RandomOption(3, IncubusEntityType),
            RandomOption(15, ImpEntityType),
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
            RandomOption(4, DryadEntityType),
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
            RandomOption(3, BuffCowEntityType),
            RandomOption(8, BuffAllayEntityType),
        ),
        listOf(RandomOption(1, MinotaurEntityType))
    );

    fun roll() = RolledDungeonType(
        RandomUtil.pick(mapTypes).option,
        entityTypes,
        RandomUtil.pick(bossEntityTypes).option
    )
}
