package de.fuballer.mcendgame.component.dungeon.type.data

import de.fuballer.mcendgame.component.custom_entity.data.CustomEntityType
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
            RandomOption(10, CustomEntityType.WITHER_SKELETON),
            RandomOption(150, CustomEntityType.MELEE_SKELETON),
            RandomOption(90, CustomEntityType.SKELETON),
            RandomOption(20, CustomEntityType.MAGMA_CUBE),
            RandomOption(20, CustomEntityType.BLAZE),
            RandomOption(3, CustomEntityType.SUCCUBUS),
            RandomOption(3, CustomEntityType.INCUBUS),
            RandomOption(15, CustomEntityType.IMP),
        ),
        listOf(RandomOption(1, CustomEntityType.CERBERUS))
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
            RandomOption(10, CustomEntityType.WITHER_SKELETON),
            RandomOption(50, CustomEntityType.ZOMBIE),
            RandomOption(30, CustomEntityType.HUSK),
            RandomOption(50, CustomEntityType.SKELETON),
            RandomOption(20, CustomEntityType.MELEE_SKELETON),
            RandomOption(30, CustomEntityType.STRAY),
            RandomOption(12, CustomEntityType.NECROMANCER),
            RandomOption(3, CustomEntityType.REAPER)
        ),
        listOf(RandomOption(1, CustomEntityType.DEMONIC_GOLEM))
    ),
    MYTHICAL(
        listOf(
            RandomOption(1, DungeonMapType.ICE_CAVE),
            RandomOption(1, DungeonMapType.LUSH_CAVE)
        ),
        listOf(
            RandomOption(60, CustomEntityType.PIGLIN_BRUTE),
            RandomOption(50, CustomEntityType.CYCLOPS),
            RandomOption(20, CustomEntityType.HUSK),
            RandomOption(5, CustomEntityType.WITCH),
            RandomOption(10, CustomEntityType.HARPY),
            RandomOption(10, CustomEntityType.NAGA)
        ),
        listOf(RandomOption(1, CustomEntityType.MINOTAUR))
    ),
    FOREST(
        listOf(
            RandomOption(1, DungeonMapType.LUSH_CAVE)
        ),
        listOf(
            RandomOption(50, CustomEntityType.FOREST_SKELETON),
            RandomOption(40, CustomEntityType.MELEE_FOREST_SKELETON),
            RandomOption(15, CustomEntityType.MUSHROOM),
            RandomOption(8, CustomEntityType.STALKER),
            RandomOption(4, CustomEntityType.DRYAD),
            RandomOption(1, CustomEntityType.WENDIGO)
        ),
        listOf(RandomOption(1, CustomEntityType.MANDRAGORA))
    );

    fun roll() = RolledDungeonType(
        RandomUtil.pick(mapTypes).option,
        entityTypes,
        RandomUtil.pick(bossEntityTypes).option
    )
}
