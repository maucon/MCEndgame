package de.fuballer.mcendgame.component.dungeon.type

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.component.custom_entity.types.demoic_golem.DemonicGolemEntityType
import de.fuballer.mcendgame.component.custom_entity.types.husk.HuskEntityType
import de.fuballer.mcendgame.component.custom_entity.types.melee_skeleton.MeleeSkeletonEntityType
import de.fuballer.mcendgame.component.custom_entity.types.necromancer.NecromancerEntityType
import de.fuballer.mcendgame.component.custom_entity.types.reaper.ReaperEntityType
import de.fuballer.mcendgame.component.custom_entity.types.skeleton.SkeletonEntityType
import de.fuballer.mcendgame.component.custom_entity.types.stray.StrayEntityType
import de.fuballer.mcendgame.component.custom_entity.types.wither_skeleton.WitherSkeletonEntityType
import de.fuballer.mcendgame.component.custom_entity.types.zombie.ZombieEntityType
import de.fuballer.mcendgame.component.dungeon.generation.DungeonMapType
import de.fuballer.mcendgame.component.dungeon.type.data.RolledDungeonType
import de.fuballer.mcendgame.util.random.RandomOption
import de.fuballer.mcendgame.util.random.RandomUtil
import kotlin.random.Random

enum class DungeonType(
    private val mapTypes: List<RandomOption<DungeonMapType>>,
    private val entityTypes: List<RandomOption<CustomEntityType>>,
    private val bossEntityTypes: List<CustomEntityType>
) {
    STANDARD(
        listOf(
            RandomOption(1, DungeonMapType.STRONGHOLD),
        ),
        listOf(
            RandomOption(10, WitherSkeletonEntityType),
            RandomOption(50, ZombieEntityType),
            RandomOption(30, HuskEntityType),
            RandomOption(50, SkeletonEntityType),
            RandomOption(20, MeleeSkeletonEntityType),
            RandomOption(30, StrayEntityType),
        ),
        listOf(
            ReaperEntityType,
            NecromancerEntityType,
            DemonicGolemEntityType,
        )
    );

    fun roll(random: Random) =
        RolledDungeonType(
            RandomUtil.pick(mapTypes, random).option,
            entityTypes,
            bossEntityTypes.shuffled()
        )
}
