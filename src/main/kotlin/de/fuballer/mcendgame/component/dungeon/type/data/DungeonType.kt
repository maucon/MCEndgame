package de.fuballer.mcendgame.component.dungeon.type.data

import de.fuballer.mcendgame.component.custom_entity.data.CustomEntityType
import de.fuballer.mcendgame.component.dungeon.boss.data.BossType
import de.fuballer.mcendgame.util.random.RandomOption
import de.fuballer.mcendgame.util.random.RandomUtil

enum class DungeonType(
    private val mapTypes: List<RandomOption<DungeonMapType>>,
    private val entityTypes: List<RandomOption<CustomEntityType>>,
    private val bossType: List<RandomOption<BossType>>
) {
    HELL(
        listOf(
            RandomOption(1, DungeonMapType.MINE),
            RandomOption(1, DungeonMapType.CATACOMBS),
            RandomOption(1, DungeonMapType.CATACOMBS_ALTERNATIVE)
        ),
        listOf(
            RandomOption(1, CustomEntityType.WITHER_SKELETON),
            RandomOption(3, CustomEntityType.MELEE_SKELETON),
            RandomOption(4, CustomEntityType.MAGMA_CUBE),
            RandomOption(3, CustomEntityType.BLAZE),
            RandomOption(2, CustomEntityType.SUCCUBUS),
            RandomOption(2, CustomEntityType.INCUBUS),
            RandomOption(5, CustomEntityType.IMP),
        ),
        listOf(RandomOption(1, BossType.CERBERUS))
    ),
    UNDEAD(
        listOf(
            RandomOption(1, DungeonMapType.MINE),
            RandomOption(1, DungeonMapType.CATACOMBS),
            RandomOption(1, DungeonMapType.CATACOMBS_ALTERNATIVE)
        ),
        listOf(
            RandomOption(1, CustomEntityType.WITHER_SKELETON),
            RandomOption(5, CustomEntityType.ZOMBIE),
            RandomOption(3, CustomEntityType.HUSK),
            RandomOption(5, CustomEntityType.SKELETON),
            RandomOption(2, CustomEntityType.MELEE_SKELETON),
            RandomOption(3, CustomEntityType.STRAY),
            RandomOption(1, CustomEntityType.NECROMANCER),
            RandomOption(1, CustomEntityType.REAPER)
        ),
        listOf(RandomOption(1, BossType.CERBERUS))
    ),
    MONSTER(
        listOf(
            RandomOption(1, DungeonMapType.CATACOMBS),
            RandomOption(1, DungeonMapType.CATACOMBS_ALTERNATIVE)
        ),
        listOf(
            RandomOption(1, CustomEntityType.CREEPER),
            RandomOption(1, CustomEntityType.HATCHERY),
            RandomOption(5, CustomEntityType.STALKER)
        ),
        listOf(RandomOption(1, BossType.DEMONIC_GOLEM))
    ),
    MYTHICAL(
        listOf(
            RandomOption(1, DungeonMapType.ICE_CAVE),
            RandomOption(1, DungeonMapType.LUSH_CAVE)
        ),
        listOf(
            RandomOption(5, CustomEntityType.PIGLIN_BRUTE),
            RandomOption(7, CustomEntityType.CYCLOPS),
            RandomOption(1, CustomEntityType.WITCH),
            RandomOption(2, CustomEntityType.HARPY),
            RandomOption(2, CustomEntityType.NAGA)
        ),
        listOf(RandomOption(1, BossType.MINOTAUR))
    );

    fun roll() = RolledDungeonType(
        RandomUtil.pick(mapTypes).option,
        entityTypes,
        RandomUtil.pick(bossType).option
    )
}
